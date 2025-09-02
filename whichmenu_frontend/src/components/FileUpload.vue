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

const MAX_SIZE_MB = 10;
const MAX_SIZE_BYTES = MAX_SIZE_MB * 1024 * 1024;

const ALLOWED_EXTENSIONS = ['jpg', 'jpeg', 'png', 'gif', 'webp']; 

// 이전 선택 값 제거를 위해
const fileInputRef = ref();

const handleFileUpload = (event) => {
  const selectedFiles = Array.from(event.target.files);

  // 파일 input 리셋 (이걸 안 하면 이전 선택값이 남아있음!)
  if (fileInputRef.value) {
    fileInputRef.value.reset(); // Vuetify 제공 메서드
  }

  // HEIC/HEIF 감지 (iPhone 전용)
  const heicFile = selectedFiles.find(file => {
    const ext = file.name.split('.').pop().toLowerCase();
    return ext === 'heic' || ext === 'heif';
  });

  if (heicFile) {
    alert(
      `파일: ${heicFile.name} 은(는) iPhone에서 생성된 HEIC/HEIF 포맷입니다.\n\n` +
      `해당 형식은 웹 미리보기 및 일부 브라우저에서 호환되지 않을 수 있습니다.\n` +
      `iPhone 설정 > 카메라 > 포맷 > '가장 호환되는 형식(JPG)' 으로 변경을 권장합니다.`
    );
    return;
  }

  // 일반 확장자 허용 검사
  const invalidFile = selectedFiles.find(file => {
    const ext = file.name.split('.').pop().toLowerCase();
    return !ALLOWED_EXTENSIONS.includes(ext);
  });

  if (invalidFile) {
    alert(`허용되지 않은 파일 형식입니다: ${invalidFile.name}\n\n허용된 확장자: ${ALLOWED_EXTENSIONS.join(', ')}`);
    return;
  }

  // 파일 크기 체크
  const tooLarge = selectedFiles.some(file => file.size > MAX_SIZE_BYTES);
  if (tooLarge) {
    alert(`파일당 최대 ${MAX_SIZE_MB}MB까지만 업로드 가능합니다.`);
    return;
  }

  const existingCount = props.existingFiles?.length || 0;
  const totalCount = existingCount + files.value.length + selectedFiles.length;

  if (totalCount > 3) {
    alert(`파일은 최대 3개까지 등록할 수 있습니다.\n(현재 첨부된 파일 ${existingCount}개, 기존 선택 ${files.value.length}개, 새로 선택한 파일 ${selectedFiles.length}개)`);
    return;
  }

  // 기존 파일 + 새로 선택된 파일을 합쳐야 함
  files.value = [...files.value, ...selectedFiles];

  // previews도 같이 추가
  previews.value = [
    ...previews.value,
    ...selectedFiles.map(file => ({
      file,
      url: URL.createObjectURL(file),
    })),
  ];

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
  const removed = previews.value.splice(index, 1)[0];
  URL.revokeObjectURL(removed.url);
  
  // 파일 객체로 정확히 제거
  files.value = files.value.filter(f => f !== removed.file);

  // 꼭 emit도 해줘야 백엔드 전송값이 맞아짐
  emit('files-selected', [...files.value]);
};

// 파일 용량 제한
const fileSizeLimit = (value) => {
  if (!value) return true;

  const maxSizeInMB = 10; // MB 단위
  const maxSize = maxSizeInMB * 1024 * 1024;

  const filesArray = Array.isArray(value) ? value : [value];
  const tooLarge = filesArray.some(file => file.size > maxSize);

  return tooLarge ? `파일당 최대 ${maxSizeInMB}MB까지만 업로드 가능합니다.` : true;
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
            hide-details
            hide-input 
            show-size
            chips 
            class="upload-input"
            ref="fileInputRef"
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
