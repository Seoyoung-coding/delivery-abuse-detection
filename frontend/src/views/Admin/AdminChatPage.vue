<template>

  <div class="page">


    <!-- =========================
         Header
    ========================== -->

    <header class="page-header">

      <div>

        <p class="small-title">
          ADMIN CENTER
        </p>

        <h1>
          Messages
        </h1>

      </div>


      <div class="message-count">

        {{ unreadCount }}

      </div>

    </header>


    <!-- =========================
         Conversation Section
    ========================== -->

    <section class="conversation-section">

      <div class="section-header">

        <h2>
          Conversations
        </h2>

        <span>
          {{ sellers.length }} sellers
        </span>

      </div>


      <div class="seller-list">

        <button
          v-for="seller in sellers"
          :key="seller.id"
          class="seller-card"
          :class="{
            active:
              selectedSellerId === seller.id
          }"
          @click="selectSeller(seller.id)"
        >

          <div class="seller-avatar">

            {{ seller.avatar }}

          </div>


          <div class="seller-info">

            <div class="seller-name-row">

              <h3>
                {{ seller.name }}
              </h3>

              <span class="seller-time">
                {{ seller.lastTime }}
              </span>

            </div>


            <div class="seller-message-row">

              <p>
                {{ seller.lastMessage }}
              </p>


              <span
                v-if="seller.unread > 0"
                class="unread-badge"
              >
                {{ seller.unread }}
              </span>

            </div>

          </div>

        </button>

      </div>

    </section>


    <!-- =========================
         Chat Window
    ========================== -->

    <section
      v-if="selectedSeller"
      class="chat-section"
    >

      <ChatWindow
        :title="selectedSeller.name"
        :subtitle="selectedSeller.storeName"
        :avatar="selectedSeller.avatar"
        :online="selectedSeller.online"
        :messages="selectedMessages"
        @send="sendMessage"
      />

    </section>


  </div>

</template>


<script setup lang="ts">

import {
  computed,
  ref
} from 'vue'

import ChatWindow from '@/components/chat/ChatWindow.vue'

interface ChatMessage {
  id: number | string
  text: string
  time: string
  isMine: boolean
}

interface SellerConversation {

  id: number

  name: string

  storeName: string

  avatar: string

  online: boolean

  lastMessage: string

  lastTime: string

  unread: number

}


const sellers =
  ref<SellerConversation[]>([

    {
      id: 1,

      name: 'Mina Kim',

      storeName: 'Seoul Kitchen',

      avatar: 'M',

      online: true,

      lastMessage:
        'I have a question about my store.',

      lastTime: '10:16 AM',

      unread: 2
    },

    {
      id: 2,

      name: 'Daniel Lee',

      storeName: 'Tokyo Bento',

      avatar: 'D',

      online: true,

      lastMessage:
        'Thank you for your help!',

      lastTime: '9:42 AM',

      unread: 0
    },

    {
      id: 3,

      name: 'Sarah Park',

      storeName: 'K Food House',

      avatar: 'S',

      online: false,

      lastMessage:
        'Can I update my store address?',

      lastTime: 'Yesterday',

      unread: 1
    }

  ])


const chatMessages =
  ref<Record<number, ChatMessage[]>>({

    1: [

      {
        id: 1,

        text:
          'Hello! Welcome to YamiYumi Seller Support. How can we help you today?',

        time: '10:14 AM',

        isMine: true
      },

      {
        id: 2,

        text:
          'Hi! I have a question about updating my store information.',

        time: '10:16 AM',

        isMine: false
      }

    ],


    2: [

      {
        id: 3,

        text:
          'Your menu update has been approved.',

        time: '9:40 AM',

        isMine: true
      },

      {
        id: 4,

        text:
          'Thank you for your help!',

        time: '9:42 AM',

        isMine: false
      }

    ],


    3: [

      {
        id: 5,

        text:
          'Can I update my store address?',

        time: 'Yesterday',

        isMine: false
      }

    ]

  })


const selectedSellerId =
  ref<number>(1)


const selectedSeller =
  computed(() => {

    return sellers.value.find(

      seller =>
        seller.id ===
        selectedSellerId.value

    )

  })


const selectedMessages =
  computed(() => {

    return (
      chatMessages.value[
        selectedSellerId.value
      ] ?? []
    )

  })


const unreadCount =
  computed(() => {

    return sellers.value.reduce(

      (
        total,
        seller
      ) => {

        return total + seller.unread

      },

      0
    )

  })


const selectSeller = (
  sellerId: number
) => {

  selectedSellerId.value =
    sellerId


  const seller =
    sellers.value.find(

      item =>
        item.id === sellerId

    )


  if (seller) {

    seller.unread = 0

  }

}


const sendMessage = (
  text: string
) => {

  const sellerId =
    selectedSellerId.value


  if (!chatMessages.value[sellerId]) {

    chatMessages.value[sellerId] = []

  }


  /*
   * Admin이 보내는 메시지
   *
   * 나중에 여기에서
   * REST API 또는 WebSocket 연결
   */


  chatMessages.value[sellerId].push({

    id: Date.now(),

    text,

    time:
      new Date().toLocaleTimeString(
        [],
        {
          hour: '2-digit',
          minute: '2-digit'
        }
      ),

    isMine: true

  })


  const seller =
    sellers.value.find(

      item =>
        item.id === sellerId

    )


  if (seller) {

    seller.lastMessage = text

    seller.lastTime = 'Now'

  }

}

</script>


<style scoped>

@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@500;600;700;800&display=swap');


* {
  box-sizing: border-box;
}


/* =========================
   Page
========================= */

.page {

  width: 100%;
  max-width: 520px;

  min-height: 100vh;

  margin: auto;

  padding: 22px 20px 50px;

  background-color: #ffffff;

  font-family: 'Montserrat', sans-serif;

  color: #3A251E;

}


/* =========================
   Header
========================= */

.page-header {

  display: flex;

  align-items: center;
  justify-content: space-between;

  margin-bottom: 30px;

}


.small-title {

  margin: 0 0 5px;

  color: #ff6b00;

  font-size: 10px;
  font-weight: 800;

  letter-spacing: 1px;

}


.page-header h1 {

  margin: 0;

  font-size: 27px;
  font-weight: 800;

  color: #3A251E;

}


.message-count {

  width: 42px;
  height: 42px;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 14px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-size: 13px;
  font-weight: 800;

}


/* =========================
   Conversation
========================= */

.conversation-section {

  margin-bottom: 25px;

}


.section-header {

  display: flex;

  align-items: center;
  justify-content: space-between;

  margin-bottom: 14px;

}


.section-header h2 {

  margin: 0;

  font-size: 16px;
  font-weight: 800;

}


.section-header span {

  color: #9ca3af;

  font-size: 9px;
  font-weight: 600;

}


/* =========================
   Seller List
========================= */

.seller-list {

  display: flex;

  flex-direction: column;

  gap: 7px;

  max-height: 245px;

  overflow-y: auto;

}


.seller-list::-webkit-scrollbar {

  display: none;

}


/* =========================
   Seller Card
========================= */

.seller-card {

  width: 100%;

  display: flex;

  align-items: center;

  gap: 12px;

  padding: 12px;

  border: 1px solid transparent;

  border-radius: 17px;

  background-color: white;

  text-align: left;

  cursor: pointer;

  transition: 0.2s;

}


.seller-card:hover {

  background-color: #fafafa;

}


.seller-card.active {

  border-color: #ffe0ca;

  background-color: #fff8f3;

}


/* =========================
   Seller Avatar
========================= */

.seller-avatar {

  width: 45px;
  height: 45px;

  flex-shrink: 0;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 14px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-size: 15px;
  font-weight: 800;

}


/* =========================
   Seller Info
========================= */

.seller-info {

  flex: 1;

  min-width: 0;

}


.seller-name-row {

  display: flex;

  align-items: center;
  justify-content: space-between;

  gap: 10px;

  margin-bottom: 5px;

}


.seller-name-row h3 {

  margin: 0;

  overflow: hidden;

  color: #3A251E;

  font-size: 11px;
  font-weight: 800;

  text-overflow: ellipsis;

  white-space: nowrap;

}


.seller-time {

  flex-shrink: 0;

  color: #b0b6bf;

  font-size: 8px;
  font-weight: 600;

}


.seller-message-row {

  display: flex;

  align-items: center;

  gap: 10px;

}


.seller-message-row p {

  flex: 1;

  min-width: 0;

  margin: 0;

  overflow: hidden;

  color: #9ca3af;

  font-size: 9px;

  text-overflow: ellipsis;

  white-space: nowrap;

}


/* =========================
   Unread
========================= */

.unread-badge {

  min-width: 18px;
  height: 18px;

  display: flex;

  align-items: center;
  justify-content: center;

  padding: 0 5px;

  border-radius: 50px;

  background-color: #ff6b00;

  color: white;

  font-size: 8px;
  font-weight: 800;

}


/* =========================
   Chat
========================= */

.chat-section {

  margin-bottom: 20px;

}

</style>