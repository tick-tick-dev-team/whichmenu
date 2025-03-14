// 글 작성 폼 (맛집, 공지, 문의 게시판 공통)
<script setup>
import { ref } from 'vue';

const postContent = ref('');
const emit = defineEmits(['submitPost']);

const submitPost = () => {
  if (!postContent.value.trim()) return;
  emit('submitPost', postContent.value);
  postContent.value = '';
};
</script>

<template>
  <div class="post-form">
    <textarea v-model="postContent" placeholder="글을 입력하세요"></textarea>
    <button @click="submitPost">글 등록하기</button>
  </div>
</template>
