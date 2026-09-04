<template>

  <div class="admin-chat-page">

    <!-- ============================= -->
    <!-- 왼쪽 : Seller 채팅방 목록 -->
    <!-- ============================= -->
    <div class="room-list">

      <div class="room-list-header">
        <h2>Seller Chats</h2>
      </div>


      <!-- 채팅방이 없는 경우 -->
      <div
        v-if="rooms.length === 0"
        class="empty-rooms"
      >
        No seller chats.
      </div>


      <!-- Seller 채팅방 -->
      <button
        v-for="room in rooms"
        :key="room.roomId"
        class="room-item"
        :class="{
          active:
            selectedRoom?.roomId === room.roomId
        }"
        @click="selectRoom(room)"
      >

        <div class="room-avatar">
          {{ getAvatar(room.sellerEmail) }}
        </div>


        <div class="room-info">

          <strong>
            {{ room.sellerEmail }}
          </strong>

          <span>
            Seller ID: {{ room.sellerId }}
          </span>

        </div>

      </button>

    </div>


    <!-- ============================= -->
    <!-- 오른쪽 : 실제 채팅창 -->
    <!-- ============================= -->
    <div class="chat-area">

      <!-- 선택된 Seller가 있을 때 -->
      <ChatWindow
        v-if="selectedRoom"

        :title="selectedRoom.sellerEmail"

        :subtitle="
          `Seller ID: ${selectedRoom.sellerId}`
        "

        :avatar="
          getAvatar(selectedRoom.sellerEmail)
        "

        :messages="messages"

        @send="sendMessage"
      />


      <!-- 아직 방을 선택하지 않은 경우 -->
      <div
        v-else
        class="empty-chat"
      >
        Select a seller chat.
      </div>

    </div>

  </div>

</template>


<script setup lang="ts">

import {
  ref,
  onMounted
} from 'vue'


// 경로가 다르면 이 부분만 네 폴더에 맞게 수정
import ChatWindow from '../../components/chat/ChatWindow.vue'



// =====================================================
// Backend URL
// =====================================================

const API_BASE =
  'http://localhost:8080/api/chat/admin'



// =====================================================
// 채팅방 타입
// Backend GET /api/chat/admin/rooms 응답
// =====================================================

interface ChatRoom {

  roomId: number

  sellerId: number

  sellerEmail: string

  createdAt: string
}



// =====================================================
// ChatWindow가 요구하는 Message 형식
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

// 전체 Seller 채팅방
const rooms =
  ref<ChatRoom[]>([])


// 현재 선택된 채팅방
const selectedRoom =
  ref<ChatRoom | null>(null)


// 현재 선택된 채팅방의 메시지
const messages =
  ref<ChatMessage[]>([])



// =====================================================
// Admin JWT 가져오기
// =====================================================

const getAdminToken = () => {

  // 네 로그인 코드에서 사용하는 key에 맞춰서
  // 하나만 사용해도 됨.
  return (
    localStorage.getItem('adminToken') ||
    localStorage.getItem('token')
  )
}



// =====================================================
// Authorization Header 만들기
// =====================================================

const getHeaders = () => {

  const headers: Record<string, string> = {
    'Content-Type': 'application/json'
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
// 1. 전체 Seller 채팅방 조회
//
// GET /api/chat/admin/rooms
// =====================================================

const loadRooms = async () => {

  try {

    const response =
      await fetch(
        `${API_BASE}/rooms`,
        {
          method: 'GET',
          headers: getHeaders()
        }
      )


    if (!response.ok) {

      throw new Error(
        `채팅방 조회 실패: ${response.status}`
      )

    }


    const data: ChatRoom[] =
      await response.json()


    rooms.value = data


    // 채팅방이 존재하면
    // 첫 번째 방 자동 선택
if (
  rooms.value.length > 0 &&
  selectedRoom.value === null
) {

  const firstRoom =
    rooms.value[0]

  if (firstRoom) {
    await selectRoom(firstRoom)
  }
}

  } catch (error) {

    console.error(
      'Admin 채팅방 조회 실패',
      error
    )

  }

}



// =====================================================
// 2. Seller 채팅방 선택
// =====================================================

const selectRoom = async (
  room: ChatRoom
) => {

  selectedRoom.value =
    room


  await loadMessages(
    room.roomId
  )
}



// =====================================================
// 3. 선택한 채팅방 메시지 조회
//
// GET
// /api/chat/admin/rooms/{roomId}/messages
// =====================================================

const loadMessages = async (
  roomId: number
) => {

  try {

    const response =
      await fetch(
        `${API_BASE}/rooms/${roomId}/messages`,
        {
          method: 'GET',
          headers: getHeaders()
        }
      )


    if (!response.ok) {

      throw new Error(
        `메시지 조회 실패: ${response.status}`
      )

    }


    const data =
      await response.json()


    // Backend 형식:
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
    // ChatWindow 형식으로 변경

    messages.value =
      data.map((message: any) => ({

        id:
          message.id,

        text:
          message.content,

        time:
          formatTime(
            message.createdAt
          ),

        // Admin 화면이므로
        // ADMIN 메시지는 오른쪽
        isMine:
          message.sender === 'ADMIN'

      }))


  } catch (error) {

    console.error(
      'Admin 메시지 조회 실패',
      error
    )

  }

}



// =====================================================
// 4. Admin 답장
//
// POST
// /api/chat/admin/rooms/{roomId}/messages
// =====================================================

const sendMessage = async (
  content: string
) => {

  if (!selectedRoom.value) {
    return
  }


  if (!content.trim()) {
    return
  }


  try {

    const response =
      await fetch(
        `${API_BASE}/rooms/${selectedRoom.value.roomId}/messages`,
        {
          method: 'POST',

          headers:
            getHeaders(),

          body:
            JSON.stringify({
              content: content
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


    // 방 전체를 다시 호출하지 않고
    // 방금 저장된 메시지만 화면에 추가
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


  } catch (error) {

    console.error(
      'Admin 메시지 전송 실패',
      error
    )

  }

}



// =====================================================
// 이메일 첫 글자를 Avatar로 사용
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
    new Date(dateString)


  return date.toLocaleTimeString(
    [],
    {
      hour: '2-digit',
      minute: '2-digit'
    }
  )
}



// =====================================================
// AdminChatPage 처음 열렸을 때
// Seller 채팅방 목록 조회
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
/* Seller 채팅방 목록 */
/* ======================================= */

.room-list {

  height: 600px;

  overflow-y: auto;

  background-color: white;

  border: 1px solid #eeeeee;

  border-radius: 24px;

  padding: 16px;

}


.room-list-header {

  padding: 4px 8px 16px;

  border-bottom:
    1px solid #eeeeee;

}


.room-list-header h2 {

  margin: 0;

  font-size: 18px;

  color: #3A251E;

}



/* ======================================= */
/* Seller 하나 */
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
/* Seller 정보 */
/* ======================================= */

.room-info {

  min-width: 0;

  display: flex;

  flex-direction: column;

  gap: 4px;

}


.room-info strong {

  overflow: hidden;

  text-overflow: ellipsis;

  white-space: nowrap;

  font-size: 12px;

  color: #3A251E;

}


.room-info span {

  font-size: 10px;

  color: #9ca3af;

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

  border: 1px solid #eeeeee;

  border-radius: 24px;

  background-color: white;

}

</style>