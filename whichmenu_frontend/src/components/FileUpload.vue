// 파일 업로드 컴포넌트 (공통)
<script setup>
import { ref } from 'vue';

// 부모에게 받음 (기존 파일 존재 여부 확인을 위해)
const props = defineProps({
    existingFiles: Object // 수정 대상 데이터 (update일 경우만 존재)
});

const emit = defineEmits(['files-selected', 'remove-existing-file']); // 부모에게 전달

const files = ref([]);
const previews = ref([]);

const handleFileUpload = (event) => {
    files.value = Array.from(event.target.files);

    // 미리보기 url 생성
    previews.value = files.value.map(file => ({
        file,
        url: URL.createObjectURL(file),
    }));

    emit('files-selected', files.value);
};

const cancelUpload = () => {
    files.value = [];
    previews.value = [];
    emit('files-selected', []);
};


// 삭제 버튼에서 호출할 함수 추가
const removeExistingFile = (fileId) => {
  emit('remove-existing-file', fileId);
};

// 미리보기 하나 삭제 시
const removeSinglePreview = (index) => {
  files.value.splice(index, 1);
  previews.value.splice(index, 1);
};
</script>

<template>
<!-- 기존 첨부파일 목록 -->
<div v-if="existingFiles.length > 0" class="existing-files mb-4">
  <h4>기존 첨부파일</h4>
  <div class="preview-container multi">
    <div
      v-for="file in existingFiles"
      :key="file.atchFileId"
      class="existing-file-item"
    >
      <a
        :href="`http://localhost:8080/atch/${file.filePath.split(/[/\\]/).at(-1)}`"
        target="_blank"
        rel="noopener"
      >
        <v-img
          :src="`http://localhost:8080/atch/${file.filePath.split(/[/\\]/).at(-1)}`"
          class="existing-preview-image"
          contain
        />
      </a>
      <label>{{ file.fileNm }}</label>
      <v-btn
        icon
        size="x-small"
        variant="text"
        color="error"
        @click="removeExistingFile(file.atchFileId)"
      >
        <v-icon size="x-small">mdi-delete</v-icon>
      </v-btn>
    </div>
  </div>
</div>
<!-- e: 기존 첨부파일 목록 -->

<v-container class="upload-container">
    <v-card class="upload-card" elevation="3">
        <v-card-title>파일 업로드</v-card-title>
        <v-card-text>
            <!-- 여러 개 미리보기 -->
            <!-- 미리보기 -->
            <div v-if="previews.length" class="preview-container multi">
                <div
                    v-for="(item, index) in previews"
                    :key="index"
                    class="preview-wrapper"
                >
                    <v-img
                    :src="item.url"
                    class="preview-image"
                    contain
                    />
                    <v-btn
                    icon
                    size="x-small"
                    variant="text"
                    color="error"
                    class="preview-remove"
                    @click="removeSinglePreview(index)"
                    >
                    <v-icon size="x-small">mdi-delete</v-icon>
                    </v-btn>
            </div>
            </div>

            <!-- 파일 업로드 input -->
            <v-file-input
            label="사진 업로드"
            accept="image/*"
            multiple
            @change="handleFileUpload"
            v-model="files"
            hide-details
            class="upload-input"
            />


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

.existing-file-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 5px;
}

.existing-preview-image {
  width: 80px;
  height: 80px;
  border-radius: 10px;
  object-fit: cover;
}

</style>
