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


  <!-- 실제 대표 이미지 -->
  <img
    v-if="store.imageUrl"
    :src="`http://localhost:8080${store.imageUrl}`"
    class="store-header-image"
  />


  <!-- 아직 이미지가 없는 경우 -->
  <div
    v-else
    class="header-food"
  >
  </div>


  <!-- Seller 이미지 업로드 -->
  <div
    v-if="isSellerOwner"
    class="image-upload-area"
  >

    <input
      type="file"
      accept="image/*"
      @change="handleImageChange"
    />

<button
  class="upload-image-button"
  @click="uploadImage"
>
  Upload Image
</button>

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


<!-- Admin Support -->
<button
  class="chat-button"
  @click="goToChat"
>
  <span class="chat-icon">
    💬
  </span>

  <div class="chat-text">

    <strong>
      Admin Support
    </strong>

    <span>
      Chat with YamiYumi Admin
    </span>

  </div>

  <span class="arrow">
    ›
  </span>

</button>

      
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
// 1. Vue 기능 import
// =========================

// ref
// → 값이 변경되면 화면도 자동으로 변경됨
//
// onMounted
// → StoreDetailPage가 처음 열렸을 때
//   실행할 코드를 지정할 때 사용
import {
  ref,
  onMounted
} from 'vue'


// useRoute
// → 현재 URL의 parameter를 가져오기 위해 사용
//
// useRouter
// → 뒤로가기 / 페이지 이동 등에 사용
import {
  useRoute,
  useRouter
} from 'vue-router'



// =========================
// 2. Router 사용 준비
// =========================

// 현재 URL 정보
const route = useRoute()


// 페이지 이동 기능
const router = useRouter()



// =========================
// 3. URL에서 Store ID 가져오기
// =========================

// 예:
//
// /stores/1
//
// 이라면:
//
// route.params.id
// → "1"
const storeId =
  route.params.id


console.log(
  '현재 Store ID:',
  storeId
)



// =========================
// 4. 현재 선택된 Tab
// =========================

// 기본으로 menu 탭 선택
const activeTab =
  ref('menu')



// =========================
// 5. 찜 상태
// =========================

// false → 찜 안 함
// true  → 찜 함
const favorite =
  ref(false)



// =========================
// 6. Seller 본인 Store 여부
// =========================

// ⚠ 현재는 임시값
//
// true
// → Seller 전용 버튼 표시
//
// false
// → Customer용 버튼 표시
//
// 나중에 JWT + Seller + Store 관계를
// 백엔드에서 확인해서 변경해야 함
const isSellerOwner =
  ref(true)



// =========================
// 7. Store 데이터
// =========================

// 처음에는 빈 값을 가지고 있다가
//
// GET /api/stores/{storeId}
//
// 요청이 성공하면
// 실제 DB 데이터로 변경됨
const store = ref({

  // Store ID
  id: null,


  // 가게 이름
  name: '',


  // 가게 설명
  description: '',


  // 가게 주소
  address: '',


  // Seller가 등록한 대표 이미지 URL
  //
  // 예:
  //
  // /uploads/stores/abc.jpg
  imageUrl: null,


  // =========================
  // 아직 백엔드 연결 안 한 임시값
  // =========================

  rating: 4.8,

  reviewCount: 128,

  category:
    'Burger · American · Fries',

  deliveryTime:
    '20–30 min',

  deliveryFee:
    '$0'

})



// =========================
// 8. Store 상세정보 조회
// =========================

const loadStore = async () => {

  try {

    // 현재 URL의 storeId를 이용해서
    // 백엔드에 해당 Store 조회 요청
    //
    // 예:
    //
    // GET /api/stores/1
    const response =
      await fetch(
        `http://localhost:8080/api/stores/${storeId}`
      )


    // 200번대가 아니면 실패 처리
    if (!response.ok) {

      throw new Error(
        '가게 정보를 불러오지 못했습니다.'
      )

    }


    // 백엔드 JSON 응답을
    // JavaScript 객체로 변환
    const data =
      await response.json()


    // 기존 임시값은 유지하면서
    // 백엔드에서 받은 값으로 덮어씀
    //
    // 백엔드에서 현재 오는 값:
    //
    // id
    // name
    // description
    // address
    // imageUrl
    store.value = {

      ...store.value,

      ...data

    }


    console.log(
      '가져온 Store:',
      store.value
    )


  } catch (error) {

    console.error(
      'Store 조회 실패:',
      error
    )

  }

}

// =========================
// Store 대표 이미지 선택
// =========================

// Seller가 선택한 실제 이미지 파일을 저장
const selectedImage = ref(null)


// input에서 사진을 선택했을 때 실행
const handleImageChange = (event) => {

  // 사용자가 선택한 첫 번째 파일
  const file = event.target.files[0]


  // 아무 파일도 선택하지 않았다면 종료
  if (!file) {
    return
  }


  // 선택한 파일 저장
  selectedImage.value = file


  // 개발 중 확인용
  console.log(
    '선택한 이미지:',
    selectedImage.value
  )
}


// =========================
// 9. 페이지 처음 열릴 때 Store 조회
// =========================

onMounted(() => {

  loadStore()

})



// =========================
// 10. 임시 Product 데이터
// =========================

// Product 백엔드는 아직 연결하지 않았으므로
// 일단 기존 테스트 데이터 유지
const products = ref([

  {

    id: 1,

    name:
      'Cheese Burger',

    description:
      'Beef patty, cheese, lettuce and special sauce',

    price:
      12.99,

    image:
      '🍔'

  },


  {

    id: 2,

    name:
      'Double Burger',

    description:
      'Two beef patties with double cheese',

    price:
      15.99,

    image:
      '🍔'

  },


  {

    id: 3,

    name:
      'French Fries',

    description:
      'Crispy golden french fries',

    price:
      4.99,

    image:
      '🍟'

  }

])



// =========================
// 11. 뒤로가기
// =========================

const goBack = () => {

  router.back()

}




// // =========================
// // 12. 찜 버튼
// // =========================

// const toggleFavorite = () => {

//   favorite.value =
//     !favorite.value

// }

// =========================
// 12. Seller Chat
// =========================

const goToChat = () => {

  router.push('/seller/chat')

}


// =========================
// 13. 상품 등록
// =========================

const addProduct = () => {
  router.push({
    name: 'product-register'
  })
}



// =========================
// 14. 상품 수정
// =========================

const editProduct = (
  productId
) => {

  alert(
    `Edit Product ID: ${productId}`
  )

}



// =========================
// 15. 상품 삭제
// =========================

const deleteProduct = (
  productId
) => {

  alert(
    `Delete Product ID: ${productId}`
  )

}



// =========================
// 16. 장바구니 추가
// =========================

const addToCart = (
  product
) => {

  alert(
    `${product.name} added to cart!`
  )

}

// =========================
// Store 대표 이미지 업로드
// =========================

const uploadImage = async () => {

  // 사진을 선택하지 않았으면 중단
  if (!selectedImage.value) {

    alert('이미지를 먼저 선택해주세요.')

    return
  }


  // 로그인할 때 저장한 JWT
  const token =
    localStorage.getItem('token')


  // 파일 전송용 FormData 생성
  const formData =
    new FormData()


  // 백엔드의 @RequestParam("image")와 이름을 맞춤
  formData.append(
    'image',
    selectedImage.value
  )


  try {

    // 우리가 만든 이미지 업로드 API 호출
    const response =
      await fetch(
        'http://localhost:8080/api/stores/image',
        {
          method: 'PATCH',

          headers: {
            Authorization: `Bearer ${token}`
          },

          body: formData
        }
      )


    if (!response.ok) {

      throw new Error(
        '이미지 업로드에 실패했습니다.'
      )
    }


    // 백엔드에서 반환한 imageUrl
    const imageUrl =
      await response.text()


    // 업로드 직후 화면 이미지도 바로 변경
    store.value.imageUrl =
      imageUrl


    alert(
      '대표 이미지가 변경되었습니다.'
    )


  } catch (error) {

    console.error(
      '이미지 업로드 실패:',
      error
    )

    alert(
      '이미지 업로드에 실패했습니다.'
    )
  }
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

.image-upload-area {
  position: absolute !important;

  right: 20px !important;
  bottom: 15px !important;

  top: auto !important;
  left: auto !important;

  width: auto !important;
  margin: 0 !important;

  display: flex;
  align-items: center;
  justify-content: flex-start;

  gap: 10px;

  transform: none !important;

  z-index: 20;
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

.image-upload-area {
  position: absolute;
  top: 20px;
  right: 20px;

  display: flex;
  align-items: center;
  gap: 10px;

  z-index: 10;
}

.chat-button {

  width: 100%;

  display: flex;

  align-items: center;
  justify-content: center;

  gap: 8px;

  padding: 14px;

  margin-bottom: 20px;

  border: none;
  border-radius: 14px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-family: 'Montserrat', sans-serif;

  font-size: 12px;
  font-weight: 800;

  cursor: pointer;

  transition: 0.2s;

}


.chat-button:hover {

  background-color: #ffe6d2;

}


</style>