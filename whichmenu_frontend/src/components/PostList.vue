<script setup>
const { post } = defineProps({
  post: Object,
});

const emit = defineEmits(['editComment', 'deleteComment', 'editPost']);

// 수정 버튼 클릭 시 emit으로 mode, bbsId 전달 (자식은 직접 처리 안 함)
function editPost(bbsId, status) {
    if(status == "editPost"){
        emit('editPost', {
            mode: 'update',
            bbsId: bbsId
        });
    } else if (status == "deletePost"){
        emit('editPost', {
            mode: 'delete',
            bbsId: bbsId
        });
    }
}

</script>

<template>
    <v-card class="post-item" outlined>
        <v-card-text>
        <h2>{{ post.bbsTtl }}</h2>
        <br><hr><br>
        <div class="post-image-wrapper" v-if="post.fileList?.length != 0">
            <!--사진이 한장일때-->
            <v-img
                v-if="post.fileList?.length == 1"
                :src="`http://localhost:8080/atch/${post.fileList[0].filePath.split(/[/\\]/).at(-1)}`"
                max-width="100%"
                max-height="300"
                class="mx-auto"
                contain
                rounded
            />

            <!--사진이 여러장일때 캐러셀-->
            <v-carousel
                v-else
                hide-delimiters
                show-arrows="hover"
                height="200"
                class="post-carousel"
            >
                <v-carousel-item
                    v-for="file in post.fileList"
                    :key="file.atchFileId"
                >
                    <img
                        :src="`http://localhost:8080/atch/${file.filePath.split(/[/\\]/).at(-1)}`"
                        alt="첨부 이미지"
                        class="post-image"
                    />
                </v-carousel-item>
            </v-carousel>

        </div>

        <p class="post-content">{{ post.bbsCn }}</p>
        <div class="post-meta">
            <span>{{ post.regNm }} - {{ post.regDt }}</span>
        </div>

        <div class="post-actions">
            <v-btn size="x-small" variant="text" @click="editPost(post.bbsId, 'editPost')">수정</v-btn>
            <v-btn size="x-small" variant="text" color="error" @click="editPost(post.bbsId, 'deletePost')">삭제</v-btn>
        </div>
        <br><hr><br>
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
  width: 100%;
  max-width: 1200px;
  min-width: 360px;
  height: 200px;
  object-fit: contain;
  border-radius: 8px;
}


.post-carousel {
  width: 100%;
  max-width: 1200px;
  min-width: 360px;
  margin: 0 auto 12px auto;
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
