<script setup>
import { computed, reactive } from "vue";
import { addOrder } from "@/services/orderService";
import { useRouter } from "vue-router";
import { useCartStore } from "../store/cartStore";
import { storeToRefs } from "pinia";
import { useAccountStore } from "../store/accountStore";

//cart 상품 가져오기
const cartStore = useCartStore();
const { orderItems } = storeToRefs(cartStore);
const accountStore = useAccountStore();

//라우터 객체
const router = useRouter();

//반응형 상태
const state = reactive({
  form: {
    name: "",
    address: "",
    payment: "card",
    cardNumber: "",
  },
});

console.log("orderItems.value:", orderItems.value);
//최종 결제 금액
const computedTotalPrice = computed(() => {
  let result = 0;
  orderItems.value.forEach((i) => {
    if (i.item) result += i.item.price;
  });
  return result;
});

//주문 데이터 제출
const submit = async () => {
  if (state.form.payment !== "card") {
    state.form.cardNumber = "";
  }
  state.form.itemIds = orderItems.value.filter((i) => i.item).map((i) => i.item.itemId);
  state.form.memberId = accountStore.loginUser.memberId;
  state.form.amount = computedTotalPrice.value;

  console.log("주문 데이터:", state.form); 
  console.log("itemIds:", state.form.itemIds);

  const res = await addOrder(state.form);

  if (res.status === 200) {
    const messages = ["주문이 완료되었습니다."];

    if (state.form.payment === "bank") {
      const price = computedTotalPrice.value.toLocaleString();
      messages.push(`한국은행 123-45678-90 계좌로 ${price}원을 입금해주시기 바랍니다.`);
    }
    window.alert(messages.join("\n"));
    await router.push("/");
  }
};
</script>

<template>
  <form class="order-form" @submit.prevent="submit">
    <div class="container">
      <div class="py-5 text-center">
        <div class="h4">
          <b>주문하기</b>
        </div>
        <p class="h6 font-lg mt-3">주문 내역을 확인하시고 주문하기 버튼을 클릭해주세요</p>
      </div>
      <div class="row g-5">
        <div class="col-md-5 col-lg-4 order-md-last">
          <div class="mb-3">
            <span class="h5 mb-3 align-middle me-2">
              <b>구입 목록</b>
            </span>
            <span class="badge bg-primary rounded-pill align-middle">
              {{ orderItems.length }}</span
            >
          </div>
          <ul class="items list-group mb-3">
            <li
              class="p-3 list-group-item d-flex justify-content-between"
              v-for="i in orderItems"
              :key="i.cartId"
            >
              <template v-if="i.item">
                <!-- ✅ 이렇게 분리 -->
                <div>
                  <h6 class="my-0">{{ i.item.itemName }}</h6>
                </div>
                <span class="text-muted"> {{ i.item.price.toLocaleString() }}원 </span>
              </template>
            </li>
          </ul>
          <div class="border p-4 bg-light h5 rounded text-center total-price">
            <span>합계</span>
            <b>{{ computedTotalPrice.toLocaleString() }}원</b>
          </div>
          <button type="submit" class="w-100 btn btn-primary py-4 mt-4">결제하기</button>
        </div>
        <div class="col-md-7 col-lg-8">
          <div class="h5 mb-3">
            <b>주문자 정보</b>
          </div>
          <div class="row g-3">
            <div class="col-12">
              <label for="name" class="form-label"> 이름 </label>
              <input
                type="text"
                id="name"
                class="form-control p-3"
                v-model="state.form.memberName"
              />
            </div>
            <div class="col-12 pt-1">
              <label for="address" class="form-label"> 주소</label>
              <input
                type="text"
                id="address"
                class="form-control p-3"
                v-model="state.form.address"
              />
            </div>
          </div>
          <div class="h5 mt-5 mb-3">
            <b>결제 수단</b>
          </div>
          <div class="my-3">
            <div class="form-check">
              <input
                type="radio"
                id="card"
                name="paymentMethod"
                class="form-check-input"
                value="card"
                v-model="state.form.payment"
              />
              <label for="card" class="form-check-label"> 신용카드 </label>
            </div>
            <div class="form-check">
              <input
                type="radio"
                id="bank"
                name="paymentMethod"
                class="form-check-input"
                value="bank"
                v-model="state.form.payment"
              />
              <label for="bank" class="form-check-label"> 무통장입금 </label>
            </div>
          </div>
          <div class="pt-1" v-if="state.form.payment === 'card'">
            <label for="cardNum" class="form-label"> 카드번호</label>
            <input
              type="text"
              id="cardNum"
              class="form-control p-3"
              v-model="state.form.cardNumber"
            />
          </div>
        </div>
      </div>
    </div>
  </form>
</template>

<style lang="scss" scoped></style>
