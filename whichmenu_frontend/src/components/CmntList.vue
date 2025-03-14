// 댓글 리스트 컴포넌트 (맛집게시판, 문의게시판에서 사용)
<script setup>
import { ref } from 'vue';

defineProps({
  comments: Array, // 댓글 데이터 배열
  boardType: String, // 게시판 타입 (맛집, 문의 등)
});

const emit = defineEmits(['deleteComment', 'editComment', 'addComment']);
const newComment = ref('');
</script>

<template>
  <div class="cmnt-list">
    <div v-for="comment in comments" :key="comment.id" class="cmnt-item">
      <p>{{ comment.content }}</p>
      <span>{{ comment.author }} - {{ comment.createdAt }}</span>
      <button @click="emit('editComment', comment.id)">수정</button>
      <button @click="emit('deleteComment', comment.id)">삭제</button>
    </div>

    <textarea v-model="newComment" placeholder="댓글을 입력하세요"></textarea>
    <button @click="emit('addComment', newComment)">등록</button>
  </div>
</template>