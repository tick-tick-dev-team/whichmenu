// 파일 업로드 컴포넌트 (공통)
<script setup>
import { ref } from 'vue';

const file = ref(null);
const previewUrl = ref(null);

const handleFileUpload = (event) => {
  const uploadedFile = event.target.files[0];
  if (uploadedFile) {
    file.value = uploadedFile;
    previewUrl.value = URL.createObjectURL(uploadedFile);
  }
};

const cancelUpload = () => {
  file.value = null;
  previewUrl.value = null;
};
</script>

<template>
  <v-container class="upload-container">
    <v-card class="upload-card" elevation="3">
      <v-card-title>파일 업로드</v-card-title>
      <v-card-text>
        <!-- 파일 미리보기 -->
        <div v-if="previewUrl" class="preview-container">
          <v-img :src="previewUrl" class="preview-image" contain></v-img>
        </div>

        <!-- 업로드 버튼 -->
        <v-file-input
          label="사진 업로드"
          accept="image/*"
          @change="handleFileUpload"
          hide-details
          class="upload-input"
        ></v-file-input>

        <!-- 업로드 취소 버튼 (이미지 있을 때만 보이기) -->
        <v-btn v-if="file" color="red" @click="cancelUpload" class="mt-3">
          업로드 취소
        </v-btn>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<style scoped>
.upload-container {
  max-width: 400px;
  margin: auto;
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
