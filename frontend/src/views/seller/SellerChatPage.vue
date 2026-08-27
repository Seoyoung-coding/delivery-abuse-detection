<template>

  <div class="page">

    <!-- =========================
         Page Header
    ========================== -->

    <header class="page-header">

      <button
        class="back-button"
        @click="router.back()"
      >
        ‹
      </button>


      <div class="header-title">

        <p class="small-title">
          SELLER CENTER
        </p>

        <h1>
          Support
        </h1>

      </div>


      <div class="header-space"></div>

    </header>


    <!-- =========================
         Intro
    ========================== -->

    <section class="intro-card">

      <div class="intro-icon">
        💬
      </div>


      <div class="intro-info">

        <h2>
          Need some help?
        </h2>

        <p>
          Chat directly with our admin team.
        </p>

      </div>

    </section>


    <!-- =========================
         Chat
    ========================== -->

    <ChatWindow
      title="YamiYumi Admin"
      subtitle="Admin Support"
      avatar="Y"
      :online="true"
      :messages="messages"
      @send="sendMessage"
    />

  </div>

</template>


<script setup lang="ts">

import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

import ChatWindow from '@/components/chat/ChatWindow.vue'


// =========================
// Message Type
// =========================

interface ChatMessage {

  id: number | string

  text: string

  time: string

  isMine: boolean

}


// =========================
// Backend Message Type
// =========================

interface BackendMessage {

  id: number

  content: string

  sender: 'SELLER' | 'ADMIN'

  createdAt: string

}


// =========================
// Router
// =========================

const router = useRouter()


// =========================
// Messages
// =========================

// 임시 데이터 없음
const messages = ref<ChatMessage[]>([])


// =========================
// Token
// =========================

const getToken = () => {

  return localStorage.getItem('token')

}


// =========================
// Convert Backend Message
// =========================

const convertMessage = (
  message: BackendMessage
): ChatMessage => {

  const date = new Date(message.createdAt)

  return {

    id: message.id,

    text: message.content,

    time: date.toLocaleTimeString(
      [],
      {
        hour: '2-digit',
        minute: '2-digit'
      }
    ),

    // Seller 페이지니까
    // SELLER가 보낸 메시지가 오른쪽
    isMine: message.sender === 'SELLER'

  }

}


// =========================
// Load Messages
// =========================

const loadMessages = async () => {

  try {

    const token = getToken()


    const response = await fetch(
        'http://localhost:8080/api/chat/seller/messages',
      {
        method: 'GET',

        headers: {
          'Authorization': `Bearer ${token}`
        }
      }
    )


    if (!response.ok) {

      throw new Error(
        'Failed to load messages'
      )

    }


    const data: BackendMessage[] =
      await response.json()


    messages.value =
      data.map(convertMessage)


  } catch (error) {

    console.error(
      'Failed to load chat messages:',
      error
    )

  }

}


// =========================
// Send Message
// =========================

const sendMessage = async (text: string) => {

  try {

    const token = localStorage.getItem('token')

  const response = await fetch(
    'http://localhost:8080/api/chat/seller/messages',
      {
        method: 'POST',

        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },

        body: JSON.stringify({
          content: text
        })
      }
    )


    if (!response.ok) {

      console.error(
        'Send failed:',
        response.status
      )

      return

    }


    // =========================
    // 화면에 바로 추가
    // =========================

    const now = new Date()

    messages.value.push({

      id: Date.now(),

      text: text,

      time: now.toLocaleTimeString(
        [],
        {
          hour: '2-digit',
          minute: '2-digit'
        }
      ),

      isMine: true

    })


    console.log(
      'Message sent:',
      text
    )


  } catch (error) {

    console.error(
      'Failed to send message:',
      error
    )

  }

}


// =========================
// Page Load
// =========================

onMounted(() => {

  loadMessages()

})

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

  display: grid;

  grid-template-columns: 42px 1fr 42px;

  align-items: center;

  margin-bottom: 25px;

}


.back-button {

  width: 40px;
  height: 40px;

  display: flex;

  align-items: center;
  justify-content: center;

  padding: 0 0 4px;

  border: none;
  border-radius: 13px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-size: 29px;

  cursor: pointer;

}


.header-title {

  text-align: center;

}


.small-title {

  margin: 0 0 3px;

  color: #ff6b00;

  font-size: 9px;
  font-weight: 800;

  letter-spacing: 1px;

}


.header-title h1 {

  margin: 0;

  color: #3A251E;

  font-size: 20px;
  font-weight: 800;

}


.header-space {

  width: 42px;

}


/* =========================
   Intro Card
========================= */

.intro-card {

  display: flex;

  align-items: center;

  gap: 13px;

  padding: 15px;

  margin-bottom: 18px;

  border-radius: 18px;

  background: linear-gradient(
    135deg,
    #fff4eb,
    #fff9f5
  );

}


.intro-icon {

  width: 45px;
  height: 45px;

  flex-shrink: 0;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 14px;

  background-color: white;

  font-size: 21px;

}


.intro-info h2 {

  margin: 0 0 4px;

  color: #3A251E;

  font-size: 12px;
  font-weight: 800;

}


.intro-info p {

  margin: 0;

  color: #9ca3af;

  font-size: 9px;
  font-weight: 500;

}

</style>