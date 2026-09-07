<template>

  <div class="page">

    <!-- 뒤로가기 -->
    <button
      class="back-button"
      @click="goBack"
    >
      ‹
    </button>


    <p class="eyebrow">
      ACTIVITY
    </p>

    <h1>
      Notifications
    </h1>

    <p class="description">
      Support updates, receipts, and account activity.
    </p>


    <!-- 알림이 없는 경우 -->
    <div
      v-if="notifications.length === 0"
      class="empty"
    >
      No notifications yet.
    </div>


    <!-- 실제 Notification 목록 -->
    <button
      v-for="notification in notifications"
      :key="notification.id"

      class="notification-card"

      :class="{
        unread: !notification.read
      }"

      @click="openNotification(notification)"
    >

      <div class="icon-box">

        {{
          getIcon(
            notification.type
          )
        }}

      </div>


      <div class="notification-content">

        <div class="title-row">

          <strong>
            {{ notification.title }}
          </strong>


          <span
            v-if="!notification.read"
            class="unread-dot"
          />

        </div>


        <p>
          {{ notification.message }}
        </p>


        <span class="time">
          {{
            formatTime(
              notification.createdAt
            )
          }}
        </span>

      </div>

    </button>

  </div>

</template>


<script setup lang="ts">

import {
  onMounted,
  ref
} from 'vue'

import {
  useRouter
} from 'vue-router'


const router =
  useRouter()


// =====================================================
// Notification 타입
// =====================================================

interface Notification {

  id: number

  type:
    | 'SUPPORT_CLOSED'
    | 'SUPPORT_AUTO_CLOSED'
    | 'SUPPORT_REPLY'
    | 'ORDER_RECEIPT'

  title: string

  message: string

  chatRoomId:
    number | null

  orderId:
    number | null

  read: boolean

  createdAt: string
}


// =====================================================
// 상태
// =====================================================

const notifications =
  ref<Notification[]>([])


// =====================================================
// 뒤로가기
// =====================================================

const goBack = () => {

  router.back()

}


// =====================================================
// 알림 조회
//
// GET /api/notifications
// =====================================================

const loadNotifications =
  async () => {

    const token =
      localStorage.getItem(
        'token'
      )


    if (!token) {
      return
    }


    try {

      const response =
        await fetch(
          'http://localhost:8080/api/notifications',
          {
            method: 'GET',

            headers: {
              Authorization:
                `Bearer ${token}`
            }
          }
        )


      if (!response.ok) {

        throw new Error(
          `Notification 조회 실패: ${response.status}`
        )

      }


      notifications.value =
        await response.json()


    } catch (error) {

      console.error(
        'Notification 조회 실패:',
        error
      )

    }

  }


// =====================================================
// Notification 클릭
// =====================================================

const openNotification =
  async (
    notification: Notification
  ) => {

    // 읽지 않은 알림이면 읽음 처리
    if (!notification.read) {

      await markAsRead(
        notification
      )

    }


    // SUPPORT 관련 알림이고
    // chatRoomId가 있으면
    // 다음 단계에서 종료된 ChatRoom 조회 화면으로 연결할 예정
    if (
      notification.chatRoomId
    ) {

      console.log(
        'Closed ChatRoom:',
        notification.chatRoomId
      )

    }


    // ORDER_RECEIPT는
    // Payment / Order 만들고 나서 연결
    if (
      notification.orderId
    ) {

      console.log(
        'Order:',
        notification.orderId
      )

    }

  }


// =====================================================
// 읽음 처리
//
// PATCH /api/notifications/{id}/read
// =====================================================

const markAsRead =
  async (
    notification: Notification
  ) => {

    const token =
      localStorage.getItem(
        'token'
      )


    if (!token) {
      return
    }


    try {

      const response =
        await fetch(
          `http://localhost:8080/api/notifications/${notification.id}/read`,
          {
            method: 'PATCH',

            headers: {
              Authorization:
                `Bearer ${token}`
            }
          }
        )


      if (!response.ok) {

        throw new Error(
          '알림 읽음 처리 실패'
        )

      }


      notification.read =
        true


    } catch (error) {

      console.error(
        '알림 읽음 처리 실패:',
        error
      )

    }

  }


// =====================================================
// 알림 종류별 아이콘
// =====================================================

const getIcon = (
  type: Notification['type']
) => {

  if (
    type ===
    'SUPPORT_CLOSED'
  ) {

    return '✓'

  }


  if (
    type ===
    'SUPPORT_AUTO_CLOSED'
  ) {

    return '!'
  }


  if (
    type ===
    'SUPPORT_REPLY'
  ) {

    return '💬'

  }


  if (
    type ===
    'ORDER_RECEIPT'
  ) {

    return '🧾'

  }


  return '•'
}


// =====================================================
// 시간 표시
// =====================================================

const formatTime = (
  createdAt: string
) => {

  if (!createdAt) {
    return ''
  }


  return new Date(
    createdAt
  ).toLocaleString()

}


// =====================================================
// 페이지 최초 실행
// =====================================================

onMounted(() => {

  loadNotifications()

})

</script>


<style scoped>

.page {

  max-width: 520px;

  min-height: 100vh;

  margin: auto;

  padding: 30px 24px 100px;

  font-family:
    'Montserrat',
    sans-serif;

  color: #3A251E;

}


.back-button {

  width: 48px;
  height: 48px;

  margin-bottom: 25px;

  border: none;
  border-radius: 14px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-size: 28px;

  cursor: pointer;

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


.description {

  margin: 7px 0 25px;

  color: #9ca3af;

  font-size: 11px;

}


.empty {

  padding: 30px;

  border-radius: 18px;

  background-color: #fff4eb;

  color: #9ca3af;

  text-align: center;

  font-size: 11px;

}


.notification-card {

  width: 100%;

  display: flex;

  gap: 12px;

  margin-bottom: 10px;

  padding: 16px;

  border: 1px solid #eeeeee;

  border-radius: 16px;

  background-color: white;

  color: #3A251E;

  text-align: left;

  cursor: pointer;

}


.notification-card.unread {

  background-color: #fff4eb;

}


.icon-box {

  width: 40px;
  height: 40px;

  flex-shrink: 0;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 12px;

  background-color: white;

  color: #ff6b00;

  font-weight: 800;

}


.notification-content {

  flex: 1;

}


.title-row {

  display: flex;

  align-items: center;

  justify-content: space-between;

  gap: 10px;

}


.title-row strong {

  font-size: 11px;

}


.notification-content p {

  margin: 6px 0;

  color: #8f817a;

  font-size: 9px;

  line-height: 1.5;

}


.time {

  color: #b8ada7;

  font-size: 8px;

}


.unread-dot {

  width: 7px;
  height: 7px;

  flex-shrink: 0;

  border-radius: 50%;

  background-color: #ff6b00;

}

</style>