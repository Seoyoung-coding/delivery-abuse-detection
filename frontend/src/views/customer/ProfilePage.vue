<template>

  <div class="page">

    <!-- =========================
         Profile Title
    ========================== -->

    <h1>Profile</h1>


    <!-- =========================
         User Information
    ========================== -->

    <div class="user">

      <div class="avatar">
        👤
      </div>


      <div>

        <!-- 현재 사용자 이름 -->
        <h2>
          {{ userName }}
        </h2>


        <!-- 현재 로그인한 사용자 email -->
        <p>
          {{ userEmail }}
        </p>

      </div>

    </div>


    <!-- =========================
         Menu
    ========================== -->

    <div class="menu">


      <!-- Delivery Address -->
      <div class="menu-item">

        <span>
          📍 Delivery Address
        </span>

        <span>
          ›
        </span>

      </div>


      <!-- Payment -->
      <div class="menu-item">

        <span>
          💳 Payment Methods
        </span>

        <span>
          ›
        </span>

      </div>


      <!-- Order -->
      <div class="menu-item">

        <span>
          📦 Order History
        </span>

        <span>
          ›
        </span>

      </div>


      <!-- Favorites -->
      <div class="menu-item">

        <span>
          ♡ Favorites
        </span>

        <span>
          ›
        </span>

      </div>


      <!-- =========================
           Seller 전용 My Store
      ========================== -->

      <div
        v-if="isSeller"
        class="menu-item seller-menu"
        @click="goToMyStore"
      >

        <span>
          🏪 My Store
        </span>

        <span>
          ›
        </span>

      </div>


      <!-- Settings -->
      <div class="menu-item">

        <span>
          ⚙ Settings
        </span>

        <span>
          ›
        </span>

      </div>

    </div>


    <!-- =========================
         Logout
    ========================== -->

    <button
      class="logout"
      @click="logout"
    >
      Logout
    </button>


    <!-- =========================
         Bottom Navigation
    ========================== -->

    <BottomNav />

  </div>

</template>


<script setup>

// =========================
// 1. Vue 기능 import
// =========================

import {
  ref,
  onMounted
} from 'vue'

import { useRouter } from 'vue-router'

import BottomNav from '@/components/BottomNav.vue'


// =========================
// 2. Router
// =========================

const router = useRouter()


// =========================
// 3. User 정보
// =========================

// 화면에 보여줄 이메일
const userEmail = ref('')

// 화면에 보여줄 사용자 이름
const userName = ref('User')


// =========================
// 4. Seller 정보
// =========================

// 현재 사용자가 Seller인지
const isSeller = ref(false)

// Seller에게 Store가 존재하는지
const hasStore = ref(false)


// =========================
// 5. Profile 페이지 시작
// =========================

onMounted(async () => {

  console.log('ProfilePage mounted')

  // 사용자 정보 조회
  await loadCurrentUser()

  // Seller 상태 조회
  await loadSellerStatus()

})


// =========================
// 6. 현재 로그인 사용자 조회
// =========================

const loadCurrentUser = async () => {

  // 로그인할 때 저장된 JWT
  const token = localStorage.getItem('token')


  console.log(
    'ProfilePage token:',
    token
  )


  // =========================
  // 중요
  //
  // token이 없더라도
  // 여기서 login으로 보내지 않는다.
  //
  // Profile 화면은 그대로 유지
  // =========================

  if (!token) {

    console.log(
      'ProfilePage: token 없음'
    )

    userName.value = 'User'
    userEmail.value = ''

    return
  }


  try {

    const response = await fetch(
      'http://localhost:8080/api/customers/me',
      {
        method: 'GET',

        headers: {

          Authorization: `Bearer ${token}`

        }
      }
    )


    console.log(
      '/api/customers/me status:',
      response.status
    )


    // =========================
    // 401이어도 로그인 화면으로 보내지 않음
    // =========================

    if (response.status === 401) {

      console.log(
        '현재 사용자 인증 실패'
      )

      return
    }


    // =========================
    // 기타 서버 오류
    // =========================

    if (!response.ok) {

      const result =
        await response.text()

      console.error(
        '사용자 조회 실패:',
        result
      )

      return
    }


    // =========================
    // 백엔드에서 email String 반환
    // =========================

    const email =
      await response.text()


    console.log(
      '현재 사용자:',
      email
    )


    // 이메일 저장
    userEmail.value = email


    // username 필드가 아직 없으므로
    // email 앞부분을 임시 이름으로 사용
    //
    // seller@gmail.com
    // →
    // seller

    if (email.includes('@')) {

      userName.value =
        email.split('@')[0]

    } else {

      userName.value = email

    }


  } catch (error) {

    console.error(
      '사용자 조회 실패:',
      error
    )

  }

}


// =========================
// 7. Seller 상태 조회
// =========================

const loadSellerStatus = async () => {

  const token =
    localStorage.getItem('token')


  // token 없으면 그냥 일반 Customer 처리
  if (!token) {

    isSeller.value = false
    hasStore.value = false

    return
  }


  try {

    const response = await fetch(
      'http://localhost:8080/api/sellers/me/status',
      {
        method: 'GET',

        headers: {

          Authorization: `Bearer ${token}`

        }
      }
    )


    console.log(
      '/api/sellers/me/status status:',
      response.status
    )


    // =========================
    // Seller API 인증 실패
    //
    // 절대로 logout시키지 않는다.
    // 그냥 일반 Customer 취급
    // =========================

    if (
      response.status === 401 ||
      response.status === 403
    ) {

      console.log(
        'Seller 상태 조회 불가 → 일반 Customer 처리'
      )

      isSeller.value = false
      hasStore.value = false

      return
    }


    // =========================
    // 기타 서버 오류
    // =========================

    if (!response.ok) {

      const result =
        await response.text()

      console.error(
        'Seller 상태 조회 실패:',
        result
      )

      isSeller.value = false
      hasStore.value = false

      return
    }


    // =========================
    // 정상 응답
    //
    // 예:
    //
    // {
    //   "seller": true,
    //   "hasStore": true
    // }
    // =========================

    const result =
      await response.json()


    console.log(
      'Seller 상태:',
      result
    )


    isSeller.value =
      result.seller === true


    hasStore.value =
      result.hasStore === true


  } catch (error) {

    console.error(
      'Seller 상태 조회 실패:',
      error
    )


    // 오류 발생해도
    // Profile 페이지는 유지
    isSeller.value = false
    hasStore.value = false

  }

}


// =========================
// 8. My Store 클릭
// =========================

const goToMyStore = () => {

  // Seller지만 Store가 아직 없음
  if (!hasStore.value) {

    router.push('/store-setup')

    return
  }


  // Store가 이미 존재
  router.push('/seller/store')

}


// =========================
// 9. Logout
// =========================

const logout = () => {

  // JWT 삭제
  localStorage.removeItem('token')


  // Logout 버튼을 눌렀을 때만
  // 로그인 페이지 이동
  router.push('/login')

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

  margin: auto;

  padding: 25px 20px 100px;

  background-color: #ffffff;

  font-family: 'Montserrat', sans-serif;

  color: #3A251E;

}


/* =========================
   Page Title
========================= */

.page h1 {

  margin: 0;

  font-size: 27px;

  font-weight: 800;

  color: #3A251E;

}


/* =========================
   User
========================= */

.user {

  display: flex;

  align-items: center;

  gap: 15px;

  padding: 24px 0;

}


/* =========================
   Profile Avatar
========================= */

.avatar {

  width: 65px;
  height: 65px;

  display: flex;

  justify-content: center;
  align-items: center;

  flex-shrink: 0;

  background-color: #fff4eb;

  border-radius: 50%;

  font-size: 28px;

}


/* =========================
   User Name
========================= */

.user h2 {

  margin: 0 0 5px;

  font-size: 17px;

  font-weight: 800;

}


/* =========================
   Email
========================= */

.user p {

  margin: 0;

  color: #9ca3af;

  font-size: 11px;

}


/* =========================
   Menu
========================= */

.menu {

  margin-top: 10px;

}


.menu-item {

  display: flex;

  justify-content: space-between;
  align-items: center;

  padding: 18px 4px;

  border-bottom: 1px solid #f3f4f6;

  font-size: 13px;

  font-weight: 500;

  cursor: pointer;

  transition: 0.2s;

}


.menu-item:hover {

  padding-left: 9px;

  color: #ff6b00;

}


/* =========================
   Seller Menu
========================= */

.seller-menu {

  color: #ff6b00;

  font-weight: 700;

}


/* =========================
   Logout
========================= */

.logout {

  width: 100%;

  padding: 13px;

  margin-top: 30px;

  background-color: #fff4eb;

  color: #ff6b00;

  border: none;

  border-radius: 10px;

  font-family: 'Montserrat', sans-serif;

  font-weight: 700;

  cursor: pointer;

  transition: 0.2s;

}


.logout:hover {

  background-color: #ffe7d3;

}

</style>