<template>
  <div class="app-container">
    <header class="app-header">
      <h1>Prueba Técnica Full Stack</h1>
      <nav class="app-nav">
        <router-link to="/products">Productos</router-link>
        <router-link to="/login">Login</router-link>
      </nav>
    </header>

    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>

<script setup>
</script>

<style>
body {
  margin: 0;
  font-family: Arial, sans-serif;
  background: #f5f5f5;
  color: #222;
}

#app {
  min-height: 100vh;
}

.app-container {
  min-height: 100vh;
}

.app-header {
  background: #1f2937;
  color: white;
  padding: 16px 24px;
}

.app-header h1 {
  margin: 0 0 12px 0;
  font-size: 24px;
}

.app-nav {
  display: flex;
  gap: 16px;
}

.app-nav a {
  color: white;
  text-decoration: none;
  font-weight: bold;
}

.app-nav a.router-link-active {
  text-decoration: underline;
}

.app-main {
  padding: 24px;
}
</style>