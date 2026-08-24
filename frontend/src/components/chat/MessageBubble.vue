<template>

  <div
    class="message-row"
    :class="{ mine: isMine }"
  >

    <div
      class="bubble"
      :class="{ mine: isMine }"
    >

      <p>
        {{ text }}
      </p>

      <span>
        {{ time }}
      </span>

    </div>

  </div>

</template>


<script setup lang="ts">

defineProps<{
  text: string
  time: string
  isMine: boolean
}>()

</script>


<style scoped>

.message-row {
  display: flex;

  justify-content: flex-start;

  margin-bottom: 12px;
}

.message-row.mine {
  justify-content: flex-end;
}

.bubble {
  max-width: 75%;

  padding: 11px 14px;

  border-radius: 18px;

  background-color: #f3f4f6;

  color: #3A251E;
}

.bubble.mine {
  background-color: #ff6b00;

  color: white;
}

.bubble p {
  margin: 0;

  font-size: 12px;

  line-height: 1.5;
}

.bubble span {
  display: block;

  margin-top: 5px;

  font-size: 8px;

  text-align: right;

  opacity: 0.65;
}

</style>