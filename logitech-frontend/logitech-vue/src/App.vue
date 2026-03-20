<script setup>
import AppHeader from "@/components/AppHeader.vue";
import AppFooter from "@/components/AppFooter.vue";
import { useAccountStore } from "@/store/accountStore.js";
import { watch } from "vue";
import { useRoute } from "vue-router";
import { check } from "@/services/accountService.js";
import ChatBot from "@/components/ChatBot.vue"

//계정 스토어
const accountStore = useAccountStore();

//라우트 객체
const route = useRoute();

//로그인 여부 확인
const checkAccount = async () => {
  const res = await check();

  if (res && res.status === 200 && res.data) {
    accountStore.setChecked(true);
    accountStore.setLoggedIn(true);
    accountStore.setLoginUser(res.data);
  } else {
    accountStore.setChecked(true);
    accountStore.setLoggedIn(false);
  }
};

//커스텀 생성 훅
(async function onCreated() {
  await checkAccount();
})();

//URL이 바뀔때마다 로그인 여부를 확인
watch(
  () => route.path,
  () => {
    checkAccount();
  }
);
</script>

<template>
  <template v-if="accountStore.checked">
    <AppHeader />
    <main>
      <router-view>
      </router-view>
    </main>
    <AppFooter />
    <ChatBot />
  </template>
</template>
