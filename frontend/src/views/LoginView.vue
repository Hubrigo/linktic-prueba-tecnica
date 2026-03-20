<template>
  <div class="flex justify-center pt-6 md:pt-10">
    <div class="w-full max-w-md rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
      <div class="mb-6">
        <h2 class="text-2xl font-bold text-slate-800">Iniciar sesión</h2>
        <p class="mt-2 text-sm text-slate-500">
          Ingresa tus credenciales para acceder al módulo de productos e inventario.
        </p>
      </div>

      <form @submit.prevent="handleLogin" class="space-y-4">
        <div>
          <label for="username" class="mb-2 block text-sm font-semibold text-slate-700">
            Usuario
          </label>
          <input
            id="username"
            v-model="username"
            type="text"
            placeholder="admin"
            class="w-full rounded-lg border border-slate-300 px-3 py-2.5 text-slate-800 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
          />
        </div>

        <div>
          <label for="password" class="mb-2 block text-sm font-semibold text-slate-700">
            Contraseña
          </label>
          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="admin123"
            class="w-full rounded-lg border border-slate-300 px-3 py-2.5 text-slate-800 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
          />
        </div>

        <button
          type="submit"
          :disabled="loading"
          class="w-full rounded-lg bg-blue-600 px-4 py-2.5 font-medium text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {{ loading ? "Ingresando..." : "Ingresar" }}
        </button>
      </form>

      <div
        v-if="successMessage"
        class="mt-4 rounded-lg border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm text-emerald-700"
      >
        {{ successMessage }}
      </div>

      <div
        v-if="error"
        class="mt-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
      >
        {{ error }}
      </div>

      <div class="mt-6 rounded-lg bg-slate-50 px-4 py-3 text-sm text-slate-600">
        <p class="font-semibold text-slate-700">Credenciales de prueba</p>
        <p class="mt-1">Usuario: <span class="font-medium">admin</span></p>
        <p>Contraseña: <span class="font-medium">admin123</span></p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue"
import { storeToRefs } from "pinia"
import { useRouter } from "vue-router"
import { useAuthStore } from "../stores/authStore"

const router = useRouter()
const authStore = useAuthStore()

const { loading, error } = storeToRefs(authStore)

const username = ref("")
const password = ref("")
const successMessage = ref("")

const handleLogin = async () => {
  successMessage.value = ""

  try {
    const ok = await authStore.login({
      username: username.value,
      password: password.value
    })

    if (ok) {
      successMessage.value = "Login exitoso"

      setTimeout(() => {
        router.push("/products")
      }, 700)
    }
  } catch (e) {
    console.error(e)
  }
}
</script>