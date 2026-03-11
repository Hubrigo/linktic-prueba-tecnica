<template>
  <div>

    <header class="navbar">
      <div class="navbar-left">
        <h2 class="logo">Prueba Técnica Full Stack</h2>

        <nav class="nav-links">
          <router-link to="/products">Productos</router-link>
          <router-link to="/login">Login</router-link>
        </nav>
      </div>

      <div class="navbar-right">
        <button v-if="isLogged" @click="logout" class="logout-btn">
          Cerrar sesión
        </button>
      </div>
    </header>

    <main class="container">
      <router-view />
    </main>

  </div>
</template>

<script setup>

import { computed } from "vue"
import { useRouter } from "vue-router"

const router = useRouter()

const isLogged = computed(() => {
  return !!localStorage.getItem("token")
})

const logout = () => {
  localStorage.removeItem("token")
  router.push("/login")
}

</script>

<style>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;

  background: #1e293b;
  color: white;
  padding: 14px 30px;
}

.logo {
  margin: 0;
}

.navbar-left {
  display: flex;
  align-items: center;
  gap: 30px;
}

.nav-links {
  display: flex;
  gap: 20px;
}

.nav-links a {
  color: #cbd5f5;
  text-decoration: none;
  font-weight: 500;
}

.nav-links a:hover {
  color: white;
}

.navbar-right {
  display: flex;
  align-items: center;
}

.logout-btn {
  padding: 6px 14px;
  background: #ef4444;
  border: none;
  border-radius: 6px;
  color: white;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.logout-btn:hover {
  background: #dc2626;
}
.container {
  margin-top: 20px;
}
</style>