<template>

  <div class="page">

    <button
      class="back-button"
      @click="goBack"
    >
      ‹
    </button>


    <p class="eyebrow">
      SECURITY
    </p>

    <h1>
      Change Password
    </h1>

    <p class="description">
      Update your account password.
    </p>


    <div class="form-card">

      <label>
        Current Password
      </label>

      <input
        v-model="currentPassword"
        type="password"
        placeholder="Current password"
      />


      <label class="second-label">
        New Password
      </label>

      <input
        v-model="newPassword"
        type="password"
        placeholder="New password"
      />


      <label class="second-label">
        Confirm New Password
      </label>

      <input
        v-model="confirmPassword"
        type="password"
        placeholder="Confirm new password"
        @keyup.enter="changePassword"
      />


      <button
        class="save-button"
        @click="changePassword"
      >
        Change Password
      </button>


      <p
        v-if="message"
        class="message"
        :class="{
          error: isError
        }"
      >
        {{ message }}
      </p>

    </div>

  </div>

</template>


<script setup>

import {
  ref
} from 'vue'

import {
  useRouter
} from 'vue-router'


const router =
  useRouter()


const currentPassword =
  ref('')

const newPassword =
  ref('')

const confirmPassword =
  ref('')


const message =
  ref('')

const isError =
  ref(false)


const goBack = () => {

  router.back()

}


// =========================
// Password 변경
// =========================

const changePassword =
  async () => {

    message.value =
      ''

    isError.value =
      false


    if (
      !currentPassword.value ||
      !newPassword.value ||
      !confirmPassword.value
    ) {

      message.value =
        'Please fill in all fields.'

      isError.value =
        true

      return
    }


    if (
      newPassword.value !==
      confirmPassword.value
    ) {

      message.value =
        'New passwords do not match.'

      isError.value =
        true

      return
    }


    const token =
      localStorage.getItem(
        'token'
      )


    if (!token) {

      message.value =
        'Login is required.'

      isError.value =
        true

      return
    }


    try {

      const response =
        await fetch(
          'http://localhost:8080/api/customers/me/password',
          {
            method:
              'PATCH',

            headers: {

              'Content-Type':
                'application/json',

              Authorization:
                `Bearer ${token}`

            },

            body:
              JSON.stringify({

                currentPassword:
                  currentPassword.value,

                newPassword:
                  newPassword.value

              })
          }
        )


      const result =
        await response.text()


      if (!response.ok) {

        throw new Error(
          result ||
          'Password change failed.'
        )

      }


      message.value =
        'Password updated successfully.'


      currentPassword.value =
        ''

      newPassword.value =
        ''

      confirmPassword.value =
        ''


    } catch (error) {

      console.error(
        'Password 변경 실패:',
        error
      )


      message.value =
        error.message ||
        'Password change failed.'

      isError.value =
        true
    }

  }

</script>


<style scoped>

.page {

  max-width: 520px;

  min-height: 100vh;

  margin: auto;

  padding: 30px 24px;

  font-family:
    'Montserrat',
    sans-serif;

  color: #3A251E;

}


.back-button {

  width: 48px;
  height: 48px;

  margin-bottom: 30px;

  border: none;
  border-radius: 14px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-size: 28px;

  cursor: pointer;

}


.eyebrow {

  margin-bottom: 5px;

  color: #ff6b00;

  font-size: 10px;

  font-weight: 800;

  letter-spacing: 1.4px;

}


h1 {

  margin: 0;

  font-size: 28px;

}


.description {

  margin-top: 8px;

  color: #9ca3af;

  font-size: 11px;

}


.form-card {

  margin-top: 30px;

  padding: 20px;

  border-radius: 20px;

  background-color: #fff4eb;

}


label {

  display: block;

  margin-bottom: 8px;

  font-size: 11px;

  font-weight: 700;

}


.second-label {

  margin-top: 18px;

}


input {

  width: 100%;

  box-sizing: border-box;

  padding: 13px;

  border: 1px solid #eeeeee;

  border-radius: 12px;

  background-color: white;

  outline: none;

}


.save-button {

  width: 100%;

  margin-top: 18px;

  padding: 13px;

  border: none;

  border-radius: 12px;

  background-color: #ff6b00;

  color: white;

  font-family: inherit;

  font-weight: 700;

  cursor: pointer;

}


.message {

  margin-top: 14px;

  color: #3A7A45;

  font-size: 10px;

}


.message.error {

  color: #d94c00;

}

</style>