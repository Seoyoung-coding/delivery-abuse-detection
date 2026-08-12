<template>

  <div class="page">

    <!-- Header -->
    <header class="header">

      <div>
        <p class="deliver-text">
          Deliver to
        </p>

        <div class="location">
          📍 123 State Street
          <span>⌄</span>
        </div>
      </div>


      <button
        class="profile-button"
        @click="goToProfile"
      >
        👤
      </button>

    </header>


    <!-- Search -->
    <div
      class="search-box"
      @click="goToSearch"
    >

      <span class="search-icon">
        ⌕
      </span>

      <span class="search-placeholder">
        Search restaurants or food
      </span>

    </div>


    <!-- Welcome -->
    <section class="welcome">

      <div>
        <p class="small-title">
          Hello 👋
        </p>

        <h1>
          What would you<br />
          like to eat today?
        </h1>
      </div>

    </section>


    <!-- Categories -->
    <section>

      <div class="section-header">

        <h2>
          Categories
        </h2>

        <button @click="goToCategories">
          See all
        </button>

      </div>


      <div class="category-list">

        <div class="category-item">
          <div class="category-icon">
            🍔
          </div>

          <span>Burger</span>
        </div>


        <div class="category-item">
          <div class="category-icon">
            🍕
          </div>

          <span>Pizza</span>
        </div>


        <div class="category-item">
          <div class="category-icon">
            🍣
          </div>

          <span>Sushi</span>
        </div>


        <div class="category-item">
          <div class="category-icon">
            🍜
          </div>

          <span>Noodles</span>
        </div>


        <div class="category-item">
          <div class="category-icon">
            🥗
          </div>

          <span>Healthy</span>
        </div>

      </div>

    </section>


    <!-- Promotion -->
    <section class="promotion">

      <div>

        <p class="promo-small">
          SPECIAL OFFER
        </p>

        <h2>
          Free delivery
        </h2>

        <p>
          On your first YAMIYUMI order
        </p>

        <button>
          Order now
        </button>

      </div>


      <div class="promo-food">
        🍔
      </div>

    </section>


    <!-- Popular -->
    <section>

      <div class="section-header">

        <h2>
          Popular near you
        </h2>

        <button>
          See all
        </button>

      </div>


      <!-- Restaurant -->
      <div class="restaurant-card">

        <div class="restaurant-image">
          🍔

          <span class="delivery-badge">
            Free delivery
          </span>
        </div>


        <div class="restaurant-info">

          <div>

            <h3>
              Yummy Burger
            </h3>

            <p>
              Burgers · American · Fries
            </p>

          </div>


          <div class="rating">
            ★ 4.8
          </div>

        </div>


        <div class="restaurant-meta">

          <span>
            🕒 20–30 min
          </span>

          <span>
            $0 delivery
          </span>

        </div>

      </div>


      <!-- Restaurant -->
      <div class="restaurant-card">

        <div class="restaurant-image pizza">
          🍕

          <span class="delivery-badge">
            Popular
          </span>
        </div>


        <div class="restaurant-info">

          <div>

            <h3>
              Orange Pizza House
            </h3>

            <p>
              Pizza · Italian
            </p>

          </div>


          <div class="rating">
            ★ 4.7
          </div>

        </div>


        <div class="restaurant-meta">

          <span>
            🕒 25–35 min
          </span>

          <span>
            $1.99 delivery
          </span>

        </div>

      </div>

    </section>


    <BottomNav />

  </div>

</template>


<script setup>

import { useRouter } from 'vue-router'

import BottomNav from '@/components/BottomNav.vue'


const router = useRouter()


const goToSearch = () => {
  router.push('/search')
}


const goToCategories = () => {
  router.push('/categories')
}


const goToProfile = () => {
  router.push('/profile')
}

</script>


<style scoped>

@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@500;600;700;800&display=swap');


* {
  box-sizing: border-box;
}


.page {
  width: 100%;
  max-width: 520px;

  min-height: 100vh;

  margin: auto;

  padding: 22px 20px 100px;

  background-color: #ffffff;

  font-family: 'Montserrat', sans-serif;

  color: #3A251E;
}


/* Header */

.header {
  display: flex;

  justify-content: space-between;
  align-items: center;

  margin-bottom: 22px;
}


.deliver-text {
  margin: 0 0 5px;

  font-size: 11px;
  font-weight: 600;

  color: #9ca3af;
}


.location {
  display: flex;
  align-items: center;

  gap: 5px;

  font-size: 14px;
  font-weight: 700;
}


.profile-button {
  width: 42px;
  height: 42px;

  border: none;
  border-radius: 50%;

  background-color: #fff4eb;

  cursor: pointer;

  font-size: 18px;
}


/* Search */

.search-box {
  height: 52px;

  display: flex;
  align-items: center;

  gap: 12px;

  padding: 0 16px;

  background-color: #f3f4f6;

  border-radius: 14px;

  cursor: pointer;

  margin-bottom: 30px;
}


.search-icon {
  font-size: 23px;

  color: #ff6b00;
}


.search-placeholder {
  color: #9ca3af;

  font-size: 13px;
}


/* Welcome */

.welcome {
  margin-bottom: 30px;
}


.small-title {
  margin: 0 0 8px;

  color: #ff6b00;

  font-size: 13px;
  font-weight: 700;
}


.welcome h1 {
  margin: 0;

  line-height: 1.25;

  font-size: 27px;
  font-weight: 800;

  color: #3A251E;
}


/* Section */

section {
  margin-bottom: 30px;
}


.section-header {
  display: flex;

  justify-content: space-between;
  align-items: center;

  margin-bottom: 16px;
}


.section-header h2 {
  margin: 0;

  font-size: 18px;
  font-weight: 800;
}


.section-header button {
  border: none;

  background: none;

  color: #ff6b00;

  font-size: 12px;
  font-weight: 700;

  cursor: pointer;
}


/* Categories */

.category-list {
  display: flex;

  gap: 15px;

  overflow-x: auto;

  padding-bottom: 5px;
}


.category-list::-webkit-scrollbar {
  display: none;
}


.category-item {
  min-width: 68px;

  display: flex;
  flex-direction: column;

  align-items: center;

  gap: 8px;

  font-size: 11px;
  font-weight: 600;
}


.category-icon {
  width: 62px;
  height: 62px;

  display: flex;

  align-items: center;
  justify-content: center;

  background-color: #fff4eb;

  border-radius: 18px;

  font-size: 28px;

  cursor: pointer;
}


.category-icon:hover {
  background-color: #ffe6d2;
}


/* Promotion */

.promotion {
  display: flex;

  justify-content: space-between;
  align-items: center;

  padding: 22px;

  min-height: 170px;

  border-radius: 22px;

  background: linear-gradient(
    135deg,
    #ff6b00,
    #ff9647
  );

  color: white;

  overflow: hidden;
}


.promo-small {
  margin: 0 0 7px;

  font-size: 10px;
  font-weight: 800;

  letter-spacing: 1px;
}


.promotion h2 {
  margin: 0 0 7px;

  font-size: 23px;
  font-weight: 800;
}


.promotion p {
  font-size: 11px;
}


.promotion button {
  padding: 9px 15px;

  margin-top: 7px;

  border: none;
  border-radius: 8px;

  background-color: white;

  color: #ff6b00;

  font-size: 11px;
  font-weight: 800;

  cursor: pointer;
}


.promo-food {
  font-size: 75px;

  transform: rotate(-10deg);
}


/* Restaurant */

.restaurant-card {
  margin-bottom: 25px;

  cursor: pointer;
}


.restaurant-image {
  position: relative;

  height: 185px;

  display: flex;

  justify-content: center;
  align-items: center;

  border-radius: 18px;

  background-color: #fff1e6;

  font-size: 90px;
}


.restaurant-image.pizza {
  background-color: #fff7df;
}


.delivery-badge {
  position: absolute;

  top: 13px;
  left: 13px;

  padding: 7px 10px;

  border-radius: 8px;

  background-color: white;

  color: #ff6b00;

  font-size: 10px;
  font-weight: 700;

  box-shadow: 0 3px 10px rgba(0,0,0,0.08);
}


.restaurant-info {
  display: flex;

  justify-content: space-between;

  margin-top: 12px;
}


.restaurant-info h3 {
  margin: 0 0 5px;

  font-size: 15px;
  font-weight: 800;
}


.restaurant-info p {
  margin: 0;

  color: #9ca3af;

  font-size: 11px;
}


.rating {
  color: #ff6b00;

  font-size: 12px;
  font-weight: 700;
}


.restaurant-meta {
  display: flex;

  gap: 17px;

  margin-top: 8px;

  color: #6b7280;

  font-size: 10px;
}

</style>