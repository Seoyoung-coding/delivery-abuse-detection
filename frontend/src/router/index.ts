import {
  createRouter,
  createWebHistory
} from 'vue-router'


// ======================================================
// Customer
// ======================================================

import LoginPage from '@/views/customer/LoginPage.vue'
import SignupPage from '@/views/customer/SignupPage.vue'
import HomePage from '@/views/customer/HomePage.vue'
import SearchPage from '@/views/customer/SearchPage.vue'
import CategoriesPage from '@/views/customer/CategoriesPage.vue'
import CartPage from '@/views/customer/CartPage.vue'
import OrderPage from '@/views/customer/OrderPage.vue'
import ProfilePage from '@/views/customer/ProfilePage.vue'
import SettingPage from '@/views/customer/SettingPage.vue'


// ======================================================
// Seller
// ======================================================

import SellerApplicationPage from '@/views/seller/SellerApplicationPage.vue'

import StoreDetailPage from '@/views/seller/StoreDetailPage.vue'

import SellerChatPage from '@/views/seller/SellerChatPage.vue'

import ProductRegisterPage from '@/views/seller/ProductRegisterPage.vue'

// ======================================================
// Admin
// ======================================================

import AdminLoginPage from '@/views/admin/AdminLoginPage.vue'

import AdminHomePage from '@/views/admin/AdminHomePage.vue'

import SellerRequestsPage from '@/views/admin/SellerRequestsPage.vue'

import SellersPage from '@/views/admin/SellersPage.vue'

import ReportsPage from '@/views/admin/ReportsPage.vue'

import AdminChatPage from '@/views/admin/AdminChatPage.vue'



// ======================================================
// Routes
// ======================================================

const routes = [

  // ====================================================
  // Default
  // ====================================================

  {
    path: '/',
    redirect: '/login'
  },


  // ====================================================
  // Customer
  // ====================================================

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


  {
    path: '/settings',
    name: 'Settings',
    component: SettingPage
  },


  // ====================================================
  // Seller
  // ====================================================

  {
    path: '/seller-application',
    name: 'SellerApplication',
    component: SellerApplicationPage
  },


  // 승인된 Seller의 My Store 관리 페이지
  {
    path: '/seller/store',
    name: 'StoreDetail',
    component: StoreDetailPage
  },

  {
  path: '/seller/chat',
  name: 'seller-chat',
  component: SellerChatPage
  },

  {
  path: '/seller/products/register',
  name: 'product-register',
  component: ProductRegisterPage
  },

  // ====================================================
  // Admin
  // ====================================================

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
  name: 'admin-chat',
  component: AdminChatPage
  }

]


// ======================================================
// Router 생성
// ======================================================

const router = createRouter({

  history: createWebHistory(),

  routes

})


export default router