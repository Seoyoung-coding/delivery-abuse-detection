<template>
  <div class="product-create-page">

    <h1>상품 등록</h1>

    <form @submit.prevent="createProduct">

      <!-- 상품 이름 -->
      <div>
        <label>Name</label>

        <input
          v-model="name"
          type="text"
          placeholder="Name"
          required
        />
      </div>


      <!-- 상품 설명 -->
      <div>
        <label>Description</label>

        <textarea
          v-model="description"
          placeholder="Description"
        ></textarea>
      </div>


      <!-- 상품 가격 -->
      <div>
        <label>Prices</label>

        <input
          v-model="price"
          type="number"
          step="0.01"
          min="0"
          placeholder="0.00"
          required
        />
      </div>


      <!-- 상품 이미지 -->
      <div>
        <label>Product Image</label>

        <input
          type="file"
          accept="image/*"
          @change="handleImage"
        />
      </div>


      <!-- 이미지 미리보기 -->
      <div v-if="imagePreview">
        <img
          :src="imagePreview"
          alt="Preview Image"
          class="preview-image"
        />
      </div>


      <!-- 등록 -->
      <button
        type="submit"
        :disabled="loading"
      >
        {{ loading ? 'registering ...' : 'registered!' }}
      </button>

    </form>


    <!-- 성공 메시지 -->
    <p v-if="successMessage">
      {{ successMessage }}
    </p>


    <!-- 에러 메시지 -->
    <p v-if="errorMessage">
      {{ errorMessage }}
    </p>

  </div>
</template>


<script setup>
import { ref } from 'vue'


// =========================
// 입력값
// =========================

const name = ref('')
const description = ref('')
const price = ref('')


// 실제 이미지 파일
const image = ref(null)


// 이미지 미리보기 주소
const imagePreview = ref(null)


// 요청 상태
const loading = ref(false)


// 결과 메시지
const successMessage = ref('')
const errorMessage = ref('')



// =========================
// 이미지 선택
// =========================
const handleImage = (event) => {

  const file = event.target.files[0]

  if (!file) {
    image.value = null
    imagePreview.value = null
    return
  }


  // 실제 서버로 보낼 이미지
  image.value = file


  // 브라우저에서 미리보기
  imagePreview.value =
    URL.createObjectURL(file)
}



// =========================
// 상품 등록
// =========================
const createProduct = async () => {

  loading.value = true

  successMessage.value = ''
  errorMessage.value = ''


  try {

    // 로그인할 때 저장했던 JWT
    const token =
      localStorage.getItem('token')


    if (!token) {
      throw new Error(
        '로그인이 필요합니다.'
      )
    }


    // multipart/form-data 생성
    const formData =
      new FormData()


    // ProductCreateRequest의 필드명과
    // 정확히 동일해야 함
    formData.append(
      'name',
      name.value
    )

    formData.append(
      'description',
      description.value
    )

    formData.append(
      'price',
      price.value
    )


    // 이미지를 선택한 경우만 추가
    if (image.value) {

      formData.append(
        'image',
        image.value
      )
    }


    // Backend 요청
    const response =
      await fetch(
        'http://localhost:8080/api/products',
        {
          method: 'POST',

          headers: {

            // Content-Type은 직접 넣으면 안 됨.
            // 브라우저가 multipart boundary까지 자동 설정함.
            Authorization:
              `Bearer ${token}`
          },

          body: formData
        }
      )


    if (!response.ok) {

      const message =
        await response.text()

      throw new Error(
        message || '상품 등록 실패'
      )
    }


    const message =
      await response.text()


    successMessage.value =
      message


    // 등록 성공 후 입력창 초기화
    name.value = ''
    description.value = ''
    price.value = ''
    image.value = null
    imagePreview.value = null


  } catch (error) {

    errorMessage.value =
      error.message

  } finally {

    loading.value = false
  }
}

</script>


<style scoped>

.product-create-page {
  max-width: 600px;
  margin: 40px auto;
}

form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

form > div {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

input,
textarea {
  padding: 10px;
}

textarea {
  min-height: 120px;
}

button {
  padding: 12px;
  cursor: pointer;
}

button:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}

.preview-image {
  width: 200px;
  height: 200px;
  object-fit: cover;
}

</style>
