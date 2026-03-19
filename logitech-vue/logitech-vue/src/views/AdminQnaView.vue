<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getQnaList, deleteQna } from '@/services/qnaService'

const router = useRouter()
const qnaList = ref([])

onMounted(() => load())

const load = async () => {
    const res = await getQnaList()
    if (res?.status === 200) qnaList.value = res.data.filter(q => q.level === 1)
}

const remove = async (id) => {
    if (!confirm('질문과 답변을 모두 삭제하시겠습니까?')) return
    const res = await deleteQna(id)
    if (res?.status === 200 || res?.status === 204) {
        await load()
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
    <h3 class="mb-4">Q&A 관리</h3>
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
              <th>관리</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="qnaList.length === 0">
              <td colspan="7" class="text-center py-4 text-muted">등록된 Q&A가 없습니다.</td>
            </tr>
            <tr v-for="(q, idx) in qnaList" :key="q.id">
              <td>{{ idx + 1 }}</td>
              <td style="cursor:pointer" @click="router.push(`/qna/${q.id}`)">
                {{ q.title }}
              </td>
              <td>{{ q.writer }}</td>
              <td>{{ q.viewCount }}</td>
              <td>{{ formatDate(q.createdAt) }}</td>
              <td>
                <span :class="q.hasAnswer ? 'badge bg-success' : 'badge bg-secondary'">
                  {{ q.hasAnswer ? '답변완료' : '미답변' }}
                </span>
              </td>
              <td>
                <button class="btn btn-sm btn-danger" @click="remove(q.id)">삭제</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>