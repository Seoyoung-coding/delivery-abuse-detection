import { createRouter, createWebHistory } from 'vue-router'

// =========================
// Customer
// =========================

import LoginPage from '@/views/customer/LoginPage.vue'
import SignupPage from '@/views/customer/SignupPage.vue'
import HomePage from '@/views/customer/HomePage.vue'
import SearchPage from '@/views/customer/SearchPage.vue'
import CategoriesPage from '@/views/customer/CategoriesPage.vue'
import CartPage from '@/views/customer/CartPage.vue'
import OrderPage from '@/views/customer/OrderPage.vue'
import ProfilePage from '@/views/customer/ProfilePage.vue'

// =========================
// Admin
// =========================

import AdminLoginPage from '@/views/Admin/AdminLoginPage.vue'
import AdminHomePage from '@/views/Admin/AdminHomePage.vue'
import SellerRequestsPage from '@/views/Admin/SellerRequestsPage.vue'
import SellersPage from '@/views/Admin/SellersPage.vue'
import ReportsPage from '@/views/Admin/ReportsPage.vue'
import AdminChatPage from '@/views/Admin/AdminChatPage.vue'


const routes = [

  // =========================
  // Default
  // =========================

  {
    path: '/',
    redirect: '/login'
  },


  // =========================
  // Customer
  // =========================

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
  },


  // =========================
  // Admin
  // =========================

  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: AdminLoginPage
  },

  {
    path: '/admin',
    name: 'AdminHome',
    component: AdminHomePage
  },

  {
    path: '/admin/requests',
    name: 'SellerRequests',
    component: SellerRequestsPage
  },

  {
    path: '/admin/sellers',
    name: 'Sellers',
    component: SellersPage
  },

  {
    path: '/admin/reports',
    name: 'Reports',
    component: ReportsPage
  },

  {
    path: '/admin/chat',
    name: 'AdminChat',
    component: AdminChatPage
  }

]


const router = createRouter({

  history: createWebHistory(),

  routes

})


export default router