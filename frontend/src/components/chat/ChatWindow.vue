<template>

  <div class="chat-window">

    <div class="chat-header">

      <div class="avatar">
        {{ avatar }}
      </div>

      <div>
        <h3>{{ title }}</h3>
        <p>{{ subtitle }}</p>
      </div>

    </div>


    <div class="messages">

      <MessageBubble
        v-for="message in messages"
        :key="message.id"
        :text="message.text"
        :time="message.time"
        :is-mine="message.isMine"
      />

    </div>


    <MessageInput
      @send="sendMessage"
    />

  </div>

</template>


<script setup lang="ts">

import MessageBubble from './MessageBubble.vue'
import MessageInput from './MessageInput.vue'


defineProps<{
  title: string
  subtitle: string
  avatar: string
  online?: boolean
  messages: {
    id: number | string
    text: string
    time: string
    isMine: boolean
  }[]
}>()


const emit = defineEmits<{
  send: [message: string]
}>()


const sendMessage = (message: string) => {
  emit('send', message)
}

</script>


<style scoped>

.chat-window {
  display: flex;
  flex-direction: column;

  height: 600px;

  padding: 16px;

  border: 1px solid #eeeeee;
  border-radius: 24px;

  background-color: white;
}

.chat-header {
  display: flex;
  align-items: center;

  gap: 12px;

  padding-bottom: 15px;

  border-bottom: 1px solid #eeeeee;
}

.avatar {
  width: 42px;
  height: 42px;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 14px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-weight: 800;
}

.chat-header h3 {
  margin: 0;

  font-size: 13px;

  color: #3A251E;
}

.chat-header p {
  margin: 4px 0 0;

  font-size: 9px;

  color: #9ca3af;
}

.messages {
  flex: 1;

  padding: 20px 0;

  overflow-y: auto;
}

</style>