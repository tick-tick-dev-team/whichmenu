// 글 작성 폼 (맛집, 공지, 문의 게시판 공통)
<script setup>
import FileUpload from '@/components/FileUpload.vue';
import axios from 'axios';
import { ref, watch } from 'vue';

const props = defineProps({
    modelValue: Boolean,
    bbsType: String
});
const emit = defineEmits(['update:modelValue', 'submitPost']);

// 닫기 버튼 클릭 시
const closeDialog = () => {
    emit('update:modelValue', false);
};

// 게시글 등록에 쓸 변수
const bbsTtl = ref('');
const bbsCn = ref('');
const regNm = ref('');
const isAnonymous = ref(false); // 체크 상태로 익명 처리
const files = ref([]); // 부모 쪽에서 파일을 여러 개 받을 수 있도록 배열로

// 체크 상태가 바뀔 때 익명 처리
watch(isAnonymous, (val) => {
    if (val) {
        regNm.value = '익명';
    } else {
        regNm.value = ''; // 다시 입력받게 초기화
    }
});

// FileUpload 로부터 파일 목록을 받도록 커스텀 이벤트 핸들러 추가
const handleFilesSelected = (selectedFiles) => {
    files.value = selectedFiles;
};

// 등록 버튼 클릭 시
const submitPost = async () => {
    if (!bbsTtl.value || !bbsCn.value) {
        alert('제목과 내용을 입력해주세요.');
        return;
    }

    if (!regNm.value && !isAnonymous.value) {
        alert('별명을 입력해주세요.');
        return;
    }

    const formData = new FormData();

    // 게시글 정보
    formData.append('bbsTtl', bbsTtl.value);                // 게시글 제목
    formData.append('bbsCn', bbsCn.value);               // 게시글 내용
    formData.append('bbsType', props.bbsType);                 // 게시판 유형 (예: 'R')
    formData.append('regNm', regNm.value);                     // 등록자
    formData.append('rlsYn', 'Y');         // 공개 여부

    // 첨부파일들
    files.value.forEach(file => {
        formData.append('files', file);                        // 여러 파일 지원
    });

    console.log('dmdmm', formData.value);

    try {
        const res = await axios.post('/api/bbs/insert', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });

        if (res.data.result === 'success') {
            alert('등록 성공!');
            emit('submitPost'); // 부모에서 처리할 후속 동작
            closeDialog();       // 다이얼로그 닫기
        } else {
            alert('등록 실패: ' + res.data.message);
        }
    } catch (e) {
        console.error(e);
        alert('에러 발생');
    }
};
</script>

<template>
    <v-dialog v-model="props.modelValue" max-width="600px" persistent>
        <v-card>
            <v-card-title>게시글 작성</v-card-title>
            <v-card-text>
                <v-text-field
                    v-model="bbsTtl"
                    label="제목"
                    placeholder="제목을 입력해주세요"
                    variant="outlined"
                    class="mb-3"
                />
                <v-textarea
                    v-model="bbsCn"
                    label="내용 작성"
                    placeholder="내용을 입력해주세요"
                    auto-grow
                    variant="outlined"
                />
                <!-- 익명 체크박스 -->
                <v-checkbox
                    v-model="isAnonymous"
                    label="익명으로 작성하기"
                    color="primary"
                    class="mb-3"
                ></v-checkbox>

                <!-- 익명이 아닐 경우에만 입력창 보이기 -->
                <v-text-field
                    v-if="!isAnonymous"
                    v-model="regNm"
                    label="별명"
                    placeholder="별명을 입력해주세요"
                    variant="outlined"
                    class="mb-3"
                />
                <!-- 파일 업로드 컴포넌트 -->
                <FileUpload @files-selected="handleFilesSelected" />
            </v-card-text>
            <v-card-actions>
                <v-spacer />
                <v-btn color="primary" @click="submitPost">등록</v-btn>
                <v-btn color="error" @click="closeDialog">닫기</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>
