import api from "@/api/axios.js";

//장바구니 상품 목록 조회
export const getCarts = () => {
    return api.get("/carts/list").catch(e => e.response);
};

//장바구니 상품 추가
export const addItem = (itemId) => {
    return api.post("/carts/add", { itemId }).catch(e => e.response);
};

//장바구니에서 상품 삭제
export const deleteItem = (cartId) => {
    return api.delete(`/carts/delete/${cartId}`).catch(e => e.response);
};