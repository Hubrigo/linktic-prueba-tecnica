<template>
  <div class="space-y-6">
    <div class="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
      <div class="flex flex-col gap-5 xl:flex-row xl:items-end xl:justify-between">
        <div>
          <h1 class="text-2xl font-bold text-slate-800">Productos</h1>
          <p class="mt-1 text-sm text-slate-500">
            Consulta, filtra y navega por el catálogo de productos registrados.
          </p>
        </div>

        <div class="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-3">
          <div>
            <label class="mb-2 block text-sm font-semibold text-slate-700">
              Buscar
            </label>
            <input
              v-model="filters.search"
              type="text"
              placeholder="Buscar por nombre o SKU"
              :class="[inputBaseClasses, inputStateClasses.normal]"
            />
          </div>

          <div>
            <label class="mb-2 block text-sm font-semibold text-slate-700">
              Estado
            </label>
            <select
              v-model="filters.status"
              :class="[inputBaseClasses, inputStateClasses.normal]"
            >
              <option value="">Todos</option>
              <option value="ACTIVE">ACTIVE</option>
              <option value="INACTIVE">INACTIVE</option>
            </select>
          </div>

          <div class="flex items-end gap-2">
            <button
              @click="applyFilters"
              :disabled="loading"
              :class="buttonClasses.primary"
            >
              Filtrar
            </button>

            <button
              @click="clearFilters"
              :disabled="loading"
              :class="buttonClasses.secondary"
            >
              Limpiar
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="rounded-xl border border-slate-200 bg-white shadow-sm">
      <div class="border-b border-slate-200 px-6 py-4">
        <div class="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
          <h2 class="text-lg font-semibold text-slate-800">Listado de productos</h2>
          <p class="text-sm text-slate-500">
            Total encontrados:
            <span class="font-semibold text-slate-700">
              {{ meta.totalElements }}
            </span>
          </p>
        </div>
      </div>

      <div class="p-6">
        <AppAlert
          v-if="error"
          type="error"
          :message="error"
        />

        <AppLoader
          v-else-if="loading"
          message="Cargando productos..."
        />

        <EmptyState
          v-else-if="!products.length"
          title="No se encontraron productos"
          description="Prueba ajustando los filtros o crea un nuevo producto desde la barra de navegación."
        />

        <div v-else class="space-y-4">
          <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-slate-200">
              <thead class="bg-slate-50">
                <tr>
                  <th class="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wider text-slate-500">
                    SKU
                  </th>
                  <th class="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wider text-slate-500">
                    Nombre
                  </th>
                  <th class="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wider text-slate-500">
                    Precio
                  </th>
                  <th class="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wider text-slate-500">
                    Estado
                  </th>
                  <th class="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wider text-slate-500">
                    Acción
                  </th>
                </tr>
              </thead>

              <tbody class="divide-y divide-slate-200 bg-white">
                <tr
                  v-for="product in products"
                  :key="product.id"
                  class="transition hover:bg-slate-50"
                >
                  <td class="px-4 py-4 text-sm text-slate-700">
                    {{ product.sku }}
                  </td>
                  <td class="px-4 py-4 text-sm font-medium text-slate-800">
                    {{ product.name }}
                  </td>
                  <td class="px-4 py-4 text-sm text-slate-700">
                    {{ formatPrice(product.price) }}
                  </td>
                  <td class="px-4 py-4 text-sm">
                    <span
                      :class="product.status === 'ACTIVE'
                        ? 'inline-flex rounded-full bg-emerald-100 px-2.5 py-1 text-xs font-semibold text-emerald-700'
                        : 'inline-flex rounded-full bg-slate-200 px-2.5 py-1 text-xs font-semibold text-slate-700'"
                    >
                      {{ product.status }}
                    </span>
                  </td>
                  <td class="px-4 py-4 text-sm">
                    <router-link
                      :to="`/products/${product.id}`"
                      class="font-medium text-blue-600 transition hover:text-blue-700"
                    >
                      Ver detalle
                    </router-link>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <div
            v-if="meta.totalPages > 0"
            class="flex flex-col gap-4 border-t border-slate-200 pt-4 sm:flex-row sm:items-center sm:justify-between"
          >
            <p class="text-sm text-slate-500">
              Página
              <span class="font-semibold text-slate-700">
                {{ currentPage + 1 }}
              </span>
              de
              <span class="font-semibold text-slate-700">
                {{ meta.totalPages }}
              </span>
            </p>

            <div class="flex gap-2">
              <button
                @click="changePage(currentPage - 1)"
                :disabled="currentPage === 0 || loading"
                :class="buttonClasses.secondary"
              >
                Anterior
              </button>

              <button
                @click="changePage(currentPage + 1)"
                :disabled="currentPage + 1 >= meta.totalPages || loading"
                :class="buttonClasses.secondary"
              >
                Siguiente
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue"
import { storeToRefs } from "pinia"
import { useProductStore } from "../stores/productStore"
import AppAlert from "../components/AppAlert.vue"
import AppLoader from "../components/AppLoader.vue"
import EmptyState from "../components/EmptyState.vue"
import {
  buttonClasses,
  inputBaseClasses,
  inputStateClasses
} from "../utils/uiClasses"

const productStore = useProductStore()
const { products, loading, error, meta } = storeToRefs(productStore)

const currentPage = ref(0)

const filters = reactive({
  search: "",
  status: ""
})

const loadProducts = async () => {
  try {
    await productStore.fetchProducts({
      page: currentPage.value,
      size: 5,
      search: filters.search || undefined,
      status: filters.status || undefined,
      sortBy: "createdAt",
      direction: "desc"
    })
  } catch (e) {
    console.error(e)
  }
}

const applyFilters = async () => {
  currentPage.value = 0
  await loadProducts()
}

const clearFilters = async () => {
  filters.search = ""
  filters.status = ""
  currentPage.value = 0
  await loadProducts()
}

const changePage = async (page) => {
  if (page < 0 || page >= meta.value.totalPages) return
  currentPage.value = page
  await loadProducts()
}

const formatPrice = (value) => {
  return new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
    maximumFractionDigits: 0
  }).format(value)
}

onMounted(async () => {
  await loadProducts()
})
</script>