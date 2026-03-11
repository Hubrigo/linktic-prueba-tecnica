import { createRouter, createWebHistory } from "vue-router"

import ProductsView from "../views/ProductsView.vue"
import ProductDetailView from "../views/ProductDetailView.vue"
import LoginView from "../views/LoginView.vue"

const routes = [
  {
    path: "/",
    redirect: "/products"
  },
  {
    path: "/login",
    component: LoginView,
    meta: { public: true }
  },
  {
    path: "/products",
    component: ProductsView
  },
  {
    path: "/products/:id",
    component: ProductDetailView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {

  const token = localStorage.getItem("token")

  if (!to.meta.public && !token) {
    next("/login")
    return
  }

  if (to.path === "/login" && token) {
    next("/products")
    return
  }

  next()
})

export default router