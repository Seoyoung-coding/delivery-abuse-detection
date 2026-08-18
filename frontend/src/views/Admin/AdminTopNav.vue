<template>

  <nav class="admin-nav">

    <RouterLink
      to="/admin"
      class="nav-item"
      exact-active-class="active"
    >
      <span class="icon">⌂</span>
      <span>Home</span>
    </RouterLink>


    <RouterLink
      to="/admin/requests"
      class="nav-item"
      active-class="active"
    >
      <span class="icon">✉</span>
      <span>Requests</span>
    </RouterLink>


    <RouterLink
      to="/admin/sellers"
      class="nav-item"
      active-class="active"
    >
      <span class="icon">▣</span>
      <span>Sellers</span>
    </RouterLink>


    <RouterLink
      to="/admin/reports"
      class="nav-item"
      active-class="active"
    >
      <span class="icon">!</span>
      <span>Reports</span>
    </RouterLink>


    <RouterLink
      to="/admin/chat"
      class="nav-item"
      active-class="active"
    >
      <span class="icon">●</span>
      <span>Chat</span>
    </RouterLink>

  </nav>

</template>


<script setup>

import { RouterLink } from 'vue-router'

</script>


<style scoped>

.admin-nav {
  display: flex;

  justify-content: space-between;
  align-items: center;

  margin-bottom: 28px;

  padding: 8px 6px 0;

  background-color: white;

  border-bottom: 1px solid #f0e5de;
}


.nav-item {
  flex: 1;

  position: relative;

  display: flex;
  flex-direction: column;

  align-items: center;

  gap: 5px;

  padding: 10px 3px 12px;

  text-decoration: none;

  color: #a69a94;

  font-size: 9px;
  font-weight: 700;

  transition: 0.2s;
}


.icon {
  height: 25px;

  display: flex;
  align-items: center;
  justify-content: center;

  font-size: 21px;
  font-weight: 800;
}


.nav-item:hover {
  color: #ff6b00;
}


.nav-item.active {
  color: #3A251E;
}


.nav-item.active::after {
  content: '';

  position: absolute;

  bottom: -1px;

  width: 32px;
  height: 3px;

  border-radius: 10px;

  background-color: #ff6b00;
}

</style>