<script setup>
import { ref, watch, onMounted } from 'vue';
import axios from 'axios';
import '@mdi/font/css/materialdesignicons.css'

const props = defineProps({
  modelValue: Boolean,
  mlMenuId: { // 수정 시 전달받는 메뉴 고유 ID
    type: [String, Number],
    default: null
  }
});
const emit = defineEmits(['update:modelValue', 'registered']);

// 식당 리스트 및 옵션
const centerList = ref([]);
const infoTypeOptions = [
  { title: 'URL(링크)', value: 'URL' },
  { title: '첨부파일', value: 'FILE' },
];

// 기본 폼 상태 정의
const initialForm = {
  restId: '',
  bgngDt: '',
  endDt: '',
  infoInitType: '',
  url: '',
  file: null,
  useYn: 'Y',
};

// 반응형 폼
const form = ref({ ...initialForm });

// 기존 파일명 저장 (수정 모드 시 비교용)
const originalFileName = ref('');

// 날짜 유효성 상태
const dateValid = ref(null);

// 날짜 중복 체크
async function checkDateOverlap() {
  if (form.value.bgngDt && form.value.endDt) {
    try {
      const res = await axios.get('/api/mlmenu/overlap-check', {
        params: {
          bgngDt: form.value.bgngDt,
          endDt: form.value.endDt,
          restId: form.value.restId || null,
        },
      });
      dateValid.value = res.data?.isOverLap === true;
    } catch (e) {
      console.error('중복 체크 오류', e);
      dateValid.value = null;
    }
  } else {
    dateValid.value = null;
  }
}

// infoInitType 갱신 (DAY → URL, WEEK → FILE)
const updateInfoInitTypeByRestId = (restId) => {
  const selected = centerList.value.find(r => r.restId === restId);
  if (selected) {
    form.value.infoInitType = selected.infoInitType === 'DAY' ? 'URL' : 'FILE';
  }
};

// 식당 리스트 불러오기
const fetchCenters = async () => {
  try {
    const res = await axios.get('/api/rest/list', { params: { useYn: 'Y' } });
    centerList.value = res.data;

    // 수정 모드가 아닐 때만 기본값 세팅
    if (!props.mlMenuId && centerList.value.length > 0) {
      form.value.restId = centerList.value[0].restId;
      updateInfoInitTypeByRestId(form.value.restId);
    }
  } catch (e) {
    console.error('식당 리스트 조회 실패:', e);
  }
};

// 폼 초기화
const resetForm = () => {
  form.value = { 
    ...initialForm,
    restId: centerList.value[0]?.restId || '',
    file: null
  };
  originalFileName.value = '';
  dateValid.value = null;

  if (form.value.restId) {
    updateInfoInitTypeByRestId(form.value.restId);
  }
};

// 수정 모드 - 기존 데이터 불러오기
const fetchMenuDataById = async (id) => {
  if (!id) return resetForm();
  try {
    const res = await axios.get(`/api/mlmenu/${id}`);
    const data = res.data.data;
    if (data) {
      form.value.restId = data.restId || '';
      form.value.bgngDt = data.bgngDt || '';
      form.value.endDt = data.endDt || '';
      form.value.useYn = data.useYn || 'Y';

      // DB infoInitType(DAY/WEEK)에 맞춰 URL/FILE로 변환
      form.value.infoInitType = data.infoInitType === 'DAY' ? 'URL' : 'FILE';

      if (data.infoInitType === 'DAY') {
        // URL 모드
        form.value.url = data.outsdReferUrl || '';
        form.value.file = null;
        originalFileName.value = '';
      } else if (data.infoInitType === 'WEEK') {
        // FILE 모드
        form.value.url = '';
        if (data.fileList && data.fileList.fileNm) {
          originalFileName.value = data.fileList.fileNm;
          form.value.file = null;  // 아직 새 파일 선택 안 한 상태
        } else {
          originalFileName.value = '';
          form.value.file = null;
        }
      }
    }
  } catch (e) {
    console.error('수정 데이터 조회 실패:', e);
    resetForm();
  }
};

// 파일 변경 여부 체크 함수
const isFileChanged = () => {
  // 새로 선택된 파일이 있으면 변경된 것
  if (form.value.file && form.value.file instanceof File) return true;

  // 파일 타입이 'FILE'인데도 새 파일 선택이 없으면 변경 안 된 것
  if (form.value.infoInitType === 'FILE' && !form.value.file) return false;

  // 그 외는 변경 없다고 판단
  return false;
};

// 취소 → 닫기 + 초기화
const cancel = () => {
  resetForm();
  emit('update:modelValue', false);
};

// 등록/수정
const submit = async () => {
  if (dateValid.value !== true) {
    alert('개시 기간이 유효하지 않습니다. 날짜를 확인해주세요.');
    return;
  }

  const formData = new FormData();
  formData.append('restId', form.value.restId);
  formData.append('bgngDt', form.value.bgngDt);
  formData.append('endDt', form.value.endDt);
  formData.append('infoInitType', form.value.infoInitType);
  formData.append('useYn', form.value.useYn);

  if (form.value.infoInitType === 'URL') {
    formData.append('url', form.value.url);
  } else if (form.value.infoInitType === 'FILE') {
    // 파일이 변경되었는지 체크
    const fileChanged = form.value.file && form.value.file instanceof File;
    formData.append('fileChangedYn', fileChanged ? 'Y' : 'N');

    if (fileChanged) {
      formData.append('file', form.value.file);
    }
  }

  try {
    if (props.mlMenuId) {
      await axios.put(`/api/mlmenu/z${props.mlMenuId}`, formData);
      alert('수정 완료');
    } else {
      await axios.post('/api/mlmenu/register', formData);
      alert('등록 완료');
    }

    emit('update:modelValue', false);
    emit('registered');
  } catch (e) {
    console.error('등록/수정 실패:', e);
    alert('처리에 실패했습니다.');
  }
};

// Watchers
watch(() => form.value.restId, (newVal) => {
  updateInfoInitTypeByRestId(newVal);
});
watch(() => form.value.bgngDt, (newStart) => {
  const end = form.value.endDt;
  if (!end || newStart > end) {
    form.value.endDt = newStart;
  }
});
watch(() => props.modelValue, (isOpen) => {
  if (!isOpen) {
    resetForm();
  }
});
watch(() => props.mlMenuId, (newId) => {
  if (newId) {
    fetchMenuDataById(newId);
  } else {
    resetForm();
  }
}, { immediate: true });

// 마운트 시 데이터 불러오기
onMounted(fetchCenters);
</script>

<template>
  <v-dialog
    :model-value="modelValue"
    @update:modelValue="val => emit('update:modelValue', val)"
    max-width="800px"
    persistent
  >
    <v-card>
      <v-card-title class="text-h6 font-weight-bold">
        {{ props.mlMenuId ? '식단 수정' : '식단 등록' }}
      </v-card-title>
      <v-card-text>
        <!-- 식당 선택 -->
        <v-select
          v-model="form.restId"
          :items="centerList"
          item-title="restNm"
          item-value="restId"
          label="식당 선택"
          density="comfortable"
          variant="outlined"
          :menu-props="{ openOnFocus: false }"
        />

        <!-- 개시 기간 -->
        <v-row class="mt-4" align="center">
          <v-col cols="5">
            <v-text-field
              v-model="form.bgngDt"
              label="식단 시작일"
              type="date"
              variant="outlined"
              density="comfortable"
              @change="checkDateOverlap"
            />
          </v-col>
          <v-col cols="5">
            <v-text-field
              v-model="form.endDt"
              label="식단 종료일"
              type="date"
              variant="outlined"
              density="comfortable"
              :min="form.bgngDt"
              @change="checkDateOverlap"
            />
          </v-col>
          <v-col cols="2" class="d-flex justify-center align-center" style="margin-bottom: 15px;">
            <v-icon
              :color="dateValid === true ? 'green' : dateValid === false ? 'red' : 'grey'"
              size="32"
            >
              {{ dateValid === true ? 'mdi-check-circle' : 'mdi-check-circle-outline' }}
            </v-icon>
          </v-col>
        </v-row>

        <!-- 정보유형 -->
        <v-select
          v-model="form.infoInitType"
          :items="infoTypeOptions"
          item-title="title"
          item-value="value"
          label="정보개시유형"
          variant="outlined"
          density="comfortable"
          readonly
        />

        <!-- URL or 파일 업로드 -->
        <div v-if="form.infoInitType === 'URL'" class="mt-4">
          <v-text-field
            v-model="form.url"
            label="외부 링크(URL)"
            variant="outlined"
            density="comfortable"
          />
        </div>
        <div v-else-if="form.infoInitType === 'FILE'" class="mt-4">
          <v-file-input
            v-model="form.file"
            :counter="true"
            :show-size="true"
            label="식단 이미지 업로드"
            accept="image/*"
            variant="outlined"
            density="comfortable"
            :placeholder="originalFileName || '파일을 선택하세요'"
          />
        </div>

        <!-- 사용 여부 -->
        <v-checkbox
          v-model="form.useYn"
          :true-value="'Y'"
          :false-value="'N'"
          label="사용 여부"
          color="deep-purple-accent-2"
          density="compact"
        />
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" color="grey" @click="cancel">취소</v-btn>
        <v-btn variant="text" color="blue" @click="submit">{{ props.mlMenuId ? '수정' : '등록' }}</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style scoped>
.v-card-title {
  border-bottom: 1px solid #eee;
}
.v-card-actions {
  border-top: 1px solid #eee;
}
</style>
