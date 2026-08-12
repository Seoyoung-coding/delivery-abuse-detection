<template>

  <div class="page">

    <h1>Your Cart</h1>


    <div class="restaurant">
      Yummy Burger
    </div>


    <div class="cart-item">

      <div class="food">
        🍔
      </div>


      <div class="info">

        <h3>
          Classic Burger
        </h3>

        <p>
          Cheese, lettuce, tomato
        </p>

        <strong>
          $12.99
        </strong>

      </div>


      <div class="quantity">

        <button>-</button>

        <span>1</span>

        <button>+</button>

      </div>

    </div>


    <div class="summary">

      <div>
        <span>Subtotal</span>
        <span>$12.99</span>
      </div>

      <div>
        <span>Delivery fee</span>
        <span>$0.00</span>
      </div>

      <div>
        <span>Tax</span>
        <span>$1.17</span>
      </div>

      <div class="total">
        <span>Total</span>
        <span>$14.16</span>
      </div>

    </div>


    <button
      class="checkout"
      @click="goToOrder"
    >
      Continue to checkout
    </button>


    <BottomNav />

  </div>

</template>


<script setup>

import { useRouter } from 'vue-router'

import BottomNav from '@/components/BottomNav.vue'


const router = useRouter()


const goToOrder = () => {
  router.push('/order')
}

</script>


<style scoped>

.page {
  max-width: 520px;

  min-height: 100vh;

  margin: auto;

  padding: 25px 20px 100px;

  font-family: 'Montserrat', sans-serif;

  color: #3A251E;
}


.restaurant {
  margin: 20px 0;

  font-size: 14px;
  font-weight: 800;

  color: #ff6b00;
}


.cart-item {
  display: flex;

  gap: 13px;

  padding: 16px 0;

  border-bottom: 1px solid #e5e7eb;
}


.food {
  width: 72px;
  height: 72px;

  display: flex;

  justify-content: center;
  align-items: center;

  background-color: #fff4eb;

  border-radius: 14px;

  font-size: 38px;
}


.info {
  flex: 1;
}


.info h3 {
  margin: 3px 0 5px;

  font-size: 14px;
}


.info p {
  margin: 0 0 8px;

  color: #9ca3af;

  font-size: 10px;
}


.info strong {
  color: #ff6b00;

  font-size: 13px;
}


.quantity {
  display: flex;

  align-items: center;

  gap: 8px;
}


.quantity button {
  width: 28px;
  height: 28px;

  border: none;

  border-radius: 8px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-weight: 800;
}


.summary {
  margin-top: 30px;
}


.summary div {
  display: flex;

  justify-content: space-between;

  margin-bottom: 13px;

  color: #6b7280;

  font-size: 12px;
}


.summary .total {
  padding-top: 16px;

  border-top: 1px solid #e5e7eb;

  color: #3A251E;

  font-size: 16px;
  font-weight: 800;
}


.checkout {
  width: 100%;

  padding: 15px;

  margin-top: 20px;

  border: none;

  border-radius: 12px;

  background-color: #ff6b00;

  color: white;

  font-weight: 800;

  cursor: pointer;

  box-shadow:
    0 6px 16px rgba(255, 107, 0, 0.3);
}

</style>