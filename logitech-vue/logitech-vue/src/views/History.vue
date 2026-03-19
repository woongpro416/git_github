<script setup>
import { reactive } from 'vue';
import { getOrders, cancelOrder } from '../services/orderService';
import { useAccountStore } from "../store/accountStore";

const accountStore = useAccountStore();

const state = reactive({
    orders: []
});

const load = async () => {
    const res = await getOrders(accountStore.loginUser.memberId);
    if (res.status === 200) {
        state.orders = res.data;
    }
};

(async function onCreated() {
    await load();
})();

// 주문 취소
const cancel = async (orderId) => {
    if (!confirm('주문을 취소하시겠습니까?')) return
    const res = await cancelOrder(orderId)
    if (res?.status === 200 || res?.status === 204) {
        alert('주문이 취소되었습니다.')
        await load()
    } else {
        alert('취소 처리 중 오류가 발생했습니다.')
    }
}
</script>

<template>
<div class="history">
    <div class="container">
        <table class="table table-bordered my-4">
            <thead>
            <tr>
                <th class="text-center">번호</th>
                <th>주문자명</th>
                <th>결제 수단</th>
                <th>결제 금액</th>
                <th>상태</th>
                <th>결제일시</th>
                <th>자세히 보기</th>
                <th>취소</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(o, idx) in state.orders" :key="o.orderId">
                <td class="text-center">{{ idx + 1 }}</td>
                <td>{{ o.name }}</td>
                <td>{{ o.payment == 'card' ? '카드' : '무통장입금' }}</td>
                <td>{{ o.amount ? o.amount.toLocaleString() : 0 }}원</td>
                <td>
                    <span :class="o.status === 'ORDER' ? 'badge bg-primary' : 'badge bg-danger'">
                        {{ o.status === 'ORDER' ? '주문완료' : '취소됨' }}
                    </span>
                </td>
                <td>{{ new Date(o.createAt).toLocaleString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit' }) }}</td>
                <td>
                    <router-link :to="`/history/${o.orderId}`">자세히 보기</router-link>
                </td>
                <td>
                    <!-- ORDER 상태일 때만 취소 버튼 표시 -->
                    <button v-if="o.status === 'ORDER'"
                        class="btn btn-sm btn-danger"
                        @click="cancel(o.orderId)">
                        취소
                    </button>
                    <span v-else class="text-muted">-</span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</template>

<style lang="scss" scoped>
</style>