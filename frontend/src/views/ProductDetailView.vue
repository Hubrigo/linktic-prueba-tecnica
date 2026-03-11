<template>
  <div class="detail-page">
    <button @click="goBack" class="back-btn">
      ← Volver
    </button>

    <h2 class="page-title">Detalle del producto</h2>

    <p v-if="loading" class="info-msg">Cargando detalle...</p>
    <p v-else-if="error" class="error-msg">{{ error }}</p>

    <div v-else-if="product" class="detail-layout">
      <section class="product-card">
        <div class="card-header">
          <h3>{{ product.name }}</h3>
          <span class="status-badge" :class="product.status?.toLowerCase()">
            {{ product.status }}
          </span>
        </div>

        <div class="card-grid">
          <div class="info-item">
            <span class="label">ID</span>
            <span class="value">{{ product.id }}</span>
          </div>

          <div class="info-item">
            <span class="label">SKU</span>
            <span class="value">{{ product.sku }}</span>
          </div>

          <div class="info-item">
            <span class="label">Precio</span>
            <span class="value price">${{ formatPrice(product.price) }}</span>
          </div>

          <div class="info-item">
            <span class="label">Fecha creación</span>
            <span class="value">{{ formatDate(product.createdAt) }}</span>
          </div>
        </div>
      </section>

      <section class="purchase-card">
        <h3>Inventario y compra</h3>

        <p v-if="inventoryLoading" class="info-msg">Cargando inventario...</p>
        <p v-else-if="inventoryError" class="error-msg">{{ inventoryError }}</p>

        <div v-else class="stock-box">
          <span class="label">Stock disponible</span>
          <span class="stock-value">{{ inventory?.available ?? 0 }}</span>
        </div>

        <div class="purchase-box">
          <label for="quantity" class="label">Cantidad</label>
          <input
            id="quantity"
            v-model.number="quantity"
            type="number"
            min="1"
            class="quantity-input"
          />

          <button
            @click="handlePurchase"
            :disabled="purchasing || inventoryLoading || !inventory"
            class="buy-btn"
          >
            {{ purchasing ? 'Comprando...' : 'Comprar' }}
          </button>
        </div>

        <p v-if="purchaseMessage" class="success-msg">
          {{ purchaseMessage }}
        </p>

        <p v-if="purchaseError" class="error-msg">
          {{ purchaseError }}
        </p>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { useRoute, useRouter } from "vue-router"
import { getProductById } from "../api/productsApi"
import { getInventoryByProductId, purchaseProduct } from "../api/inventoryApi"

const route = useRoute()
const router = useRouter()

const product = ref(null)
const inventory = ref(null)

const loading = ref(true)
const inventoryLoading = ref(true)
const purchasing = ref(false)

const error = ref("")
const inventoryError = ref("")
const purchaseError = ref("")
const purchaseMessage = ref("")

const quantity = ref(1)

const goBack = () => {
  router.back()
}

const formatPrice = (value) => {
  return new Intl.NumberFormat("es-CO").format(value || 0)
}

const formatDate = (value) => {
  if (!value) return "-"
  return new Date(value).toLocaleString("es-CO")
}

const loadProduct = async () => {
  try {
    const data = await getProductById(route.params.id)
    product.value = data
  } catch (e) {
    console.error(e)
    error.value = "Error cargando el producto"
  } finally {
    loading.value = false
  }
}

const loadInventory = async () => {
  inventoryLoading.value = true
  inventoryError.value = ""

  try {
    const data = await getInventoryByProductId(route.params.id)
    inventory.value = data
  } catch (e) {
    console.error(e)
    inventory.value = null
    inventoryError.value = "No se pudo cargar el inventario"
  } finally {
    inventoryLoading.value = false
  }
}

const handlePurchase = async () => {
  purchaseError.value = ""
  purchaseMessage.value = ""

  if (!quantity.value || quantity.value < 1) {
    purchaseError.value = "La cantidad debe ser mayor a 0"
    return
  }

  try {
    purchasing.value = true

    const response = await purchaseProduct({
      productId: route.params.id,
      quantity: quantity.value
    })

    purchaseMessage.value = `Compra realizada con éxito. Stock restante: ${response.remainingStock}`
    quantity.value = 1

    await loadInventory()
  } catch (e) {
    console.error(e)

    if (e.response?.status === 409) {
      purchaseError.value = "No hay stock suficiente"
    } else if (e.response?.status === 404) {
      purchaseError.value = "No se encontró inventario para este producto"
    } else if (e.response?.status === 503) {
      purchaseError.value = "El servicio no está disponible en este momento"
    } else {
      purchaseError.value = "Error al realizar la compra"
    }
  } finally {
    purchasing.value = false
  }
}

onMounted(async () => {
  await loadProduct()
  await loadInventory()
})
</script>

<style>
.detail-page {
  max-width: 1100px;
}

.page-title {
  margin-bottom: 20px;
}

.back-btn {
  margin-bottom: 16px;
  padding: 10px 16px;
  background: #2c3e50;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.back-btn:hover {
  background: #1a252f;
}

.detail-layout {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
}

.product-card,
.purchase-card {
  background: white;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.card-header h3,
.purchase-card h3 {
  margin: 0;
}

.status-badge {
  padding: 6px 12px;
  border-radius: 999px;
  font-size: 13px;
  font-weight: bold;
}

.status-badge.active {
  background: #dcfce7;
  color: #166534;
}

.status-badge.inactive {
  background: #fee2e2;
  color: #991b1b;
}

.card-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 12px;
  background: #f8fafc;
  border-radius: 8px;
}

.label {
  font-size: 13px;
  color: #475569;
  font-weight: bold;
}

.value {
  word-break: break-word;
}

.price {
  font-size: 18px;
  font-weight: bold;
  color: #0f172a;
}

.stock-box {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 14px;
  background: #f8fafc;
  border-radius: 8px;
  margin: 16px 0;
}

.stock-value {
  font-size: 28px;
  font-weight: bold;
  color: #0f172a;
}

.purchase-box {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 16px;
}

.quantity-input {
  width: 120px;
  padding: 10px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
}

.buy-btn {
  width: fit-content;
  padding: 10px 18px;
  background: #2563eb;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.buy-btn:hover {
  background: #1d4ed8;
}

.buy-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.info-msg {
  color: #334155;
}

.success-msg {
  color: #166534;
  background: #dcfce7;
  padding: 12px;
  border-radius: 6px;
  margin-top: 16px;
}

.error-msg {
  color: #991b1b;
  background: #fee2e2;
  padding: 12px;
  border-radius: 6px;
  margin-top: 16px;
}

@media (max-width: 900px) {
  .detail-layout {
    grid-template-columns: 1fr;
  }

  .card-grid {
    grid-template-columns: 1fr;
  }
}
</style>