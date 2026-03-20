import { defineStore } from 'pinia'

export const useAccountStore = defineStore('account', {
    state: () => ({
        checked: false,
        loggedIn: false,
        loginUser: null,
    }),
    actions: {
        setChecked(val) { this.checked = val },
        setLoggedIn(val) { this.loggedIn = val },
        setLoginUser(val) { this.loginUser = val },
    }
})