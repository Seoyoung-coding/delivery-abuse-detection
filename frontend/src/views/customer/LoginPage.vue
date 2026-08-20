<!-- template part -->
<template>
  <div class="page-container">
    <div class="login-card">

      <div class="header-box">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 500 500"
          class="logo-svg"
        >
          <!-- logo style -->
          <g transform="translate(250, 195)">
            <path
              d="M -30 -32 C -38 -48, -15 -62, 0 -48 C 15 -62, 38 -48, 30 -32 C 38 -18, -38 -18, -30 -32 Z"
              fill="#FFB054"
            />

            <path
              d="M -58 -10 C -58 42, 58 42, 58 -10 Z"
              fill="#FF7A00"
            />
          </g>

          <text
            x="250"
            y="320"
            class="brand-title"
          >
            YAMIYUMI
          </text>

          <text
            x="250"
            y="352"
            class="tagline"
          >
            FRESH &amp; DELICIOUS
          </text>
        </svg>

        <p class="subtitle">
          Login into your account
        </p>
      </div>


      <!-- Input group -->
      <div class="input-group">

        <label>
          Email address
        </label>

        <div class="input-email">

          <input
            type="email"
            v-model="userEmail"
            placeholder="Kim@email.com"
            @keyup.enter="handleLogin"
          />

        </div>


        <label>
          Passwords
        </label>

        <div class="input-password">

          <input
            type="password"
            v-model="userPw"
            placeholder="Enter your password"
            @keyup.enter="handleLogin"
          />


          <div class="icon-box">

            <svg
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="white"
              stroke-width="2"
            >
              <path
                d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"
              />

              <polyline
                points="22,6 12,13 2,6"
              />
            </svg>

          </div>

        </div>

      </div>


      <a
        href="#"
        class="forgot-link"
      >
        Forgot password?
      </a>


      <!-- Login button -->
      <button
        class="btn-login"
        @click="handleLogin"
        :disabled="isLoggingIn"
      >
        {{ isLoggingIn ? 'Logging in...' : 'Login now' }}
      </button>


      <div class="divider">
        <span>OR</span>
      </div>


      <!-- Signup button -->
      <button
        class="btn-signup"
        @click="handleSignup"
      >
        Signup now
      </button>

    </div>
  </div>
</template>


<script setup>

// =========================
// 1. Import
// =========================

import { ref } from 'vue'
import { useRouter } from 'vue-router'


// =========================
// 2. Router
// =========================

const router = useRouter()


// =========================
// 3. Input
// =========================

const userEmail = ref('')
const userPw = ref('')


// =========================
// 4. Login 상태
// =========================

const isLoggingIn = ref(false)


// =========================
// 5. Login
// =========================

const handleLogin = async () => {

  // -------------------------
  // 입력값 검사
  // -------------------------

  if (!userEmail.value.trim() || !userPw.value) {

    alert(
      'Please enter your email and password.'
    )

    return
  }


  // 이전에 잘못 저장된 token 제거
  localStorage.removeItem('token')

  isLoggingIn.value = true


  try {

    // -------------------------
    // 백엔드 로그인 요청
    // -------------------------

    const response = await fetch(
      'http://localhost:8080/api/customers/login',
      {
        method: 'POST',

        headers: {
          'Content-Type': 'application/json'
        },

        body: JSON.stringify({

          email:
            userEmail.value.trim(),

          password:
            userPw.value
        })
      }
    )


    // -------------------------
    // 응답을 일단 text로 읽기
    // -------------------------
    //
    // 이유:
    //
    // 백엔드가
    //
    // {
    //   "message": "...",
    //   "token": "eyJ..."
    // }
    //
    // JSON으로 보내든,
    //
    // eyJhbGciOi...
    //
    // 문자열로 보내든
    // 둘 다 처리하기 위해서
    // -------------------------

    const responseText =
      await response.text()


    console.log(
      'Customer login status:',
      response.status
    )

    console.log(
      'Customer login response:',
      responseText
    )


    // -------------------------
    // 로그인 실패
    // -------------------------

    if (!response.ok) {

      let errorMessage =
        'Email or password is incorrect.'


      // JSON 에러 응답이면 message 확인
      try {

        const errorResult =
          JSON.parse(responseText)

        if (errorResult.message) {

          errorMessage =
            errorResult.message
        }

      } catch {

        if (responseText) {

          errorMessage =
            responseText
        }
      }


      alert(errorMessage)

      return
    }


    // =========================
    // JWT 꺼내기
    // =========================

    let token = null

    let successMessage =
      'Login successful.'


    try {

      // JSON으로 변환 시도
      const result =
        JSON.parse(responseText)


      // -------------------------
      // 경우 1
      //
      // {
      //   "message": "...",
      //   "token": "eyJ..."
      // }
      // -------------------------

      if (
        typeof result === 'object' &&
        result !== null
      ) {

        token =
          result.token || null

        if (result.message) {

          successMessage =
            result.message
        }
      }


      // -------------------------
      // 경우 2
      //
      // "eyJ..."
      // -------------------------

      else if (
        typeof result === 'string'
      ) {

        token = result
      }

    } catch {

      // -------------------------
      // 경우 3
      //
      // 백엔드가 그냥
      // eyJ....
      // 문자열 자체를 반환
      // -------------------------

      token = responseText
    }


    // =========================
    // token 정리
    // =========================

    if (token) {

      token = String(token)
        .replace(/^"+|"+$/g, '')
        .trim()
    }


    console.log(
      'JWT extracted:',
      token
    )


    // =========================
    // JWT가 제대로 왔는지 검사
    // =========================

    if (
      !token ||
      token === 'undefined' ||
      token === 'null' ||
      !token.startsWith('eyJ')
    ) {

      console.error(
        'JWT token was not returned correctly.'
      )

      console.error(
        'Backend response:',
        responseText
      )

      alert(
        'Login succeeded, but the JWT token was not returned correctly.'
      )

      return
    }


    // =========================
    // JWT 저장
    // =========================

    localStorage.setItem(
      'token',
      token
    )


    // 저장됐는지 확인
    console.log(
      'Saved customer token:',
      localStorage.getItem('token')
    )


    // =========================
    // 로그인 성공
    // =========================

    console.log(successMessage)


    // Home으로 이동
    router.push('/home')


  } catch (error) {

    console.error(
      'Customer login error:',
      error
    )

    alert(
      'Unable to connect to the server.'
    )

  } finally {

    isLoggingIn.value = false
  }
}


// =========================
// 6. Signup
// =========================

const handleSignup = () => {

  router.push('/signup')
}

</script>


<!-- style -->
<style scoped>

@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@600;800&display=swap');


.brand-title {

  font-family:
    'Montserrat',
    -apple-system,
    sans-serif;

  font-weight: 1000;

  fill: #3A251E;

  text-anchor: middle;

  letter-spacing: 4px;

  font-size: 70px;
}


.tagline {

  font-family:
    'Montserrat',
    -apple-system,
    sans-serif;

  font-weight: 600;

  fill: #A08C82;

  text-anchor: middle;

  letter-spacing: 5px;

  font-size: 20px;
}


.page-container {

  display: flex;

  justify-content: center;

  align-items: center;

  min-height: 90vh;
}


.login-card {

  width: 100%;

  max-width: 360px;

  padding: 30px 24px;

  background: #ffffff;

  border-radius: 24px;

  box-shadow:
    0 10px 30px
    rgba(0, 0, 0, 0.05);

  box-sizing: border-box;
}


.header-box {

  text-align: center;

  margin-bottom: 25px;
}


.logo-svg {

  width: 170px;

  height: auto;
}


.subtitle {

  font-size: 14px;

  font-weight: 700;

  color: #4b5563;

  margin-top: 15px;

  margin-bottom: 0;
}


.input-group {

  margin-bottom: 16px;

  text-align: left;
}


.input-group label {

  display: block;

  font-size: 13px;

  font-weight: 600;

  color: #4b5563;

  margin-bottom: 6px;
}


.input-email {

  display: flex;

  align-items: center;

  background-color: #f3f4f6;

  border-radius: 10px;

  overflow: hidden;
}


.input-email input {

  flex: 1;

  border: none;

  background: transparent;

  padding: 12px 14px;

  font-size: 14px;

  outline: none;

  color: #1f2937;
}


.input-password {

  display: flex;

  align-items: center;

  background-color: #f3f4f6;

  border-radius: 10px;

  overflow: hidden;
}


.input-password input {

  flex: 1;

  border: none;

  background: transparent;

  padding: 12px 14px;

  font-size: 14px;

  outline: none;

  color: #1f2937;
}


.icon-box {

  width: 42px;

  height: 42px;

  background-color: #ff6b00;

  display: flex;

  justify-content: center;

  align-items: center;

  flex-shrink: 0;

  border-radius: 8px;

  margin-right: 2px;
}


.forgot-link {

  display: block;

  text-align: right;

  font-size: 12px;

  color: #2563eb;

  text-decoration: underline;

  margin-top: 10px;

  margin-bottom: 5px;
}


.btn-login {

  width: 100%;

  padding: 13px;

  background-color: #ff6b00;

  color: white;

  border: none;

  border-radius: 10px;

  font-size: 15px;

  font-weight: 700;

  cursor: pointer;

  margin-top: 8px;

  box-shadow:
    0 6px 16px
    rgba(255, 107, 0, 0.35);
}


.btn-login:disabled {

  opacity: 0.6;

  cursor: not-allowed;
}


.divider {

  display: flex;

  align-items: center;

  margin: 20px 0;
}


.divider::before,
.divider::after {

  content: '';

  flex: 1;

  height: 1px;

  background-color: #e5e7eb;
}


.divider span {

  padding: 0 10px;

  font-size: 12px;

  color: #9ca3af;

  font-weight: 600;
}


.btn-signup {

  width: 100%;

  padding: 12px;

  background-color: transparent;

  color: #ff6b00;

  border:
    1.5px solid
    #ff6b00;

  border-radius: 10px;

  font-size: 15px;

  font-weight: 700;

  cursor: pointer;
}

</style>