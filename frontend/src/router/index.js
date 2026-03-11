import { createRouter, createWebHistory } from 'vue-router'
import ProductsView from '../views/ProductsView.vue'
import ProductDetailView from '../views/ProductDetailView.vue'
import LoginView from '../views/LoginView.vue'

const routes = [
  {
    path: '/',
    redirect: '/products'
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/products',
    name: 'products',
    component: ProductsView
  },
  {
    path: '/products/:id',
    name: 'product-detail',
    component: ProductDetailView,
    props: true
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router