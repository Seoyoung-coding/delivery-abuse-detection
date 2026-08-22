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


import { ref, onMounted } from 'vue'

import { useRoute, useRouter } from 'vue-router'

const route = useRoute()

const router = useRouter()



const storeId = route.params.id // url에서 store 아이디 가져오기


// 개발 중 확인용
console.log(
  '현재 Store ID:',
  storeId
)


const activeTab = ref('menu')


const favorite = ref(false)



// =========================
// Seller 본인 Store 여부
// =========================

// 현재는 테스트용으로 true
//
// true
// → 이 Store의 주인
// → Add Product / Edit / Delete 표시
//
// false
// → 일반 Customer
// → Add to Cart 표시
//
// 나중에는 백엔드에서
// JWT를 확인해서 실제 Store 주인인지 판단해야 함
const isSellerOwner = ref(true)



// =========================
// Store 데이터
// =========================

// 처음 페이지가 열렸을 때 사용할 기본값
//
// 백엔드 요청이 성공하면
// 아래 값들이 실제 DB 데이터로 변경됨
const store = ref({

  // Store PK
  id: null,

  // Store 이름
  name: '',

  // Store 설명
  description: '',

  // Store 주소
  address: '',

  // Seller가 업로드한 대표 이미지 주소
  //
  // 예:
  // /uploads/stores/abc.jpg
  imageUrl: null,


  // =========================
  // 아래 값들은 아직 임시
  // =========================

  // 평점
  rating: 4.8,

  // 리뷰 수
  reviewCount: 128,

  // 카테고리
  category:
    'Burger · American · Fries',

  // 배달 예상 시간
  deliveryTime:
    '20–30 min',

  // 배달비
  deliveryFee:
    '$0'

})



// =========================
// 8. Store 정보 가져오기
// =========================

// async
// → 서버 요청처럼 시간이 걸리는 작업을
//   await와 함께 사용하기 위해 필요
const loadStore = async () => {

  try {

    // =========================
    // 8-1. 백엔드에 GET 요청
    // =========================

    // 현재 Store ID를 URL에 넣음
    //
    // 예:
    //
    // storeId = 1
    //
    // ↓
    //
    // GET
    // http://localhost:8080/api/stores/1
    const response = await fetch(
      `http://localhost:8080/api/stores/${storeId}`
    )


    // =========================
    // 8-2. 요청 실패 확인
    // =========================

    // response.ok는
    //
    // 200 ~ 299
    //
    // 응답이면 true
    //
    // 404 / 500 등이면 false
    if (!response.ok) {

      throw new Error(
        '가게 정보를 불러오지 못했습니다.'
      )

    }


    // =========================
    // 8-3. JSON 데이터로 변환
    // =========================

    // 백엔드 StoreResponse가 예를 들어:
    //
    // {
    //   "id": 1,
    //   "name": "Yummy Burger",
    //   "description": "Fresh Burger",
    //   "address": "123 State Street",
    //   "imageUrl": "/uploads/stores/abc.jpg"
    // }
    //
    // 라면
    //
    // data에 이 객체가 들어감
    const data = await response.json()


    // =========================
    // 8-4. Store 데이터 업데이트
    // =========================

    // 기존 store.value 값을 유지하면서
    //
    // 백엔드에서 받은 data를 덮어씀
    //
    // 이렇게 하는 이유는
    //
    // rating
    // reviewCount
    // category
    // deliveryTime
    // deliveryFee
    //
    // 같은 임시값은 현재 백엔드 응답에 없기 때문
    store.value = {

      // 기존 값 유지
      ...store.value,

      // 백엔드 값으로 덮어쓰기
      ...data

    }


    // 개발 중 확인용
    console.log(
      '가져온 Store:',
      store.value
    )


  } catch (error) {

    // 서버 연결 실패
    // 404
    // 500
    //
    // 등이 발생하면 여기로 옴
    console.error(
      'Store 조회 실패:',
      error
    )

  }

}



// =========================
// 9. 페이지가 처음 열릴 때 실행
// =========================

// StoreDetailPage가 화면에 나타나는 순간
// loadStore() 실행
onMounted(() => {

  loadStore()

})



// =========================
// 10. 임시 Product 데이터
// =========================

// 아직 Product 백엔드는 연결하지 않았기 때문에
// 기존 임시 데이터 그대로 유지
const products = ref([

  {
    // Product ID
    id: 1,

    // 상품 이름
    name: 'Cheese Burger',

    // 상품 설명
    description:
      'Beef patty, cheese, lettuce and special sauce',

    // 상품 가격
    price: 12.99,

    // 현재는 실제 사진 대신 emoji 사용
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
// 11. 뒤로가기
// =========================

const goBack = () => {

  // 브라우저 이전 페이지로 이동
  router.back()

}



// =========================
// 12. 찜 버튼
// =========================

const toggleFavorite = () => {

  // false → true
  // true → false
  //
  // 로 변경
  favorite.value =
    !favorite.value

}



// =========================
// 13. 상품 등록
// =========================

const addProduct = () => {

  // 현재는 테스트용 alert
  alert(
    'Product registration page'
  )


  // 나중에 Product 등록 페이지를 만들면:
  //
  // router.push(
  //   `/stores/${storeId}/products/new`
  // )

}



// =========================
// 14. 상품 수정
// =========================

const editProduct = (productId) => {

  // 어떤 상품을 수정할지
  // productId를 받음
  alert(
    `Edit Product ID: ${productId}`
  )

}



// =========================
// 15. 상품 삭제
// =========================

const deleteProduct = (productId) => {

  // 어떤 상품을 삭제할지
  // productId를 받음
  alert(
    `Delete Product ID: ${productId}`
  )

}



// =========================
// 16. 장바구니 추가
// =========================

const addToCart = (product) => {

  // 클릭한 product 객체를 받아옴
  alert(
    `${product.name} added to cart!`
  )

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