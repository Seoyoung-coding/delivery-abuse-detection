<template>

  <div class="page">

    <h1>Profile</h1>


    <div class="user">

      <div class="avatar">
        👤
      </div>


      <div>

        <h2>
          Kim
        </h2>

        <p>
          kim@email.com
        </p>

      </div>

    </div>


    <div class="menu">

      <div class="menu-item">
        <span>📍 Delivery Address</span>
        <span>›</span>
      </div>

      <div class="menu-item">
        <span>💳 Payment Methods</span>
        <span>›</span>
      </div>

      <div class="menu-item">
        <span>📦 Order History</span>
        <span>›</span>
      </div>

      <div class="menu-item">
        <span>♡ Favorites</span>
        <span>›</span>
      </div>

      <div class="menu-item">
        <span>⚙ Settings</span>
        <span>›</span>
      </div>

    </div>


    <button
      class="logout"
      @click="logout"
    >
      Logout
    </button>


    <BottomNav />

  </div>

</template>


<script setup>

import { useRouter } from 'vue-router'

import BottomNav from '@/components/BottomNav.vue'


const router = useRouter()


const logout = () => {
  router.push('/login')
}

</script>


<style scoped>

.page {
  max-width: 520px;

  min-height: 100vh;

  margin: auto;

  padding: 25px 20px 100px;

  font-family: 'Montserrat', sans-serif;

  color: #3A251E;
}


.user {
  display: flex;

  align-items: center;

  gap: 15px;

  padding: 24px 0;
}


.avatar {
  width: 65px;
  height: 65px;

  display: flex;

  justify-content: center;
  align-items: center;

  background-color: #fff4eb;

  border-radius: 50%;

  font-size: 28px;
}


.user h2 {
  margin: 0 0 5px;

  font-size: 17px;
}


.user p {
  margin: 0;

  color: #9ca3af;

  font-size: 11px;
}


.menu {
  margin-top: 10px;
}


.menu-item {
  display: flex;

  justify-content: space-between;

  padding: 18px 4px;

  border-bottom: 1px solid #f3f4f6;

  font-size: 13px;

  cursor: pointer;
}


.logout {
  width: 100%;

  padding: 13px;

  margin-top: 30px;

  background-color: #fff4eb;

  color: #ff6b00;

  border: none;

  border-radius: 10px;

  font-weight: 700;

  cursor: pointer;
}

</style>