import { defineStore } from "pinia";

export const useCartStore = defineStore("carts", {
    state: () => ({
        orderItems: [],
    }),
    actions: {
        setOrderItems(items) {
            this.orderItems = items;
        }
    }
});