import axios from "axios"

const api = axios.create({
  baseURL: "http://localhost:8081"
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

export const getProducts = async (params = {}) => {
  const response = await api.get("/products", { params })

  return {
    data: response.data.data.map(mapResource),
    meta: response.data.meta
  }
}

export const getProductById = async (id) => {
  const response = await api.get(`/products/${id}`)
  return mapResource(response.data.data)
}

export const createProduct = async (payload) => {
  const response = await api.post("/products", payload)
  return mapResource(response.data.data)
}