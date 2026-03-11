import axios from "axios"

const api = axios.create({
  baseURL: "http://localhost:8081"
})

export const login = async (credentials) => {
  const response = await api.post("/auth/login", credentials)
  return response.data
}