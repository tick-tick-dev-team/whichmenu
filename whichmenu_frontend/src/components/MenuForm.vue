<script setup>
import { ref, watch, onMounted } from 'vue';
import axios from 'axios';
import '@mdi/font/css/materialdesignicons.css'

const props = defineProps({ modelValue: Boolean });
const emit = defineEmits(['update:modelValue', 'registered']);

// 식당 리스트 및 옵션
const centerList = ref([]);
const infoTypeOptions = [
  { title: 'URL(링크)', value: 'URL' },
  { title: '첨부파일'  , value: 'FILE' },
];

// 날짜 유효성 상태
const dateValid = ref(null);

// 폼 데이터
const form = ref({
  restId: '',
  bgngDt: '',
  endDt: '',
  infoInitType: '',
  url: '',
  file: null,
  useYn: 'Y',
});

// 날짜 중복 체크 함수 (예: 백엔드 호출 or 로컬 검증)
async function checkDateOverlap() {
  if (form.value.bgngDt && form.value.endDt) {
    try {
      // 예: Axios로 중복 확인 API 호출
      console.log('typeof form.bgngDt:', typeof form.value.bgngDt);
      console.log(form.value.endDt);
      console.log(form.value.restId);
      const res = await axios.get('/api/mlmenu/overlap-check', {
        params: {
          bgngDt: form.value.bgngDt,
          endDt: form.value.endDt,
          restId: form.value.restId || null, // 수정일 경우 본인 제외
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

// 🔹 식당 리스트 불러오기 + 초기 선택
const fetchCenters = async () => {
  try {
    const res = await axios.get('/api/rest/list', {
      params: { useYn: 'Y' },
    });
    centerList.value = res.data;

    if (centerList.value.length > 0) {
      // 첫 번째 식당 자동 선택
      form.value.restId = centerList.value[0].restId;

      // infoInitType 반영
      updateInfoInitTypeByRestId(form.value.restId);
    }
  } catch (e) {
    console.error('식당 리스트 조회 실패:', e);
  }
};

// 🔹 식당 선택 시 infoInitType 동기화
const updateInfoInitTypeByRestId = (restId) => {
  const selected = centerList.value.find(r => r.restId === restId);
  if (selected) {
    form.value.infoInitType = selected.infoInitType === 'DAY' ? 'URL' : 'FILE';
  }
};

watch(() => form.value.restId, (newVal) => {
  updateInfoInitTypeByRestId(newVal);
});

onMounted(fetchCenters);


// 취소
const cancel = () => {
  emit('update:modelValue', false);
};

// 등록
const submit = async () => {

  if (dateValid.value !== true) {
    alert('개시 기간이 유효하지 않습니다. 날짜를 확인해주세요.');
    return;
  }
  try {
    const formData = new FormData();
    formData.append('restId'      , form.value.restId);
    formData.append('bgngDt'      , form.value.bgngDt);
    formData.append('endDt'       , form.value.endDt);
    formData.append('infoInitType', form.value.infoInitType);
    formData.append('useYn'       , form.value.useYn);
    if (form.value.infoInitType === 'URL') {
      formData.append('url'       , form.value.url);
    } else if (form.value.infoInitType === 'FILE' && form.value.file) {
      formData.append('file'      , form.value.file);
    }

    await axios.post('/api/mlmenu/register', formData);
    alert('등록 완료');
    emit('update:modelValue', false);
    emit('registered');
  } catch (e) {
    console.error('등록 실패:', e);
    alert('등록에 실패했습니다.');
  }
};
</script>

<template>
  <v-dialog
  :model-value="modelValue"
  @update:modelValue="val => emit('update:modelValue', val)"
  max-width="800px"
  persistent
>
    <v-card>
      <v-card-title class="text-h6 font-weight-bold">식단 등록</v-card-title>
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
        ></v-select>

        <!-- 개시 기간 -->
        <v-row class="mt-4" align="center">
          <v-col cols="5" class="d-flex align-center">
            <v-text-field
              v-model="form.bgngDt"
              label="식단 시작일"
              type="date"
              variant="outlined"
              density="comfortable"
              class="flex-grow-1"
              @change="checkDateOverlap"
            ></v-text-field>
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
            ></v-text-field>
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
        ></v-select>

        <!-- URL or 파일 업로드 -->
        <div v-if="form.infoInitType === 'URL'" class="mt-4">
          <v-text-field
            v-model="form.url"
            label="외부 링크(URL)"
            variant="outlined"
            density="comfortable"
          ></v-text-field>
        </div>

        <div v-else-if="form.infoInitType === 'FILE'" class="mt-4">
          <v-file-input
            v-model="form.file"
            label="식단 이미지 업로드"
            accept="image/*"
            show-size
            variant="outlined"
            density="comfortable"
          ></v-file-input>
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

      <!-- 버튼 -->
      <v-card-actions class="justify-end">
        <v-btn variant="text" color="grey" @click="cancel">취소</v-btn>
        <v-btn variant="text" color="blue" @click="submit">등록</v-btn>
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