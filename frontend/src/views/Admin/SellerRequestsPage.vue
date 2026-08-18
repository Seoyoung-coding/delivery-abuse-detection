<template>

  <div class="page">

    <AdminTopNav />


    <div class="page-header">

      <div>

        <p class="eyebrow">
          SELLER MANAGEMENT
        </p>

        <h1>
          Requests
        </h1>

        <p>
          Review new seller applications.
        </p>

      </div>


      <div class="count">
        {{ requests.length }}
      </div>

    </div>


    <div
      v-if="requests.length > 0"
      class="request-list"
    >

      <div
        v-for="request in requests"
        :key="request.id"
        class="request-card"
      >

        <div class="request-main">

          <div class="store-icon">
            {{ request.storeName.charAt(0) }}
          </div>


          <div class="request-info">

            <h3>
              {{ request.storeName }}
            </h3>

            <p>
              {{ request.email }}
            </p>

            <span>
              {{ request.address }}
            </span>

          </div>

        </div>


        <div class="actions">

          <button
            class="deny-button"
            @click="denyRequest(request.id)"
          >
            Deny
          </button>


          <button
            class="accept-button"
            @click="acceptRequest(request.id)"
          >
            Accept
          </button>

        </div>

      </div>

    </div>


    <div
      v-else
      class="empty-card"
    >

      <div class="empty-icon">
        ✓
      </div>

      <h2>All caught up!</h2>

      <p>
        There are no seller applications waiting for review.
      </p>

    </div>

  </div>

</template>


<script setup>

import { ref } from 'vue'

import AdminTopNav from '@/components/admin/AdminTopNav.vue'


const requests = ref([

  {
    id: 1,
    storeName: 'Yami Kitchen',
    email: 'yami@email.com',
    address: 'Santa Barbara, CA'
  },

  {
    id: 2,
    storeName: 'Tokyo Bowl',
    email: 'tokyo@email.com',
    address: 'Goleta, CA'
  },

  {
    id: 3,
    storeName: 'Seoul Food',
    email: 'seoul@email.com',
    address: 'Santa Barbara, CA'
  }

])


const acceptRequest = (id) => {

  // 나중에:
  // PATCH /api/admin/seller-applications/{id}/approve

  requests.value =
    requests.value.filter(
      request => request.id !== id
    )

}


const denyRequest = (id) => {

  // 나중에:
  // PATCH /api/admin/seller-applications/{id}/reject

  requests.value =
    requests.value.filter(
      request => request.id !== id
    )

}

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
  display: flex;

  justify-content: space-between;
  align-items: center;

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
  margin: 6px 0 0;

  color: #9ca3af;

  font-size: 11px;
}


.count {
  min-width: 36px;
  height: 36px;

  display: flex;

  justify-content: center;
  align-items: center;

  border-radius: 50%;

  background-color: #ff6b00;

  color: white;

  font-size: 13px;
  font-weight: 800;
}


.request-card {
  margin-bottom: 12px;

  padding: 18px;

  border: 1px solid #f1e7e1;

  border-radius: 18px;

  background-color: white;
}


.request-main {
  display: flex;

  gap: 13px;
}


.store-icon {
  width: 48px;
  height: 48px;

  flex-shrink: 0;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 15px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-size: 19px;
  font-weight: 800;
}


.request-info h3 {
  margin: 3px 0 4px;

  font-size: 14px;
}


.request-info p {
  margin: 0;

  color: #8f8580;

  font-size: 10px;
}


.request-info span {
  display: block;

  margin-top: 5px;

  color: #b0a6a0;

  font-size: 9px;
}


.actions {
  display: flex;

  justify-content: flex-end;

  gap: 9px;

  margin-top: 18px;
}


button {
  padding: 9px 18px;

  border-radius: 20px;

  font-family: inherit;

  font-size: 10px;
  font-weight: 700;

  cursor: pointer;
}


.deny-button {
  border: 1px solid #ddd2cc;

  background-color: white;

  color: #6e615b;
}


.accept-button {
  border: none;

  background-color: #ff6b00;

  color: white;
}


.empty-card {
  margin-top: 50px;

  padding: 35px 25px;

  text-align: center;

  border-radius: 22px;

  background-color: #fff4eb;
}


.empty-icon {
  width: 55px;
  height: 55px;

  margin: auto;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 50%;

  background-color: white;

  color: #ff6b00;

  font-size: 25px;
  font-weight: 800;
}


.empty-card h2 {
  margin-bottom: 7px;

  font-size: 17px;
}


.empty-card p {
  color: #9ca3af;

  font-size: 10px;
}

</style>