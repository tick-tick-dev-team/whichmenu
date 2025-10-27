<template>
  <div style="text-align:center; padding:40px;">
    <h2>네이버 로그인 처리중...</h2>
  </div>
</template>

<script setup>
import { onMounted } from "vue"
import { useRouter } from "vue-router"

const router = useRouter()

onMounted(async () => {
  // 🔹 URL의 해시(#) 부분에서 토큰 추출
  const hash = window.location.hash.substring(1)
  const params = new URLSearchParams(hash)
  const accessToken = params.get('access_token')

  if (!accessToken) {
    console.error("Access token not found in callback URL")
    router.push('/login')
    return
  }

  try {
    // 🔹 토큰을 백엔드로 전송
    const res = await fetch("/api/auth/naver/token", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ access_token: accessToken })
    })

    if (!res.ok) throw new Error("서버 통신 실패")

    const userData = await res.json()
    console.log("서버에서 받은 유저:", userData)

    // 🔹 로그인 완료 후 메인으로 이동
    router.push('/ml/mlMain')
  } catch (err) {
    console.error("로그인 처리 오류:", err)
    router.push('/login')
  }
})
</script>
