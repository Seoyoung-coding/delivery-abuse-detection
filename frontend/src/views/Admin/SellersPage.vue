<template>

  <div class="page">

    <AdminTopNav />


    <div class="page-header">

      <p class="eyebrow">
        SELLER MANAGEMENT
      </p>

      <h1>Sellers</h1>

      <p>
        Manage approved sellers and stores.
      </p>

    </div>


    <div
      v-for="seller in sellers"
      :key="seller.id"
      class="seller-card"
    >

      <div class="seller-top">

        <div class="store-icon">
          {{ seller.storeName.charAt(0) }}
        </div>


        <div class="seller-info">

          <div class="name-row">

            <h3>
              {{ seller.storeName }}
            </h3>

            <span class="active-badge">
              Active
            </span>

          </div>

          <p>
            {{ seller.email }}
          </p>

        </div>

      </div>


      <div class="seller-stats">

        <div>
          <strong>{{ seller.feeds }}</strong>
          <span>Feeds</span>
        </div>

        <div>
          <strong>{{ seller.reports }}</strong>
          <span>Reports</span>
        </div>

        <div>
          <strong>{{ seller.orders }}</strong>
          <span>Orders</span>
        </div>

      </div>


      <button class="manage-button">
        Manage Seller
      </button>

    </div>

  </div>

</template>


<script setup>

import { ref } from 'vue'

import AdminTopNav from '@/components/admin/AdminTopNav.vue'


const sellers = ref([

  {
    id: 1,
    storeName: 'Yami Kitchen',
    email: 'yami@email.com',
    feeds: 14,
    reports: 1,
    orders: 82
  },

  {
    id: 2,
    storeName: 'Tokyo Bowl',
    email: 'tokyo@email.com',
    feeds: 7,
    reports: 0,
    orders: 45
  }

])

</script>


<style scoped>

.page {
  max-width: 520px;

  min-height: 100vh;

  margin: auto;

  padding: 20px 20px 100px;

  font-family: 'Montserrat', sans-serif;

  color: #3A251E;
}


.page-header {
  margin-bottom: 25px;
}


.eyebrow {
  margin: 0 0 5px;

  color: #ff6b00;

  font-size: 10px;
  font-weight: 800;

  letter-spacing: 1.4px;
}


h1 {
  margin: 0;

  font-size: 28px;
}


.page-header p:not(.eyebrow) {
  margin-top: 6px;

  color: #9ca3af;

  font-size: 11px;
}


.seller-card {
  margin-bottom: 14px;

  padding: 18px;

  border-radius: 18px;

  background-color: #fff4eb;
}


.seller-top {
  display: flex;

  align-items: center;

  gap: 12px;
}


.store-icon {
  width: 48px;
  height: 48px;

  display: flex;

  justify-content: center;
  align-items: center;

  border-radius: 14px;

  background-color: white;

  color: #ff6b00;

  font-weight: 800;
}


.seller-info {
  flex: 1;
}


.name-row {
  display: flex;

  justify-content: space-between;
  align-items: center;
}


.name-row h3 {
  margin: 0;

  font-size: 13px;
}


.active-badge {
  padding: 4px 8px;

  border-radius: 20px;

  background-color: white;

  color: #ff6b00;

  font-size: 8px;
  font-weight: 800;
}


.seller-info p {
  margin: 4px 0 0;

  color: #9ca3af;

  font-size: 9px;
}


.seller-stats {
  display: grid;

  grid-template-columns: repeat(3, 1fr);

  margin-top: 18px;

  padding: 14px;

  border-radius: 12px;

  background-color: white;

  text-align: center;
}


.seller-stats div {
  display: flex;

  flex-direction: column;

  gap: 4px;
}


.seller-stats strong {
  font-size: 14px;
}


.seller-stats span {
  color: #aaa09a;

  font-size: 8px;
}


.manage-button {
  width: 100%;

  margin-top: 12px;

  padding: 10px;

  border: none;

  border-radius: 10px;

  background-color: #ff6b00;

  color: white;

  font-family: inherit;

  font-size: 10px;
  font-weight: 700;

  cursor: pointer;
}

</style>