<template>

  <div class="page">

    <!-- =========================
         Store Header Image
    ========================== -->
    <div class="store-header">

      <!-- 뒤로가기 -->
      <button
        class="back-button"
        @click="goBack"
      >
        ‹
      </button>


      <!-- 임시 대표 이미지 -->
      <div class="header-food">
        {{ store.image }}
      </div>

    </div>


    <!-- =========================
         Store Main Info
    ========================== -->
    <section class="store-main">

      <!-- 가게 이름 -->
      <h1>
        {{ store.name }}
      </h1>


      <!-- 평점 -->
      <div class="rating">

        <span class="star">
          ★
        </span>

        <strong>
          {{ store.rating }}
        </strong>

        <span>
          ({{ store.reviewCount }} reviews)
        </span>

      </div>


      <!-- 카테고리 -->
      <p class="category">
        {{ store.category }}
      </p>


      <!-- 가게 설명 -->
      <p class="description">
        {{ store.description }}
      </p>


      <!-- 배달 정보 -->
      <div class="delivery-info">

        <div>

          <span class="info-label">
            Delivery time
          </span>

          <strong>
            {{ store.deliveryTime }}
          </strong>

        </div>


        <div>

          <span class="info-label">
            Delivery fee
          </span>

          <strong>
            {{ store.deliveryFee }}
          </strong>

        </div>

      </div>


      <!-- 찜 / 공유 -->
      <div class="store-actions">

        <button @click="toggleFavorite">

          <span v-if="favorite">
            ♥
          </span>

          <span v-else>
            ♡
          </span>

          Favorite

        </button>


        <button>
          ⤴ Share
        </button>

      </div>

    </section>


    <!-- =========================
         Tabs
    ========================== -->
    <div class="tabs">

      <button
        :class="{ active: activeTab === 'menu' }"
        @click="activeTab = 'menu'"
      >
        Menu
      </button>


      <button
        :class="{ active: activeTab === 'info' }"
        @click="activeTab = 'info'"
      >
        Info
      </button>


      <button
        :class="{ active: activeTab === 'reviews' }"
        @click="activeTab = 'reviews'"
      >
        Reviews
      </button>

    </div>


    <!-- =========================
         MENU
    ========================== -->
    <section
      v-if="activeTab === 'menu'"
      class="menu-section"
    >

      <!-- 메뉴 제목 + Seller 상품등록 -->
      <div class="menu-header">

        <div>

          <p class="small-title">
            MENU
          </p>

          <h2>
            Popular dishes
          </h2>

        </div>


        <!--
          이 Store의 Seller일 때만 보여줄 버튼

          지금은 테스트를 위해
          isSellerOwner = true/false로 직접 변경 가능

          나중에는 JWT로 실제 소유자를 확인하게 됨
        -->
        <button
          v-if="isSellerOwner"
          class="add-product-button"
          @click="addProduct"
        >
          + Add product
        </button>

      </div>


      <!-- =========================
           Product List
      ========================== -->

      <div
        v-for="product in products"
        :key="product.id"
        class="product-card"
      >

        <!-- 상품 정보 -->
        <div class="product-info">

          <h3>
            {{ product.name }}
          </h3>


          <p>
            {{ product.description }}
          </p>


          <strong class="price">
            ${{ product.price.toFixed(2) }}
          </strong>


          <!-- =========================
               Seller 화면
          ========================== -->
          <div
            v-if="isSellerOwner"
            class="seller-actions"
          >

            <button @click="editProduct(product.id)">
              Edit
            </button>

            <button
              class="delete-button"
              @click="deleteProduct(product.id)"
            >
              Delete
            </button>

          </div>


          <!-- =========================
               Customer 화면
          ========================== -->
          <button
            v-else
            class="cart-button"
            @click="addToCart(product)"
          >
            Add to cart
          </button>

        </div>


        <!-- 상품 이미지 -->
        <div class="product-image">

          {{ product.image }}

        </div>

      </div>

    </section>


    <!-- =========================
         INFO
    ========================== -->
    <section
      v-if="activeTab === 'info'"
      class="tab-content"
    >

      <h2>
        Store information
      </h2>


      <div class="info-row">

        <span>
          Address
        </span>

        <strong>
          {{ store.address }}
        </strong>

      </div>


      <div class="info-row">

        <span>
          Category
        </span>

        <strong>
          {{ store.category }}
        </strong>

      </div>


      <div class="info-row">

        <span>
          Delivery time
        </span>

        <strong>
          {{ store.deliveryTime }}
        </strong>

      </div>

    </section>


    <!-- =========================
         REVIEWS
    ========================== -->
    <section
      v-if="activeTab === 'reviews'"
      class="tab-content"
    >

      <h2>
        Reviews
      </h2>

      <p class="empty-text">
        Reviews will appear here.
      </p>

    </section>

  </div>

</template>


<script setup>

// =========================
// 1. 필요한 Vue 기능 import
// =========================

import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'


// =========================
// 2. Router 사용 준비
// =========================

const route = useRoute()
const router = useRouter()


// =========================
// 3. URL에서 Store ID 가져오기
// =========================

// 예:
// /stores/1
//
// route.params.id
// → 1

const storeId = route.params.id

console.log('현재 Store ID:', storeId)


// =========================
// 4. 현재 선택된 Tab
// =========================

const activeTab = ref('menu')


// =========================
// 5. 찜 상태
// =========================

const favorite = ref(false)


// =========================
// 6. Seller 본인 Store 여부
// =========================

// 임시 테스트값
//
// true
// → Seller 화면
// → Add product / Edit / Delete 표시
//
// false
// → Customer 화면
// → Add to cart 표시
//
// 나중에는 JWT를 이용해서
// 실제 Store 주인인지 백엔드에서 확인할 것

const isSellerOwner = ref(true)


// =========================
// 7. 임시 Store 데이터
// =========================

// 나중에는
//
// GET /api/stores/{id}
//
// 로 백엔드에서 가져올 예정

const store = ref({

  id: Number(storeId),

  name: 'Yummy Burger',

  image: '🍔',

  rating: 4.8,

  reviewCount: 128,

  category: 'Burger · American · Fries',

  description:
    'Fresh burgers made with quality ingredients.',

  deliveryTime: '20–30 min',

  deliveryFee: '$0',

  address: '123 State Street'

})


// =========================
// 8. 임시 Product 데이터
// =========================

// 나중에는 Seller가 등록한 상품을
// DB에서 가져올 예정

const products = ref([

  {
    id: 1,

    name: 'Cheese Burger',

    description:
      'Beef patty, cheese, lettuce and special sauce',

    price: 12.99,

    image: '🍔'
  },


  {
    id: 2,

    name: 'Double Burger',

    description:
      'Two beef patties with double cheese',

    price: 15.99,

    image: '🍔'
  },


  {
    id: 3,

    name: 'French Fries',

    description:
      'Crispy golden french fries',

    price: 4.99,

    image: '🍟'
  }

])


// =========================
// 9. 뒤로가기
// =========================

const goBack = () => {

  router.back()

}


// =========================
// 10. 찜 버튼
// =========================

const toggleFavorite = () => {

  favorite.value = !favorite.value

}


// =========================
// 11. 상품 등록
// =========================

const addProduct = () => {

  alert('Product registration page')

  // 나중에 만들 페이지
  //
  // router.push(`/stores/${storeId}/products/new`)

}


// =========================
// 12. 상품 수정
// =========================

const editProduct = (productId) => {

  alert(`Edit Product ID: ${productId}`)

}


// =========================
// 13. 상품 삭제
// =========================

const deleteProduct = (productId) => {

  alert(`Delete Product ID: ${productId}`)

}


// =========================
// 14. 장바구니 추가
// =========================

const addToCart = (product) => {

  alert(`${product.name} added to cart!`)

}

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

  margin: 0 auto;

  padding-bottom: 80px;

  background: #ffffff;

  color: #3A251E;

  font-family: 'Montserrat', sans-serif;

}


/* =========================
   Header Image
========================= */

.store-header {

  position: relative;

  height: 230px;

  display: flex;

  align-items: center;
  justify-content: center;

  background:
    linear-gradient(
      135deg,
      #fff0e4,
      #ffd8bb
    );

}


.header-food {

  font-size: 110px;

}


.back-button {

  position: absolute;

  top: 20px;
  left: 20px;

  width: 42px;
  height: 42px;

  border: none;
  border-radius: 50%;

  background: rgba(255,255,255,0.92);

  color: #3A251E;

  font-size: 32px;

  cursor: pointer;

}


/* =========================
   Store Main
========================= */

.store-main {

  padding: 25px 20px 5px;

}


.store-main h1 {

  margin: 0 0 10px;

  font-size: 25px;
  font-weight: 800;

}


.rating {

  display: flex;

  align-items: center;

  gap: 5px;

  margin-bottom: 8px;

  font-size: 12px;

  color: #777;

}


.star {

  color: #ffb000;

  font-size: 16px;

}


.category {

  margin: 0 0 8px;

  color: #9ca3af;

  font-size: 12px;

}


.description {

  margin: 0;

  color: #737373;

  font-size: 12px;

  line-height: 1.6;

}


/* =========================
   Delivery
========================= */

.delivery-info {

  display: grid;

  grid-template-columns: 1fr 1fr;

  margin-top: 22px;

  padding: 17px;

  background: #fafafa;

  border-radius: 14px;

}


.delivery-info div {

  display: flex;

  flex-direction: column;

  gap: 5px;

}


.info-label {

  color: #9ca3af;

  font-size: 10px;

}


.delivery-info strong {

  font-size: 12px;

}


/* =========================
   Favorite / Share
========================= */

.store-actions {

  display: grid;

  grid-template-columns: 1fr 1fr;

  gap: 10px;

  margin-top: 16px;

}


.store-actions button {

  padding: 12px;

  border: 1px solid #ececec;

  border-radius: 10px;

  background: white;

  color: #3A251E;

  font-size: 11px;
  font-weight: 700;

  cursor: pointer;

}


/* =========================
   Tabs
========================= */

.tabs {

  display: grid;

  grid-template-columns: repeat(3, 1fr);

  margin-top: 20px;

  border-top: 1px solid #eeeeee;
  border-bottom: 1px solid #eeeeee;

}


.tabs button {

  padding: 17px;

  border: none;

  background: white;

  color: #aaaaaa;

  font-size: 12px;
  font-weight: 700;

  cursor: pointer;

}


.tabs button.active {

  color: #ff6b00;

  border-bottom: 3px solid #ff6b00;

}


/* =========================
   Menu
========================= */

.menu-section {

  padding: 25px 20px;

}


.menu-header {

  display: flex;

  justify-content: space-between;
  align-items: center;

  margin-bottom: 18px;

}


.small-title {

  margin: 0 0 4px;

  color: #ff6b00;

  font-size: 9px;
  font-weight: 800;

  letter-spacing: 1px;

}


.menu-header h2 {

  margin: 0;

  font-size: 18px;

  font-weight: 800;

}


.add-product-button {

  padding: 10px 13px;

  border: none;
  border-radius: 9px;

  background: #ff6b00;

  color: white;

  font-size: 10px;
  font-weight: 700;

  cursor: pointer;

}


/* =========================
   Product
========================= */

.product-card {

  display: flex;

  justify-content: space-between;

  gap: 15px;

  padding: 19px 0;

  border-bottom: 1px solid #eeeeee;

}


.product-info {

  flex: 1;

}


.product-info h3 {

  margin: 0 0 7px;

  font-size: 14px;
  font-weight: 800;

}


.product-info p {

  margin: 0 0 10px;

  color: #9ca3af;

  font-size: 10px;

  line-height: 1.5;

}


.price {

  display: block;

  margin-bottom: 12px;

  color: #3A251E;

  font-size: 13px;

}


.product-image {

  width: 95px;
  height: 95px;

  flex-shrink: 0;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 15px;

  background: #fff4eb;

  font-size: 48px;

}


/* =========================
   Seller Buttons
========================= */

.seller-actions {

  display: flex;

  gap: 7px;

}


.seller-actions button {

  padding: 7px 11px;

  border: 1px solid #ff6b00;

  border-radius: 7px;

  background: white;

  color: #ff6b00;

  font-size: 9px;

  font-weight: 700;

  cursor: pointer;

}


.seller-actions .delete-button {

  border-color: #ef4444;

  color: #ef4444;

}


/* =========================
   Customer Cart Button
========================= */

.cart-button {

  padding: 8px 12px;

  border: none;
  border-radius: 8px;

  background: #ff6b00;

  color: white;

  font-size: 9px;
  font-weight: 700;

  cursor: pointer;

}


/* =========================
   Info / Review
========================= */

.tab-content {

  padding: 25px 20px;

}


.tab-content h2 {

  margin: 0 0 20px;

  font-size: 18px;

}


.info-row {

  display: flex;

  justify-content: space-between;

  gap: 20px;

  padding: 15px 0;

  border-bottom: 1px solid #eeeeee;

  font-size: 11px;

}


.info-row span {

  color: #9ca3af;

}


.info-row strong {

  text-align: right;

}


.empty-text {

  color: #9ca3af;

  font-size: 12px;

}

</style>