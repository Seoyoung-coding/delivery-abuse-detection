<template>

  <div class="page">

    <AdminTopNav />


    <div class="header">

      <div>
        <p class="eyebrow">
          ADMIN CENTER
        </p>

        <h1>
          Dashboard
        </h1>

        <p class="description">
          Manage sellers, reports and messages.
        </p>
      </div>

    </div>


    <div class="summary-grid">

      <RouterLink
        to="/admin/requests"
        class="summary-card"
      >

        <div class="summary-icon">
          ✉
        </div>

        <div>
          <strong>3</strong>
          <p>Pending Requests</p>
        </div>

      </RouterLink>


      <RouterLink
        to="/admin/sellers"
        class="summary-card"
      >

        <div class="summary-icon">
          ▣
        </div>

        <div>
          <strong>12</strong>
          <p>Active Sellers</p>
        </div>

      </RouterLink>


      <RouterLink
        to="/admin/reports"
        class="summary-card"
      >

        <div class="summary-icon warning">
          !
        </div>

        <div>
          <strong>5</strong>
          <p>Pending Reports</p>
        </div>

      </RouterLink>


      <RouterLink
        to="/admin/chat"
        class="summary-card"
      >

        <div class="summary-icon">
          ●
        </div>

        <div>
          <strong>2</strong>
          <p>Unread Messages</p>
        </div>

      </RouterLink>

    </div>


    <section class="section">

      <div class="section-title">

        <div>
          <h2>Needs attention</h2>
          <p>Items waiting for your review.</p>
        </div>

      </div>


      <RouterLink
        to="/admin/requests"
        class="attention-card"
      >

        <div class="attention-left">

          <div class="small-icon">
            ✉
          </div>

          <div>
            <strong>Seller applications</strong>
            <p>3 applications are waiting.</p>
          </div>

        </div>

        <span class="arrow">›</span>

      </RouterLink>


      <RouterLink
        to="/admin/reports"
        class="attention-card"
      >

        <div class="attention-left">

          <div class="small-icon danger">
            !
          </div>

          <div>
            <strong>Reported content</strong>
            <p>5 reports need review.</p>
          </div>

        </div>

        <span class="arrow">›</span>

      </RouterLink>

    </section>

  </div>

</template>


<script setup>

import AdminTopNav from '@/components/admin/AdminTopNav.vue'

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


.header {
  margin-top: 5px;
  margin-bottom: 25px;
}


.eyebrow {
  margin: 0 0 5px;

  color: #ff6b00;

  font-size: 10px;
  font-weight: 800;

  letter-spacing: 1.5px;
}


h1 {
  margin: 0;

  font-size: 28px;
}


.description {
  margin-top: 7px;

  color: #9ca3af;

  font-size: 12px;
}


.summary-grid {
  display: grid;

  grid-template-columns: 1fr 1fr;

  gap: 12px;
}


.summary-card {
  display: flex;

  align-items: center;

  gap: 12px;

  padding: 18px 14px;

  border-radius: 18px;

  background-color: #fff4eb;

  text-decoration: none;

  color: #3A251E;
}


.summary-icon {
  width: 38px;
  height: 38px;

  flex-shrink: 0;

  display: flex;

  justify-content: center;
  align-items: center;

  border-radius: 12px;

  background-color: white;

  color: #ff6b00;

  font-size: 19px;
  font-weight: 800;
}


.summary-card strong {
  font-size: 20px;
}


.summary-card p {
  margin: 3px 0 0;

  color: #9ca3af;

  font-size: 9px;
  font-weight: 600;
}


.section {
  margin-top: 32px;
}


.section-title {
  margin-bottom: 14px;
}


.section-title h2 {
  margin: 0;

  font-size: 18px;
}


.section-title p {
  margin: 5px 0 0;

  color: #9ca3af;

  font-size: 11px;
}


.attention-card {
  display: flex;

  justify-content: space-between;
  align-items: center;

  margin-bottom: 10px;

  padding: 15px;

  border: 1px solid #f1e8e2;

  border-radius: 15px;

  text-decoration: none;

  color: #3A251E;
}


.attention-left {
  display: flex;

  align-items: center;

  gap: 12px;
}


.small-icon {
  width: 37px;
  height: 37px;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 10px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-weight: 800;
}


.attention-left strong {
  font-size: 12px;
}


.attention-left p {
  margin: 4px 0 0;

  color: #9ca3af;

  font-size: 10px;
}


.arrow {
  font-size: 24px;

  color: #c7bdb7;
}

</style>