<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAccountStore } from '@/store/accountStore'
import { getQnaList, searchQna, addQuestion } from '@/services/qnaService'

const router = useRouter()
const accountStore = useAccountStore()

const qnaList = ref([])
const keyword = ref('')
const showModal = ref(false)
const form = reactive({ title: '', content: '' })

const load = async () => {
    const res = await getQnaList()
    if (res?.status === 200) {
        const all = res.data
        qnaList.value = all
            .filter(q => q.level === 1)
            .map(q => ({
                ...q,
                hasAnswer: all.some(a => a.level === 2 && a.parentId === q.id)
            }))
    }
}

const search = async () => {
    if (!keyword.value.trim()) {
        load()
        return
    }
    const res = await searchQna(keyword.value)
    if (res?.status === 200) {
        const all = res.data
        qnaList.value = all
            .filter(q => q.level === 1)
            .map(q => ({
                ...q,
                hasAnswer: all.some(a => a.level === 2 && a.parentId === q.id)
            }))
    }
}

const submitQuestion = async () => {
    if (!form.title.trim() || !form.content.trim()) {
        alert('제목과 내용을 입력해주세요.')
        return
    }

    const res = await addQuestion({
        title: form.title,
        content: form.content,
        writer: accountStore.loginUser.name  
    })
    if (res?.status === 200) {
        alert('질문이 등록되었습니다.')
        form.title = ''
        form.content = ''
        showModal.value = false
        await load()
    } else {
        alert('등록 중 오류가 발생했습니다.')
    }
}

const formatDate = (dateStr) => {
    if (!dateStr) return '-'
    return new Date(dateStr).toLocaleDateString('ko-KR')
}

;(async function onCreated() {
    await load()
})()
</script>

<template>
  <div class="container py-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h3>Q&A</h3>
      <button v-if="accountStore.loggedIn"
        class="btn btn-primary" @click="showModal = true">
        <i class="bi bi-pencil me-1"></i>질문 등록
      </button>
    </div>

    <!-- 검색 -->
    <div class="row mb-3">
      <div class="col-md-4">
        <div class="input-group">
          <input v-model="keyword" type="text" class="form-control"
            placeholder="제목으로 검색" @keyup.enter="search" />
          <button class="btn btn-outline-secondary" @click="search">
            <i class="bi bi-search"></i>
          </button>
          <button v-if="keyword" class="btn btn-outline-danger" @click="keyword = ''; load()">
            <i class="bi bi-x"></i>
          </button>
        </div>
      </div>
    </div>

    <!-- 목록 -->
    <div class="card shadow-sm">
      <div class="card-body p-0">
        <table class="table table-hover mb-0">
          <thead class="table-dark">
            <tr>
              <th>번호</th>
              <th>제목</th>
              <th>작성자</th>
              <th>조회수</th>
              <th>등록일</th>
              <th>답변여부</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="qnaList.length === 0">
              <td colspan="6" class="text-center py-4 text-muted">등록된 Q&A가 없습니다.</td>
            </tr>
            <tr v-for="(q, idx) in qnaList" :key="q.id"
              style="cursor:pointer" @click="router.push(`/qna/${q.id}`)">
              <td>{{ idx + 1 }}</td>
              <td>{{ q.title }}</td>
              <td>{{ q.writer }}</td>
              <td>{{ q.viewCount }}</td>
              <td>{{ formatDate(q.createdAt) }}</td>
              <td>
                <span :class="q.hasAnswer ? 'badge bg-success' : 'badge bg-secondary'">
                  {{ q.hasAnswer ? '답변완료' : '미답변' }}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 질문 등록 모달 -->
    <div v-if="showModal" class="modal-backdrop">
      <div class="modal-box card shadow p-4">
        <h5 class="mb-3">질문 등록</h5>
        <div class="mb-3">
          <label class="form-label">제목</label>
          <input v-model="form.title" type="text" class="form-control"
            placeholder="제목을 입력해주세요" />
        </div>
        <div class="mb-4">
          <label class="form-label">내용</label>
          <textarea v-model="form.content" class="form-control" rows="5"
            placeholder="내용을 입력해주세요"></textarea>
        </div>
        <div class="d-flex gap-2">
          <button class="btn btn-primary w-100" @click="submitQuestion">등록</button>
          <button class="btn btn-outline-secondary w-100" @click="showModal = false">취소</button>
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
  width: 600px;
  background: white;
  border-radius: 8px;
}
</style>