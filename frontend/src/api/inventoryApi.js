import axios from "axios"

const api = axios.create({
  baseURL: "http://localhost:8082"
})

export const getInventoryByProductId = async (productId) => {
  const response = await api.get(`/inventory/${productId}`)
  return response.data
}

export const purchaseProduct = async (payload) => {
  const response = await api.post("/inventory/purchase", payload, {
    headers: {
      "Idempotency-Key": `purchase-${Date.now()}`
    }
  })
  return response.data
}