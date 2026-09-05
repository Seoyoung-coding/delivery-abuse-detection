<template>

  <div class="page">

    <AdminTopNav />


    <div class="page-header">

      <p class="eyebrow">
        SELLER MANAGEMENT
      </p>

      <h1>Sellers</h1>

      <p>
        Manage approved sellers and stores.
      </p>

    </div>


    <!-- Seller가 없는 경우 -->
    <div
      v-if="sellers.length === 0"
      class="empty-sellers"
    >
      No sellers found.
    </div>


    <!-- 실제 DB Seller 목록 -->
    <div
      v-for="seller in sellers"
      :key="seller.id"
      class="seller-card"
    >

      <div class="seller-top">

        <div class="store-icon">
          {{ seller.storeName?.charAt(0) || 'S' }}
        </div>


        <div class="seller-info">

          <div class="name-row">

            <h3>
              {{ seller.storeName }}
            </h3>

            <span class="active-badge">
              Active
            </span>

          </div>


          <p>
            {{ seller.email }}
          </p>

        </div>

      </div>


      <div class="seller-stats">

        <div>

          <strong>
            {{ seller.feeds }}
          </strong>

          <span>
            Feeds
          </span>

        </div>


        <div>

          <strong>
            {{ seller.reports }}
          </strong>

          <span>
            Reports
          </span>

        </div>


        <div>

          <strong>
            {{ seller.orders }}
          </strong>

          <span>
            Orders
          </span>

        </div>

      </div>


      <button class="manage-button">
        Manage Seller
      </button>

    </div>

  </div>

</template>


<script setup>

import {
  ref,
  onMounted
} from 'vue'

import AdminTopNav from '@/components/admin/AdminTopNav.vue'


// =====================================================
// 실제 DB Seller 목록
// =====================================================

const sellers =
  ref([])


// =====================================================
// Admin : 실제 Seller 전체 조회
//
// GET /api/admin/sellers
// =====================================================

const loadSellers =
  async () => {

    try {

      const response =
        await fetch(
          'http://localhost:8080/api/admin/sellers'
        )


      // API 요청 실패
      if (!response.ok) {

        throw new Error(
          `Seller 조회 실패: ${response.status}`
        )

      }


      // Backend 응답
      const data =
        await response.json()


      // =================================================
      // Backend 형식
      //
      // {
      //   sellerId,
      //   customerId,
      //   email
      // }
      //
      // ↓
      //
      // 현재 Seller Management UI 형식으로 변환
      // =================================================

      sellers.value =
        data.map(
          seller => ({

            id:
              seller.sellerId,

            // 아직 Store 이름 API 연결 전
            storeName:
              `Seller #${seller.sellerId}`,

            email:
              seller.email,

            // 아래 3개는
            // 실제 통계 연결 전이므로 0
            feeds:
              0,

            reports:
              0,

            orders:
              0

          })
        )


    } catch (error) {

      console.error(
        'Seller 조회 실패:',
        error
      )

    }

  }


// =====================================================
// 페이지가 처음 열리면
// 실제 Seller DB 조회
// =====================================================

onMounted(() => {

  loadSellers()

})

</script>


<style scoped>

.page {

  max-width: 520px;

  min-height: 100vh;

  margin: auto;

  padding: 20px 20px 100px;

  font-family:
    'Montserrat',
    sans-serif;

  color: #3A251E;

}


.page-header {

  margin-bottom: 25px;

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


.page-header p:not(.eyebrow) {

  margin-top: 6px;

  color: #9ca3af;

  font-size: 11px;

}


/* ======================================== */
/* Seller 없음 */
/* ======================================== */

.empty-sellers {

  padding: 30px;

  border-radius: 18px;

  background-color: #fff4eb;

  color: #9ca3af;

  text-align: center;

  font-size: 11px;

}


/* ======================================== */
/* Seller Card */
/* ======================================== */

.seller-card {

  margin-bottom: 14px;

  padding: 18px;

  border-radius: 18px;

  background-color: #fff4eb;

}


.seller-top {

  display: flex;

  align-items: center;

  gap: 12px;

}


/* ======================================== */
/* Seller Icon */
/* ======================================== */

.store-icon {

  width: 48px;

  height: 48px;

  display: flex;

  justify-content: center;

  align-items: center;

  border-radius: 14px;

  background-color: white;

  color: #ff6b00;

  font-weight: 800;

}


/* ======================================== */
/* Seller Info */
/* ======================================== */

.seller-info {

  flex: 1;

}


.name-row {

  display: flex;

  justify-content: space-between;

  align-items: center;

}


.name-row h3 {

  margin: 0;

  font-size: 13px;

}


.active-badge {

  padding: 4px 8px;

  border-radius: 20px;

  background-color: white;

  color: #ff6b00;

  font-size: 8px;

  font-weight: 800;

}


.seller-info p {

  margin: 4px 0 0;

  color: #9ca3af;

  font-size: 9px;

}


/* ======================================== */
/* Seller Statistics */
/* ======================================== */

.seller-stats {

  display: grid;

  grid-template-columns:
    repeat(3, 1fr);

  margin-top: 18px;

  padding: 14px;

  border-radius: 12px;

  background-color: white;

  text-align: center;

}


.seller-stats div {

  display: flex;

  flex-direction: column;

  gap: 4px;

}


.seller-stats strong {

  font-size: 14px;

}


.seller-stats span {

  color: #aaa09a;

  font-size: 8px;

}


/* ======================================== */
/* Manage Button */
/* ======================================== */

.manage-button {

  width: 100%;

  margin-top: 12px;

  padding: 10px;

  border: none;

  border-radius: 10px;

  background-color: #ff6b00;

  color: white;

  font-family: inherit;

  font-size: 10px;

  font-weight: 700;

  cursor: pointer;

}


.manage-button:hover {

  opacity: 0.9;

}

</style>