<template>

  <div class="page">

    <AdminTopNav />


    <div class="page-header">

      <p class="eyebrow">
        SELLER SUPPORT
      </p>

      <h1>Messages</h1>

      <p>
        Chat with registered sellers.
      </p>

    </div>


    <div
      v-for="chat in chats"
      :key="chat.id"
      class="chat-card"
    >

      <div class="seller-icon">
        {{ chat.storeName.charAt(0) }}
      </div>


      <div class="chat-info">

        <div class="chat-top">

          <strong>
            {{ chat.storeName }}
          </strong>

          <span>
            {{ chat.time }}
          </span>

        </div>


        <p>
          {{ chat.lastMessage }}
        </p>

      </div>


      <div
        v-if="chat.unread > 0"
        class="unread"
      >
        {{ chat.unread }}
      </div>

    </div>

  </div>

</template>


<script setup>

import { ref } from 'vue'

import AdminTopNav from '@/components/admin/AdminTopNav.vue'


const chats = ref([

  {
    id: 1,
    storeName: 'Yami Kitchen',
    lastMessage: 'I updated the reported content.',
    time: '2:31 PM',
    unread: 2
  },

  {
    id: 2,
    storeName: 'Tokyo Bowl',
    lastMessage: 'Thank you!',
    time: 'Yesterday',
    unread: 0
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


.chat-card {
  display: flex;

  align-items: center;

  gap: 12px;

  padding: 15px 5px;

  border-bottom: 1px solid #f1e8e3;

  cursor: pointer;
}


.seller-icon {
  width: 45px;
  height: 45px;

  flex-shrink: 0;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 50%;

  background-color: #fff4eb;

  color: #ff6b00;

  font-size: 16px;
  font-weight: 800;
}


.chat-info {
  flex: 1;

  min-width: 0;
}


.chat-top {
  display: flex;

  justify-content: space-between;
}


.chat-top strong {
  font-size: 12px;
}


.chat-top span {
  color: #aaa19c;

  font-size: 8px;
}


.chat-info p {
  overflow: hidden;

  margin: 5px 0 0;

  color: #918680;

  font-size: 9px;

  text-overflow: ellipsis;
  white-space: nowrap;
}


.unread {
  width: 20px;
  height: 20px;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 50%;

  background-color: #ff6b00;

  color: white;

  font-size: 8px;
  font-weight: 800;
}

</style>