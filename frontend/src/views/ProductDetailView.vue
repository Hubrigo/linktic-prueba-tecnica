<template>
  <div class="space-y-6">
    <div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
      <div>
        <h1 class="text-2xl font-bold text-slate-800">Detalle del producto</h1>
        <p class="mt-1 text-sm text-slate-500">
          Consulta la información del producto, su inventario disponible y realiza una compra.
        </p>
      </div>

      <router-link
        to="/products"
        class="inline-flex items-center rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-50"
      >
        Volver
      </router-link>
    </div>

    <AppLoader
      v-if="loadingProduct"
      message="Cargando información del producto..."
    />

    <AppAlert
      v-else-if="productError"
      type="error"
      :message="productError"
    />

    <template v-else-if="product">
      <div class="grid gap-6 lg:grid-cols-3">
        <div class="rounded-xl border border-slate-200 bg-white p-6 shadow-sm lg:col-span-2">
          <div class="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
            <div>
              <p class="text-sm font-medium uppercase tracking-wide text-slate-500">
                {{ product.sku }}
              </p>
              <h2 class="mt-1 text-2xl font-bold text-slate-800">
                {{ product.name }}
              </h2>
              <p class="mt-3 text-3xl font-bold text-blue-600">
                {{ formatPrice(product.price) }}
              </p>
            </div>

            <span
              :class="product.status === 'ACTIVE'
                ? 'inline-flex rounded-full bg-emerald-100 px-3 py-1 text-sm font-semibold text-emerald-700'
                : 'inline-flex rounded-full bg-slate-200 px-3 py-1 text-sm font-semibold text-slate-700'"
            >
              {{ product.status }}
            </span>
          </div>

          <div class="mt-8 grid gap-4 sm:grid-cols-2">
            <div class="rounded-lg bg-slate-50 p-4">
              <p class="text-xs font-semibold uppercase tracking-wide text-slate-500">
                ID del producto
              </p>
              <p class="mt-2 break-all text-sm text-slate-700">
                {{ product.id }}
              </p>
            </div>

            <div class="rounded-lg bg-slate-50 p-4">
              <p class="text-xs font-semibold uppercase tracking-wide text-slate-500">
                Estado actual
              </p>
              <p class="mt-2 text-sm font-medium text-slate-700">
                {{ product.status }}
              </p>
            </div>
          </div>
        </div>

        <div class="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
          <div class="mb-4">
            <h3 class="text-lg font-semibold text-slate-800">Inventario</h3>
            <p class="mt-1 text-sm text-slate-500">
              Estado actual del stock del producto.
            </p>
          </div>

          <AppLoader
            v-if="loadingInventory"
            message="Cargando inventario..."
          />

          <AppAlert
            v-else-if="inventoryError"
            type="warning"
            :message="inventoryError"
          />

          <div v-else-if="inventory" class="space-y-4">
            <div class="rounded-lg bg-slate-50 p-4">
              <p class="text-xs font-semibold uppercase tracking-wide text-slate-500">
                Stock disponible
              </p>
              <p class="mt-2 text-3xl font-bold text-slate-800">
                {{ inventory.available }}
              </p>
            </div>

            <div class="rounded-lg bg-slate-50 p-4">
              <p class="text-xs font-semibold uppercase tracking-wide text-slate-500">
                Product ID asociado
              </p>
              <p class="mt-2 break-all text-sm text-slate-700">
                {{ inventory.productId }}
              </p>
            </div>
          </div>

          <EmptyState
            v-else
            title="Sin inventario registrado"
            description="Este producto aún no tiene stock asociado. Puedes crearlo desde la opción de inventario."
          />
        </div>
      </div>

      <div class="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
        <div class="mb-6">
          <h3 class="text-lg font-semibold text-slate-800">Comprar producto</h3>
          <p class="mt-1 text-sm text-slate-500">
            Ingresa la cantidad que deseas comprar. Se validará el stock disponible.
          </p>
        </div>

        <form @submit.prevent="handlePurchase" class="space-y-4">
          <div class="max-w-xs">
            <label class="mb-2 block text-sm font-semibold text-slate-700">
              Cantidad
            </label>
            <input
              v-model.number="quantity"
              type="number"
              min="1"
              placeholder="Ej: 2"
              :class="[
                inputBaseClasses,
                fieldErrors.quantity ? inputStateClasses.error : inputStateClasses.normal
              ]"
            />
            <p v-if="fieldErrors.quantity" class="mt-1 text-sm text-red-600">
              {{ fieldErrors.quantity }}
            </p>
          </div>

          <div class="flex flex-col gap-3 sm:flex-row">
            <button
              type="submit"
              :disabled="purchasing || !product"
              :class="buttonClasses.primary"
            >
              {{ purchasing ? "Procesando compra..." : "Comprar" }}
            </button>

            <button
              type="button"
              @click="reloadInventory"
              :disabled="loadingInventory"
              :class="buttonClasses.secondary"
            >
              Actualizar inventario
            </button>
          </div>
        </form>

        <div class="mt-4 space-y-3">
          <AppAlert
            v-if="purchaseMessage"
            type="success"
            :message="purchaseMessage"
          />

          <AppAlert
            v-if="purchaseError"
            type="error"
            :message="purchaseError"
          />
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, onBeforeUnmount, watch } from "vue"
import { storeToRefs } from "pinia"
import { useRoute } from "vue-router"
import { useProductStore } from "../stores/productStore"
import { useInventoryStore } from "../stores/inventoryStore"
import AppAlert from "../components/AppAlert.vue"
import AppLoader from "../components/AppLoader.vue"
import EmptyState from "../components/EmptyState.vue"
import {
  buttonClasses,
  inputBaseClasses,
  inputStateClasses
} from "../utils/uiClasses"

const route = useRoute()
const productStore = useProductStore()
const inventoryStore = useInventoryStore()

const {
  product,
  loading: loadingProduct,
  error: productError
} = storeToRefs(productStore)

const {
  inventory,
  loading: loadingInventory,
  purchasing,
  error: inventoryError,
  purchaseError,
  purchaseMessage
} = storeToRefs(inventoryStore)

const quantity = ref(1)

const fieldErrors = reactive({
  quantity: ""
})

const clearFieldErrors = () => {
  fieldErrors.quantity = ""
}

const productId = () => route.params.id

const loadData = async () => {
  try {
    await productStore.fetchProductById(productId())
  } catch (e) {
    console.error(e)
  }

  try {
    await inventoryStore.fetchInventoryByProductId(productId())
  } catch (e) {
    console.error(e)
  }
}

const validatePurchase = () => {
  clearFieldErrors()

  if (
    quantity.value === null ||
    quantity.value === "" ||
    Number.isNaN(Number(quantity.value))
  ) {
    fieldErrors.quantity = "La cantidad es obligatoria"
    return false
  }

  if (Number(quantity.value) < 1) {
    fieldErrors.quantity = "La cantidad debe ser mayor a 0"
    return false
  }

  return true
}

const handlePurchase = async () => {
  inventoryStore.clearMessages()

  if (!validatePurchase()) return

  try {
    await inventoryStore.buyProduct({
      productId: productId(),
      quantity: Number(quantity.value)
    })

    quantity.value = 1
    clearFieldErrors()
    await inventoryStore.fetchInventoryByProductId(productId())
  } catch (e) {
    console.error(e)
  }
}

const reloadInventory = async () => {
  try {
    await inventoryStore.fetchInventoryByProductId(productId())
  } catch (e) {
    console.error(e)
  }
}

const formatPrice = (value) => {
  return new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
    maximumFractionDigits: 0
  }).format(value)
}

watch(
  () => route.params.id,
  async () => {
    quantity.value = 1
    clearFieldErrors()
    inventoryStore.clearMessages()
    await loadData()
  }
)

onMounted(async () => {
  inventoryStore.clearMessages()
  await loadData()
})

onBeforeUnmount(() => {
  productStore.clearProduct()
  inventoryStore.clearMessages()
  clearFieldErrors()
})
</script>