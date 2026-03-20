<template>
  <div class="min-h-screen bg-slate-50">
    <header class="sticky top-0 z-20 border-b border-slate-200 bg-slate-900 text-white shadow-sm">
      <div class="mx-auto flex max-w-7xl flex-col gap-4 px-6 py-4 lg:flex-row lg:items-center lg:justify-between">
        <div class="flex flex-col gap-4 lg:flex-row lg:items-center lg:gap-8">
          <router-link
            to="/products"
            class="text-lg font-bold tracking-tight text-white transition hover:text-slate-200"
          >
            Prueba Técnica Full Stack
          </router-link>

          <nav class="flex flex-wrap items-center gap-2 text-sm">
            <router-link
              to="/products"
              :class="navLinkClass('/products')"
            >
              Productos
            </router-link>

            <router-link
              v-if="isLogged"
              to="/products/create"
              :class="navLinkClass('/products/create')"
            >
              Crear producto
            </router-link>

            <router-link
              v-if="isLogged"
              to="/inventory/create"
              :class="navLinkClass('/inventory/create')"
            >
              Crear inventario
            </router-link>

            <router-link
              v-if="!isLogged"
              to="/login"
              :class="navLinkClass('/login')"
            >
              Login
            </router-link>
          </nav>
        </div>

        <div class="flex items-center gap-3">
          <span
            v-if="isLogged"
            class="hidden rounded-full bg-emerald-500/15 px-3 py-1 text-xs font-semibold text-emerald-300 sm:inline-flex"
          >
            Sesión activa
          </span>

          <button
            v-if="isLogged"
            @click="handleLogout"
            class="rounded-lg bg-red-500 px-4 py-2 text-sm font-medium text-white transition hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-300"
          >
            Cerrar sesión
          </button>
        </div>
      </div>
    </header>

    <main class="mx-auto max-w-7xl px-6 py-6">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { storeToRefs } from "pinia"
import { useRoute, useRouter } from "vue-router"
import { useAuthStore } from "./stores/authStore"

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const { isLogged } = storeToRefs(authStore)

const handleLogout = () => {
  authStore.logout()
  router.push("/login")
}

const navLinkClass = (path) => {
  const isActive = route.path === path

  return [
    "rounded-lg px-3 py-2 font-medium transition",
    isActive
      ? "bg-white/10 text-white"
      : "text-slate-200 hover:bg-white/5 hover:text-white"
  ]
}
</script>