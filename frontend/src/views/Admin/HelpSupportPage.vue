<template>

  <div class="page">

    <button
      class="back-button"
      @click="goBack"
    >
      ← Back
    </button>


    <p class="eyebrow">
      HELP & SUPPORT
    </p>

    <h1>
      How can we help?
    </h1>

    <p class="description">
      Ask our AI assistant or connect with a support agent.
    </p>


    <!-- 다음 단계에서 AI Bot 연결 -->
    <div class="support-box">

      <h3>
        YamiYumi AI Support
      </h3>

      <p>
        AI assistant will be connected here.
      </p>

    </div>


    <!-- 다음 단계에서 실제 상담 연결 -->
    <button
    class="agent-button"
    @click="goToSupportChat"
    >
    Connect to a support agent
    </button>

  </div>

</template>


<script setup>

import {
  useRouter
} from 'vue-router'


const router =
  useRouter()


const goToSupportChat = () => {

  router.push(
    '/support/chat'
  )

}


const goBack = () => {

  router.back()

}

</script>


<style scoped>

.page {

  max-width: 520px;

  min-height: 100vh;

  margin: auto;

  padding: 30px 20px;

  font-family:
    'Montserrat',
    sans-serif;

  color: #3A251E;

}


.back-button {

  margin-bottom: 30px;

  border: none;

  background: none;

  color: #ff6b00;

  font-weight: 700;

  cursor: pointer;

}


.eyebrow {

  color: #ff6b00;

  font-size: 10px;

  font-weight: 800;

  letter-spacing: 1.4px;

}


h1 {

  margin: 5px 0 8px;

  font-size: 28px;

}


.description {

  margin-bottom: 30px;

  color: #9ca3af;

  font-size: 11px;

}


.support-box {

  min-height: 250px;

  padding: 20px;

  border-radius: 20px;

  background-color: #fff4eb;

}


.support-box h3 {

  margin-top: 0;

  font-size: 14px;

}


.support-box p {

  color: #9ca3af;

  font-size: 10px;

}


.agent-button {

  width: 100%;

  margin-top: 20px;

  padding: 13px;

  border: none;

  border-radius: 12px;

  background-color: #ff6b00;

  color: white;

  font-family: inherit;

  font-weight: 700;

  cursor: pointer;

}

</style>