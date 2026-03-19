import { defineStore } from 'pinia'
import api from "../api/axios"


export const useMemberStore = defineStore("member", {
    state: () => ({
        member: null,
        members: []
    }),

    //axios 동작 기술
    actions: {
        async signup() {},
        async getMember() {},
        async updateMember() {},
        async changePassword() {},
        async deleteMember() {}
    }
});