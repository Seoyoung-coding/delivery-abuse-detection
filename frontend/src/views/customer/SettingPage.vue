<template>

  <div class="page">

    <!-- =========================
         Header
    ========================== -->

    <div class="header">

      <button
        class="back-button"
        @click="goBack"
      >
        ‹
      </button>


      <h1>
        Settings
      </h1>

    </div>


    <!-- =========================
         Account Section
    ========================== -->

    <div class="section">

      <p class="section-title">
        ACCOUNT
      </p>


      <!-- Account Information -->
      <div class="setting-item">

        <div class="setting-left">

          <div class="icon-box">
            👤
          </div>


          <div>

            <p class="setting-name">
              Account Information
            </p>

            <p class="setting-description">
              Manage your account details
            </p>

          </div>

        </div>


        <span class="arrow">
          ›
        </span>

      </div>


      <!-- Change Password -->
      <div class="setting-item">

        <div class="setting-left">

          <div class="icon-box">
            🔒
          </div>


          <div>

            <p class="setting-name">
              Change Password
            </p>

            <p class="setting-description">
              Update your password
            </p>

          </div>

        </div>


        <span class="arrow">
          ›
        </span>

      </div>

    </div>



    <!-- =========================
         Preferences Section
    ========================== -->

    <div class="section">

      <p class="section-title">
        PREFERENCES
      </p>


      <!-- Notifications -->
      <div class="setting-item">

        <div class="setting-left">

          <div class="icon-box">
            🔔
          </div>


          <div>

            <p class="setting-name">
              Notifications
            </p>

            <p class="setting-description">
              Manage notification preferences
            </p>

          </div>

        </div>


        <span class="arrow">
          ›
        </span>

      </div>


      <!-- Language -->
      <div class="setting-item">

        <div class="setting-left">

          <div class="icon-box">
            🌐
          </div>


          <div>

            <p class="setting-name">
              Language
            </p>

            <p class="setting-description">
              English
            </p>

          </div>

        </div>


        <span class="arrow">
          ›
        </span>

      </div>

    </div>



    <!-- =========================
         Support Section
    ========================== -->

    <div class="section">

      <p class="section-title">
        SUPPORT
      </p>


      <!-- Help -->
      <div class="setting-item">

        <div class="setting-left">

          <div class="icon-box">
            ?
          </div>


          <div>

            <p class="setting-name">
              Help & Support
            </p>

            <p class="setting-description">
              Get help with YAMIYUMI
            </p>

          </div>

        </div>


        <span class="arrow">
          ›
        </span>

      </div>


      <!-- Privacy -->
      <div class="setting-item">

        <div class="setting-left">

          <div class="icon-box">
            🛡
          </div>


          <div>

            <p class="setting-name">
              Privacy
            </p>

            <p class="setting-description">
              Privacy and account security
            </p>

          </div>

        </div>


        <span class="arrow">
          ›
        </span>

      </div>


      <!-- =========================
           Become a Seller
      ========================== -->

      <div
        class="setting-item"
        @click="goToSellerApplication"
      >

        <div class="setting-left">

          <div class="icon-box">
            🏪
          </div>


          <div>

            <p class="setting-name">
              Become a Seller
            </p>

            <p class="setting-description">
              Apply to start selling on YAMIYUMI
            </p>

          </div>

        </div>


        <span class="arrow">
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
// 1. Import
// =========================

import { useRouter } from 'vue-router'

import BottomNav from '@/components/BottomNav.vue'


// =========================
// 2. Router
// =========================

const router = useRouter()


// =========================
// 3. 뒤로가기
// =========================

const goBack = () => {

  router.push('/profile')

}


// =========================
// 4. Seller 신청 페이지 이동
// =========================

const goToSellerApplication = () => {

  router.push('/seller-application')

}


// =========================
// 5. Logout
// =========================

const logout = () => {

  // 로그인 JWT 삭제
  localStorage.removeItem('token')


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
   Header
========================= */

.header {

  position: relative;

  display: flex;

  align-items: center;

  margin-bottom: 30px;

}


.header h1 {

  margin: 0;

  font-size: 27px;

  font-weight: 800;

  color: #3A251E;

}


/* =========================
   Back Button
========================= */

.back-button {

  width: 38px;
  height: 38px;

  display: flex;

  align-items: center;
  justify-content: center;

  margin-right: 10px;

  padding: 0;

  border: none;

  border-radius: 10px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-size: 30px;

  font-family: Arial, sans-serif;

  line-height: 1;

  cursor: pointer;

  transition: 0.2s;

}


.back-button:hover {

  background-color: #ffe7d3;

}


/* =========================
   Section
========================= */

.section {

  margin-bottom: 30px;

}


.section-title {

  margin: 0 0 10px 4px;

  color: #9ca3af;

  font-size: 10px;

  font-weight: 700;

  letter-spacing: 1.3px;

}


/* =========================
   Setting Item
========================= */

.setting-item {

  display: flex;

  align-items: center;

  justify-content: space-between;

  min-height: 70px;

  padding: 12px 4px;

  border-bottom: 1px solid #f3f4f6;

  cursor: pointer;

  transition:
    padding-left 0.2s,
    background-color 0.2s;

}


.setting-item:hover {

  padding-left: 9px;

  background-color: #fffaf7;

}


/* =========================
   Left
========================= */

.setting-left {

  display: flex;

  align-items: center;

  gap: 13px;

  min-width: 0;

}


/* =========================
   Icon
========================= */

.icon-box {

  width: 42px;
  height: 42px;

  display: flex;

  justify-content: center;
  align-items: center;

  flex-shrink: 0;

  background-color: #fff4eb;

  color: #ff6b00;

  border-radius: 11px;

  font-size: 18px;

  font-weight: 700;

}


/* =========================
   Setting Name
========================= */

.setting-name {

  margin: 0 0 4px;

  color: #3A251E;

  font-size: 13px;

  font-weight: 700;

}


/* =========================
   Setting Description
========================= */

.setting-description {

  margin: 0;

  color: #9ca3af;

  font-size: 10px;

  font-weight: 500;

}


/* =========================
   Arrow
========================= */

.arrow {

  margin-left: 15px;

  color: #c4c4c4;

  font-size: 22px;

  font-weight: 500;

  flex-shrink: 0;

}


/* =========================
   Logout
========================= */

.logout {

  width: 100%;

  padding: 13px;

  margin-top: 5px;

  background-color: #fff4eb;

  color: #ff6b00;

  border: none;

  border-radius: 10px;

  font-family: 'Montserrat', sans-serif;

  font-size: 13px;

  font-weight: 700;

  cursor: pointer;

  transition: 0.2s;

}


.logout:hover {

  background-color: #ffe7d3;

}

</style>