import { defineStore } from "pinia"
import { ref } from "vue"
import {
  getInventoryByProductId,
  purchaseProduct,
  createInventory
} from "../api/inventoryApi"

export const useInventoryStore = defineStore("inventory", () => {
  const inventory = ref(null)
  const loading = ref(false)
  const saving = ref(false)
  const purchasing = ref(false)
  const error = ref("")
  const saveError = ref("")
  const saveMessage = ref("")
  const purchaseError = ref("")
  const purchaseMessage = ref("")

  const fetchInventoryByProductId = async (productId) => {
    loading.value = true
    error.value = ""
    inventory.value = null

    try {
      const response = await getInventoryByProductId(productId)
      inventory.value = response
      return response
    } catch (e) {
      if (e.response?.status === 404) {
        error.value = "No hay inventario registrado para este producto"
      } else {
        error.value = "No se pudo cargar el inventario"
      }
      throw e
    } finally {
      loading.value = false
    }
  }

  const saveInventory = async (payload) => {
    saving.value = true
    saveError.value = ""
    saveMessage.value = ""

    try {
      const response = await createInventory(payload)
      saveMessage.value = "Inventario creado exitosamente"
      return response
    } catch (e) {
      if (e.response?.status === 400) {
        saveError.value = "Datos inválidos para crear el inventario"
      } else if (e.response?.status === 409) {
        saveError.value = "Ya existe inventario para este producto"
      } else {
        saveError.value = "Error creando el inventario"
      }
      throw e
    } finally {
      saving.value = false
    }
  }

  const setPurchaseError = (message) => {
    purchaseError.value = message
  }

  const buyProduct = async (payload) => {
    purchasing.value = true
    purchaseError.value = ""
    purchaseMessage.value = ""

    try {
      const response = await purchaseProduct(payload)
      purchaseMessage.value = `Compra realizada con éxito. Stock restante: ${response.remainingStock}`
      return response
    } catch (e) {
      if (e.response?.status === 409) {
        purchaseError.value = "No hay stock suficiente"
      } else if (e.response?.status === 404) {
        purchaseError.value = "No se encontró inventario para este producto"
      } else if (e.response?.status === 503) {
        purchaseError.value = "El servicio no está disponible en este momento"
      } else {
        purchaseError.value = "Error al realizar la compra"
      }
      throw e
    } finally {
      purchasing.value = false
    }
  }

  const clearMessages = () => {
    error.value = ""
    saveError.value = ""
    saveMessage.value = ""
    purchaseError.value = ""
    purchaseMessage.value = ""
  }

  return {
    inventory,
    loading,
    saving,
    purchasing,
    error,
    saveError,
    saveMessage,
    purchaseError,
    purchaseMessage,
    fetchInventoryByProductId,
    saveInventory,
    setPurchaseError,
    buyProduct,
    clearMessages
  }
})