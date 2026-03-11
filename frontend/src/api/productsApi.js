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

export const getProducts = async (params = {}) => {
  const response = await api.get("/products", { params })
  return response.data
}

export const getProductById = async (id) => {
  const response = await api.get(`/products/${id}`)
  return response.data
}