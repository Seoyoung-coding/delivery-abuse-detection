<template>

  <div
    class="page"
    :class="{
      'support-page': isSupportMode
    }"
  >

    <!-- =========================
         Back
    ========================== -->

    <button
      class="back-button"
      @click="goBack"
    >
      ← Back
    </button>


    <!-- =========================
         Header
    ========================== -->

    <header class="page-header">

      <p class="small-title">

        {{
          isAdminMode
            ? 'ADMIN CENTER'
            : 'HELP & SUPPORT'
        }}

      </p>


      <h1>

        {{
          isAdminMode
            ? 'Support Messages'
            : 'Support Chat'
        }}

      </h1>


      <p class="header-description">

        {{
          isAdminMode
            ? 'Manage support conversations.'
            : 'Connect with the YAMIYUMI support team.'
        }}

      </p>

    </header>


    <!-- =====================================================
         USER MODE
         Seller / Customer 선택
    ====================================================== -->

    <section
      v-if="
        isSupportMode &&
        supportStep === 'select'
      "
      class="support-selection"
    >

      <h2>
        Which type of support do you need?
      </h2>


      <!-- Seller Support -->

      <button
        class="support-option"
        @click="chooseSellerSupport"
      >

        <div>

          <strong>
            I'm a Seller
          </strong>

          <p>
            Store, product, and seller account support
          </p>

        </div>

        <span>
          ›
        </span>

      </button>


      <!-- Customer Support -->

      <button
        class="support-option"
        @click="chooseCustomerSupport"
      >

        <div>

          <strong>
            I'm a Customer
          </strong>

          <p>
            Order, refund, and customer account support
          </p>

        </div>

        <span>
          ›
        </span>

      </button>

    </section>


    <!-- =====================================================
         USER MODE
         Seller ID 확인
    ====================================================== -->

    <section
      v-if="
        isSupportMode &&
        supportStep === 'seller-id'
      "
      class="seller-verification"
    >

      <p class="small-title">
        SELLER SUPPORT
      </p>

      <h2>
        Verify your Seller account
      </h2>

      <p class="verification-description">
        Enter the Seller ID associated with your current account.
      </p>


      <input
        v-model="sellerIdInput"
        class="seller-id-input"
        type="number"
        placeholder="Seller ID"
        @keyup.enter="verifySeller"
      />


      <button
        class="continue-button"
        @click="verifySeller"
      >
        Continue
      </button>

    </section>


    <!-- =====================================================
         ADMIN MODE
    ====================================================== -->

    <div
      v-if="isAdminMode"
      class="admin-chat-layout"
    >

      <!-- =========================
           Admin 채팅방 목록
      ========================== -->

      <section class="conversation-section">

        <div class="section-header">

          <h2>
            Conversations
          </h2>

          <span>
            {{ adminRooms.length }}
          </span>

        </div>


        <div
          v-if="adminRooms.length === 0"
          class="empty-list"
        >
          No support conversations.
        </div>


        <div class="conversation-list">

          <button
            v-for="room in adminRooms"
            :key="room.roomId"

            class="conversation-card"

            :class="{
              active:
                selectedAdminRoom?.roomId ===
                room.roomId
            }"

            @click="
              selectAdminRoom(room)
            "
          >

            <div class="avatar">

              {{
                getAvatar(
                  room.customerEmail
                )
              }}

            </div>


            <div class="conversation-info">

              <strong>

                {{
                  room.customerEmail
                }}

              </strong>


              <span>

                {{
                  getSupportLabel(
                    room.supportType
                  )
                }}

              </span>


              <small>

                Customer ID:
                {{ room.customerId }}

              </small>

            </div>

          </button>

        </div>

      </section>


      <!-- =========================
           Admin Chat Window
      ========================== -->

      <section class="chat-section">

        <ChatWindow
          v-if="selectedAdminRoom"

          :title="
            selectedAdminRoom.customerEmail
          "

          :subtitle="
            getSupportLabel(
              selectedAdminRoom.supportType
            )
          "

          :avatar="
            getAvatar(
              selectedAdminRoom.customerEmail
            )
          "

          :messages="messages"

          @send="sendMessage"
        />

        <button
        v-if="selectedAdminRoom"
        class="close-chat-button"
        @click="closeChat"
      >
        Close Chat
      </button>


        <div
          v-else
          class="empty-chat"
        >
          Select a support conversation.
        </div>

      </section>

    </div>


    <!-- =====================================================
         USER MODE
         실제 Support Chat
    ====================================================== -->

    <section
      v-if="
        isSupportMode &&
        supportStep === 'chat'
      "
      class="user-chat-section"
    >

      <ChatWindow

        :title="userChatTitle"

        :subtitle="userChatSubtitle"

        avatar="Y"

        :messages="messages"

        @send="sendMessage"
      />

      <button
      class="close-chat-button"
      @click="closeChat"
    >
      Close Chat
    </button>


      <!-- =========================
           Admin 답변 대기 안내
      ========================== -->

      <div
        v-if="awaitingAdmin"
        class="waiting-notice"
      >

        Your message has been sent.
        A support agent may take
        1–2 business days to respond.

      </div>

    </section>


    <!-- =========================
         Error
    ========================== -->

    <div
      v-if="errorMessage"
      class="error-message"
    >

      {{ errorMessage }}

    </div>

  </div>

</template>


<script setup lang="ts">

import {
  computed,
  onMounted,
  onUnmounted,
  ref
} from 'vue'

import {
  useRoute,
  useRouter
} from 'vue-router'

import ChatWindow
  from '@/components/chat/ChatWindow.vue'


// =====================================================
// Router
// =====================================================

const route =
  useRoute()

const router =
  useRouter()


// =====================================================
// 현재 ChatPage 모드
//
// /admin/chat
// → Admin
//
// /support/chat
// → Seller / Customer
// =====================================================

const isAdminMode =
  computed(() => {

    return route.path.startsWith(
      '/admin'
    )

  })


const isSupportMode =
  computed(() => {

    return route.path.startsWith(
      '/support'
    )

  })


// =====================================================
// 공통 Message 타입
// =====================================================

interface ChatMessage {

  id: number | string

  text: string

  time: string

  isMine: boolean
}


// =====================================================
// Backend Message 타입
// =====================================================

interface BackendMessage {

  id: number

  sender:
    | 'SELLER'
    | 'CUSTOMER'
    | 'ADMIN'

  content: string

  createdAt: string
}


// =====================================================
// Admin Room 타입
// =====================================================

type SupportType =
  | 'SELLER_SUPPORT'
  | 'CUSTOMER_SUPPORT'


interface AdminRoom {

  roomId: number

  customerId: number

  customerEmail: string

  supportType: SupportType

  createdAt: string

  updatedAt: string
}


// =====================================================
// 공통 메시지
// =====================================================

const messages =
  ref<ChatMessage[]>([])


// =====================================================
// 오류
// =====================================================

const errorMessage =
  ref('')


// =====================================================
// ADMIN 상태
// =====================================================

const adminRooms =
  ref<AdminRoom[]>([])


const selectedAdminRoom =
  ref<AdminRoom | null>(null)


// =====================================================
// USER Support 상태
// =====================================================

type SupportStep =
  | 'select'
  | 'seller-id'
  | 'chat'


const supportStep =
  ref<SupportStep>(
    'select'
  )


const selectedSupportType =
  ref<SupportType | null>(
    null
  )


const sellerIdInput =
  ref('')


const verifiedSellerId =
  ref<number | null>(
    null
  )


// =====================================================
// Admin 답변 대기
// =====================================================

const awaitingAdmin =
  ref(false)


// =====================================================
// Polling
// =====================================================

let pollingTimer:
  number | null =
  null


// =====================================================
// Token
// =====================================================

const getAdminToken = () => {

  return (
    localStorage.getItem(
      'adminToken'
    )
    ||
    localStorage.getItem(
      'token'
    )
  )
}


const getUserToken = () => {

  return localStorage.getItem(
    'token'
  )
}


// =====================================================
// Header 생성
// =====================================================

const createHeaders = (
  token: string
) => {

  return {

    'Content-Type':
      'application/json',

    Authorization:
      `Bearer ${token}`

  }
}


// =====================================================
// Support 표시 이름
// =====================================================

const getSupportLabel = (
  supportType: SupportType
) => {

  if (
    supportType ===
    'SELLER_SUPPORT'
  ) {

    return 'Seller Support'

  }


  return 'Customer Support'
}


// =====================================================
// Avatar
// =====================================================

const getAvatar = (
  email: string
) => {

  if (!email) {

    return '?'

  }


  return email
    .charAt(0)
    .toUpperCase()
}


// =====================================================
// 시간
// =====================================================

const formatTime = (
  createdAt: string
) => {

  if (!createdAt) {

    return ''

  }


  return new Date(
    createdAt
  ).toLocaleTimeString(
    [],
    {
      hour:
        '2-digit',

      minute:
        '2-digit'
    }
  )
}


// =====================================================
// Backend Message -> UI Message
// =====================================================

const convertMessages = (
  data: BackendMessage[],
  adminView: boolean
) => {

  messages.value =
    data.map(
      message => ({

        id:
          message.id,

        text:
          message.content,

        time:
          formatTime(
            message.createdAt
          ),

        // Admin 화면:
        // ADMIN이 내 메시지
        //
        // User 화면:
        // ADMIN이 아닌 메시지가 내 메시지
        isMine:
          adminView
            ? message.sender === 'ADMIN'
            : message.sender !== 'ADMIN'

      })
    )
}


// =====================================================
// Waiting 상태 계산
// =====================================================

const updateWaitingStatus = (
  data: BackendMessage[]
) => {

  if (
    data.length === 0
  ) {

    awaitingAdmin.value =
      false

    return

  }


  const lastMessage =
    data[
      data.length - 1
    ]


  awaitingAdmin.value =
    lastMessage?.sender !==
    'ADMIN'
}


// =====================================================
// ADMIN
// 채팅방 목록 조회
// =====================================================

const loadAdminRooms =
  async () => {

    errorMessage.value =
      ''


    const token =
      getAdminToken()


    if (!token) {

      errorMessage.value =
        'Admin login is required.'

      return
    }


    try {

      const response =
        await fetch(
          'http://localhost:8080/api/chat/admin/rooms',
          {
            method:
              'GET',

            headers:
              createHeaders(
                token
              )
          }
        )


      if (!response.ok) {

        throw new Error(
          `Admin room request failed: ${response.status}`
        )

      }


      const data:
        AdminRoom[] =
        await response.json()


      adminRooms.value =
        data


      // 첫 방 자동 선택
      if (
        adminRooms.value.length > 0
        &&
        selectedAdminRoom.value ===
          null
      ) {

        const firstRoom =
          adminRooms.value[0]


        if (firstRoom) {

          await selectAdminRoom(
            firstRoom
          )

        }

      }


    } catch (error) {

      console.error(
        'Admin 채팅방 조회 실패:',
        error
      )


      errorMessage.value =
        'Failed to load support conversations.'

    }

  }


// =====================================================
// ADMIN
// 채팅방 선택
// =====================================================

const selectAdminRoom =
  async (
    room: AdminRoom
  ) => {

    selectedAdminRoom.value =
      room


    await loadAdminMessages(
      room.roomId
    )
  }


// =====================================================
// ADMIN
// 메시지 조회
// =====================================================

const loadAdminMessages =
  async (
    roomId: number
  ) => {

    const token =
      getAdminToken()


    if (!token) {

      return

    }


    try {

      const response =
        await fetch(
          `http://localhost:8080/api/chat/admin/rooms/${roomId}/messages`,
          {
            method:
              'GET',

            headers:
              createHeaders(
                token
              )
          }
        )


      if (!response.ok) {

        throw new Error(
          `Message request failed: ${response.status}`
        )

      }


      const data:
        BackendMessage[] =
        await response.json()


      convertMessages(
        data,
        true
      )


    } catch (error) {

      console.error(
        'Admin 메시지 조회 실패:',
        error
      )

    }

  }


// =====================================================
// USER
// Seller Support 선택
// =====================================================

const chooseSellerSupport = () => {

  selectedSupportType.value =
    'SELLER_SUPPORT'


  sellerIdInput.value =
    ''


  verifiedSellerId.value =
    null


  errorMessage.value =
    ''


  supportStep.value =
    'seller-id'
}


// =====================================================
// USER
// Customer Support 선택
// =====================================================

const chooseCustomerSupport =
  async () => {

    selectedSupportType.value =
      'CUSTOMER_SUPPORT'


    verifiedSellerId.value =
      null


    errorMessage.value =
      ''


    const success =
      await loadCustomerMessages()


    if (success) {

      supportStep.value =
        'chat'


      startPolling()

    }

  }


// =====================================================
// USER
// Seller ID 검증
// =====================================================

const verifySeller =
  async () => {

    errorMessage.value =
      ''


    const sellerId =
      Number(
        sellerIdInput.value
      )


    if (
      !sellerId ||
      sellerId <= 0
    ) {

      errorMessage.value =
        'Please enter a valid Seller ID.'

      return
    }


    const token =
      getUserToken()


    if (!token) {

      errorMessage.value =
        'Login is required.'

      return
    }


    try {

      // -----------------------------------------------
      // 1. Seller ID만 먼저 검증
      // -----------------------------------------------

      const response =
        await fetch(
          `http://localhost:8080/api/chat/support/seller/verify?sellerId=${sellerId}`,
          {
            method: 'GET',

            headers:
              createHeaders(
                token
              )
          }
        )


      if (!response.ok) {

        errorMessage.value =
          'Seller ID does not match the current account.'

        return
      }


      const result =
        await response.json()


      if (!result.verified) {

        errorMessage.value =
          'Seller ID does not match the current account.'

        return
      }


      // -----------------------------------------------
      // 2. Seller 인증 성공
      // -----------------------------------------------

      verifiedSellerId.value =
        sellerId

      selectedSupportType.value =
        'SELLER_SUPPORT'


      // -----------------------------------------------
      // 3. 인증 성공한 뒤에만
      // Seller Support 메시지 조회
      // -----------------------------------------------

      const success =
        await loadSellerMessages(
          sellerId
        )


      if (!success) {

        errorMessage.value =
          'Seller verification succeeded, but the chat could not be loaded.'

        return
      }


      // -----------------------------------------------
      // 4. 실제 채팅 화면으로 전환
      // -----------------------------------------------

      supportStep.value =
        'chat'


      startPolling()


    } catch (error) {

      console.error(
        'Seller verification failed:',
        error
      )


      errorMessage.value =
        'Unable to verify Seller account.'

    }

  }


// =====================================================
// USER
// Seller Support 메시지 조회
// =====================================================

const loadSellerMessages =
  async (
    sellerId: number,
    showError = true
  ) => {

    const token =
      getUserToken()


    if (!token) {

      if (showError) {

        errorMessage.value =
          'Login is required.'

      }

      return false
    }


    try {

      const response =
        await fetch(
          `http://localhost:8080/api/chat/support/seller/messages?sellerId=${sellerId}`,
          {
            method:
              'GET',

            headers:
              createHeaders(
                token
              )
          }
        )


      if (!response.ok) {

        throw new Error(
          `Seller Support failed: ${response.status}`
        )

      }


      const data:
        BackendMessage[] =
        await response.json()


      convertMessages(
        data,
        false
      )


      updateWaitingStatus(
        data
      )


      return true


    } catch (error) {

      console.error(
        'Seller Support 조회 실패:',
        error
      )


      if (showError) {

        errorMessage.value =
          'Seller ID does not match the current account.'

      }


      return false

    }

  }


// =====================================================
// USER
// Customer Support 메시지 조회
// =====================================================

const loadCustomerMessages =
  async (
    showError = true
  ) => {

    const token =
      getUserToken()


    if (!token) {

      if (showError) {

        errorMessage.value =
          'Login is required.'

      }

      return false
    }


    try {

      const response =
        await fetch(
          'http://localhost:8080/api/chat/support/customer/messages',
          {
            method:
              'GET',

            headers:
              createHeaders(
                token
              )
          }
        )


      if (!response.ok) {

        throw new Error(
          `Customer Support failed: ${response.status}`
        )

      }


      const data:
        BackendMessage[] =
        await response.json()


      convertMessages(
        data,
        false
      )


      updateWaitingStatus(
        data
      )


      return true


    } catch (error) {

      console.error(
        'Customer Support 조회 실패:',
        error
      )


      if (showError) {

        errorMessage.value =
          'Customer Support is not available for this account. If you are a Seller, use Seller Support.'

      }


      return false

    }

  }


// =====================================================
// 공통 Send
// =====================================================

const sendMessage =
  async (
    content: string
  ) => {

    if (
      !content.trim()
    ) {

      return

    }


    if (isAdminMode.value) {

      await sendAdminMessage(
        content
      )

      return

    }


    await sendSupportMessage(
      content
    )
  }


// =====================================================
// ADMIN
// 메시지 전송
// =====================================================

const sendAdminMessage =
  async (
    content: string
  ) => {

    if (
      !selectedAdminRoom.value
    ) {

      return

    }


    const token =
      getAdminToken()


    if (!token) {

      return

    }


    try {

      const response =
        await fetch(
          `http://localhost:8080/api/chat/admin/rooms/${selectedAdminRoom.value.roomId}/messages`,
          {
            method:
              'POST',

            headers:
              createHeaders(
                token
              ),

            body:
              JSON.stringify({
                content
              })
          }
        )


      if (!response.ok) {

        throw new Error(
          `Admin message failed: ${response.status}`
        )

      }


      const saved:
        BackendMessage =
        await response.json()


      messages.value.push({

        id:
          saved.id,

        text:
          saved.content,

        time:
          formatTime(
            saved.createdAt
          ),

        isMine:
          true

      })


    } catch (error) {

      console.error(
        'Admin 메시지 전송 실패:',
        error
      )

    }

  }


// =====================================================
// CUSTOMER
// 메시지 전송
// =====================================================

const sendSupportMessage =
  async (
    content: string
  ) => {

    const token =
      getUserToken()


    if (!token) {

      errorMessage.value =
        'Login is required.'

      return

    }


    try {

      let response:
        Response


      // Seller Support
      if (
        selectedSupportType.value ===
        'SELLER_SUPPORT'
      ) {

        if (
          verifiedSellerId.value ===
          null
        ) {

          return

        }


        response =
          await fetch(
            'http://localhost:8080/api/chat/support/seller/messages',
            {
              method:
                'POST',

              headers:
                createHeaders(
                  token
                ),

              body:
                JSON.stringify({

                  sellerId:
                    verifiedSellerId.value,

                  content

                })
            }
          )

      }


      // Customer Support
      else {

        response =
          await fetch(
            'http://localhost:8080/api/chat/support/customer/messages',
            {
              method:
                'POST',

              headers:
                createHeaders(
                  token
                ),

              body:
                JSON.stringify({
                  content
                })
            }
          )

      }


      if (!response.ok) {

        throw new Error(
          `Support message failed: ${response.status}`
        )

      }


      const saved:
        BackendMessage =
        await response.json()


      messages.value.push({

        id:
          saved.id,

        text:
          saved.content,

        time:
          formatTime(
            saved.createdAt
          ),

        isMine:
          true

      })


      // Admin 답변 대기
      awaitingAdmin.value =
        true


    } catch (error) {

      console.error(
        'Support 메시지 전송 실패:',
        error
      )


      errorMessage.value =
        'Failed to send the message.'

    }

  }


  // =====================================================
  // Chat 종료
  // Customer / Seller / Admin 공용
  // =====================================================

const closeChat =
  async () => {

    const confirmed =
      window.confirm(
        'Are you sure you want to close this conversation?'
      )


    if (!confirmed) {
      return
    }


    try {

      let response:
        Response


      // =================================================
      // Admin
      // =================================================

      if (isAdminMode.value) {

        if (!selectedAdminRoom.value) {
          return
        }


        const token =
          getAdminToken()


        if (!token) {

          errorMessage.value =
            'Admin login is required.'

          return
        }


        response =
          await fetch(
            `http://localhost:8080/api/chat/admin/rooms/${selectedAdminRoom.value.roomId}/close`,
            {
              method: 'PATCH',

              headers:
                createHeaders(
                  token
                )
            }
          )


        if (!response.ok) {

          const message =
            await response.text()

          throw new Error(
            message ||
            'Failed to close chat.'
          )
        }


        // 종료된 방은 ACTIVE 목록에서 제거
        selectedAdminRoom.value =
          null

        messages.value =
          []


        // 남아있는 ACTIVE 채팅방 다시 조회
        await loadAdminRooms()


        return
      }


      // =================================================
      // Customer / Seller
      // =================================================

      const token =
        getUserToken()


      if (!token) {

        errorMessage.value =
          'Login is required.'

        return
      }


      // =================================================
      // Seller Support 종료
      // =================================================

      if (
        selectedSupportType.value ===
        'SELLER_SUPPORT'
      ) {

        if (
          verifiedSellerId.value ===
          null
        ) {

          return
        }


        response =
          await fetch(
            `http://localhost:8080/api/chat/support/seller/close?sellerId=${verifiedSellerId.value}`,
            {
              method: 'PATCH',

              headers:
                createHeaders(
                  token
                )
            }
          )
      }


      // =================================================
      // Customer Support 종료
      // =================================================

      else {

        response =
          await fetch(
            'http://localhost:8080/api/chat/support/customer/close',
            {
              method: 'PATCH',

              headers:
                createHeaders(
                  token
                )
            }
          )
      }


      if (!response.ok) {

        const message =
          await response.text()


        throw new Error(
          message ||
          'Failed to close chat.'
        )
      }


      // =================================================
      // 종료 성공
      // =================================================

      stopPolling()


      messages.value =
        []


      awaitingAdmin.value =
        false


      verifiedSellerId.value =
        null


      selectedSupportType.value =
        null


      // 다시 Seller / Customer 선택 화면으로
      supportStep.value =
        'select'


    } catch (error) {

      console.error(
        'Chat 종료 실패:',
        error
      )


      errorMessage.value =
        'Failed to close the conversation.'
    }
  }


// =====================================================
// USER
// 현재 채팅 새로 조회
// =====================================================

const refreshSupportMessages =
  async () => {

    if (
      supportStep.value !==
      'chat'
    ) {

      return

    }


    if (
      selectedSupportType.value ===
      'SELLER_SUPPORT'
      &&
      verifiedSellerId.value !==
        null
    ) {

      await loadSellerMessages(
        verifiedSellerId.value,
        false
      )

      return
    }


    if (
      selectedSupportType.value ===
      'CUSTOMER_SUPPORT'
    ) {

      await loadCustomerMessages(
        false
      )

    }

  }


// =====================================================
// Polling 시작
//
// Admin 답장이 오면 자동으로 메시지 갱신
// =====================================================

const startPolling = () => {

  stopPolling()


  pollingTimer =
    window.setInterval(
      () => {

        refreshSupportMessages()

      },
      10000
    )
}


// =====================================================
// Polling 중단
// =====================================================

const stopPolling = () => {

  if (
    pollingTimer !== null
  ) {

    window.clearInterval(
      pollingTimer
    )


    pollingTimer =
      null

  }

}


// =====================================================
// User Chat 제목
// =====================================================

const userChatTitle =
  computed(() => {

    if (
      selectedSupportType.value ===
      'SELLER_SUPPORT'
    ) {

      return 'Seller Support'

    }


    return 'Customer Support'
  })


const userChatSubtitle =
  computed(() => {

    return 'YAMIYUMI Support Agent'

  })


// =====================================================
// Back
// =====================================================

const goBack = () => {

  // User가 실제 채팅 중
  if (
    isSupportMode.value
    &&
    supportStep.value ===
      'chat'
  ) {

    stopPolling()


    messages.value =
      []


    awaitingAdmin.value =
      false


    if (
      selectedSupportType.value ===
      'SELLER_SUPPORT'
    ) {

      supportStep.value =
        'seller-id'

    } else {

      supportStep.value =
        'select'

    }


    return
  }


  // Seller ID 화면
  if (
    isSupportMode.value
    &&
    supportStep.value ===
      'seller-id'
  ) {

    supportStep.value =
      'select'


    errorMessage.value =
      ''


    return
  }


  router.back()
}


// =====================================================
// Page Load
// =====================================================

onMounted(() => {

  if (
    isAdminMode.value
  ) {

    loadAdminRooms()

  }

})


// =====================================================
// Page 종료
// =====================================================

onUnmounted(() => {

  stopPolling()

})

</script>


<style scoped>

.page {

  max-width: 1000px;

  min-height: 100vh;

  margin: auto;

  padding:
    24px 20px 100px;

  font-family:
    'Montserrat',
    sans-serif;

  color: #3A251E;

}


.page.support-page {

  max-width: 520px;

}


.back-button {

  margin-bottom: 22px;

  padding: 0;

  border: none;

  background: none;

  color: #ff6b00;

  font-family: inherit;

  font-size: 12px;

  font-weight: 700;

  cursor: pointer;

}


.page-header {

  margin-bottom: 25px;

}


.small-title {

  margin: 0 0 5px;

  color: #ff6b00;

  font-size: 10px;

  font-weight: 800;

  letter-spacing: 1.4px;

}


.page-header h1 {

  margin: 0;

  font-size: 28px;

}


.header-description {

  margin-top: 6px;

  color: #9ca3af;

  font-size: 11px;

}


/* =====================================================
   Admin Layout
===================================================== */

.admin-chat-layout {

  display: grid;

  grid-template-columns:
    300px 1fr;

  gap: 20px;

}


.conversation-section {

  height: 600px;

  padding: 16px;

  overflow-y: auto;

  border:
    1px solid #eeeeee;

  border-radius: 20px;

  background: white;

}


.section-header {

  display: flex;

  justify-content:
    space-between;

  align-items: center;

  padding-bottom: 14px;

  border-bottom:
    1px solid #eeeeee;

}


.section-header h2 {

  margin: 0;

  font-size: 16px;

}


.section-header span {

  color: #ff6b00;

  font-size: 11px;

  font-weight: 700;

}


.conversation-card {

  width: 100%;

  display: flex;

  align-items: center;

  gap: 10px;

  margin-top: 8px;

  padding: 12px;

  border: none;

  border-radius: 14px;

  background: transparent;

  text-align: left;

  cursor: pointer;

}


.conversation-card:hover {

  background:
    #fafafa;

}


.conversation-card.active {

  background:
    #fff4eb;

}


.avatar {

  width: 40px;

  height: 40px;

  flex-shrink: 0;

  display: flex;

  align-items: center;

  justify-content: center;

  border-radius: 12px;

  background: white;

  color: #ff6b00;

  font-weight: 800;

}


.conversation-info {

  min-width: 0;

  display: flex;

  flex-direction: column;

  gap: 3px;

}


.conversation-info strong {

  overflow: hidden;

  text-overflow: ellipsis;

  white-space: nowrap;

  font-size: 11px;

}


.conversation-info span {

  color: #9ca3af;

  font-size: 9px;

}


.conversation-info small {

  color: #c0b5af;

  font-size: 8px;

}


.chat-section {

  min-width: 0;

}


.empty-chat {

  height: 600px;

  display: flex;

  align-items: center;

  justify-content: center;

  border:
    1px solid #eeeeee;

  border-radius: 24px;

  background: white;

  color: #9ca3af;

  font-size: 11px;

}


.empty-list {

  padding: 30px 10px;

  color: #9ca3af;

  text-align: center;

  font-size: 10px;

}


/* =====================================================
   User Support 선택
===================================================== */

.support-selection,
.seller-verification {

  padding: 20px;

  border-radius: 20px;

  background:
    #fff4eb;

}


.support-selection h2,
.seller-verification h2 {

  margin:
    0 0 18px;

  font-size: 18px;

}


.support-option {

  width: 100%;

  display: flex;

  align-items: center;

  justify-content:
    space-between;

  margin-bottom: 10px;

  padding: 16px;

  border: none;

  border-radius: 14px;

  background: white;

  color: #3A251E;

  text-align: left;

  cursor: pointer;

}


.support-option strong {

  font-size: 12px;

}


.support-option p {

  margin:
    4px 0 0;

  color: #9ca3af;

  font-size: 9px;

}


.support-option span {

  color: #ff6b00;

  font-size: 20px;

}


/* =====================================================
   Seller Verification
===================================================== */

.verification-description {

  color: #9ca3af;

  font-size: 10px;

}


.seller-id-input {

  width: 100%;

  box-sizing:
    border-box;

  margin:
    15px 0 10px;

  padding: 13px;

  border:
    1px solid #eeeeee;

  border-radius: 12px;

  outline: none;

}


.continue-button {

  width: 100%;

  padding: 12px;

  border: none;

  border-radius: 12px;

  background: #ff6b00;

  color: white;

  font-family: inherit;

  font-weight: 700;

  cursor: pointer;

}


/* =====================================================
   User Chat
===================================================== */

.user-chat-section {

  width: 100%;

}


.waiting-notice {

  margin-top: 12px;

  padding: 12px 14px;

  border-radius: 12px;

  background:
    #fff4eb;

  color: #8a675a;

  font-size: 10px;

  line-height: 1.5;

}


/* =====================================================
   Error
===================================================== */

.error-message {

  margin-top: 15px;

  padding: 12px;

  border-radius: 12px;

  background:
    #fff1eb;

  color: #d94c00;

  font-size: 10px;

}


@media (
  max-width: 760px
) {

  .admin-chat-layout {

    grid-template-columns:
      1fr;

  }


  .conversation-section {

    height: auto;

    max-height: 300px;

  }

}

.close-chat-button {

  width: 100%;

  margin-top: 12px;

  padding: 11px;

  border: 1px solid #dddddd;

  border-radius: 12px;

  background-color: white;

  color: #8f817a;

  font-family: inherit;

  font-size: 10px;

  font-weight: 700;

  cursor: pointer;

}


.close-chat-button:hover {

  background-color: #f7f7f7;

}



</style>