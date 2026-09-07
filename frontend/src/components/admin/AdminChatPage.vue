<template>

  <div class="admin-chat-page">

    <!-- ============================= -->
    <!-- 왼쪽 : Support 채팅방 목록 -->
    <!-- ============================= -->

    <div class="room-list">

      <div class="room-list-header">

        <h2>
          Support Chats
        </h2>

      </div>


      <!-- 채팅방이 없는 경우 -->

      <div
        v-if="rooms.length === 0"
        class="empty-rooms"
      >
        No support chats.
      </div>


      <!-- Support 채팅방 목록 -->

      <button
        v-for="room in rooms"
        :key="room.roomId"

        class="room-item"

        :class="{
          active:
            selectedRoom?.roomId ===
            room.roomId
        }"

        @click="selectRoom(room)"
      >

        <!-- Avatar -->

        <div class="room-avatar">

          {{
            getAvatar(
              room.customerEmail
            )
          }}

        </div>


        <!-- 사용자 정보 -->

        <div class="room-info">

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


          <span class="customer-id">

            Customer ID:
            {{ room.customerId }}

          </span>

        </div>

      </button>

    </div>


    <!-- ============================= -->
    <!-- 오른쪽 : 실제 채팅창 -->
    <!-- ============================= -->

    <div class="chat-area">

      <!-- 채팅방 선택된 경우 -->

      <ChatWindow
        v-if="selectedRoom"

        :title="
          selectedRoom.customerEmail
        "

        :subtitle="
          getSupportLabel(
            selectedRoom.supportType
          )
        "

        :avatar="
          getAvatar(
            selectedRoom.customerEmail
          )
        "

        :messages="messages"

        @send="sendMessage"
      />


      <!-- 아직 선택된 방이 없는 경우 -->

      <div
        v-else
        class="empty-chat"
      >
        Select a support chat.
      </div>

    </div>

  </div>

</template>


<script setup lang="ts">

import {
  ref,
  onMounted
} from 'vue'

import ChatWindow
  from '@/components/chat/ChatWindow.vue'


// =====================================================
// Backend URL
// =====================================================

const API_BASE =
  'http://localhost:8080/api/chat/admin'


// =====================================================
// Support Type
// =====================================================

type SupportType =
  | 'SELLER_SUPPORT'
  | 'CUSTOMER_SUPPORT'


// =====================================================
// Backend ChatRoom 응답 타입
//
// GET /api/chat/admin/rooms
// =====================================================

interface ChatRoom {

  roomId: number

  customerId: number

  customerEmail: string

  supportType: SupportType

  createdAt: string

  updatedAt: string
}


// =====================================================
// ChatWindow용 Message 타입
// =====================================================

interface ChatMessage {

  id: number | string

  text: string

  time: string

  isMine: boolean
}


// =====================================================
// 상태
// =====================================================

// 현재 Admin에게 허용된
// Support 채팅방 목록
const rooms =
  ref<ChatRoom[]>([])


// 현재 선택한 채팅방
const selectedRoom =
  ref<ChatRoom | null>(null)


// 현재 채팅방의 메시지
const messages =
  ref<ChatMessage[]>([])


// =====================================================
// Admin JWT
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


// =====================================================
// Authorization Header
// =====================================================

const getHeaders = () => {

  const headers:
    Record<string, string> = {

      'Content-Type':
        'application/json'
    }


  const token =
    getAdminToken()


  if (token) {

    headers.Authorization =
      `Bearer ${token}`

  }


  return headers
}


// =====================================================
// Support Type -> 화면 표시 이름
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
// 1. 현재 Admin 담당 채팅방 조회
//
// SELLER_ADMIN
// → SELLER_SUPPORT만 Backend에서 반환
//
// CUSTOMER_ADMIN
// → CUSTOMER_SUPPORT만 Backend에서 반환
//
// GET /api/chat/admin/rooms
// =====================================================

const loadRooms =
  async () => {

    try {

      const response =
        await fetch(
          `${API_BASE}/rooms`,
          {
            method:
              'GET',

            headers:
              getHeaders()
          }
        )


      if (!response.ok) {

        throw new Error(
          `채팅방 조회 실패: ${response.status}`
        )

      }


      const data:
        ChatRoom[] =
          await response.json()


      rooms.value =
        data


      // 기존에 선택된 방이
      // 새 목록에 존재하지 않으면 초기화
      if (
        selectedRoom.value
        &&
        !rooms.value.some(
          room =>
            room.roomId ===
            selectedRoom.value?.roomId
        )
      ) {

        selectedRoom.value =
          null

        messages.value =
          []

      }


      // 첫 번째 채팅방 자동 선택
      if (
        rooms.value.length > 0
        &&
        selectedRoom.value === null
      ) {

        const firstRoom =
          rooms.value[0]


        if (firstRoom) {

          await selectRoom(
            firstRoom
          )

        }

      }


    } catch (error) {

      console.error(
        'Admin 채팅방 조회 실패:',
        error
      )

    }

  }


// =====================================================
// 2. 채팅방 선택
// =====================================================

const selectRoom =
  async (
    room: ChatRoom
  ) => {

    selectedRoom.value =
      room


    await loadMessages(
      room.roomId
    )
  }


// =====================================================
// 3. 선택된 채팅방 메시지 조회
//
// GET
// /api/chat/admin/rooms/{roomId}/messages
// =====================================================

const loadMessages =
  async (
    roomId: number
  ) => {

    try {

      const response =
        await fetch(
          `${API_BASE}/rooms/${roomId}/messages`,
          {
            method:
              'GET',

            headers:
              getHeaders()
          }
        )


      if (!response.ok) {

        throw new Error(
          `메시지 조회 실패: ${response.status}`
        )

      }


      const data =
        await response.json()


      // Backend:
      //
      // {
      //   id,
      //   sender,
      //   content,
      //   createdAt
      // }
      //
      // ↓
      //
      // ChatWindow 형식으로 변환

      messages.value =
        data.map(
          (message: any) => ({

            id:
              message.id,

            text:
              message.content,

            time:
              formatTime(
                message.createdAt
              ),

            // Admin이 보낸 메시지는
            // 오른쪽 말풍선
            isMine:
              message.sender ===
              'ADMIN'

          })
        )


    } catch (error) {

      console.error(
        'Admin 메시지 조회 실패:',
        error
      )

    }

  }


// =====================================================
// 4. Admin 답장
//
// POST
// /api/chat/admin/rooms/{roomId}/messages
//
// Backend에서 현재 JWT Admin을 찾아서
// 실제 admin_id까지 저장
// =====================================================

const sendMessage =
  async (
    content: string
  ) => {

    if (
      !selectedRoom.value
    ) {

      return

    }


    if (
      !content.trim()
    ) {

      return

    }


    try {

      const response =
        await fetch(
          `${API_BASE}/rooms/${selectedRoom.value.roomId}/messages`,
          {
            method:
              'POST',

            headers:
              getHeaders(),

            body:
              JSON.stringify({
                content:
                  content
              })
          }
        )


      if (!response.ok) {

        throw new Error(
          `메시지 전송 실패: ${response.status}`
        )

      }


      const message =
        await response.json()


      // DB 저장 성공 후
      // 화면에도 바로 추가
      messages.value.push({

        id:
          message.id,

        text:
          message.content,

        time:
          formatTime(
            message.createdAt
          ),

        isMine:
          true

      })


      // updatedAt 기준 정렬을
      // 최신 상태로 반영하기 위해
      // 채팅방 목록 재조회
      await loadRooms()


    } catch (error) {

      console.error(
        'Admin 메시지 전송 실패:',
        error
      )

    }

  }


// =====================================================
// 이메일 첫 글자 -> Avatar
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
// 시간 표시
// =====================================================

const formatTime = (
  dateString: string
) => {

  if (!dateString) {

    return ''

  }


  const date =
    new Date(
      dateString
    )


  return date.toLocaleTimeString(
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
// 페이지 처음 열릴 때
// 현재 Admin 담당 Support 목록 조회
// =====================================================

onMounted(() => {

  loadRooms()

})

</script>


<style scoped>

.admin-chat-page {

  display: grid;

  grid-template-columns:
    280px 1fr;

  gap: 20px;

  width: 100%;

  min-height: 600px;

}


/* ======================================= */
/* Support 채팅방 목록 */
/* ======================================= */

.room-list {

  height: 600px;

  overflow-y: auto;

  background-color: white;

  border:
    1px solid #eeeeee;

  border-radius: 24px;

  padding: 16px;

}


.room-list-header {

  padding:
    4px 8px 16px;

  border-bottom:
    1px solid #eeeeee;

}


.room-list-header h2 {

  margin: 0;

  font-size: 18px;

  color: #3A251E;

}


/* ======================================= */
/* Support 채팅방 하나 */
/* ======================================= */

.room-item {

  width: 100%;

  display: flex;

  align-items: center;

  gap: 12px;

  padding: 12px;

  margin-top: 8px;

  border: none;

  border-radius: 14px;

  background: transparent;

  cursor: pointer;

  text-align: left;

}


.room-item:hover {

  background-color:
    #fafafa;

}


.room-item.active {

  background-color:
    #fff4eb;

}


/* ======================================= */
/* Avatar */
/* ======================================= */

.room-avatar {

  width: 40px;

  height: 40px;

  flex-shrink: 0;

  display: flex;

  align-items: center;

  justify-content: center;

  border-radius: 12px;

  background-color:
    #fff4eb;

  color:
    #ff6b00;

  font-weight: 800;

}


/* ======================================= */
/* User 정보 */
/* ======================================= */

.room-info {

  min-width: 0;

  display: flex;

  flex-direction: column;

  gap: 4px;

}


.room-info strong {

  overflow: hidden;

  text-overflow:
    ellipsis;

  white-space:
    nowrap;

  font-size: 12px;

  color: #3A251E;

}


.room-info span {

  font-size: 10px;

  color: #9ca3af;

}


.customer-id {

  font-size: 8px !important;

  color: #c1b7b2 !important;

}


/* ======================================= */
/* 채팅 */
/* ======================================= */

.chat-area {

  min-width: 0;

}


.empty-chat,
.empty-rooms {

  display: flex;

  align-items: center;

  justify-content: center;

  color: #9ca3af;

}


.empty-chat {

  height: 600px;

  border:
    1px solid #eeeeee;

  border-radius: 24px;

  background-color: white;

}

</style>