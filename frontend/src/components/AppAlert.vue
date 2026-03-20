<template>
  <div :class="alertClass">
    {{ message }}
  </div>
</template>

<script setup>
import { computed } from "vue"
import { alertClasses } from "../utils/uiClasses"

const props = defineProps({
  type: {
    type: String,
    default: "info"
  },
  message: {
    type: String,
    required: true
  }
})

const alertClass = computed(() => {
  return alertClasses[props.type] || alertClasses.info
})
</script>