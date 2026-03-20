import { defineStore } from "pinia"
import { computed, ref } from "vue"
import { login as loginApi } from "../api/authApi"

export const useAuthStore = defineStore("auth", () => {
  const token = ref(localStorage.getItem("token") || "")
  const loading = ref(false)
  const error = ref("")

  const isLogged = computed(() => !!token.value)

  const login = async (credentials) => {
    loading.value = true
    error.value = ""

    try {
      const data = await loginApi(credentials)
      token.value = data.token
      localStorage.setItem("token", data.token)
      return true
    } catch (e) {
      error.value = "Usuario o contraseña inválidos"
      return false
    } finally {
      loading.value = false
    }
  }

  const logout = () => {
    token.value = ""
    localStorage.removeItem("token")
  }

  return {
    token,
    loading,
    error,
    isLogged,
    login,
    logout
  }
})