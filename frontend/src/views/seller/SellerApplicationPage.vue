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


      <div>
        <h1>Seller Application</h1>

        <p class="header-description">
          Start selling on YAMIYUMI
        </p>
      </div>

    </div>



    <!-- =========================
         Loading
    ========================== -->

    <div
      v-if="isLoading"
      class="loading-box"
    >

      Checking your application...

    </div>



    <!-- =========================
         PENDING
    ========================== -->

    <div
      v-else-if="applicationStatus === 'PENDING'"
      class="status-card pending-card"
    >

      <div class="status-icon">
        ⏳
      </div>


      <h2>
        Application Pending
      </h2>


      <p>
        Your seller application has been submitted successfully.
        Our admin team is currently reviewing your application.
      </p>


      <div class="status-badge pending">
        PENDING
      </div>


      <button
        class="secondary-button"
        @click="goToProfile"
      >
        Back to Profile
      </button>

    </div>



    <!-- =========================
         APPROVED
    ========================== -->

    <div
      v-else-if="applicationStatus === 'APPROVED'"
      class="status-card approved-card"
    >

      <div class="status-icon">
        🎉
      </div>


      <h2>
        You're Approved!
      </h2>


      <p>
        Your seller application has been approved.
        You can now set up your store and start selling on YAMIYUMI.
      </p>


      <div class="status-badge approved">
        APPROVED
      </div>


      <button
        class="submit-button"
        @click="goToStore"
      >
        Go to My Store
      </button>

    </div>



    <!-- =========================
         REJECTED
    ========================== -->

    <div
      v-else-if="applicationStatus === 'REJECTED'"
      class="status-card rejected-card"
    >

      <div class="status-icon">
        📋
      </div>


      <h2>
        Application Not Approved
      </h2>


      <p>
        Your seller application was not approved.
        You can review your information and submit another application.
      </p>


      <div class="status-badge rejected">
        REJECTED
      </div>


      <button
        class="secondary-button"
        @click="resetApplication"
      >
        Apply Again
      </button>

    </div>



    <!-- =========================
         Application Form
    ========================== -->

    <template v-else>

      <!-- Introduction -->
      <div class="intro-card">

        <div class="intro-icon">
          🏪
        </div>


        <div>

          <h2>
            Become a YAMIYUMI Seller
          </h2>


          <p>
            Tell us about your store.
            Your application will be reviewed by our admin team.
          </p>

        </div>

      </div>



      <!-- =========================
           Application
      ========================== -->

      <div class="form-section">

        <p class="section-title">
          STORE INFORMATION
        </p>



        <!-- Store Name -->
        <div class="input-group">

          <label>
            Store Name
            <span class="required">*</span>
          </label>


          <input
            v-model="storeName"
            type="text"
            placeholder="Enter your store name"
            maxlength="100"
          />

        </div>



        <!-- Description -->
        <div class="input-group">

          <label>
            Store Description
            <span class="required">*</span>
          </label>


          <textarea
            v-model="description"
            placeholder="Tell customers about your store and what you sell"
            maxlength="500"
          ></textarea>


          <div class="character-count">
            {{ description.length }} / 500
          </div>

        </div>



        <!-- Address -->
        <div class="input-group">

          <label>
            Store Address
            <span class="required">*</span>
          </label>


          <div class="address-input">

            <span class="address-icon">
              📍
            </span>


            <input
              v-model="address"
              type="text"
              placeholder="Enter your store address"
            />

          </div>

        </div>

      </div>



      <!-- =========================
           Application Information
      ========================== -->

      <div class="notice-box">

        <div class="notice-title">

          <span>💡</span>

          <span>
            Before you apply
          </span>

        </div>


        <p>
          Please make sure your store information is accurate.
          Once submitted, your application will be reviewed by a YAMIYUMI administrator.
        </p>

      </div>



      <!-- =========================
           Agreement
      ========================== -->

      <label class="agreement">

        <input
          v-model="agreed"
          type="checkbox"
        />


        <span>
          I confirm that the information provided above is accurate.
        </span>

      </label>



      <!-- =========================
           Error Message
      ========================== -->

      <p
        v-if="errorMessage"
        class="error-message"
      >
        {{ errorMessage }}
      </p>



      <!-- =========================
           Submit
      ========================== -->

      <button
        class="submit-button"
        :disabled="isSubmitting"
        @click="submitApplication"
      >

        {{ isSubmitting ? 'Submitting...' : 'Submit Application' }}

      </button>



      <button
        class="cancel-button"
        :disabled="isSubmitting"
        @click="goBack"
      >
        Cancel
      </button>

    </template>



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

import { useRouter } from 'vue-router'

import BottomNav from '@/components/BottomNav.vue'



// =========================
// 2. Router
// =========================

const router = useRouter()



// =========================
// 3. Form Data
// =========================

const storeName = ref('')

const description = ref('')

const address = ref('')

const agreed = ref(false)



// =========================
// 4. Application 상태
// =========================

// null
// PENDING
// APPROVED
// REJECTED

const applicationStatus = ref(null)

const isLoading = ref(true)

const isSubmitting = ref(false)

const errorMessage = ref('')



// =========================
// 5. 페이지 시작
// =========================

onMounted(async () => {

  await loadApplicationStatus()

})



// =========================
// 6. Seller 신청 상태 조회
// =========================

const loadApplicationStatus = async () => {

  const token =
    localStorage.getItem('token')


  // 로그인 정보가 없는 경우
  if (!token) {

    isLoading.value = false

    return
  }


  try {

    const response = await fetch(
      'http://localhost:8080/api/seller-applications/me/status',
      {
        method: 'GET',

        headers: {

          Authorization: `Bearer ${token}`

        }
      }
    )


    console.log(
      'Seller application status:',
      response.status
    )


    // =========================
    // 신청한 기록이 없는 경우
    // =========================

    if (response.status === 404) {

      applicationStatus.value = null

      return
    }


    // =========================
    // 인증 오류
    // =========================

    if (
      response.status === 401 ||
      response.status === 403
    ) {

      console.log(
        'Seller application authentication failed'
      )

      applicationStatus.value = null

      return
    }


    if (!response.ok) {

      console.error(
        'Seller application status load failed'
      )

      return
    }



    // =========================
    // 응답 읽기
    //
    // 백엔드 응답이
    //
    // "PENDING"
    //
    // 일 수도 있고
    //
    // {
    //   "status": "PENDING"
    // }
    //
    // 일 수도 있으므로 둘 다 처리
    // =========================

  const responseText =
    await response.text()


  console.log(
  'STATUS RESPONSE:',
  response.status,
  responseText
)


    if (!responseText) {

      applicationStatus.value = null

      return
    }


    try {

      const result =
        JSON.parse(responseText)


      if (typeof result === 'string') {

        applicationStatus.value =
          result

      } else {

        applicationStatus.value =
          result.status || null

      }


    } catch {

      // 그냥 String으로 반환된 경우
      applicationStatus.value =
        responseText
          .replaceAll('"', '')
          .trim()

    }


    console.log(
      'Current seller application:',
      applicationStatus.value
    )


  } catch (error) {

    console.error(
      'Seller application status error:',
      error
    )

  } finally {

    isLoading.value = false

  }

}



// =========================
// 7. Seller 신청
// =========================

const submitApplication = async () => {

  // 이전 에러 삭제
  errorMessage.value = ''


  // =========================
  // 입력값 검사
  // =========================

  if (!storeName.value.trim()) {

    errorMessage.value =
      'Please enter your store name.'

    return

  }


  if (!description.value.trim()) {

    errorMessage.value =
      'Please enter your store description.'

    return

  }


  if (!address.value.trim()) {

    errorMessage.value =
      'Please enter your store address.'

    return

  }


  if (!agreed.value) {

    errorMessage.value =
      'Please confirm that your information is accurate.'

    return

  }



  // =========================
  // JWT 가져오기
  // =========================

  const token =
    localStorage.getItem('token')


  if (!token) {

    alert(
      'Please login before applying.'
    )

    router.push('/login')

    return

  }



  // =========================
  // 백엔드 요청 시작
  // =========================

  isSubmitting.value = true


  try {

    const response = await fetch(
      'http://localhost:8080/api/seller-applications',
      {
        method: 'POST',

        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`
        },

        body: JSON.stringify({

          storeName:
            storeName.value.trim(),

          description:
            description.value.trim(),

          address:
            address.value.trim()

        })
      }
    )


    // =========================
    // HTTP 상태 확인
    // =========================

    console.log(
      'SUBMIT STATUS:',
      response.status
    )


    // =========================
    // 성공
    // =========================

    if (response.ok) {

      console.log(
        'Seller application submitted'
      )


      applicationStatus.value =
        'PENDING'


      // 입력값 초기화
      storeName.value = ''

      description.value = ''

      address.value = ''

      agreed.value = false


      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      })


      return
    }


    // =========================
    // 실패 응답
    // =========================

    const result =
      await response.text()


    console.error(
      'Seller application failed:',
      result
    )


    // =========================
    // 인증 실패
    // =========================

    if (
      response.status === 401 ||
      response.status === 403
    ) {

      errorMessage.value =
        'Your login session is invalid. Please login again.'

      return
    }


    // =========================
    // 중복 신청 등
    // =========================

    if (response.status === 400) {

      errorMessage.value =
        result ||
        'Unable to submit the seller application.'

      return
    }


    // =========================
    // 기타 오류
    // =========================

    errorMessage.value =
      'Unable to submit the application. Please try again.'


  } catch (error) {

    console.error(
      'Seller application error:',
      error
    )


    errorMessage.value =
      'Unable to connect to the server.'


  } finally {

    isSubmitting.value = false

  }

}


// =========================
// 8. REJECTED 후 다시 신청
// =========================

const resetApplication = () => {

  applicationStatus.value = null

  errorMessage.value = ''

}


// =========================
// 9. 뒤로가기
// =========================

const goBack = () => {

  router.push('/settings')

}


// =========================
// 10. Profile 이동
// =========================

const goToProfile = () => {

  router.push('/profile')

}


// =========================
// 11. My Store 이동
// =========================

const goToStore = () => {

  router.push('/seller/store')

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

  padding: 25px 20px 110px;

  background-color: #ffffff;

  font-family: 'Montserrat', sans-serif;

  color: #3A251E;

}


/* =========================
   Header
========================= */

.header {

  display: flex;

  align-items: center;

  margin-bottom: 30px;

}


.header h1 {

  margin: 0;

  color: #3A251E;

  font-size: 24px;

  font-weight: 800;

}


.header-description {

  margin: 4px 0 0;

  color: #9ca3af;

  font-size: 10px;

  font-weight: 500;

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

  flex-shrink: 0;

  margin-right: 12px;

  padding: 0;

  border: none;

  border-radius: 10px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-family: Arial, sans-serif;

  font-size: 30px;

  line-height: 1;

  cursor: pointer;

  transition: 0.2s;

}


.back-button:hover {

  background-color: #ffe7d3;

}


/* =========================
   Intro Card
========================= */

.intro-card {

  display: flex;

  align-items: center;

  gap: 15px;

  padding: 18px;

  margin-bottom: 28px;

  background-color: #fff8f2;

  border: 1px solid #ffe4cc;

  border-radius: 15px;

}


.intro-icon {

  width: 52px;
  height: 52px;

  display: flex;

  align-items: center;
  justify-content: center;

  flex-shrink: 0;

  background-color: #ffffff;

  border-radius: 13px;

  font-size: 25px;

}


.intro-card h2 {

  margin: 0 0 6px;

  color: #3A251E;

  font-size: 14px;

  font-weight: 800;

}


.intro-card p {

  margin: 0;

  color: #8f8f8f;

  font-size: 10px;

  font-weight: 500;

  line-height: 1.6;

}


/* =========================
   Form
========================= */

.form-section {

  margin-bottom: 25px;

}


.section-title {

  margin: 0 0 18px 3px;

  color: #9ca3af;

  font-size: 10px;

  font-weight: 700;

  letter-spacing: 1.3px;

}


/* =========================
   Input Group
========================= */

.input-group {

  margin-bottom: 22px;

}


.input-group label {

  display: block;

  margin-bottom: 8px;

  color: #3A251E;

  font-size: 11px;

  font-weight: 700;

}


.required {

  color: #ff6b00;

}


/* =========================
   Input
========================= */

.input-group input,
.input-group textarea {

  width: 100%;

  padding: 13px 14px;

  background-color: #f8f8f8;

  border: 1px solid #eeeeee;

  border-radius: 10px;

  outline: none;

  color: #3A251E;

  font-family: 'Montserrat', sans-serif;

  font-size: 12px;

  transition: 0.2s;

}


.input-group input:focus,
.input-group textarea:focus {

  background-color: #ffffff;

  border-color: #ff8b3d;

  box-shadow:
    0 0 0 3px rgba(255, 107, 0, 0.08);

}


.input-group input::placeholder,
.input-group textarea::placeholder {

  color: #b6b6b6;

}


/* =========================
   Textarea
========================= */

.input-group textarea {

  min-height: 120px;

  resize: vertical;

  line-height: 1.6;

}


.character-count {

  margin-top: 5px;

  color: #b0b0b0;

  font-size: 9px;

  text-align: right;

}


/* =========================
   Address
========================= */

.address-input {

  position: relative;

}


.address-icon {

  position: absolute;

  top: 50%;
  left: 13px;

  z-index: 2;

  transform: translateY(-50%);

  font-size: 15px;

}


.address-input input {

  padding-left: 40px;

}


/* =========================
   Notice
========================= */

.notice-box {

  padding: 15px;

  margin: 10px 0 20px;

  background-color: #fafafa;

  border-radius: 11px;

}


.notice-title {

  display: flex;

  align-items: center;

  gap: 7px;

  margin-bottom: 7px;

  color: #3A251E;

  font-size: 11px;

  font-weight: 700;

}


.notice-box p {

  margin: 0;

  color: #9ca3af;

  font-size: 9px;

  font-weight: 500;

  line-height: 1.7;

}


/* =========================
   Agreement
========================= */

.agreement {

  display: flex;

  align-items: flex-start;

  gap: 9px;

  margin-bottom: 20px;

  cursor: pointer;

}


.agreement input {

  width: 15px;
  height: 15px;

  margin: 1px 0 0;

  accent-color: #ff6b00;

  flex-shrink: 0;

}


.agreement span {

  color: #747474;

  font-size: 10px;

  font-weight: 500;

  line-height: 1.5;

}


/* =========================
   Error
========================= */

.error-message {

  padding: 11px 12px;

  margin: 0 0 15px;

  background-color: #fff2f2;

  color: #dc2626;

  border-radius: 8px;

  font-size: 10px;

  font-weight: 600;

}


/* =========================
   Submit Button
========================= */

.submit-button {

  width: 100%;

  padding: 14px;

  background-color: #ff6b00;

  color: #ffffff;

  border: none;

  border-radius: 10px;

  font-family: 'Montserrat', sans-serif;

  font-size: 12px;

  font-weight: 800;

  cursor: pointer;

  box-shadow:
    0 6px 16px rgba(255, 107, 0, 0.22);

  transition: 0.2s;

}


.submit-button:hover:not(:disabled) {

  background-color: #e96000;

  transform: translateY(-1px);

}


.submit-button:disabled {

  opacity: 0.6;

  cursor: not-allowed;

}


/* =========================
   Cancel Button
========================= */

.cancel-button,
.secondary-button {

  width: 100%;

  padding: 13px;

  margin-top: 10px;

  background-color: #fff4eb;

  color: #ff6b00;

  border: none;

  border-radius: 10px;

  font-family: 'Montserrat', sans-serif;

  font-size: 11px;

  font-weight: 700;

  cursor: pointer;

  transition: 0.2s;

}


.cancel-button:hover,
.secondary-button:hover {

  background-color: #ffe7d3;

}


/* =========================
   Loading
========================= */

.loading-box {

  padding: 50px 20px;

  color: #9ca3af;

  font-size: 11px;

  font-weight: 600;

  text-align: center;

}


/* =========================
   Status Card
========================= */

.status-card {

  padding: 35px 25px;

  margin-top: 20px;

  border-radius: 16px;

  text-align: center;

}


.status-icon {

  margin-bottom: 15px;

  font-size: 40px;

}


.status-card h2 {

  margin: 0 0 10px;

  color: #3A251E;

  font-size: 19px;

  font-weight: 800;

}


.status-card > p {

  max-width: 350px;

  margin: 0 auto 18px;

  color: #898989;

  font-size: 10px;

  font-weight: 500;

  line-height: 1.7;

}


/* =========================
   Status Background
========================= */

.pending-card {

  background-color: #fffaf3;

  border: 1px solid #ffe6c5;

}


.approved-card {

  background-color: #f4fff7;

  border: 1px solid #d4f2dc;

}


.rejected-card {

  background-color: #fff7f7;

  border: 1px solid #f4dcdc;

}


/* =========================
   Status Badge
========================= */

.status-badge {

  display: inline-block;

  padding: 6px 14px;

  margin-bottom: 20px;

  border-radius: 20px;

  font-size: 9px;

  font-weight: 800;

  letter-spacing: 1px;

}


.status-badge.pending {

  background-color: #fff0d9;

  color: #d97706;

}


.status-badge.approved {

  background-color: #dcfce7;

  color: #16a34a;

}


.status-badge.rejected {

  background-color: #fee2e2;

  color: #dc2626;

}

</style>