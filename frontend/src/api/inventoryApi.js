import axios from "axios"

const api = axios.create({
  baseURL: "http://localhost:8082"
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token")
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

const mapResource = (resource) => ({
  id: resource.id,
  ...resource.attributes
})

export const getInventoryByProductId = async (productId) => {
  const response = await api.get(`/inventory/${productId}`)
  return mapResource(response.data.data)
}

export const createInventory = async (payload) => {
  const response = await api.post("/inventory", payload)
  return mapResource(response.data.data)
}

export const purchaseProduct = async (payload) => {
  const response = await api.post("/inventory/purchase", payload, {
    headers: {
      "Idempotency-Key": `purchase-${Date.now()}`
    }
  })
  return mapResource(response.data.data)
}