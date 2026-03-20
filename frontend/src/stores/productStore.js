import { defineStore } from "pinia"
import { ref } from "vue"
import { getProducts, getProductById, createProduct } from "../api/productsApi"

export const useProductStore = defineStore("products", () => {
  const products = ref([])
  const product = ref(null)
  const loading = ref(false)
  const saving = ref(false)
  const error = ref("")
  const saveError = ref("")
  const saveMessage = ref("")

  const meta = ref({
    page: 0,
    size: 5,
    totalPages: 0,
    totalElements: 0
  })

  const fetchProducts = async (params = {}) => {
    loading.value = true
    error.value = ""

    try {
      const response = await getProducts(params)
      products.value = response.data || []
      meta.value = {
        page: response.meta?.page ?? 0,
        size: response.meta?.size ?? 5,
        totalPages: response.meta?.totalPages ?? 0,
        totalElements: response.meta?.totalElements ?? 0
      }
    } catch (e) {
      error.value = "Error cargando productos"
      throw e
    } finally {
      loading.value = false
    }
  }

  const fetchProductById = async (id) => {
    loading.value = true
    error.value = ""

    try {
      const response = await getProductById(id)
      product.value = response
      return response
    } catch (e) {
      error.value = "Error cargando el producto"
      throw e
    } finally {
      loading.value = false
    }
  }

  const saveProduct = async (payload) => {
    saving.value = true
    saveError.value = ""
    saveMessage.value = ""

    try {
      const response = await createProduct(payload)
      saveMessage.value = "Producto creado exitosamente"
      return response
    } catch (e) {
      if (e.response?.status === 400) {
        saveError.value = "Datos inválidos para crear el producto"
      } else if (e.response?.status === 409) {
        saveError.value = "Ya existe un producto con ese SKU"
      } else {
        saveError.value = "Error creando el producto"
      }
      throw e
    } finally {
      saving.value = false
    }
  }

  const clearProduct = () => {
    product.value = null
  }

  const clearSaveMessages = () => {
  error.value = ""
  saveError.value = ""
  saveMessage.value = ""
}

  return {
    products,
    product,
    loading,
    saving,
    error,
    saveError,
    saveMessage,
    meta,
    fetchProducts,
    fetchProductById,
    saveProduct,
    clearProduct,
    clearSaveMessages
  }
})