<template>

  <div class="page">

    <div class="header">

      <button
        class="back-button"
        @click="goBack"
      >
        ‹
      </button>

      <h1>Search</h1>

    </div>


    <div class="search-box">

      <span>⌕</span>

      <input
        v-model="searchText"
        placeholder="Search food or restaurants"
      />

    </div>


    <section>

      <h2>Popular searches</h2>


      <div class="search-tags">

        <button>🍔 Burger</button>

        <button>🍕 Pizza</button>

        <button>🍣 Sushi</button>

        <button>🍗 Chicken</button>

        <button>🍜 Noodles</button>

        <button>🥗 Healthy</button>

      </div>

    </section>


    <section>

      <h2>Recent searches</h2>


      <div class="recent-item">
        <span>🕘</span>

        <span>Japanese food</span>
      </div>


      <div class="recent-item">
        <span>🕘</span>

        <span>Burger</span>
      </div>

    </section>


    <BottomNav />

  </div>

</template>


<script setup>

import { ref } from 'vue'

import { useRouter } from 'vue-router'

import BottomNav from '@/components/BottomNav.vue'


const router = useRouter()

const searchText = ref('')


const goBack = () => {
  router.back()
}

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


.header {
  display: flex;

  align-items: center;

  gap: 15px;

  margin-bottom: 25px;
}


.header h1 {
  margin: 0;

  font-size: 22px;
}


.back-button {
  width: 40px;
  height: 40px;

  border: none;

  border-radius: 50%;

  background-color: #f3f4f6;

  font-size: 28px;

  cursor: pointer;
}


.search-box {
  display: flex;

  align-items: center;

  gap: 12px;

  padding: 0 15px;

  height: 52px;

  background-color: #f3f4f6;

  border-radius: 14px;

  margin-bottom: 30px;
}


.search-box span {
  color: #ff6b00;

  font-size: 22px;
}


.search-box input {
  flex: 1;

  border: none;

  outline: none;

  background: none;

  font-size: 13px;
}


section {
  margin-bottom: 30px;
}


section h2 {
  font-size: 17px;

  margin-bottom: 15px;
}


.search-tags {
  display: flex;

  flex-wrap: wrap;

  gap: 10px;
}


.search-tags button {
  padding: 10px 14px;

  border: none;

  border-radius: 20px;

  background-color: #fff4eb;

  color: #3A251E;

  cursor: pointer;

  font-weight: 600;
}


.recent-item {
  display: flex;

  gap: 12px;

  padding: 13px 0;

  border-bottom: 1px solid #f3f4f6;

  color: #6b7280;

  font-size: 13px;
}

</style>