<script setup>
import { defineProps, defineEmits, reactive, watch } from 'vue';
import axios from 'axios';

const props = defineProps({
    modelValue: Boolean,
    editData: Object
});

const emit = defineEmits(['update:modelValue', 'registered']);

// form 수정 객체
const form = reactive({
    restId: null,
    restNm: '',
    restAddr: '',
    restDtl: '',
    infoInitType: 'DAY',
    useYn: 'Y',
});

// form 초기화 함수
function resetForm() {
    Object.assign(form, {
        restId: null,
        restNm: '',
        restAddr: '',
        restDtl: '',
        infoInitType: 'DAY',
        useYn: 'Y',
    });
}

// editData 변경 감지 → form에 복사
watch(() => props.editData, (val) => {
    if (val) {
        Object.assign(form, JSON.parse(JSON.stringify(val))); // 깊은 복사
    } else {
        resetForm();
    }
}, { immediate: true });

// 등록 or 수정 버튼 클릭 시
async function submitRest() {
    try {
        // 유효성 검사
        if (!form.restNm) {
            alert('식당명을 입력하세요.');
            return;
        }
        if (!form.restAddr) {
            alert('식당주소를 입력하세요.');
            return;
        }
        if (!form.infoInitType) {
            alert('정보개시유형을 선택하세요.');
            return;
        }

        const url = form.restId ? '/api/rest/update' : '/api/rest/insert';
        const res = await axios.post(url, form, {
            headers: { 'Content-Type': 'application/json' },
        });

        if (res.data.result === 'success') {
            alert(form.restId ? '수정 완료' : '등록 완료');
            emit('registered');
            emit('update:modelValue', false);
            resetForm();
        } else {
            alert('처리 실패: ' + res.data.message);
        }

    } catch (e) {
        console.error(e);
        alert('서버 오류가 발생했습니다.');
    }
}
</script>

<template>
    <v-dialog
        :model-value="props.modelValue"
        @update:model-value="val => {
            emit('update:modelValue', val);
            if (!val) resetForm();
        }"
        max-width="500px"
        persistent
    >
        <v-card>
            <v-card-title class="text-h6">
                {{ form.restId ? '식당 수정' : '식당 등록' }}
            </v-card-title>
            <v-card-text>
                <v-text-field v-model="form.restNm" label="식당명" required />
                <v-text-field v-model="form.restAddr" label="주소" />
                <v-text-field v-model="form.restDtl" label="식당정보" />
                <v-select
                    v-model="form.infoInitType"
                    :items="['DAY', 'WEEK']"
                    label="정보개시유형"
                />
                <v-checkbox
                    v-model="form.useYn"
                    :true-value="'Y'"
                    :false-value="'N'"
                    label="사용 여부"
                    color="deep-purple-accent-2"
                    density="compact"
                />
            </v-card-text>
            <v-card-actions>
                <v-spacer />
                <v-btn text @click="emit('update:modelValue', false); resetForm()">닫기</v-btn>
                <v-btn color="primary" text @click="submitRest">
                    {{ form.restId ? '수정' : '등록' }}
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>
