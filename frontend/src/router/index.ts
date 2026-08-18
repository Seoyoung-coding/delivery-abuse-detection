import { createRouter, createWebHistory } from 'vue-router'

import LoginPage from '@/views/LoginPage.vue'
import SignupPage from '@/views/SignupPage.vue'
import HomePage from '@/views/HomePage.vue'
import SearchPage from '@/views/SearchPage.vue'
import CategoriesPage from '@/views/customer/CategoriesPage.vue'
import CartPage from '@/views/customer/CartPage.vue'
import OrderPage from '@/views/OrderPage.vue'
import ProfilePage from '@/views/ProfilePage.vue'
import AdminHomePage from '@/views/admin/AdminHomePage.vue'
import SellerRequestsPage from '@/views/admin/SellerRequestsPage.vue'
import SellersPage from '@/views/admin/SellersPage.vue'
import ReportsPage from '@/views/admin/ReportsPage.vue'
import AdminChatPage from '@/views/admin/AdminChatPage.vue'


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
  },

  {
  path: '/admin',
  component: AdminHomePage
},

{
  path: '/admin/requests',
  component: SellerRequestsPage
},

{
  path: '/admin/sellers',
  component: SellersPage
},

{
  path: '/admin/reports',
  component: ReportsPage
},

{
  path: '/admin/chat',
  component: AdminChatPage
}

]


const router = createRouter({
  history: createWebHistory(),
  routes
})


export default router