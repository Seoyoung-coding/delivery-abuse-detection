<template>
  <div class="page-container">
    <div class="signup-card">

      <!-- Header -->
      <div class="header-box">
        <h1 class="title">Signup</h1>

        <p class="login-text">
          Already have account?
          <span class="login-link" @click="goToLogin">
            Login
          </span>
        </p>
      </div>


      <!-- Input Area -->
      <div class="input-group">

        <!-- Username -->
        <div class="input-box">
          <div class="input-icon">
            <svg
              width="21"
              height="21"
              viewBox="0 0 24 24"
              fill="none"
              stroke="#ff6b00"
              stroke-width="2"
            >
              <circle cx="12" cy="8" r="4" />
              <path d="M4 21c0-4 3.6-7 8-7s8 3 8 7" />
            </svg>
          </div>

          <input
            type="text"
            v-model="username"
            placeholder="Username"
          />
        </div>


        <!-- Email -->
        <div class="input-box">
          <div class="input-icon">
            <svg
              width="21"
              height="21"
              viewBox="0 0 24 24"
              fill="none"
              stroke="#ff6b00"
              stroke-width="2"
            >
              <rect x="3" y="5" width="18" height="14" rx="2" />
              <polyline points="3,7 12,13 21,7" />
            </svg>
          </div>

          <input
            type="email"
            v-model="userEmail"
            placeholder="Email"
          />
        </div>


        <!-- Password -->
        <div class="input-box">
          <div class="input-icon">
            <svg
              width="21"
              height="21"
              viewBox="0 0 24 24"
              fill="none"
              stroke="#ff6b00"
              stroke-width="2"
            >
              <circle cx="8" cy="15" r="4" />
              <path d="M11 12l8-8" />
              <path d="M15 4l5 5" />
              <path d="M14 9l2 2" />
            </svg>
          </div>

          <input
            type="password"
            v-model="userPw"
            placeholder="Password"
          />
        </div>


        <!-- Confirm Password -->
        <div class="input-box">
          <div class="input-icon">
            <svg
              width="21"
              height="21"
              viewBox="0 0 24 24"
              fill="none"
              stroke="#ff6b00"
              stroke-width="2"
            >
              <circle cx="8" cy="15" r="4" />
              <path d="M11 12l8-8" />
              <path d="M15 4l5 5" />
              <path d="M14 9l2 2" />
            </svg>
          </div>

          <input
            type="password"
            v-model="confirmPw"
            placeholder="Confirm Password"
          />
        </div>

      </div>


      <!-- Register Button -->
      <button
        class="btn-register"
        @click="handleSignup"
      >
        REGISTER
      </button>


      <!-- Divider -->
      <div class="divider">
        <span>OR</span>
      </div>


      <!-- Social Login -->
      <div class="social-login">

        <!-- Google -->
        <button class="social-btn google">
          G
        </button>

        <!-- Facebook -->
        <button class="social-btn facebook">
          f
        </button>

        <!-- X -->
        <button class="social-btn twitter">
          𝕏
        </button>

        <!-- Apple -->
        <button class="social-btn apple">
          
        </button>

      </div>

    </div>
  </div>
</template>


<script setup>

// 1. 필요한 기능 import
import { ref } from 'vue'
import { useRouter } from 'vue-router'


// 2. Router 사용 준비
const router = useRouter()


// 3. 사용자가 입력한 값을 저장할 변수
const username = ref('')
const userEmail = ref('')
const userPw = ref('')
const confirmPw = ref('')


// 4. Register now 버튼을 눌렀을 때 실행되는 함수
const handleSignup = async () => {

  // 5. 빈칸이 있는지 확인
  if (
    !username.value ||
    !userEmail.value ||
    !userPw.value ||
    !confirmPw.value
  ) {
    alert('Please fill in all fields.')
    return
  }


  // 6. 비밀번호와 비밀번호 확인이 같은지 검사
  if (userPw.value !== confirmPw.value) {
    alert('Passwords do not match.')
    return
  }


  // 7. Spring Boot 백엔드에 회원가입 요청 보내기
  const response = await fetch(
    'http://localhost:8080/api/customers/signup',
    {
      method: 'POST',

      headers: {
        'Content-Type': 'application/json'
      },

      // 사용자가 입력한 이메일과 비밀번호를 JSON으로 변환
      body: JSON.stringify({
        email: userEmail.value,
        password: userPw.value
      })
    }
  )


  // 8. 회원가입이 성공했는지 확인
  if (response.ok) {

    // 9. 성공 알림
    alert('회원가입 완료')


    // 10. 로그인 페이지로 이동
    router.push('/login')

  } else {

    // 11. 회원가입 실패
    alert('회원가입 실패')

  }
}


// 12. 이미 회원인 경우 Login 버튼을 눌러 로그인 페이지로 이동
const goToLogin = () => {
  router.push('/login')
}

</script>


<style scoped>

@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@500;600;700;800&display=swap');


* {
  box-sizing: border-box;
}


.page-container {
  display: flex;
  justify-content: center;
  align-items: center;

  min-height: 95vh;

  font-family: 'Montserrat', sans-serif;
}


/* 전체 카드 */

.signup-card {
  width: 100%;
  max-width: 400px;

  padding: 38px 30px;

  background-color: #ffffff;

  border-radius: 24px;

  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.06);
}


/* Header */

.header-box {
  text-align: center;
  margin-bottom: 35px;
}


.title {
  margin: 0;

  font-size: 32px;
  font-weight: 800;

  color: #3A251E;
}


.login-text {
  margin-top: 12px;

  font-size: 13px;
  font-weight: 500;

  color: #9ca3af;
}


.login-link {
  margin-left: 4px;

  color: #ff6b00;

  font-weight: 700;

  cursor: pointer;
}


.login-link:hover {
  text-decoration: underline;
}


/* Input Area */

.input-group {
  display: flex;
  flex-direction: column;

  gap: 18px;
}


.input-box {
  width: 100%;
  height: 52px;

  display: flex;
  align-items: center;

  border: 1.5px solid #e5e7eb;

  border-radius: 10px;

  background-color: #ffffff;

  transition: 0.2s;
}


.input-box:focus-within {
  border-color: #ff6b00;

  box-shadow:
    0 0 0 3px rgba(255, 107, 0, 0.08);
}


.input-icon {
  width: 50px;

  display: flex;
  align-items: center;
  justify-content: center;

  flex-shrink: 0;
}


.input-box input {
  width: 100%;
  height: 100%;

  border: none;
  outline: none;

  padding-right: 15px;

  background: transparent;

  color: #1f2937;

  font-size: 14px;
  font-family: 'Montserrat', sans-serif;
}


.input-box input::placeholder {
  color: #b0bac6;
}


/* Register Button */

.btn-register {
  width: 100%;

  padding: 14px;

  margin-top: 25px;

  border: none;
  border-radius: 10px;

  background-color: #ff6b00;

  color: white;

  font-size: 14px;
  font-weight: 700;

  cursor: pointer;

  box-shadow:
    0 6px 16px rgba(255, 107, 0, 0.30);

  transition: 0.2s;
}


.btn-register:hover {
  background-color: #e96100;

  transform: translateY(-1px);
}


/* OR */

.divider {
  display: flex;
  align-items: center;

  margin: 28px 0 22px;
}


.divider::before,
.divider::after {
  content: '';

  flex: 1;

  height: 1px;

  background-color: #d1d5db;
}


.divider span {
  padding: 0 15px;

  color: #9ca3af;

  font-size: 12px;
  font-weight: 600;
}


/* Social Buttons */

.social-login {
  display: flex;

  justify-content: space-between;

  gap: 14px;
}


.social-btn {
  flex: 1;

  height: 52px;

  border: 1px solid #e5e7eb;

  border-radius: 10px;

  background-color: white;

  cursor: pointer;

  font-size: 25px;
  font-weight: 700;

  box-shadow:
    0 4px 10px rgba(0, 0, 0, 0.06);

  transition: 0.2s;
}


.social-btn:hover {
  transform: translateY(-2px);

  box-shadow:
    0 6px 14px rgba(0, 0, 0, 0.10);
}


/* Google */

.google {
  color: #4285F4;
}


/* Facebook */

.facebook {
  color: #1877F2;

  font-family: Arial, sans-serif;

  font-size: 30px;
}


/* X */

.twitter {
  color: #111827;

  font-size: 23px;
}


/* Apple */

.apple {
  color: black;

  font-size: 30px;
}

</style>