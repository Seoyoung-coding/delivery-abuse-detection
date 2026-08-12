import { createRouter, createWebHistory } from 'vue-router'

import LoginPage from '@/views/LoginPage.vue'
import SignupPage from '@/views/SignupPage.vue'
import HomePage from '@/views/HomePage.vue'
import SearchPage from '@/views/SearchPage.vue'
import CategoriesPage from '@/views/CategoriesPage.vue'
import CartPage from '@/views/CartPage.vue'
import OrderPage from '@/views/OrderPage.vue'
import ProfilePage from '@/views/ProfilePage.vue'


const routes = [

  {
    path: '/',
    redirect: '/login'
  },

  {
    path: '/login',
    name: 'Login',
    component: LoginPage
  },

  {
    path: '/signup',
    name: 'Signup',
    component: SignupPage
  },

  {
    path: '/home',
    name: 'Home',
    component: HomePage
  },

  {
    path: '/search',
    name: 'Search',
    component: SearchPage
  },

  {
    path: '/categories',
    name: 'Categories',
    component: CategoriesPage
  },

  {
    path: '/cart',
    name: 'Cart',
    component: CartPage
  },

  {
    path: '/order',
    name: 'Order',
    component: OrderPage
  },

  {
    path: '/profile',
    name: 'Profile',
    component: ProfilePage
  }

]


const router = createRouter({
  history: createWebHistory(),
  routes
})


export default router