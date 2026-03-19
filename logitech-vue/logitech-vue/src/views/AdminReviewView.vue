<script setup>
import { ref, onMounted } from 'vue'
import { getAllReviews, deleteReview } from '@/services/reviewService'

const reviews = ref([])

onMounted(() => loadReviews())

const loadReviews = async () => {
  const res = await getAllReviews()
  if (res?.status === 200) reviews.value = res.data
}

const removeReview = async (reviewId) => {
  if (!confirm('리뷰를 삭제하시겠습니까?')) return
  const res = await deleteReview(reviewId)
  if (res?.status === 200 || res?.status === 204) {
    await loadReviews()
  } else {
    alert('삭제 중 오류가 발생했습니다.')
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleDateString('ko-KR')
}
</script>

<template>
  <div class="container-fluid py-4">
    <h3 class="mb-4">리뷰 관리</h3>
    <div class="card shadow-sm">
      <div class="card-body p-0">
        <table class="table table-hover mb-0">
          <thead class="table-dark">
            <tr>
              <th>ID</th>
              <th>작성자</th>
              <th>상품명</th>
              <th>별점</th>
              <th>내용</th>
              <th>작성일</th>
              <th>관리</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="reviews.length === 0">
              <td colspan="7" class="text-center py-4 text-muted">리뷰가 없습니다.</td>
            </tr>
            <tr v-for="review in reviews" :key="review.reviewId">
              <td>{{ review.reviewId }}</td>
              <td>{{ review.memberName }}</td>
              <td>{{ review.itemName }}</td>
              <td>
                <span class="text-warning">{{ '★'.repeat(review.rating) }}</span>
                <span class="text-secondary">{{ '★'.repeat(5 - review.rating) }}</span>
              </td>
              <td>{{ review.content }}</td>
              <td>{{ formatDate(review.createdAt) }}</td>
              <td>
                <button class="btn btn-sm btn-danger" @click="removeReview(review.reviewId)">
                  삭제
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

