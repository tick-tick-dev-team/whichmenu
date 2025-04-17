<script setup>
const { post } = defineProps({
  post: Object,
});

const emit = defineEmits(['editComment', 'deleteComment']);
console.log('Current index:', post.bbsId);
//const imageUrl = (post.bbsId === "8" || post.bbsId === "9") ? (post.bbsId === "8" ? '/img/kimbab-ramyun.png' : '/img/ohokacheu.png') : ''; 
</script>

<template>
    <v-card class="post-item" outlined>
        <v-card-text>
        <div class="post-image-wrapper" v-if="imageUrl">
            <img :src="imageUrl" alt="식단 이미지" class="post-image" />
        </div>

        <p class="post-content">{{ post.bbsCn }}</p>
        <div class="post-meta">
            <span>{{ post.regNm }} - {{ post.regDt }}</span>
        </div>

        <div class="post-actions">
            <v-btn size="x-small" variant="text" @click="emit('editComment', post.bbsId)">수정</v-btn>
            <v-btn size="x-small" variant="text" color="error" @click="emit('deleteComment', post.bbsId)">삭제</v-btn>
        </div>
        </v-card-text>
    </v-card>
</template>

<style scoped>
.post-item {
    margin-bottom: 16px;
}

.post-image-wrapper {
    display: flex;
    justify-content: center;
    margin-bottom: 12px;
}

.post-image {
    max-width: 100%;
    height: auto;
    border-radius: 8px;
}

.post-content {
    font-size: 1rem;
    margin-bottom: 8px;
    text-align: center;
}

.post-meta {
    font-size: 0.875rem;
    color: #666;
    margin-bottom: 12px;
    text-align: center;
}

.post-actions {
    display: flex;
    justify-content: flex-end;
}

</style>
