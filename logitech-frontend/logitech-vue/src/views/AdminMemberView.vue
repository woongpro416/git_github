<script setup>
import { ref, onMounted } from 'vue'
import { getMemberList, searchMember, deleteMember, changeRole as changeRoleApi } from '@/services/adminServcie'

const members = ref([])
const keyword = ref('')

onMounted(() => loadMembers())

const loadMembers = async () => {
  const res = await getMemberList()
  if (res?.status === 200) members.value = res.data
}

const search = async () => {
  if (!keyword.value.trim()) {
    loadMembers()
    return
  }
  const res = await searchMember(keyword.value)
  if (res?.status === 200) members.value = res.data
}

const resetSearch = () => {
  keyword.value = ''
  loadMembers()
}

const changeRole = async (memberId, role) => {
  const label = role === 'ADMIN' ? '관리자로 승급' : '일반회원으로 강등'
  if (!confirm(`해당 회원을 ${label}하시겠습니까?`)) return
  const res = await changeRoleApi(memberId, role)
  if (res?.status === 200 || res?.status === 204) {
    await loadMembers()
  } else {
    alert('등급 변경 중 오류가 발생했습니다.')
  }
}

const kickMember = async (memberId, name) => {
  if (!confirm(`'${name}' 회원을 강퇴하시겠습니까?`)) return
  const res = await deleteMember(memberId)
  if (res?.status === 200 || res?.status === 204) {
    await loadMembers()
  } else {
    alert('강퇴 처리 중 오류가 발생했습니다.')
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleDateString('ko-KR')
}
</script>

<template>
  <div class="container-fluid py-4">
    <h3 class="mb-4">회원 관리</h3>

    <!-- 검색 -->
    <div class="row mb-3">
      <div class="col-md-4">
        <div class="input-group">
          <input v-model="keyword" type="text" class="form-control" placeholder="이름 또는 아이디 검색"
            @keyup.enter="search" />
          <button class="btn btn-outline-secondary" @click="search">
            <i class="bi bi-search"></i>
          </button>
          <button class="btn btn-outline-danger" @click="resetSearch" v-if="keyword">
            <i class="bi bi-x"></i>
          </button>
        </div>
      </div>
    </div>

    <!-- 회원 목록 테이블 -->
    <div class="card shadow-sm">
      <div class="card-body p-0">
        <table class="table table-hover mb-0">
          <thead class="table-dark">
            <tr>
              <th>ID</th>
              <th>이름</th>
              <th>아이디</th>
              <th>이메일</th>
              <th>가입일</th>
              <th>등급</th>
              <th>관리</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="members.length === 0">
              <td colspan="7" class="text-center py-4 text-muted">회원이 없습니다.</td>
            </tr>
            <tr v-for="member in members" :key="member.memberId">
              <td>{{ member.memberId }}</td>
              <td>{{ member.name }}</td>
              <td>{{ member.loginId }}</td>
              <td>{{ member.email }}</td>
              <td>{{ formatDate(member.createdAt) }}</td>
              <td>
                <span :class="member.role === 'ADMIN' ? 'badge bg-danger' : 'badge bg-primary'">
                  {{ member.role }}
                </span>
              </td>
              <td>
                <div class="d-flex gap-1">
                  <!-- 등급 변경 -->
                  <button v-if="member.role === 'USER'"
                    class="btn btn-sm btn-outline-warning"
                    @click="changeRole(member.memberId, 'ADMIN')">
                    관리자 승급
                  </button>
                  <button v-else
                    class="btn btn-sm btn-outline-secondary"
                    @click="changeRole(member.memberId, 'USER')">
                    일반 강등
                  </button>
                  <!-- 강퇴 -->
                  <button class="btn btn-sm btn-danger"
                    @click="kickMember(member.memberId, member.name)">
                    강퇴
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

