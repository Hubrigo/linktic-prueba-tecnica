<template>
  <div class="mx-auto max-w-3xl space-y-6">
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-2xl font-bold text-slate-800">Crear producto</h1>
        <p class="mt-1 text-sm text-slate-500">
          Registra un nuevo producto en el catálogo del sistema.
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
        <div class="grid gap-5 md:grid-cols-2">
          <div>
            <label class="mb-2 block text-sm font-semibold text-slate-700">
              SKU
            </label>
            <input
              v-model="form.sku"
              type="text"
              placeholder="Ej: SKU-001"
              :class="[
                inputBaseClasses,
                fieldErrors.sku ? inputStateClasses.error : inputStateClasses.normal
              ]"
            />
            <p v-if="fieldErrors.sku" class="mt-1 text-sm text-red-600">
              {{ fieldErrors.sku }}
            </p>
          </div>

          <div>
            <label class="mb-2 block text-sm font-semibold text-slate-700">
              Estado
            </label>
            <select
              v-model="form.status"
              :class="[
                inputBaseClasses,
                fieldErrors.status ? inputStateClasses.error : inputStateClasses.normal
              ]"
            >
              <option value="">Selecciona un estado</option>
              <option value="ACTIVE">ACTIVE</option>
              <option value="INACTIVE">INACTIVE</option>
            </select>
            <p v-if="fieldErrors.status" class="mt-1 text-sm text-red-600">
              {{ fieldErrors.status }}
            </p>
          </div>
        </div>

        <div>
          <label class="mb-2 block text-sm font-semibold text-slate-700">
            Nombre
          </label>
          <input
            v-model="form.name"
            type="text"
            placeholder="Ej: Teclado mecánico"
            :class="[
              inputBaseClasses,
              fieldErrors.name ? inputStateClasses.error : inputStateClasses.normal
            ]"
          />
          <p v-if="fieldErrors.name" class="mt-1 text-sm text-red-600">
            {{ fieldErrors.name }}
          </p>
        </div>

        <div>
          <label class="mb-2 block text-sm font-semibold text-slate-700">
            Precio
          </label>
          <input
            v-model.number="form.price"
            type="number"
            min="0"
            step="1"
            placeholder="Ej: 150000"
            :class="[
              inputBaseClasses,
              fieldErrors.price ? inputStateClasses.error : inputStateClasses.normal
            ]"
          />
          <p v-if="fieldErrors.price" class="mt-1 text-sm text-red-600">
            {{ fieldErrors.price }}
          </p>
        </div>

        <div class="space-y-3">
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
        </div>

        <div class="flex flex-col gap-3 sm:flex-row">
          <button
            type="submit"
            :disabled="saving"
            :class="buttonClasses.primary"
          >
            {{ saving ? "Guardando..." : "Crear producto" }}
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
import { reactive, onBeforeUnmount } from "vue"
import { storeToRefs } from "pinia"
import { useProductStore } from "../stores/productStore"
import AppAlert from "../components/AppAlert.vue"
import {
  buttonClasses,
  inputBaseClasses,
  inputStateClasses
} from "../utils/uiClasses"

const productStore = useProductStore()
const { saving, saveError, saveMessage } = storeToRefs(productStore)

const initialForm = () => ({
  sku: "",
  name: "",
  price: null,
  status: "ACTIVE"
})

const form = reactive(initialForm())

const fieldErrors = reactive({
  sku: "",
  name: "",
  price: "",
  status: ""
})

const clearFieldErrors = () => {
  fieldErrors.sku = ""
  fieldErrors.name = ""
  fieldErrors.price = ""
  fieldErrors.status = ""
}

const resetForm = () => {
  Object.assign(form, initialForm())
  clearFieldErrors()
  productStore.clearSaveMessages()
}

const validateForm = () => {
  clearFieldErrors()

  let isValid = true

  if (!form.sku || !form.sku.trim()) {
    fieldErrors.sku = "El SKU es obligatorio"
    isValid = false
  }

  if (!form.name || !form.name.trim()) {
    fieldErrors.name = "El nombre es obligatorio"
    isValid = false
  }

  if (form.price === null || form.price === "" || Number.isNaN(Number(form.price))) {
    fieldErrors.price = "El precio es obligatorio"
    isValid = false
  } else if (Number(form.price) < 0) {
    fieldErrors.price = "El precio debe ser mayor o igual a 0"
    isValid = false
  }

  if (!form.status) {
    fieldErrors.status = "Debes seleccionar un estado"
    isValid = false
  }

  return isValid
}

const handleSubmit = async () => {
  productStore.clearSaveMessages()

  if (!validateForm()) return

  try {
    await productStore.saveProduct({
      sku: form.sku.trim(),
      name: form.name.trim(),
      price: Number(form.price),
      status: form.status
    })

    Object.assign(form, initialForm())
    clearFieldErrors()
  } catch (e) {
    console.error(e)
  }
}

onBeforeUnmount(() => {
  productStore.clearSaveMessages()
})
</script>