<script setup>
import { defineProps, defineEmits, reactive } from 'vue';

const props = defineProps({
    modelValue: Boolean
});

const emit = defineEmits(['update:modelValue', 'submitRestaurant']);

// form 객체
const form = reactive({
    restNm: '',
    restAddr: '',
    restDtl: '',
    infoInitType: 'DAY',
    useYn: 'Y',  // 기본값 '사용'
});

// 등록 버튼 클릭 시 처리
function submit() {
    emit('submitRestaurant', form); // form 데이터를 부모에게 전달
    emit('update:modelValue', false); // 다이얼로그 닫기
}
</script>

<template>
    <v-dialog :model-value="props.modelValue" @update:model-value="val => emit('update:modelValue', val)" max-width="500px" persistent>
        <v-card>
            <v-card-title class="text-h6">식당 등록</v-card-title>
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
                <v-btn text @click="emit('update:modelValue', false)">취소</v-btn>
                <v-btn color="primary" text @click="submit">등록</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>
