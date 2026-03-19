<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useAccountStore } from "@/store/accountStore";
import { getMember, deleteMember, logout } from "@/services/accountService";

const router = useRouter();
const accountStore = useAccountStore();
const member = ref(null);

onMounted(async () => {
  const memberId = accountStore.loginUser?.memberId;
  if (!memberId) {
    router.push("/login");
    return;
  }
  const res = await getMember(memberId);
  if (res?.status === 200) {
    member.value = res.data;
  }
});

const goEdit = () => {
  router.push("/mypage/edit");
};

const confirmDelete = async () => {
  if (!confirm("정말 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.")) return;

  const memberId = accountStore.loginUser?.memberId;
  const res = await deleteMember(memberId);
  if (res?.status === 200 || res?.status === 204) {
    await logout();
    accountStore.setLoggedIn(false);
    accountStore.setLoginUser(null);
    alert("탈퇴가 완료되었습니다.");
    router.push("/");
  } else {
    alert("탈퇴 처리 중 오류가 발생했습니다.");
  }
};

const formatDate = (dateStr) => {
  if (!dateStr) return "-";
  return new Date(dateStr).toLocaleDateString("ko-KR");
};
</script>

<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card shadow-sm">
          <div class="card-body p-4">
            <h3 class="card-title mb-4">마이페이지</h3>

            <div v-if="member">
              <ul class="list-group list-group-flush mb-4">
                <li class="list-group-item d-flex justify-content-between">
                  <span class="text-muted">이름</span>
                  <strong>{{ member.name }}</strong>
                </li>
                <li class="list-group-item d-flex justify-content-between">
                  <span class="text-muted">아이디</span>
                  <strong>{{ member.loginId }}</strong>
                </li>
                <li class="list-group-item d-flex justify-content-between">
                  <span class="text-muted">이메일</span>
                  <strong>{{ member.email }}</strong>
                </li>
                <li class="list-group-item d-flex justify-content-between">
                  <span class="text-muted">전화번호</span>
                  <strong>{{ member.phone || "-" }}</strong>
                </li>
                <li class="list-group-item d-flex justify-content-between">
                  <span class="text-muted">주소</span>
                  <strong>{{ member.address || "-" }}</strong>
                </li>
                <li class="list-group-item d-flex justify-content-between">
                  <span class="text-muted">등급</span>
                  <span
                    :class="
                      member.role === 'ADMIN' ? 'badge bg-danger' : 'badge bg-primary'
                    "
                  >
                    {{ member.role === "ADMIN" ? "관리자" : "일반회원" }}
                  </span>
                </li>
                <li class="list-group-item d-flex justify-content-between">
                  <span class="text-muted">가입일</span>
                  <strong>{{ formatDate(member.createdAt) }}</strong>
                </li>
              </ul>

              <div class="d-flex gap-2">
                <button class="btn btn-primary w-100" @click="goEdit">
                  <i class="bi bi-pencil-square me-1"></i>정보 수정
                </button>
                <button class="btn btn-outline-danger w-100" @click="confirmDelete">
                  <i class="bi bi-person-x me-1"></i>회원 탈퇴
                </button>
              </div>
            </div>

            <div v-else class="text-center py-4">
              <div class="spinner-border text-primary" role="status"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
