// 파일 업로드 컴포넌트 (공통)
<script setup>
import { ref } from 'vue';

const emit = defineEmits(['files-selected']); // 부모에게 전달

const files = ref([]);
const previews = ref([]);

const handleFileUpload = (event) => {
    files.value = Array.from(event.target.files);

    // 미리보기 URL 목록 생성
    previews.value = files.value.map(file => URL.createObjectURL(file));

    emit('files-selected', files.value); // 부모에게 전달
};

const cancelUpload = () => {
    files.value = [];
    previews.value = [];
    emit('files-selected', []);
};
</script>

<template>
<v-container class="upload-container">
    <v-card class="upload-card" elevation="3">
        <v-card-title>파일 업로드</v-card-title>
        <v-card-text>
            <!-- 여러 개 미리보기 -->
            <div v-if="previews.length" class="preview-container multi">
                <v-img
                    v-for="(url, index) in previews"
                    :key="index"
                    :src="url"
                    class="preview-image"
                    contain
                />
            </div>

            <v-file-input
                label="사진 업로드"
                accept="image/*"
                multiple
                @change="handleFileUpload"
                v-model="files"
                hide-details
                class="upload-input"
            ></v-file-input>

            <v-btn v-if="previews.length" color="red" @click="cancelUpload" class="mt-3">
                업로드 취소
            </v-btn>
        </v-card-text>
    </v-card>
</v-container>
</template>

<style scoped>
.upload-container {
    max-width: 400px;
}

.upload-card {
    padding: 20px;
    text-align: center;
}

.preview-container {
    display: flex;
    justify-content: center;
    margin-bottom: 15px;
}

.preview-container.multi {
    flex-wrap: wrap;
    gap: 10px;
}
.preview-image {
    width: 100px;
    height: 100px;
    border-radius: 10px;
    object-fit: cover;
}

.upload-input {
    margin-top: 10px;
}
</style>
