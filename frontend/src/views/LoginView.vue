<template>
  <div class="login-page">
    <div class="login-card">
      <h2>Iniciar sesión</h2>
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="field">
          <label for="username">Usuario</label>
          <input
            id="username"
            v-model="username"
            type="text"
            placeholder="admin"
          />
        </div>

        <div class="field">
          <label for="password">Contraseña</label>
          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="admin123"
          />
        </div>

        <button type="submit" :disabled="loading">
          {{ loading ? "Ingresando..." : "Ingresar" }}
        </button>
      </form>

      <p v-if="successMessage" class="success-msg">{{ successMessage }}</p>
      <p v-if="errorMessage" class="error-msg">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue"
import { useRouter } from "vue-router"
import { login } from "../api/authApi"

const router = useRouter()

const username = ref("")
const password = ref("")
const loading = ref(false)
const errorMessage = ref("")
const successMessage = ref("")

const handleLogin = async () => {
  errorMessage.value = ""
  successMessage.value = ""

  try {
    loading.value = true

    const data = await login({
      username: username.value,
      password: password.value
    })

    localStorage.setItem("token", data.token)
    successMessage.value = "Login exitoso"

    setTimeout(() => {
      router.push("/products")
    }, 700)
  } catch (e) {
    console.error(e)
    errorMessage.value = "Usuario o contraseña inválidos"
  } finally {
    loading.value = false
  }
}
</script>

<style>
.login-page {
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.login-card {
  width: 100%;
  max-width: 420px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 24px;
}

.login-card h2 {
  margin-top: 0;
}

.subtitle {
  color: #475569;
  margin-bottom: 20px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.field label {
  font-weight: bold;
  color: #334155;
}

.field input {
  padding: 10px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
}

.login-form button {
  padding: 10px 16px;
  background: #2563eb;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.login-form button:hover {
  background: #1d4ed8;
}

.login-form button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.success-msg {
  color: #166534;
  background: #dcfce7;
  padding: 12px;
  border-radius: 6px;
  margin-top: 16px;
}

.error-msg {
  color: #991b1b;
  background: #fee2e2;
  padding: 12px;
  border-radius: 6px;
  margin-top: 16px;
}

.hint-box {
  margin-top: 20px;
  background: #f8fafc;
  padding: 14px;
  border-radius: 8px;
  color: #334155;
}
</style>