<template>
  <div class="admin-login-page">

    <!-- =========================
         Background decoration
    ========================== -->
    <div class="background-decoration">

      <div class="shape shape-one"></div>

      <div class="shape shape-two"></div>

      <div class="shape shape-three"></div>

      <div class="shape shape-four"></div>

    </div>


    <!-- =========================
         Login Card
    ========================== -->
    <div class="login-card">

      <!-- Logo / Header -->
      <div class="login-header">

        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 500 500"
          class="logo-svg"
        >
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
        </svg>


        <div class="admin-label">
          ADMIN
        </div>


        <p class="welcome-text">
          Welcome back
        </p>

        <p class="description">
          Sign in to your admin dashboard
        </p>

      </div>


      <!-- =========================
           Login Form
      ========================== -->
      <div class="form-box">

        <!-- Email -->
        <div class="input-group">

          <label>
            Email
          </label>

          <div class="input-wrapper">

            <svg
              class="input-icon"
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
            >
              <path
                d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"
              />

              <polyline
                points="22,6 12,13 2,6"
              />
            </svg>


            <input
              type="email"
              v-model="adminEmail"
              placeholder="admin@yamiyumi.com"
              @keyup.enter="handleLogin"
            />

          </div>

        </div>


        <!-- Password -->
        <div class="input-group">

          <label>
            Password
          </label>

          <div class="input-wrapper">

            <svg
              class="input-icon"
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
            >
              <rect
                x="3"
                y="11"
                width="18"
                height="10"
                rx="2"
              />

              <path
                d="M7 11V7a5 5 0 0110 0v4"
              />
            </svg>


            <input
              :type="showPassword ? 'text' : 'password'"
              v-model="adminPw"
              placeholder="Enter your password"
              @keyup.enter="handleLogin"
            />


            <!-- Show Password -->
            <button
              class="password-toggle"
              type="button"
              @click="showPassword = !showPassword"
            >

              <svg
                v-if="!showPassword"
                width="18"
                height="18"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <path
                  d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12z"
                />

                <circle
                  cx="12"
                  cy="12"
                  r="3"
                />
              </svg>


              <svg
                v-else
                width="18"
                height="18"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <path
                  d="M17.94 17.94A10.94 10.94 0 0112 20c-7 0-11-8-11-8a20.29 20.29 0 015.06-5.94"
                />

                <path
                  d="M9.9 4.24A10.67 10.67 0 0112 4c7 0 11 8 11 8a20.59 20.59 0 01-2.17 3.19"
                />

                <line
                  x1="1"
                  y1="1"
                  x2="23"
                  y2="23"
                />
              </svg>

            </button>

          </div>

        </div>


        <!-- Forgot password -->
        <div class="forgot-row">

          <a href="#">
            Forgot password?
          </a>

        </div>


        <!-- Login Button -->
        <button
          class="login-button"
          @click="handleLogin"
        >
          LOGIN
        </button>

      </div>


      <!-- Footer -->
      <div class="card-footer">

        <div class="secure-badge">
          <span class="shield">◆</span>
          Secure Admin Access
        </div>

      </div>

    </div>


    <!-- =========================
         Page Footer
    ========================== -->
    <div class="page-footer">

      <p>
        © 2026 YAMIYUMI
      </p>

      <p>
        Administration Portal
      </p>

    </div>

  </div>
</template>


<script setup>

import { ref } from 'vue'
import { useRouter } from 'vue-router'


// =========================
// Router
// =========================

const router = useRouter()


// =========================
// Input values
// =========================

const adminEmail = ref('')
const adminPw = ref('')


// =========================
// Password visibility
// =========================

const showPassword = ref(false)


// =========================
// Login
// =========================

const handleLogin = async () => {

  if (!adminEmail.value || !adminPw.value) {

    alert('Please enter your email and password.')

    return
  }


  try {

    const response = await fetch(
      'http://localhost:8080/api/admin/login',
      {
        method: 'POST',

        headers: {
          'Content-Type': 'application/json'
        },

        body: JSON.stringify({
          email: adminEmail.value,
          password: adminPw.value
        })
      }
    )


    // =========================
    // 로그인 실패
    // =========================

    if (!response.ok) {

      console.error(
        'Admin login failed:',
        response.status
      )

      alert(
        'Email or password is incorrect.'
      )

      return
    }


    // =========================
    // 로그인 성공
    // =========================

    // 백엔드가 JSON이 아니라
    // JWT 문자열 자체를 반환하므로 text()
    const token = await response.text()


    console.log(
      'Admin token:',
      token
    )


    // Admin JWT 저장
    localStorage.setItem(
      'adminToken',
      token
    )


    // AdminHomePage로 이동
    router.push('/admin')


  } catch (error) {

    console.error(
      'Admin login error:',
      error
    )

    alert(
      'Unable to connect to the server.'
    )
  }
}

</script>


<style scoped>

@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@500;600;700;800&display=swap');


/* =====================================================
   PAGE
===================================================== */

.admin-login-page {

  position: relative;

  width: 100%;
  min-height: 100vh;

  display: flex;
  justify-content: center;
  align-items: center;

  overflow: hidden;

  background:
    linear-gradient(
      135deg,
      #35374f 0%,
      #42445f 45%,
      #393b54 100%
    );

  font-family:
    'Montserrat',
    -apple-system,
    BlinkMacSystemFont,
    sans-serif;

}


/* =====================================================
   BACKGROUND SHAPES
===================================================== */

.background-decoration {

  position: absolute;

  top: 0;
  left: 0;

  width: 48%;
  height: 100%;

  pointer-events: none;

}


/* Large blue shape */
.shape-one {

  position: absolute;

  width: 380px;
  height: 380px;

  left: -90px;
  top: 40px;

  border-radius: 45% 55% 58% 42%;

  transform: rotate(30deg);

  background:
    linear-gradient(
      145deg,
      rgba(40, 113, 255, 0.55),
      rgba(61, 193, 208, 0.25)
    );

}


/* Aqua shape */
.shape-two {

  position: absolute;

  width: 230px;
  height: 310px;

  left: 50px;
  top: 155px;

  border-radius:
    35% 65% 35% 65%;

  transform:
    rotate(-22deg);

  background:
    linear-gradient(
      145deg,
      rgba(42, 211, 211, 0.55),
      rgba(43, 137, 180, 0.18)
    );

}


/* Orange Yamiyumi accent */
.shape-three {

  position: absolute;

  width: 180px;
  height: 180px;

  left: 250px;
  bottom: 40px;

  border-radius: 50%;

  background:
    rgba(255, 122, 0, 0.07);

}


/* Background ring */
.shape-four {

  position: absolute;

  width: 520px;
  height: 520px;

  left: -240px;
  bottom: -230px;

  border-radius: 50%;

  border:
    60px solid
    rgba(255, 255, 255, 0.025);

}


/* =====================================================
   LOGIN CARD
===================================================== */

.login-card {

  position: relative;

  z-index: 10;

  width: 100%;
  max-width: 390px;

  background: #ffffff;

  padding:
    42px
    42px
    28px;

  border-radius: 4px;

  box-shadow:
    0 25px 70px
    rgba(15, 16, 30, 0.35);

  box-sizing: border-box;

}


/* =====================================================
   HEADER
===================================================== */

.login-header {

  text-align: center;

  margin-bottom: 35px;

}


.logo-svg {

  width: 145px;

  height: 100px;

  margin-bottom: -5px;

}


.brand-title {

  font-family:
    'Montserrat',
    sans-serif;

  font-weight: 800;

  fill: #3a251e;

  text-anchor: middle;

  letter-spacing: 4px;

  font-size: 65px;

}


.admin-label {

  display: inline-block;

  padding: 5px 13px;

  border-radius: 20px;

  background:
    rgba(255, 122, 0, 0.10);

  color: #ff6b00;

  font-size: 10px;

  font-weight: 800;

  letter-spacing: 2px;

  margin-bottom: 18px;

}


.welcome-text {

  margin: 0;

  color: #292b3d;

  font-size: 20px;

  font-weight: 700;

}


.description {

  margin-top: 7px;

  margin-bottom: 0;

  color: #9a9ca9;

  font-size: 11px;

}


/* =====================================================
   FORM
===================================================== */

.form-box {

  width: 100%;

}


.input-group {

  margin-bottom: 25px;

}


.input-group label {

  display: block;

  color: #777985;

  font-size: 11px;

  font-weight: 600;

  margin-bottom: 5px;

}


.input-wrapper {

  display: flex;

  align-items: center;

  height: 42px;

  border-bottom:
    1px solid #c9cad1;

  transition:
    border-color 0.2s ease;

}


.input-wrapper:focus-within {

  border-bottom:
    2px solid #ff7a00;

}


.input-icon {

  margin-right: 10px;

  color: #b1b3bd;

  flex-shrink: 0;

}


.input-wrapper:focus-within
.input-icon {

  color: #ff7a00;

}


.input-wrapper input {

  flex: 1;

  min-width: 0;

  border: none;

  outline: none;

  background: transparent;

  color: #30313e;

  font-family: inherit;

  font-size: 13px;

}


.input-wrapper input::placeholder {

  color: #babcc5;

}


/* =====================================================
   PASSWORD
===================================================== */

.password-toggle {

  display: flex;

  justify-content: center;
  align-items: center;

  padding: 5px;

  border: none;

  outline: none;

  background: transparent;

  color: #a5a7b1;

  cursor: pointer;

}


.password-toggle:hover {

  color: #ff7a00;

}


/* =====================================================
   FORGOT PASSWORD
===================================================== */

.forgot-row {

  display: flex;

  justify-content: flex-end;

  margin-top: -8px;

  margin-bottom: 27px;

}


.forgot-row a {

  color: #8b8e9c;

  font-size: 10px;

  text-decoration: none;

}


.forgot-row a:hover {

  color: #ff7a00;

}


/* =====================================================
   LOGIN BUTTON
===================================================== */

.login-button {

  width: 100%;

  height: 45px;

  border: none;

  border-radius: 2px;

  background:
    linear-gradient(
      90deg,
      #ff6b00,
      #ff8a1c
    );

  color: #ffffff;

  font-family: inherit;

  font-size: 12px;

  font-weight: 800;

  letter-spacing: 0.7px;

  cursor: pointer;

  box-shadow:
    0 5px 15px
    rgba(255, 107, 0, 0.22);

  transition:
    transform 0.15s ease,
    box-shadow 0.15s ease;

}


.login-button:hover {

  transform:
    translateY(-1px);

  box-shadow:
    0 8px 20px
    rgba(255, 107, 0, 0.30);

}


.login-button:active {

  transform:
    translateY(0);

}


/* =====================================================
   CARD FOOTER
===================================================== */

.card-footer {

  display: flex;

  justify-content: center;

  margin-top: 25px;

}


.secure-badge {

  display: flex;

  align-items: center;

  gap: 6px;

  color: #b0b1ba;

  font-size: 9px;

  letter-spacing: 0.3px;

}


.shield {

  color: #ff7a00;

  font-size: 8px;

}


/* =====================================================
   PAGE FOOTER
===================================================== */

.page-footer {

  position: absolute;

  z-index: 5;

  bottom: 27px;

  text-align: center;

  color:
    rgba(255, 255, 255, 0.36);

}


.page-footer p {

  margin: 4px 0;

  font-size: 9px;

  letter-spacing: 0.4px;

}


/* =====================================================
   RESPONSIVE
===================================================== */

@media (max-width: 650px) {

  .admin-login-page {

    padding: 20px;

    box-sizing: border-box;

  }


  .login-card {

    padding:
      35px
      28px
      28px;

  }


  .background-decoration {

    width: 100%;

    opacity: 0.55;

  }


  .shape-one {

    left: -220px;

  }


  .shape-two {

    left: -80px;

  }


  .page-footer {

    display: none;

  }

}

</style>