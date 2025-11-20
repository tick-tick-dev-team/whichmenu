<template>
  <div style="text-align:center; padding:40px;">
    <h2>네이버 로그인 처리중...</h2>
  </div>
</template>

<script setup>
import { onMounted } from "vue"
import { useRouter } from "vue-router"
import { useUserStore } from '@/stores/userStore'
const userStore = useUserStore()
const router = useRouter()

onMounted(async () => {
  try {
    // 🔹 URL 쿼리에서 code와 state 추출
    const query = new URLSearchParams(window.location.search)
    const code = query.get("code")
    const state = query.get("state")

    if (!code) {
      console.error("Authorization code not found")
      router.push("/login")
      return
    }

    // 🔹 백엔드로 code 전달
    // 카카오 간편로그인 호출할때 (참고)
    // body: JSON.stringify({ code, provider: "kakao", redirect_uri: "http://localhost:3000/oauth/kakao/callback???" })

    const res = await fetch("/api/oauth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ code, state, provider : "naver" })
    })

    if (!res.ok) throw new Error("서버 통신 실패")

    const userData = await res.json()
    console.log("서버에서 받은 유저:", userData)

    // 🔹 로그인 성공 > userStore에 저장
    userStore.setUser({
      id       : userData.usrSn,
      name     : userData.name,
      email    : userData.email,
      prov     : userData.prov,
      nickNm   : userData.nickNm,
      usrRole  : userData.usrRole
    })

    // 🔹 로그인 완료 후 메인으로 이동
    router.push('/ml/mlMain')
  } catch (err) {
    console.error("로그인 처리 오류:", err)
    router.push('/login')
  }
})
</script>
