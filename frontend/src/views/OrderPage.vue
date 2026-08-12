<template>

  <div class="page">

    <h1>Your Order</h1>

    <div class="order-card">

      <div class="status-icon">
        ✓
      </div>

      <h2>
        Order confirmed!
      </h2>

      <p>
        Your food is being prepared.
      </p>


      <div class="progress">

        <div class="progress-step active">
          Order confirmed
        </div>

        <div class="progress-step">
          Preparing
        </div>

        <div class="progress-step">
          On the way
        </div>

        <div class="progress-step">
          Delivered
        </div>

      </div>

    </div>


    <BottomNav />

  </div>

</template>


<script setup>

import BottomNav from '@/components/BottomNav.vue'

</script>


<style scoped>

.page {
  max-width: 520px;

  min-height: 100vh;

  margin: auto;

  padding: 25px 20px 100px;

  font-family: 'Montserrat', sans-serif;

  color: #3A251E;
}


.order-card {
  margin-top: 30px;

  padding: 30px 22px;

  text-align: center;

  border-radius: 22px;

  background-color: #fff4eb;
}


.status-icon {
  width: 60px;
  height: 60px;

  margin: auto;

  display: flex;

  justify-content: center;
  align-items: center;

  border-radius: 50%;

  background-color: #ff6b00;

  color: white;

  font-size: 30px;
  font-weight: 800;
}


.order-card h2 {
  margin-bottom: 7px;
}


.order-card p {
  color: #9ca3af;

  font-size: 12px;
}


.progress {
  margin-top: 30px;

  text-align: left;
}


.progress-step {
  padding: 14px 15px;

  margin-bottom: 8px;

  background-color: white;

  border-radius: 10px;

  color: #9ca3af;

  font-size: 12px;
  font-weight: 600;
}


.progress-step.active {
  color: #ff6b00;

  border-left: 4px solid #ff6b00;
}

</style>