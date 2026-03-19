<script setup>
import {reactive} from "vue";
import {join} from "@/services/accountService.js";
import {useRouter} from "vue-router";

//반응형 상태
const state = reactive({
    form: {
        name: "",
        loginId: "",
        password: "",
        passwordConfirm: "",
        email: "",
        phone: "",
        address: ""
    }
});

//라우터 객체
const router = useRouter();

//회원가입 데이터 제출
const submit = async () => {
    const res = await join(state.form);

    if(res.status === 200) {
        window.alert("회원가입이 완료되었습니다.");
        await router.push("/");
    }
};
</script>


<template>
    <div class="join">
        <div class="container">

            <form class="py-5 d-flex flex-column gap-3" @submit.prevent="submit">
                <h1 class="h5 mb-3">회원가입</h1>
                <div class="form-floating">
                <input type="text" class="form-control" id="name" placeholer="이름" v-model="state.form.name">
                <label for="name">이름</label>
                </div>
                <div class="form-floating">
                <input type="text" class="form-control" id="loginId" placeholer="아이디" v-model="state.form.loginId">
                <label for="loginId">아이디</label>
                </div>
            
                <div class="form-floating">
                <input type="password" class="form-control" id="password" placeholer="비밀번호" v-model="state.form.password">
                <label for="password">비밀번호</label>
                </div>
                <div class="form-floating">
                <input type="password" class="form-control" id="passwordConfirm" placeholer="비밀번호 확인" v-model="state.form.passwordConfirm">
                <label for="passwordConfirm">비밀번호 확인</label>
                </div>
                <div class="form-floating">
                <input type="email" class="form-control" id="email" placeholer="이메일" v-model="state.form.email">
                <label for="email">이메일</label>
                </div>
                <div class="form-floating">
                <input type="text" class="form-control" id="phone" placeholer="전화번호" v-model="state.form.phone">
                <label for="phone">전화번호</label>
                </div>
                <div class="form-floating">
                <input type="text" class="form-control" id="address" placeholer="주소" v-model="state.form.address">
                <label for="address">주소</label>
                </div>

                <button type="submit" class="w-100 h6 btn py-3 btn-primary">회원가입</button>
            </form>
        </div>
    </div>
</template>

<style lang="scss" scoped>
.join > .container {
    max-width: 576px;
}
</style>