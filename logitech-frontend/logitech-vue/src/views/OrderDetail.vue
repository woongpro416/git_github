<script setup>
import { reactive, ref } from 'vue';
import { getOrder } from '../services/orderService';
import { useAccountStore } from "../store/accountStore";
import { useRoute } from "vue-router";
import { getItems } from '../services/itemService';
import { saveReview, updateReview, deleteReview, getReviewsByMember } from '../services/reviewService';

const accountStore = useAccountStore();
const route = useRoute();

const state = reactive({
    orders: {
        id: 0, name: "", address: "", payment: "",
        amount: 0, createAt: "", items: [],
    }
});

// 리뷰 관련
const myReviews = ref([])       // 내 리뷰 목록
const showReviewModal = ref(false)
const editingReview = ref(null) // 수정 중인 리뷰
const reviewForm = ref({ itemId: null, itemName: '', rating: 5, content: '' })

const load = async () => {
    const res = await getOrder(route.params.id);
    const itemRes = await getItems();

    if (res.status === 200 && itemRes.status === 200) {
        state.orders = res.data;
        const allItems = itemRes.data;

        state.orders.items = state.orders.itemIds?.split(',').map(id => {
            const found = allItems.find(i => i.itemId === Number(id));
            return found ? { itemId: found.itemId, name: found.itemName } : { itemId: null, name: '-' };
        }) ?? [];
    }

    // 내 리뷰 목록 로드
    const reviewRes = await getReviewsByMember(accountStore.loginUser.memberId)
    if (reviewRes?.status === 200) myReviews.value = reviewRes.data
};

(async function onCreated() {
    await load();
})();

// 해당 상품에 이미 리뷰 썼는지 확인
const getMyReview = (itemId) => {
    return myReviews.value.find(
        r => r.itemId === itemId && r.orderId === state.orders.orderId
    )
}

// 리뷰 모달 열기
const openReviewModal = (item, existingReview = null) => {
    editingReview.value = existingReview
    reviewForm.value = existingReview
        ? { itemId: item.itemId, itemName: item.name, rating: existingReview.rating, content: existingReview.content }
        : { itemId: item.itemId, itemName: item.name, rating: 5, content: '' }
    showReviewModal.value = true
}

const closeReviewModal = () => {
    showReviewModal.value = false
    editingReview.value = null
}

// 리뷰 등록/수정
const submitReview = async () => {
    if (!reviewForm.value.content.trim()) {
        alert('리뷰 내용을 입력해주세요.')
        return
    }

    let res
    if (editingReview.value) {
        res = await updateReview(editingReview.value.reviewId, {
            rating: reviewForm.value.rating,
            content: reviewForm.value.content
        })
    } else {
        res = await saveReview({
            memberId: accountStore.loginUser.memberId,
            itemId: reviewForm.value.itemId,
            orderId: state.orders.orderId,
            rating: reviewForm.value.rating,
            content: reviewForm.value.content
        })
    }

    if (res?.status === 200 || res?.status === 201) {
        alert(editingReview.value ? '리뷰가 수정됐습니다.' : '리뷰가 등록됐습니다.')
        await load()
        closeReviewModal()
    } else {
        alert('처리 중 오류가 발생했습니다.')
    }
}

// 리뷰 삭제
const removeReview = async (reviewId) => {
    if (!confirm('리뷰를 삭제하시겠습니까?')) return
    const res = await deleteReview(reviewId)
    if (res?.status === 200 || res?.status === 204) {
        await load()
    } else {
        alert('삭제 중 오류가 발생했습니다.')
    }
}
</script>

<template>
  <div class="order-detail py-5">
    <div class="container">
      <div class="row">
        <!-- 주문 정보 -->
        <div class="order col-lg-8">
          <div class="h5 mb-4"><b>주문 내용</b></div>
          <table class="table table-bordered">
            <tbody>
              <tr>
                <th>주문 ID</th>
                <td>{{ state.orders.orderId }}</td>
              </tr>
              <tr>
                <th>주문자명</th>
                <td>{{ state.orders.name }}</td>
              </tr>
              <tr>
                <th>주소</th>
                <td>{{ state.orders.address }}</td>
              </tr>
              <tr>
                <th>결제 금액</th>
                <td>{{ state.orders.amount?.toLocaleString() }}원</td>
              </tr>
              <tr>
                <th>결제 수단</th>
                <td>{{ state.orders.payment === 'card' ? '카드' : '무통장입금(한국은행 123-456789-777)' }}</td>
              </tr>
              <tr>
                <th>결제 일시</th>
                <td>{{ state.orders.createAt ? new Date(state.orders.createAt).toLocaleString() : '-' }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 주문 상품 + 리뷰 -->
        <div class="items col-lg-4">
          <div class="h5 mb-4"><b>주문 상품</b></div>
          <table class="table table-bordered">
            <thead>
              <tr>
                <th>번호</th>
                <th>상품명</th>
                <th>리뷰</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, idx) in state.orders.items" :key="idx">
                <td>{{ idx + 1 }}</td>
                <td>{{ item.name }}</td>
                <td>
                  <template v-if="getMyReview(item.itemId)">
                    <div class="d-flex gap-1">
                      <button class="btn btn-sm btn-outline-warning"
                        @click="openReviewModal(item, getMyReview(item.itemId))">
                        수정
                      </button>
                      <button class="btn btn-sm btn-danger"
                        @click="removeReview(getMyReview(item.itemId).reviewId)">
                        삭제
                      </button>
                    </div>
                  </template>
                  <template v-else>
                    <button class="btn btn-sm btn-outline-primary"
                      @click="openReviewModal(item)">
                      리뷰 작성
                    </button>
                  </template>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 리뷰 모달 -->
    <div v-if="showReviewModal" class="modal-backdrop">
      <div class="modal-box card shadow p-4">
        <h5 class="mb-3">{{ editingReview ? '리뷰 수정' : '리뷰 작성' }}</h5>
        <p class="text-muted mb-3">{{ reviewForm.itemName }}</p>

        <div class="mb-3">
          <label class="form-label">별점</label>
          <div class="d-flex gap-2">
            <button v-for="n in 5" :key="n"
              class="btn btn-sm"
              :class="n <= reviewForm.rating ? 'btn-warning' : 'btn-outline-secondary'"
              @click="reviewForm.rating = n">
              ★
            </button>
            <span class="ms-2">{{ reviewForm.rating }}점</span>
          </div>
        </div>

        <div class="mb-4">
          <label class="form-label">리뷰 내용</label>
          <textarea v-model="reviewForm.content" class="form-control" rows="4"
            placeholder="상품에 대한 솔직한 리뷰를 작성해주세요."></textarea>
        </div>

        <div class="d-flex gap-2">
          <button class="btn btn-primary w-100" @click="submitReview">
            {{ editingReview ? '수정 완료' : '등록' }}
          </button>
          <button class="btn btn-outline-secondary w-100" @click="closeReviewModal">
            취소
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0; left: 0;
  width: 100%; height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}
.modal-box {
  width: 500px;
  background: white;
  border-radius: 8px;
}
</style>