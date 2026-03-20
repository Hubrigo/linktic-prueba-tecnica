<template>
  <div class="rounded-xl border border-dashed border-slate-300 bg-slate-50 px-6 py-10 text-center">
    <h3 class="text-lg font-semibold text-slate-700">{{ title }}</h3>
    <p class="mt-2 text-sm text-slate-500">{{ description }}</p>
  </div>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: "Sin resultados"
  },
  description: {
    type: String,
    default: "No hay información para mostrar."
  }
})
</script>