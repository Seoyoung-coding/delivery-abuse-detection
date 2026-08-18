<template>

  <div class="page">

    <div class="card">

      <h1>Seller Setup</h1>

      <p class="description">
        Become a seller and register your store.
      </p>


      <!-- =========================
           1. Seller 등록
      ========================== -->

      <section v-if="!sellerRegistered">

        <h2>Become a Seller</h2>

        <p>
          Register your current account as a seller.
        </p>

        <button
          class="main-button"
          @click="registerSeller"
        >
          BECOME A SELLER
        </button>

      </section>


      <!-- =========================
           2. Store 등록
      ========================== -->

      <section v-else>

        <h2>Register Store</h2>


        <!-- Store 이름 -->
        <input
          v-model="storeName"
          type="text"
          placeholder="Store name"
        />


        <!-- Store 설명 -->
        <textarea
          v-model="storeDescription"
          placeholder="Store description"
        ></textarea>


        <!-- Store 주소 -->
        <input
          v-model="storeAddress"
          type="text"
          placeholder="Store address"
        />


        <button
          class="main-button"
          @click="registerStore"
        >
          REGISTER STORE
        </button>

      </section>

    </div>

  </div>

</template>


<script setup>

// 1. Vue 기능 import
import { ref } from 'vue'
import { useRouter } from 'vue-router'


// 2. Router 사용 준비
const router = useRouter()


// 3. Seller 등록 여부
// 지금은 프론트 테스트용 상태
const sellerRegistered = ref(false)


// 4. Store 입력값
const storeName = ref('')
const storeDescription = ref('')
const storeAddress = ref('')


// =========================
// Seller 등록
// =========================

const registerSeller = async () => {

  // 5. 로그인할 때 저장한 JWT 가져오기
  const token = localStorage.getItem('token')


  // 6. JWT가 없으면 로그인하지 않은 상태
  if (!token) {

    alert('로그인이 필요합니다.')

    router.push('/login')

    return
  }


  try {

    // 7. 백엔드 Seller 등록 API 호출
    const response = await fetch(
      'http://localhost:8080/api/sellers/register',
      {
        method: 'POST',

        headers: {

          // JWT를 Authorization Header에 넣어서 전송
          Authorization: `Bearer ${token}`

        }
      }
    )


    // 8. 백엔드 응답 읽기
    const result = await response.text()


    // 9. Seller 등록 성공
    if (response.ok) {

      alert('판매자 등록 성공')

      // Store 등록 화면 표시
      sellerRegistered.value = true

    } else {

      alert(result)

    }

  } catch (error) {

    console.error(error)

    alert('서버 연결 실패')

  }
}


// =========================
// Store 등록
// =========================

const registerStore = async () => {

  // 10. Store 입력값 확인
  if (
    !storeName.value ||
    !storeDescription.value ||
    !storeAddress.value
  ) {

    alert('가게 정보를 모두 입력해주세요.')

    return
  }


  // 11. JWT 다시 가져오기
  const token = localStorage.getItem('token')


  if (!token) {

    alert('로그인이 필요합니다.')

    router.push('/login')

    return
  }


  try {

    // 12. Store 등록 API 호출
    const response = await fetch(
      'http://localhost:8080/api/stores',
      {
        method: 'POST',

        headers: {

          'Content-Type': 'application/json',

          // 현재 로그인한 Seller의 JWT
          Authorization: `Bearer ${token}`

        },


        // 13. 가게 정보를 JSON으로 변환해서 전송
        body: JSON.stringify({

          name: storeName.value,

          description: storeDescription.value,

          address: storeAddress.value

        })
      }
    )


    // 14. 백엔드 응답 읽기
    const result = await response.text()


    // 15. Store 등록 성공
    if (response.ok) {

      alert('가게 등록 성공')

      // 일단 테스트 후 Home으로 이동
      router.push('/home')

    } else {

      alert(result)

    }

  } catch (error) {

    console.error(error)

    alert('서버 연결 실패')

  }
}

</script>


<style scoped>

* {
  box-sizing: border-box;
}


.page {
  min-height: 100vh;

  display: flex;
  justify-content: center;
  align-items: center;

  padding: 30px;

  background: #fafafa;
}


.card {
  width: 100%;
  max-width: 420px;

  padding: 32px;

  background: white;

  border-radius: 20px;

  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.06);
}


h1 {
  margin: 0 0 8px;

  color: #3A251E;
}


h2 {
  margin-top: 30px;

  color: #3A251E;
}


.description {
  color: #9ca3af;

  font-size: 13px;
}


section {
  display: flex;
  flex-direction: column;

  gap: 14px;
}


input,
textarea {
  width: 100%;

  padding: 14px;

  border: 1px solid #e5e7eb;

  border-radius: 10px;

  font-size: 14px;

  outline: none;
}


textarea {
  min-height: 100px;

  resize: vertical;
}


input:focus,
textarea:focus {
  border-color: #ff6b00;
}


.main-button {
  width: 100%;

  margin-top: 10px;

  padding: 14px;

  border: none;
  border-radius: 10px;

  background: #ff6b00;

  color: white;

  font-weight: 700;

  cursor: pointer;
}


.main-button:hover {
  background: #e96100;
}

</style>