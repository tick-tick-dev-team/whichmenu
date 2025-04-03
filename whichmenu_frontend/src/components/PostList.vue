<script setup>
defineProps({
  post: Object, // 개별 게시글 데이터
});
</script>

<template>
  <v-card class="post-item" outlined>
    <v-card-text>
      <p class="post-content">{{ post.content }}</p>
      <div class="post-meta">
        <span>{{ post.author }} - {{ post.createdAt }}</span>
      </div>

      <div class="post-actions">
        <v-btn size="x-small" variant="text" @click="emit('editComment', comment.id)">수정</v-btn>
        <v-btn size="x-small" variant="text" color="error" @click="emit('deleteComment', comment.id)">삭제</v-btn>
      </div>
      <v-divider class="my-2" />
    </v-card-text>
    <v-divider></v-divider>
  </v-card>
</template>

<style scoped>
.post-item {
  margin-bottom: 16px;
}

.post-content {
  font-size: 1rem;
  margin-bottom: 8px;
}

.post-meta {
  font-size: 0.875rem;
  color: #666;
  margin-bottom: 12px;
}

.post-actions {
  display: flex;
  justify-content: flex-start;
}
</style>
