<template>
  <div class="products-page">
    <div class="page-header">
      <div>
        <h2>Listado de productos</h2>
        <p class="subtitle">
          Consulta, busca y navega por el catálogo de productos.
        </p>
      </div>
    </div>

    <section class="filters-card">
      <div class="filters-grid">
        <div class="field">
          <label for="search">Buscar</label>
          <input
            id="search"
            v-model="search"
            type="text"
            placeholder="Nombre o SKU"
            @keyup.enter="handleSearch"
          />
        </div>

        <div class="field">
          <label for="status">Estado</label>
          <select id="status" v-model="status">
            <option value="">Todos</option>
            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>
          </select>
        </div>

        <div class="field">
          <label for="size">Registros por página</label>
          <select id="size" v-model.number="size" @change="handleSearch">
            <option :value="5">5</option>
            <option :value="10">10</option>
            <option :value="15">15</option>
          </select>
        </div>
      </div>

      <div class="filters-actions">
        <button class="primary-btn" @click="handleSearch">Buscar</button>
        <button class="secondary-btn" @click="clearFilters">Limpiar</button>
      </div>
    </section>

    <p v-if="loading" class="info-msg">Cargando productos...</p>
    <p v-else-if="error" class="error-msg">{{ error }}</p>

    <template v-else>
      <section v-if="products.length" class="table-card">
        <div class="table-header">
          <span>Total productos encontrados: {{ totalElements }}</span>
          <span>Página {{ page + 1 }} de {{ totalPages }}</span>
        </div>

        <div class="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>SKU</th>
                <th>Nombre</th>
                <th>Precio</th>
                <th>Estado</th>
                <th>Fecha creación</th>
                <th>Acción</th>
              </tr>
            </thead>

            <tbody>
              <tr v-for="p in products" :key="p.id">
                <td>{{ p.sku }}</td>
                <td>{{ p.name }}</td>
                <td class="price">${{ formatPrice(p.price) }}</td>
                <td>
                  <span class="status-badge" :class="p.status?.toLowerCase()">
                    {{ p.status }}
                  </span>
                </td>
                <td>{{ formatDate(p.createdAt) }}</td>
                <td>
                  <router-link class="detail-link" :to="`/products/${p.id}`">
                    Ver detalle
                  </router-link>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="pagination-box">
          <button @click="previousPage" :disabled="page === 0">
            Anterior
          </button>

          <span class="pagination-text">
            Página {{ page + 1 }} de {{ totalPages }}
          </span>

          <button @click="nextPage" :disabled="page >= totalPages - 1">
            Siguiente
          </button>
        </div>
      </section>

      <section v-else class="empty-card">
        <h3>No se encontraron productos</h3>
        <p>Intenta cambiar los filtros o limpiar la búsqueda.</p>
      </section>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { getProducts } from "../api/productsApi"

const products = ref([])
const loading = ref(false)
const error = ref("")

const search = ref("")
const status = ref("")

const page = ref(0)
const size = ref(5)
const totalPages = ref(0)
const totalElements = ref(0)

const formatPrice = (value) => {
  return new Intl.NumberFormat("es-CO").format(value || 0)
}

const formatDate = (value) => {
  if (!value) return "-"
  return new Date(value).toLocaleString("es-CO")
}

const loadProducts = async () => {
  loading.value = true
  error.value = ""

  try {
    const params = {
      page: page.value,
      size: size.value,
      sortBy: "createdAt",
      direction: "desc"
    }

    if (search.value.trim()) {
      params.search = search.value.trim()
    }

    if (status.value) {
      params.status = status.value
    }

    const data = await getProducts(params)

    products.value = data.content
    totalPages.value = data.totalPages
    totalElements.value = data.totalElements
  } catch (e) {
    console.error(e)
    error.value = "Error cargando productos"
  } finally {
    loading.value = false
  }
}

const handleSearch = async () => {
  page.value = 0
  await loadProducts()
}

const clearFilters = async () => {
  search.value = ""
  status.value = ""
  page.value = 0
  size.value = 5
  await loadProducts()
}

const previousPage = async () => {
  if (page.value > 0) {
    page.value--
    await loadProducts()
  }
}

const nextPage = async () => {
  if (page.value < totalPages.value - 1) {
    page.value++
    await loadProducts()
  }
}

onMounted(loadProducts)
</script>

<style>
.products-page {
  max-width: 1200px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin-bottom: 6px;
}

.subtitle {
  margin: 0;
  color: #475569;
}

.filters-card,
.table-card,
.empty-card {
  background: white;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
}

.filters-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.field label {
  font-size: 14px;
  font-weight: bold;
  color: #334155;
}

.field input,
.field select {
  padding: 10px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
}

.filters-actions {
  display: flex;
  gap: 10px;
  margin-top: 18px;
}

.primary-btn,
.secondary-btn,
.pagination-box button {
  padding: 10px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.primary-btn {
  background: #2563eb;
  color: white;
}

.primary-btn:hover {
  background: #1d4ed8;
}

.secondary-btn {
  background: #e2e8f0;
  color: #0f172a;
}

.secondary-btn:hover {
  background: #cbd5e1;
}

.table-header {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 14px;
  color: #475569;
  font-size: 14px;
}

.table-wrapper {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

th,
td {
  border-bottom: 1px solid #e5e7eb;
  padding: 12px;
  text-align: left;
}

th {
  background: #f8fafc;
  color: #0f172a;
  font-size: 14px;
}

tr:hover {
  background: #f8fafc;
}

.price {
  font-weight: bold;
  color: #0f172a;
}

.status-badge {
  padding: 6px 12px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: bold;
  display: inline-block;
}

.status-badge.active {
  background: #dcfce7;
  color: #166534;
}

.status-badge.inactive {
  background: #fee2e2;
  color: #991b1b;
}

.detail-link {
  color: #2563eb;
  font-weight: bold;
  text-decoration: none;
}

.detail-link:hover {
  text-decoration: underline;
}

.pagination-box {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-top: 20px;
}

.pagination-box button {
  background: #2c3e50;
  color: white;
}

.pagination-box button:hover {
  background: #1a252f;
}

.pagination-box button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-text {
  font-weight: bold;
}

.info-msg {
  color: #334155;
}

.error-msg {
  color: #991b1b;
  background: #fee2e2;
  padding: 12px;
  border-radius: 6px;
}

.empty-card h3 {
  margin-top: 0;
}

.empty-card p {
  color: #475569;
}

@media (max-width: 900px) {
  .filters-grid {
    grid-template-columns: 1fr;
  }

  .table-header {
    flex-direction: column;
  }
}
</style>