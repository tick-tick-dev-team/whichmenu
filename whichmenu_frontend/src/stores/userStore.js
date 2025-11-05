import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  // 상태(state)
  const user = ref(null)
  const isLoggedIn = ref(false)

  // 로그인 정보 저장
  function setUser(userData) {
    user.value = userData
    isLoggedIn.value = true

    // 세션스토리지에도 같이 저장 (새로고침 유지용)
    sessionStorage.setItem('user', JSON.stringify(userData))
  }

  // 세션에서 복원 (새로고침 후)
  function loadUser() {
    const stored = sessionStorage.getItem('user')
    if (stored) {
      user.value = JSON.parse(stored)
      isLoggedIn.value = true
    }
  }

  // 로그아웃
  function logout() {
    user.value = null
    isLoggedIn.value = false
    sessionStorage.removeItem('user')
  }

  return { user, isLoggedIn, setUser, loadUser, logout }
})
