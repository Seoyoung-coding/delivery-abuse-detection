<template>

  <div class="message-input-wrapper">

    <div class="message-input-box">

      <textarea
        v-model="message"
        class="message-input"
        placeholder="Type a message..."
        rows="1"
        @keydown.enter.exact.prevent="sendMessage"
      ></textarea>


      <button
        class="send-button"
        :disabled="!message.trim()"
        @click="sendMessage"
      >
        ➤
      </button>

    </div>

  </div>

</template>


<script setup lang="ts">

import { ref } from 'vue'


const emit = defineEmits<{

  (
    e: 'send',
    message: string
  ): void

}>()


const message = ref('')


const sendMessage = () => {

  const text = message.value.trim()


  if (!text) {

    return

  }


  emit(
    'send',
    text
  )


  message.value = ''

}

</script>


<style scoped>

* {
  box-sizing: border-box;
}


/* =========================
   Wrapper
========================= */

.message-input-wrapper {

  padding-top: 12px;

  background-color: white;

}


/* =========================
   Input Box
========================= */

.message-input-box {

  display: flex;

  align-items: center;

  gap: 10px;

  padding: 8px 8px 8px 15px;

  border: 1px solid #eeeeee;

  border-radius: 18px;

  background-color: #f8f8f8;

}


/* =========================
   Textarea
========================= */

.message-input {

  flex: 1;

  min-height: 24px;
  max-height: 100px;

  padding: 5px 0;

  border: none;
  outline: none;

  resize: none;

  background: transparent;

  color: #3A251E;

  font-family: inherit;

  font-size: 12px;

  line-height: 1.5;

}


.message-input::placeholder {

  color: #aeb3bb;

}


/* =========================
   Send Button
========================= */

.send-button {

  width: 38px;
  height: 38px;

  flex-shrink: 0;

  display: flex;

  align-items: center;
  justify-content: center;

  border: none;
  border-radius: 13px;

  background-color: #ff6b00;

  color: white;

  font-size: 16px;

  cursor: pointer;

  transition: 0.2s;

}


.send-button:hover {

  background-color: #ed6400;

}


.send-button:disabled {

  background-color: #ffd3b5;

  cursor: default;

}

</style>