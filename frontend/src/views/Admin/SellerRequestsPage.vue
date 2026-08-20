<template>

  <div class="page">

    <AdminTopNav />


    <div class="page-header">

      <div>

        <p class="eyebrow">
          SELLER MANAGEMENT
        </p>

        <h1>
          Requests
        </h1>

        <p>
          Review new seller applications.
        </p>

      </div>


      <div class="count">
        {{ requests.length }}
      </div>

    </div>


    <div
      v-if="requests.length > 0"
      class="request-list"
    >

      <div
        v-for="request in requests"
        :key="request.id"
        class="request-card"
      >

        <div class="request-main">

          <div class="store-icon">
            {{ request.storeName.charAt(0) }}
          </div>


          <div class="request-info">

            <h3>
              {{ request.storeName }}
            </h3>

            <p>
              {{ request.customerEmail }}
            </p>

            <span>
              {{ request.address }}
            </span>

          </div>

        </div>


        <div class="actions">

          <button
            class="deny-button"
            @click="denyRequest(request.id)"
          >
            Deny
          </button>


          <button
            class="accept-button"
            @click="acceptRequest(request.id)"
          >
            Accept
          </button>

        </div>

      </div>

    </div>


    <div
      v-else
      class="empty-card"
    >

      <div class="empty-icon">
        ✓
      </div>

      <h2>All caught up!</h2>

      <p>
        There are no seller applications waiting for review.
      </p>

    </div>

  </div>

</template>


<script setup>

import {
  ref,
  onMounted
} from 'vue'

import AdminTopNav from '@/components/admin/AdminTopNav.vue'


// =========================
// 1. 실제 Seller 신청 목록
// =========================

const requests = ref([])


// =========================
// 2. 페이지가 처음 열릴 때
// =========================

onMounted(() => {

  loadRequests()

})


// =========================
// 3. PENDING Seller 신청 조회
// =========================

const loadRequests = async () => {

  // Admin 로그인할 때 저장한 JWT 가져오기
  const adminToken =
    localStorage.getItem('adminToken')


  console.log(
    'adminToken:',
    adminToken
  )


  // Admin Token이 없으면 요청하지 않음
  if (!adminToken) {

    console.error(
      'Admin token does not exist.'
    )

    return
  }


  try {

    // =========================
    // Backend에 PENDING 신청 조회
    // =========================

    const response = await fetch(
      'http://localhost:8080/api/admin/seller-applications/pending',
      {
        method: 'GET',

        headers: {

          Authorization:
            `Bearer ${adminToken}`

        }
      }
    )


    // =========================
    // HTTP 상태 확인
    // =========================

    console.log(
      'Pending request response status:',
      response.status
    )


    // =========================
    // Backend 요청 실패
    // =========================

    if (!response.ok) {

      const errorText =
        await response.text()


      console.error(
        'Failed to load seller applications:',
        errorText
      )


      return
    }


    // =========================
    // Backend JSON → JS 데이터
    // =========================

    const data =
      await response.json()


    console.log(
      'Pending seller applications:',
      data
    )


    // =========================
    // Vue requests에 저장
    // =========================

    requests.value = data


    console.log(
      'Vue requests:',
      requests.value
    )


  } catch (error) {

    console.error(
      'loadRequests error:',
      error
    )
  }
}


// =========================
// 4. Seller 신청 승인
// =========================

const acceptRequest = async (id) => {

  // Admin JWT 가져오기
  const adminToken =
    localStorage.getItem('adminToken')


  if (!adminToken) {

    alert(
      'Admin login is required.'
    )

    return
  }


  try {

    // =========================
    // Backend 승인 요청
    // =========================

    const response = await fetch(
      `http://localhost:8080/api/admin/seller-applications/${id}/approve`,
      {
        method: 'PATCH',

        headers: {

          Authorization:
            `Bearer ${adminToken}`

        }
      }
    )


    console.log(
      'Approve response status:',
      response.status
    )


    // =========================
    // 승인 실패
    // =========================

    if (!response.ok) {

      const errorText =
        await response.text()


      console.error(
        'Approve failed:',
        errorText
      )


      alert(
        'Failed to approve seller application.'
      )


      return
    }


    // =========================
    // 승인 성공
    // =========================

    alert(
      'Seller application approved!'
    )


    // 승인됐으므로
    // PENDING 목록을 Backend에서 다시 가져옴
    await loadRequests()


  } catch (error) {

    console.error(
      'Accept request error:',
      error
    )


    alert(
      'Unable to connect to the server.'
    )
  }
}


// =========================
// 5. Seller 신청 거절
// =========================

const denyRequest = async (id) => {

  // Admin JWT 가져오기
  const adminToken =
    localStorage.getItem('adminToken')


  if (!adminToken) {

    alert(
      'Admin login is required.'
    )

    return
  }


  try {

    // =========================
    // Backend 거절 요청
    // =========================

    const response = await fetch(
      `http://localhost:8080/api/admin/seller-applications/${id}/reject`,
      {
        method: 'PATCH',

        headers: {

          Authorization:
            `Bearer ${adminToken}`

        }
      }
    )


    console.log(
      'Reject response status:',
      response.status
    )


    // =========================
    // 거절 실패
    // =========================

    if (!response.ok) {

      const errorText =
        await response.text()


      console.error(
        'Reject failed:',
        errorText
      )


      alert(
        'Failed to reject seller application.'
      )


      return
    }


    // =========================
    // 거절 성공
    // =========================

    alert(
      'Seller application rejected!'
    )


    // 거절됐으므로
    // PENDING 목록 다시 조회
    await loadRequests()


  } catch (error) {

    console.error(
      'Deny request error:',
      error
    )


    alert(
      'Unable to connect to the server.'
    )
  }
}

</script>


<style scoped>

.page {
  max-width: 520px;

  min-height: 100vh;

  margin: auto;

  padding: 20px 20px 100px;

  font-family: 'Montserrat', sans-serif;

  color: #3A251E;
}


.page-header {
  display: flex;

  justify-content: space-between;
  align-items: center;

  margin-bottom: 25px;
}


.eyebrow {
  margin: 0 0 5px;

  color: #ff6b00;

  font-size: 10px;
  font-weight: 800;

  letter-spacing: 1.4px;
}


h1 {
  margin: 0;

  font-size: 28px;
}


.page-header p:not(.eyebrow) {
  margin: 6px 0 0;

  color: #9ca3af;

  font-size: 11px;
}


.count {
  min-width: 36px;
  height: 36px;

  display: flex;

  justify-content: center;
  align-items: center;

  border-radius: 50%;

  background-color: #ff6b00;

  color: white;

  font-size: 13px;
  font-weight: 800;
}


.request-card {
  margin-bottom: 12px;

  padding: 18px;

  border: 1px solid #f1e7e1;

  border-radius: 18px;

  background-color: white;
}


.request-main {
  display: flex;

  gap: 13px;
}


.store-icon {
  width: 48px;
  height: 48px;

  flex-shrink: 0;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 15px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-size: 19px;
  font-weight: 800;
}


.request-info h3 {
  margin: 3px 0 4px;

  font-size: 14px;
}


.request-info p {
  margin: 0;

  color: #8f8580;

  font-size: 10px;
}


.request-info span {
  display: block;

  margin-top: 5px;

  color: #b0a6a0;

  font-size: 9px;
}


.actions {
  display: flex;

  justify-content: flex-end;

  gap: 9px;

  margin-top: 18px;
}


button {
  padding: 9px 18px;

  border-radius: 20px;

  font-family: inherit;

  font-size: 10px;
  font-weight: 700;

  cursor: pointer;
}


.deny-button {
  border: 1px solid #ddd2cc;

  background-color: white;

  color: #6e615b;
}


.accept-button {
  border: none;

  background-color: #ff6b00;

  color: white;
}


.empty-card {
  margin-top: 50px;

  padding: 35px 25px;

  text-align: center;

  border-radius: 22px;

  background-color: #fff4eb;
}


.empty-icon {
  width: 55px;
  height: 55px;

  margin: auto;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 50%;

  background-color: white;

  color: #ff6b00;

  font-size: 25px;
  font-weight: 800;
}


.empty-card h2 {
  margin-bottom: 7px;

  font-size: 17px;
}


.empty-card p {
  color: #9ca3af;

  font-size: 10px;
}

</style>