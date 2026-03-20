<template>
  <div class="flex flex-col items-center justify-center gap-3 py-10 text-slate-500">
    <div class="h-8 w-8 animate-spin rounded-full border-4 border-slate-200 border-t-blue-600"></div>
    <p class="text-sm">{{ message }}</p>
  </div>
</template>

<script setup>
defineProps({
  message: {
    type: String,
    default: "Cargando..."
  }
})
</script>