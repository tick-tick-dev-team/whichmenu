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
    <!-- 댓글 목록 -->
    <!-- <div v-for="comment in comments" :key="comment.id" class="cmnt-item">
      <div class="cmnt-text">
        <p class="cmnt-content">{{ comment.content }}</p>
        <span class="cmnt-meta">{{ comment.author }} - {{ comment.createdAt }}</span>
      </div>
      <div class="cmnt-actions">
        <v-btn size="x-small" variant="text" @click="emit('editComment', comment.id)">수정</v-btn>
        <v-btn size="x-small" variant="text" color="error" @click="emit('deleteComment', comment.id)">삭제</v-btn>
      </div>
      <v-divider class="my-2" />
    </div> -->

    <!-- 댓글 입력 영역: 가로 정렬 -->
    <div class="cmnt-input-row">
      <v-textarea
        v-model="newComment"
        label="댓글을 입력하세요"
        variant="outlined"
        density="compact"
        rows="2"
        hide-details
        class="comment-input"
      />
      <v-btn
        variant="outlined"
        color="primary"
        size="small"
        class="submit-btn"
        @click="emit('addComment', newComment)"
      >
        등록
      </v-btn>
    </div>
  </div>
</template>

<style scoped>
.cmnt-list {
  margin-top: 12px;
}

.cmnt-item {
  margin-bottom: 8px;
}

.cmnt-content {
  font-size: 0.95rem;
  margin: 0;
}

.cmnt-meta {
  font-size: 0.8rem;
  color: #666;
}

.cmnt-actions {
  display: flex;
  gap: 8px;
}

.cmnt-input-row {
  display: flex;
  align-items: flex-end;
  gap: 8px;
  margin-top: 8px;
  margin-bottom: 16px;
}

.comment-input {
  flex: 1;
}

.submit-btn {
  height: 40px; /* 댓글창 높이랑 맞추기 */
  min-width: 64px;
}
</style>