// 게시글 항목 (각 게시판에서 공통 사용)
<script setup>
defineProps({
  post: Object, // 개별 게시글 데이터
});
</script>

<template>
  <div class="post-item">
    <p>{{ post.content }}</p>
    <span>{{ post.author }} - {{ post.createdAt }}</span>
    <button>수정</button>
    <button>삭제</button>
    <hr />
  </div>
</template>
