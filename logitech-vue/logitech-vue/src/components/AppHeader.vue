<script setup>
import { storeToRefs } from "pinia";
import { useAccountStore } from "@/store/accountStore.js";
import { logout } from "@/services/accountService";
import { useRouter } from 'vue-router';


const accountStore = useAccountStore();
const { loggedIn, loginUser } = storeToRefs(accountStore);


//라우터 객체
const router = useRouter();
//로그아웃
const logoutAccount = async () => {
  const res = await logout();

  if (res.status === 200) {
    accountStore.setLoggedIn(false);
    await router.push('/');
  }
};
</script>


<template>
  <header>
    <div class="navbar navbar-dark bg-dark text-white shadow-sm">
      <div class="container">
        <router-link to="/" class="navbar-brand">
          <strong>Logitech</strong>
        </router-link>
        <div class="menus d-flex gap-3">
        <template v-if="!loggedIn">
            <router-link to="/login">  로그인 </router-link>
            <router-link to="/join">  회원가입 </router-link>
            <router-link to="/qna"> Q&A </router-link>
        </template>
        <template v-else>
            <router-link to ="/qna"> Q&A </router-link>
            <router-link to="/carts"> 장바구니 </router-link>
            <router-link to="/history"> 주문내역 </router-link>
            <router-link to="/mypage"> 마이페이지 </router-link>
            <router-link v-if="loginUser?.role === 'ADMIN'" to="/admin"> 대시보드 </router-link>
            <a @click="logoutAccount()"> 로그아웃</a>
        </template>
        </div>
      </div>
    </div>
  </header>
</template>


<style lang="scss">
    header {
        .menus {
            a { 
                cursor: pointer;
                color: #fff;
                text-decoration: none;
            }
        }
    }
</style>
