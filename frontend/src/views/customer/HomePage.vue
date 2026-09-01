<template>

  <div class="page">

    <!-- =========================
         Header
    ========================== -->
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


    <!-- =========================
         Search
    ========================== -->
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


    <!-- =========================
         Welcome
    ========================== -->
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


    <!-- =========================
         Categories
    ========================== -->
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

        <!-- Burger -->
        <div class="category-item">

          <div class="category-icon">
            🍔
          </div>

          <span>
            Burger
          </span>

        </div>


        <!-- Pizza -->
        <div class="category-item">

          <div class="category-icon">
            🍕
          </div>

          <span>
            Pizza
          </span>

        </div>


        <!-- Sushi -->
        <div class="category-item">

          <div class="category-icon">
            🍣
          </div>

          <span>
            Sushi
          </span>

        </div>


        <!-- Noodles -->
        <div class="category-item">

          <div class="category-icon">
            🍜
          </div>

          <span>
            Noodles
          </span>

        </div>


        <!-- Healthy -->
        <div class="category-item">

          <div class="category-icon">
            🥗
          </div>

          <span>
            Healthy
          </span>

        </div>

      </div>

    </section>


    <!-- =========================
         Promotion
    ========================== -->
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


    <!-- =========================
         Store List
    ========================== -->
    <section>

      <div class="section-header">

        <h2>
          Popular near you
        </h2>

        <button>
          See all
        </button>

      </div>


      <!-- 실제 DB Store 목록 -->
      <div
        v-for="store in stores"
        :key="store.id"
        class="store-card"
        @click="goToStore(store.id)"
      >

        <!-- 1. Store 대표 이미지 -->
        <div class="store-image">

          <img
            v-if="store.imageUrl"
            :src="`http://localhost:8080${store.imageUrl}`"
            :alt="store.name"
          />

          <span v-else>
            No image
          </span>

        </div>


        <!-- 2. Store 정보 -->
        <div class="store-info">

          <!-- 가게 이름 -->
          <h3 class="store-name">
            {{ store.name }}
          </h3>


          <!-- 가게 설명 -->
          <p>
            {{ store.description }}
          </p>


          <!-- 가게 주소 -->
          <p>
            {{ store.address }}
          </p>

        </div>

      </div>

    </section>


    <!-- =========================
         Bottom Navigation
    ========================== -->
    <BottomNav />

  </div>

</template>


<script setup>

// =========================
// 1. Import
// =========================

import {
  ref,
  onMounted
} from 'vue'

import {
  useRouter
} from 'vue-router'

import BottomNav from '@/components/BottomNav.vue'


// =========================
// 2. Router
// =========================

const router =
  useRouter()


// =========================
// 3. Store 데이터
// =========================

// 처음에는 빈 배열
const stores =
  ref([])


// =========================
// 4. Store 목록 조회
// =========================

const loadStores = async () => {

  try {

    const response =
      await fetch(
        'http://localhost:8080/api/stores'
      )


    // 요청 실패
    if (!response.ok) {

      const message =
        await response.text()

      throw new Error(
        message || 'Store 목록 조회 실패'
      )
    }


    // 백엔드 JSON 받기
    const data =
      await response.json()


    // 실제 DB Store 목록 저장
    stores.value =
      data


    console.log(
      'DB에서 받은 Store:',
      stores.value
    )


  } catch (error) {

    console.error(
      'Store 목록 조회 실패:',
      error
    )
  }
}


// =========================
// 5. 페이지 처음 열릴 때
// Store 목록 조회
// =========================

onMounted(() => {

  loadStores()

})


// =========================
// Search 페이지 이동
// =========================
const goToSearch = () => {

  router.push('/search')

}


// =========================
// Categories 페이지 이동
// =========================
const goToCategories = () => {

  router.push('/categories')

}


// =========================
// Profile 페이지 이동
// =========================
const goToProfile = () => {

  router.push('/profile')

}


// =========================
// Store 상세 페이지 이동
// =========================
const goToStore = (storeId) => {

  // 예:
  // storeId가 1이면
  // /stores/1 로 이동

  router.push(`/stores/${storeId}`)

}

</script>


<style scoped>

@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@500;600;700;800&display=swap');


* {
  box-sizing: border-box;
}


/* =========================
   전체 페이지
========================= */

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


/* =========================
   Header
========================= */

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


/* =========================
   Search
========================= */

.search-box {

  height: 52px;

  display: flex;

  align-items: center;

  gap: 12px;

  padding: 0 16px;

  margin-bottom: 30px;

  background-color: #f3f4f6;

  border-radius: 14px;

  cursor: pointer;

}


.search-icon {

  font-size: 23px;

  color: #ff6b00;

}


.search-placeholder {

  color: #9ca3af;

  font-size: 13px;

}


/* =========================
   Welcome
========================= */

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


/* =========================
   Section
========================= */

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


/* =========================
   Categories
========================= */

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

  transition: 0.2s;

}


.category-icon:hover {

  background-color: #ffe6d2;

}


/* =========================
   Promotion
========================= */

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


/* =========================
   Store List
========================= */


/* 가게 하나 전체 */
.store-card {

  display: flex;

  gap: 14px;

  padding: 17px 0;

  border-bottom: 1px solid #eeeeee;

  cursor: pointer;

  transition: 0.2s;

}


.store-card:hover {

  background-color: #fafafa;

}


/* =========================
   Store Image
========================= */

.store-image {

  width: 88px;
  height: 88px;

  flex-shrink: 0;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 18px;

  background-color: #fff4eb;

  font-size: 42px;

}


/* =========================
   Store Info
========================= */

.store-info {

  flex: 1;

  min-width: 0;

}


/* Store 이름 */
.store-name {

  display: flex;

  align-items: center;

  gap: 7px;

  margin: 1px 0 6px;

  font-size: 15px;
  font-weight: 800;

  color: #3A251E;

}


/* Free delivery / Popular / New */
.store-badge {

  padding: 4px 7px;

  border-radius: 7px;

  background-color: #e9f9ee;

  color: #23a455;

  font-size: 8px;
  font-weight: 800;

}


/* =========================
   Rating
========================= */

.rating-row {

  display: flex;

  align-items: center;

  gap: 4px;

  margin-bottom: 5px;

  font-size: 11px;

}


.star {

  color: #ffb000;

  font-size: 13px;

}


.review-count {

  color: #9ca3af;

}


.review-text {

  color: #b0b6bf;

}


/* =========================
   Category
========================= */

.store-category {

  margin: 0 0 8px;

  color: #9ca3af;

  font-size: 10px;

}


/* =========================
   Delivery Info
========================= */

.store-meta {

  display: flex;

  flex-wrap: wrap;

  gap: 12px;

  color: #6b7280;

  font-size: 10px;

  font-weight: 500;

}

</style>