<template>

  <div class="page">


    <!-- =========================
         Page Header
    ========================== -->

    <header class="page-header">

      <button
        class="back-button"
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
    />


  </div>

</template>


<script setup lang="ts">

import { ref } from 'vue'
import { useRouter } from 'vue-router'

import ChatWindow from '@/components/chat/ChatWindow.vue'

interface ChatMessage {
  id: number | string
  text: string
  time: string
  isMine: boolean
}


interface ChatMessage {
  id: number | string
  text: string
  time: string
  isMine: boolean
}


const router = useRouter()


const messages = ref<ChatMessage[]>([
  {
    id: 1,
    text: 'Hello! Welcome to YamiYumi Seller Support. How can we help you today?',
    time: '10:14 AM',
    isMine: false
  },
  {
    id: 2,
    text: 'Hi! I have a question about updating my store information.',
    time: '10:16 AM',
    isMine: true
  }
])
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