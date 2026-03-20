import api from "@/api/axios.js";

//주문 삽입
export const addOrder = (args) => {
    return api.post("/orders/new", args).catch(e => e.response);
};

// 주문 목록 조회 - memberId 필요
export const getOrders = (memberId) => {
    return api.get(`/orders/list/${memberId}`).catch(e => e.response);
};

// 주문 상세 조회
export const getOrder = (orderId) => {
    return api.get(`/orders/detail/${orderId}`).catch(e => e.response);
};

//주문 취소
export const cancelOrder = (orderId) => {
    return api.delete(`/orders/delete/${orderId}`).catch(e => e.response)
}