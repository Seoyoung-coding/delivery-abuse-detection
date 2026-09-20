<template>

  <div class="page">

    <h1>Your Cart</h1>


    <!-- 장바구니가 비어있는 경우 -->
    <div
      v-if="cartItems.length === 0"
      class="empty-cart"
    >
      Your cart is empty.
    </div>


    <!-- 실제 장바구니 상품 -->
    <div
      v-for="item in cartItems"
      :key="item.cartItemId"
      class="cart-item"
    >

      <div class="food">
        🍔
      </div>


      <div class="info">

        <h3>
          {{ item.productName }}
        </h3>

        <strong>
          ${{ Number(item.price).toFixed(2) }}
        </strong>

      </div>


      <div class="quantity">

        <button
          @click="updateQuantity(
            item,
            item.quantity - 1
          )"
          :disabled="item.quantity <= 1"
        >
          -
        </button>

        <span>
          {{ item.quantity }}
        </span>

        <button
          @click="updateQuantity(
            item,
            item.quantity + 1
          )"
        >
          +
        </button>

      </div>

    </div>


    <!-- 가격 계산 -->
    <div
      v-if="cartItems.length > 0"
      class="summary"
    >

      <div>
        <span>
          Subtotal
        </span>

        <span>
          ${{ subtotal.toFixed(2) }}
        </span>
      </div>


      <div>
        <span>
          Delivery fee
        </span>

        <span>
          ${{ deliveryFee.toFixed(2) }}
        </span>
      </div>


      <div>
        <span>
          Tax
        </span>

        <span>
          ${{ tax.toFixed(2) }}
        </span>
      </div>


      <div class="total">

        <span>
          Total
        </span>

        <span>
          ${{ total.toFixed(2) }}
        </span>

      </div>

    </div>


    <button
      v-if="cartItems.length > 0"
      class="checkout"
      @click="goToOrder"
    >
      Continue to checkout
    </button>


    <BottomNav />

  </div>

</template>


<script setup>

import {
  ref,
  computed,
  onMounted
} from 'vue'

import { useRouter } from 'vue-router'

import BottomNav from '@/components/BottomNav.vue'


const router = useRouter()


// =========================
// Cart 상품
// =========================

const cartItems = ref([])


// =========================
// 임시 Customer ID
// 나중에 JWT 로그인 사용자로 변경
// =========================

const customerId = 1


// =========================
// Cart 조회
// =========================

const loadCart = async () => {

  try {

    const response =
      await fetch(
        `http://localhost:8080/api/cart?customerId=${customerId}`,
        {
          method: 'GET'
        }
      )


    if (!response.ok) {

      const message =
        await response.text()

      throw new Error(
        message ||
        'Failed to load cart'
      )

    }


    const data =
      await response.json()


    cartItems.value =
      data


    console.log(
      'Cart:',
      cartItems.value
    )


  } catch (error) {

    console.error(
      'Cart load failed:',
      error
    )

  }

}


// =========================
// Subtotal
// 상품 가격 × 수량
// =========================

const subtotal =
  computed(() => {

    return cartItems.value.reduce(

      (sum, item) => {

        const price =
          Number(item.price)

        const quantity =
          Number(item.quantity)

        return (
          sum +
          price * quantity
        )

      },

      0
    )

  })


// =========================
// Delivery Fee
// 현재는 무료
// =========================

const deliveryFee =
  computed(() => 0)


// =========================
// Tax
// 현재 임시 9%
// =========================

const taxRate = 0.09


const tax =
  computed(() => {

    return (
      subtotal.value *
      taxRate
    )

  })


// =========================
// Total
// =========================

const total =
  computed(() => {

    return (
      subtotal.value +
      deliveryFee.value +
      tax.value
    )

  })


// =========================
// Checkout 이동
// =========================

const goToOrder = () => {

  router.push('/order')

}


// =========================
// 페이지 진입 시 Cart 조회
// =========================

onMounted(() => {

  loadCart()

})

const updateQuantity = async (item, newQuantity) => {

  if (newQuantity < 1) {
    return
  }

  try {

    const response = await fetch(
      `http://localhost:8080/api/cart/${item.cartItemId}/quantity?quantity=${newQuantity}`,
      {
        method: 'PATCH'
      }
    )

    if (!response.ok) {

      const message = await response.text()

      throw new Error(
        message || 'Failed to update quantity'
      )
    }

    item.quantity = newQuantity

  } catch (error) {

    console.error(
      'Quantity update failed:',
      error
    )

  }

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


.empty-cart {
  margin-top: 40px;

  padding: 30px;

  text-align: center;

  color: #9ca3af;

  font-size: 14px;

  border-radius: 14px;

  background-color: #fff4eb;
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

  flex-shrink: 0;

  background-color: #fff4eb;

  border-radius: 14px;

  font-size: 38px;
}


.info {
  flex: 1;
}


.info h3 {
  margin: 3px 0 8px;

  font-size: 14px;
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


.quantity span {
  min-width: 20px;

  text-align: center;

  font-size: 13px;
  font-weight: 700;
}


.quantity button {
  width: 28px;
  height: 28px;

  border: none;

  border-radius: 8px;

  background-color: #fff4eb;

  color: #ff6b00;

  font-weight: 800;

  cursor: pointer;
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