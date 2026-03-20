<template>
  <div class="mx-auto max-w-3xl space-y-6">
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-2xl font-bold text-slate-800">Crear inventario</h1>
        <p class="mt-1 text-sm text-slate-500">
          Asocia stock disponible a un producto existente.
        </p>
      </div>

      <router-link
        to="/products"
        class="inline-flex items-center rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-50"
      >
        Volver
      </router-link>
    </div>

    <div class="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
      <form @submit.prevent="handleSubmit" class="space-y-5">
        <div>
          <label class="mb-2 block text-sm font-semibold text-slate-700">
            Producto
          </label>

          <select
            v-model="form.productId"
            :disabled="productsLoading"
            :class="[
              inputBaseClasses,
              fieldErrors.productId ? inputStateClasses.error : inputStateClasses.normal,
              productsLoading ? 'cursor-not-allowed bg-slate-100' : ''
            ]"
          >
            <option value="">
              {{ productsLoading ? "Cargando productos..." : "Selecciona un producto" }}
            </option>
            <option
              v-for="product in products"
              :key="product.id"
              :value="product.id"
            >
              {{ product.name }} - {{ product.sku }}
            </option>
          </select>

          <p v-if="fieldErrors.productId" class="mt-1 text-sm text-red-600">
            {{ fieldErrors.productId }}
          </p>
        </div>

        <div>
          <label class="mb-2 block text-sm font-semibold text-slate-700">
            Stock disponible
          </label>
          <input
            v-model.number="form.available"
            type="number"
            min="0"
            step="1"
            placeholder="Ej: 10"
            :class="[
              inputBaseClasses,
              fieldErrors.available ? inputStateClasses.error : inputStateClasses.normal
            ]"
          />
          <p v-if="fieldErrors.available" class="mt-1 text-sm text-red-600">
            {{ fieldErrors.available }}
          </p>
        </div>

        <div class="space-y-3">
          <AppAlert
            v-if="productsError"
            type="error"
            :message="productsError"
          />

          <AppAlert
            v-if="saveError"
            type="error"
            :message="saveError"
          />

          <AppAlert
            v-if="saveMessage"
            type="success"
            :message="saveMessage"
          />

          <AppAlert
            v-if="!productsLoading && !products.length && !productsError"
            type="warning"
            message="No hay productos disponibles. Primero debes crear un producto."
          />
        </div>

        <div class="flex flex-col gap-3 sm:flex-row">
          <button
            type="submit"
            :disabled="saving || productsLoading || !products.length"
            :class="buttonClasses.primary"
          >
            {{ saving ? "Guardando..." : "Crear inventario" }}
          </button>

          <button
            type="button"
            @click="resetForm"
            :disabled="saving"
            :class="buttonClasses.secondary"
          >
            Limpiar
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted, onBeforeUnmount } from "vue"
import { storeToRefs } from "pinia"
import { useInventoryStore } from "../stores/inventoryStore"
import { useProductStore } from "../stores/productStore"
import AppAlert from "../components/AppAlert.vue"
import {
  buttonClasses,
  inputBaseClasses,
  inputStateClasses
} from "../utils/uiClasses"

const inventoryStore = useInventoryStore()
const productStore = useProductStore()

const { saving, saveError, saveMessage } = storeToRefs(inventoryStore)
const {
  products,
  loading: productsLoading,
  error: productsError
} = storeToRefs(productStore)

const initialForm = () => ({
  productId: "",
  available: null
})

const form = reactive(initialForm())

const fieldErrors = reactive({
  productId: "",
  available: ""
})

const clearFieldErrors = () => {
  fieldErrors.productId = ""
  fieldErrors.available = ""
}

const resetForm = () => {
  Object.assign(form, initialForm())
  clearFieldErrors()
  inventoryStore.clearMessages()
}

const validateForm = () => {
  clearFieldErrors()

  let isValid = true

  if (!form.productId) {
    fieldErrors.productId = "Debes seleccionar un producto"
    isValid = false
  }

  if (form.available === null || form.available === "" || Number.isNaN(Number(form.available))) {
    fieldErrors.available = "El stock es obligatorio"
    isValid = false
  } else if (Number(form.available) < 0) {
    fieldErrors.available = "El stock debe ser mayor o igual a 0"
    isValid = false
  }

  return isValid
}

const loadProducts = async () => {
  try {
    await productStore.fetchProducts({
      page: 0,
      size: 100
    })
  } catch (e) {
    console.error(e)
  }
}

const handleSubmit = async () => {
  inventoryStore.clearMessages()

  if (!validateForm()) return

  try {
    await inventoryStore.saveInventory({
      productId: form.productId,
      available: Number(form.available)
    })

    Object.assign(form, initialForm())
    clearFieldErrors()
  } catch (e) {
    console.error(e)
  }
}

onMounted(async () => {
  await loadProducts()
})

onBeforeUnmount(() => {
  inventoryStore.clearMessages()
})
</script>