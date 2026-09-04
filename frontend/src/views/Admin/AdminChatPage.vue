<template>

  <div class="page">

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

      <div>

        <p class="small-title">
          ADMIN CENTER
        </p>

        <h1>
          Messages
        </h1>

      </div>


      <div class="message-count">

        {{ unreadCount }}

      </div>

    </header>


    <!-- =========================
         Conversation Section
    ========================== -->

    <section class="conversation-section">

      <div class="section-header">

        <h2>
          Conversations
        </h2>

        <span>
          {{ sellers.length }} sellers
        </span>

      </div>


      <div class="seller-list">

        <button
          v-for="seller in sellers"
          :key="seller.id"
          class="seller-card"
          :class="{
            active:
              selectedSellerId === seller.id
          }"
          @click="selectSeller(seller.id)"
        >

          <div class="seller-avatar">

            {{ seller.avatar }}

          </div>


          <div class="seller-info">

            <div class="seller-name-row">

              <h3>
                {{ seller.name }}
              </h3>

              <span class="seller-time">
                {{ seller.lastTime }}
              </span>

            </div>


            <div class="seller-message-row">

              <p>
                {{ seller.lastMessage }}
              </p>


              <span
                v-if="seller.unread > 0"
                class="unread-badge"
              >
                {{ seller.unread }}
              </span>

            </div>

          </div>

        </button>

      </div>

    </section>


    <!-- =========================
         Chat Window
    ========================== -->

    <section
      v-if="selectedSeller"
      class="chat-section"
    >

      <ChatWindow
        :title="selectedSeller.name"
        :subtitle="selectedSeller.storeName"
        :avatar="selectedSeller.avatar"
        :online="selectedSeller.online"
        :messages="selectedMessages"
        @send="sendMessage"
      />

    </section>


  </div>

</template>


<script setup lang="ts">

import {
  computed,
  ref,
  onMounted
} from 'vue'

import {
  useRouter
} from 'vue-router'

import ChatWindow from '@/components/chat/ChatWindow.vue'


const router =
  useRouter()


const goBack = () => {

  router.back()

}
interface ChatMessage {
  id: number | string
  text: string
  time: string
  isMine: boolean
}

interface SellerConversation {

  id: number

  name: string

  storeName: string

  avatar: string

  online: boolean

  lastMessage: string

  lastTime: string

  unread: number

}


// =====================================================
// Backend 응답 타입
// =====================================================

interface AdminRoomResponse {
  roomId: number
  sellerId: number
  sellerEmail: string
  createdAt: string
}

interface AdminMessageResponse {
  id: number
  sender: 'SELLER' | 'ADMIN'
  content: string
  createdAt: string
}


// =====================================================
// 실제 Seller 채팅 데이터
// =====================================================

const sellers =
  ref<SellerConversation[]>([])


const chatMessages =
  ref<Record<number, ChatMessage[]>>({})


// 아직 DB 데이터를 불러오기 전에는 선택된 방 없음
const selectedSellerId =
  ref<number | null>(null)


// =====================================================
// 현재 선택된 Seller
// =====================================================

const selectedSeller =
  computed(() => {

    if (selectedSellerId.value === null) {
      return undefined
    }

    return sellers.value.find(
      seller =>
        seller.id === selectedSellerId.value
    )

  })


// =====================================================
// 현재 선택된 Seller의 메시지
// =====================================================

const selectedMessages =
  computed(() => {

    if (selectedSellerId.value === null) {
      return []
    }

    return (
      chatMessages.value[
        selectedSellerId.value
      ] ?? []
    )

  })


// =====================================================
// 읽지 않은 메시지 수
// 현재는 readAt 연결 전이라 기본적으로 0
// =====================================================

const unreadCount =
  computed(() => {

    return sellers.value.reduce(
      (
        total,
        seller
      ) => {

        return total + seller.unread

      },
      0
    )

  })


// =====================================================
// 날짜 → 화면용 시간
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
      hour: '2-digit',
      minute: '2-digit'
    }
  )

}


// =====================================================
// Admin 페이지 처음 열 때
// 실제 DB의 Seller 채팅방 + 메시지 조회
// =====================================================

const loadAdminChats =
  async () => {

    try {

      // -----------------------------------------------
      // 1. 전체 Seller 채팅방 조회
      // -----------------------------------------------

      const roomResponse =
        await fetch(
          'http://localhost:8080/api/chat/admin/rooms'
        )


      if (!roomResponse.ok) {

        throw new Error(
          `채팅방 조회 실패: ${roomResponse.status}`
        )

      }


      const rooms:
        AdminRoomResponse[] =
          await roomResponse.json()


      const loadedSellers:
        SellerConversation[] = []

      const loadedMessages:
        Record<number, ChatMessage[]> = {}


      // -----------------------------------------------
      // 2. 각각의 채팅방 메시지 조회
      // -----------------------------------------------

      for (const room of rooms) {

        const messageResponse =
          await fetch(
            `http://localhost:8080/api/chat/admin/rooms/${room.roomId}/messages`
          )


        if (!messageResponse.ok) {

          throw new Error(
            `메시지 조회 실패: ${messageResponse.status}`
          )

        }


        const messageData:
          AdminMessageResponse[] =
            await messageResponse.json()


        // Backend 메시지를
        // 기존 ChatMessage UI 형식으로 변경
        const mappedMessages:
          ChatMessage[] =
            messageData.map(
              message => ({

                id:
                  message.id,

                text:
                  message.content,

                time:
                  formatTime(
                    message.createdAt
                  ),

                // Admin 화면에서는
                // ADMIN 메시지가 내 메시지
                isMine:
                  message.sender === 'ADMIN'

              })
            )


        // roomId별 메시지 저장
        loadedMessages[
          room.roomId
        ] = mappedMessages


        // 마지막 메시지
        const lastMessage =
          mappedMessages.length > 0
            ? mappedMessages[
                mappedMessages.length - 1
              ]
            : undefined


        // ---------------------------------------------
        // Backend Room
        // →
        // 기존 SellerConversation UI 형태로 변경
        // ---------------------------------------------

        loadedSellers.push({

          // 기존 UI의 id를 roomId로 사용
          // 그래야 메시지 API와 연결하기 쉬움
          id:
            room.roomId,

          // 현재 Backend에는 seller name이 없으므로
          // 우선 이메일 표시
          name:
            room.sellerEmail,

          storeName:
            `Seller ID: ${room.sellerId}`,

          avatar:
            room.sellerEmail
              ? room.sellerEmail
                  .charAt(0)
                  .toUpperCase()
              : '?',

          // online 기능은 아직 없으므로 false
          online:
            false,

          lastMessage:
            lastMessage
              ? lastMessage.text
              : 'No messages yet',

          lastTime:
            lastMessage
              ? lastMessage.time
              : '',

          // readAt 기능 연결 전
          unread:
            0

        })

      }


      // -----------------------------------------------
      // 3. 화면 데이터 교체
      // -----------------------------------------------

      sellers.value =
        loadedSellers

      chatMessages.value =
        loadedMessages


      // -----------------------------------------------
      // 4. 첫 번째 채팅방 자동 선택
      // -----------------------------------------------

      const firstSeller =
        sellers.value[0]

      if (firstSeller) {

        selectedSellerId.value =
          firstSeller.id

      }


    } catch (error) {

      console.error(
        'Admin 채팅 조회 실패:',
        error
      )

    }

  }


// =====================================================
// Seller 채팅방 선택
// =====================================================

const selectSeller = (
  sellerId: number
) => {

  selectedSellerId.value =
    sellerId


  const seller =
    sellers.value.find(
      item =>
        item.id === sellerId
    )


  if (seller) {

    seller.unread = 0

  }

}


// =====================================================
// Admin 메시지 실제 DB 저장
// =====================================================

const sendMessage =
  async (
    text: string
  ) => {

    const sellerId =
      selectedSellerId.value


    if (sellerId === null) {
      return
    }


    if (!text.trim()) {
      return
    }


    try {

      // -----------------------------------------------
      // Backend에 Admin 메시지 저장
      // -----------------------------------------------

      const response =
        await fetch(
          `http://localhost:8080/api/chat/admin/rooms/${sellerId}/messages`,
          {
            method: 'POST',

            headers: {
              'Content-Type':
                'application/json'
            },

            body:
              JSON.stringify({
                content: text
              })
          }
        )


      if (!response.ok) {

        throw new Error(
          `메시지 전송 실패: ${response.status}`
        )

      }


      const savedMessage:
        AdminMessageResponse =
          await response.json()


      // -----------------------------------------------
      // 현재 화면에도 바로 추가
      // -----------------------------------------------

      if (!chatMessages.value[sellerId]) {

        chatMessages.value[sellerId] = []

      }


      chatMessages.value[
        sellerId
      ].push({

        id:
          savedMessage.id,

        text:
          savedMessage.content,

        time:
          formatTime(
            savedMessage.createdAt
          ),

        isMine:
          true

      })


      // -----------------------------------------------
      // 왼쪽 채팅 목록의 마지막 메시지도 갱신
      // -----------------------------------------------

      const seller =
        sellers.value.find(
          item =>
            item.id === sellerId
        )


      if (seller) {

        seller.lastMessage =
          savedMessage.content

        seller.lastTime =
          formatTime(
            savedMessage.createdAt
          )

      }


    } catch (error) {

      console.error(
        'Admin 메시지 전송 실패:',
        error
      )

    }

  }


// =====================================================
// 페이지가 처음 열릴 때 DB 데이터 불러오기
// =====================================================

onMounted(() => {

  loadAdminChats()

})

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

  padding: 22px 20px 50px;

  background-color: #ffffff;

  font-family: 'Montserrat', sans-serif;

  color: #3A251E;

}

.back-button {

  margin-bottom: 16px;

  padding: 8px 14px;

  border: none;

  border-radius: 10px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-size: 12px;

  font-weight: 700;

  cursor: pointer;

}

.back-button:hover {

  background-color: #ffe6d5;

}


/* =========================
   Header
========================= */

.page-header {

  display: flex;

  align-items: center;
  justify-content: space-between;

  margin-bottom: 30px;

}


.small-title {

  margin: 0 0 5px;

  color: #ff6b00;

  font-size: 10px;
  font-weight: 800;

  letter-spacing: 1px;

}


.page-header h1 {

  margin: 0;

  font-size: 27px;
  font-weight: 800;

  color: #3A251E;

}


.message-count {

  width: 42px;
  height: 42px;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 14px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-size: 13px;
  font-weight: 800;

}


/* =========================
   Conversation
========================= */

.conversation-section {

  margin-bottom: 25px;

}


.section-header {

  display: flex;

  align-items: center;
  justify-content: space-between;

  margin-bottom: 14px;

}


.section-header h2 {

  margin: 0;

  font-size: 16px;
  font-weight: 800;

}


.section-header span {

  color: #9ca3af;

  font-size: 9px;
  font-weight: 600;

}


/* =========================
   Seller List
========================= */

.seller-list {

  display: flex;

  flex-direction: column;

  gap: 7px;

  max-height: 245px;

  overflow-y: auto;

}


.seller-list::-webkit-scrollbar {

  display: none;

}


/* =========================
   Seller Card
========================= */

.seller-card {

  width: 100%;

  display: flex;

  align-items: center;

  gap: 12px;

  padding: 12px;

  border: 1px solid transparent;

  border-radius: 17px;

  background-color: white;

  text-align: left;

  cursor: pointer;

  transition: 0.2s;

}


.seller-card:hover {

  background-color: #fafafa;

}


.seller-card.active {

  border-color: #ffe0ca;

  background-color: #fff8f3;

}


/* =========================
   Seller Avatar
========================= */

.seller-avatar {

  width: 45px;
  height: 45px;

  flex-shrink: 0;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 14px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-size: 15px;
  font-weight: 800;

}


/* =========================
   Seller Info
========================= */

.seller-info {

  flex: 1;

  min-width: 0;

}


.seller-name-row {

  display: flex;

  align-items: center;
  justify-content: space-between;

  gap: 10px;

  margin-bottom: 5px;

}


.seller-name-row h3 {

  margin: 0;

  overflow: hidden;

  color: #3A251E;

  font-size: 11px;
  font-weight: 800;

  text-overflow: ellipsis;

  white-space: nowrap;

}


.seller-time {

  flex-shrink: 0;

  color: #b0b6bf;

  font-size: 8px;
  font-weight: 600;

}


.seller-message-row {

  display: flex;

  align-items: center;

  gap: 10px;

}


.seller-message-row p {

  flex: 1;

  min-width: 0;

  margin: 0;

  overflow: hidden;

  color: #9ca3af;

  font-size: 9px;

  text-overflow: ellipsis;

  white-space: nowrap;

}


/* =========================
   Unread
========================= */

.unread-badge {

  min-width: 18px;
  height: 18px;

  display: flex;

  align-items: center;
  justify-content: center;

  padding: 0 5px;

  border-radius: 50px;

  background-color: #ff6b00;

  color: white;

  font-size: 8px;
  font-weight: 800;

}


/* =========================
   Chat
========================= */

.chat-section {

  margin-bottom: 20px;

}

</style>