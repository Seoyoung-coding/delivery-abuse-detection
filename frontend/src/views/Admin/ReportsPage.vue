<template>

  <div class="page">

    <AdminTopNav />


    <div class="page-header">

      <p class="eyebrow">
        CONTENT SAFETY
      </p>

      <h1>Reports</h1>

      <p>
        Review reported seller content.
      </p>

    </div>


    <div
      v-for="report in reports"
      :key="report.id"
      class="report-card"
    >

      <div class="report-header">

        <span class="report-badge">
          Reported
        </span>

        <span class="report-time">
          {{ report.time }}
        </span>

      </div>


      <h3>
        {{ report.storeName }}
      </h3>

      <p class="content">
        "{{ report.content }}"
      </p>


      <div class="reason">

        <span>Reason</span>

        <strong>
          {{ report.reason }}
        </strong>

      </div>


      <div class="actions">

        <button class="dismiss">
          Dismiss
        </button>

        <button class="block">
          Block Content
        </button>

      </div>

    </div>

  </div>

</template>


<script setup>

import { ref } from 'vue'

import AdminTopNav from '@/components/admin/AdminTopNav.vue'


const reports = ref([

  {
    id: 1,
    storeName: 'Yami Kitchen',
    content: 'Special sale today!',
    reason: 'Spam',
    time: '5 min ago'
  },

  {
    id: 2,
    storeName: 'Seoul Food',
    content: 'New menu available now.',
    reason: 'Inappropriate Content',
    time: '32 min ago'
  }

])

</script>


<style scoped>

.page {
  max-width: 520px;

  min-height: 100vh;

  margin: auto;

  padding: 20px 20px 100px;

  font-family: 'Montserrat', sans-serif;

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


.report-card {
  margin-bottom: 13px;

  padding: 18px;

  border: 1px solid #f1e6df;

  border-radius: 18px;
}


.report-header {
  display: flex;

  justify-content: space-between;
  align-items: center;
}


.report-badge {
  padding: 5px 9px;

  border-radius: 20px;

  background-color: #fff0ec;

  color: #df5f3f;

  font-size: 8px;
  font-weight: 800;
}


.report-time {
  color: #b0a7a2;

  font-size: 8px;
}


.report-card h3 {
  margin: 15px 0 8px;

  font-size: 13px;
}


.content {
  padding: 12px;

  border-radius: 10px;

  background-color: #fff4eb;

  color: #746660;

  font-size: 10px;

  line-height: 1.5;
}


.reason {
  display: flex;

  justify-content: space-between;

  margin-top: 14px;

  font-size: 9px;
}


.reason span {
  color: #a49a94;
}


.actions {
  display: flex;

  gap: 8px;

  margin-top: 16px;
}


.actions button {
  flex: 1;

  padding: 10px;

  border-radius: 10px;

  font-family: inherit;

  font-size: 9px;
  font-weight: 700;

  cursor: pointer;
}


.dismiss {
  border: 1px solid #ded4cf;

  background-color: white;

  color: #746761;
}


.block {
  border: none;

  background-color: #ff6b00;

  color: white;
}

</style>