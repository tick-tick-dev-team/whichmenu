// 글 작성 폼 (맛집, 공지, 문의 게시판 공통)
<script setup>
import FileUpload from '@/components/FileUpload.vue';
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
      <v-text-field
        v-model="postContent"
        placeholder="내용을 작성해주세요~"
        hide-details="auto"
        label="내용작성란"
        variant="outlined"
        class="input-box"
      ></v-text-field>
      <FileUpload></FileUpload>
      <v-btn variant="outlined" @click="submitPost">글 등록하기</v-btn>
    
  </div>
</template>

<style scoped>
.post-form {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  background-color: #f8f8f8;
  border-radius: 8px;
}

.input-box {
  width: 300px; /* 고정 너비 */
  height: 100px;
  max-width: 100%; /* 반응형 */
  background-color: white;
  border-radius: 4px;
  border: black;
}
</style>
