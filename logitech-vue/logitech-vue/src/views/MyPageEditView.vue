<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAccountStore } from '@/store/accountStore'
import { getMember, updateMember } from '@/services/accountService'

const router = useRouter()
const accountStore = useAccountStore()

const form = ref({
  name: '',
  email: '',
  phone: '',
  address: '',
  password: '',
  passwordConfirm: ''
})

onMounted(async () => {
  const memberId = accountStore.loginUser?.memberId
  if (!memberId) {
    router.push('/login')
    return
  }
  const res = await getMember(memberId)
  if (res?.status === 200) {
    const data = res.data
    form.value.name = data.name
    form.value.email = data.email
    form.value.phone = data.phone || ''
    form.value.address = data.address || ''
  }
})

const submitUpdate = async () => {
  if (form.value.password !== form.value.passwordConfirm) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  }
  if (!form.value.password) {
    alert('비밀번호를 입력해주세요.')
    return
  }

  const memberId = accountStore.loginUser?.memberId
  const res = await updateMember(memberId, form.value)

  if (res?.status === 200 || res?.status === 204) {
    alert('수정이 완료되었습니다.')
    router.push('/mypage')
  } else {
    alert('수정 중 오류가 발생했습니다.')
  }
}
</script>


<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card shadow-sm">
          <div class="card-body p-4">
            <h3 class="card-title mb-4">회원 정보 수정</h3>

            <div class="mb-3">
              <label class="form-label">이름</label>
              <input v-model="form.name" type="text" class="form-control" />
            </div>
            <div class="mb-3">
              <label class="form-label">이메일</label>
              <input v-model="form.email" type="email" class="form-control" />
            </div>
            <div class="mb-3">
              <label class="form-label">전화번호</label>
              <input v-model="form.phone" type="text" class="form-control" />
            </div>
            <div class="mb-3">
              <label class="form-label">주소</label>
              <input v-model="form.address" type="text" class="form-control" />
            </div>
            <div class="mb-3">
              <label class="form-label">새 비밀번호</label>
              <input v-model="form.password" type="password" class="form-control" />
            </div>
            <div class="mb-4">
              <label class="form-label">비밀번호 확인</label>
              <input v-model="form.passwordConfirm" type="password" class="form-control"
                :class="{ 'is-invalid': form.passwordConfirm && form.password !== form.passwordConfirm }" />
              <div class="invalid-feedback">비밀번호가 일치하지 않습니다.</div>
            </div>

            <div class="d-flex gap-2">
              <button class="btn btn-primary w-100" @click="submitUpdate">
                <i class="bi bi-check-lg me-1"></i>수정 완료
              </button>
              <button class="btn btn-outline-secondary w-100" @click="router.push('/mypage')">
                취소
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

