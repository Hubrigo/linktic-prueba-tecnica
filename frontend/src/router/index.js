import { createRouter, createWebHistory } from "vue-router"
import { useAuthStore } from "../stores/authStore"

import ProductsView from "../views/ProductsView.vue"
import ProductDetailView from "../views/ProductDetailView.vue"
import LoginView from "../views/LoginView.vue"
import CreateProductView from "../views/CreateProductView.vue"
import CreateInventoryView from "../views/CreateInventoryView.vue"

const routes = [
  {
    path: "/",
    redirect: { name: "products" }
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
    meta: { public: true }
  },
  {
    path: "/products",
    name: "products",
    component: ProductsView,
    meta: { requiresAuth: true }
  },
  {
    path: "/products/create",
    name: "create-product",
    component: CreateProductView,
    meta: { requiresAuth: true }
  },
  {
    path: "/products/:id",
    name: "product-detail",
    component: ProductDetailView,
    meta: { requiresAuth: true }
  },
  {
    path: "/inventory/create",
    name: "create-inventory",
    component: CreateInventoryView,
    meta: { requiresAuth: true }
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: { name: "products" }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.isLogged) {
    return { name: "login" }
  }

  if (to.name === "login" && authStore.isLogged) {
    return { name: "products" }
  }

  return true
})

export default router