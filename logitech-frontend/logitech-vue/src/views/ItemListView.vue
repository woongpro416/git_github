<script setup>
import { ref, computed } from 'vue'
import { getItems } from '@/services/ItemService'
import Card from '@/components/itemCard.vue'

const items = ref([])
const isLoading = ref(true)
const keyword = ref('')
const selectedCategory = ref('ALL')

(async function onCreated() {
  const res = await getItems()
  if (res?.status === 200) items.value = res.data
  isLoading.value = false
})()

const filteredItems = computed(() => {
  let result = items.value

  // 카테고리 필터
  if (selectedCategory.value !== 'ALL') {
    result = result.filter(item => item.category === selectedCategory.value)
  }

  // 검색어 필터
  if (keyword.value.trim()) {
    result = result.filter(item =>
      item.itemName.toLowerCase().includes(keyword.value.toLowerCase())
    )
  }

  return result
})

const categories = [
  { value: 'ALL', label: '전체' },
  { value: 'NEW', label: '🆕 신상품' },
  { value: 'BEST', label: '🏆 베스트' },
  { value: 'RECOMMEND', label: '⭐ 추천' },
]
</script>

<template>
  <div class="container py-5">
    <h3 class="mb-4">전체 상품</h3>

    <!-- 검색 + 카테고리 필터 -->
    <div class="row g-3 mb-4">
      <div class="col-md-5">
        <div class="input-group">
          <span class="input-group-text"><i class="bi bi-search"></i></span>
          <input v-model="keyword" type="text" class="form-control"
            placeholder="상품명으로 검색" />
          <button v-if="keyword" class="btn btn-outline-danger"
            @click="keyword = ''">
            <i class="bi bi-x"></i>
          </button>
        </div>
      </div>
      <div class="col-md-7">
        <div class="d-flex gap-2">
          <button v-for="cat in categories" :key="cat.value"
            class="btn btn-sm"
            :class="selectedCategory === cat.value ? 'btn-primary' : 'btn-outline-primary'"
            @click="selectedCategory = cat.value">
            {{ cat.label }}
          </button>
        </div>
      </div>
    </div>

    <!-- 로딩 -->
    <div v-if="isLoading" class="text-center py-5">
      <div class="spinner-border text-primary"></div>
      <p class="mt-2 text-muted">상품을 불러오는 중...</p>
    </div>

    <!-- 상품 목록 -->
    <div v-else>
      <p class="text-muted mb-3">총 {{ filteredItems.length }}개 상품</p>
      <div v-if="filteredItems.length === 0" class="text-center py-5 text-muted">
        <i class="bi bi-search fs-1 d-block mb-3"></i>
        검색 결과가 없습니다.
      </div>
      <div v-else class="row row-cols-1 row-cols-lg-2 row-cols-xl-4 g-3">
        <div class="col" v-for="item in filteredItems" :key="item.itemId">
          <Card :item="item" />
        </div>
      </div>
    </div>
  </div>
</template>
