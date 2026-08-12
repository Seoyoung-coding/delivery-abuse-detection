<template>

  <div class="page">

    <h1>Categories</h1>

    <p class="subtitle">
      What are you craving today?
    </p>


    <div class="category-grid">

      <div class="category">
        <span>🍔</span>
        <p>Burgers</p>
      </div>

      <div class="category">
        <span>🍕</span>
        <p>Pizza</p>
      </div>

      <div class="category">
        <span>🍣</span>
        <p>Sushi</p>
      </div>

      <div class="category">
        <span>🍜</span>
        <p>Noodles</p>
      </div>

      <div class="category">
        <span>🍗</span>
        <p>Chicken</p>
      </div>

      <div class="category">
        <span>🥗</span>
        <p>Healthy</p>
      </div>

      <div class="category">
        <span>☕</span>
        <p>Cafe</p>
      </div>

      <div class="category">
        <span>🍰</span>
        <p>Dessert</p>
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


h1 {
  margin-bottom: 5px;

  font-size: 25px;
}


.subtitle {
  color: #9ca3af;

  font-size: 12px;

  margin-bottom: 30px;
}


.category-grid {
  display: grid;

  grid-template-columns: repeat(2, 1fr);

  gap: 16px;
}


.category {
  min-height: 130px;

  display: flex;

  flex-direction: column;

  justify-content: center;
  align-items: center;

  background-color: #fff4eb;

  border-radius: 20px;

  cursor: pointer;
}


.category span {
  font-size: 48px;
}


.category p {
  margin-bottom: 0;

  font-size: 13px;
  font-weight: 700;
}

</style>