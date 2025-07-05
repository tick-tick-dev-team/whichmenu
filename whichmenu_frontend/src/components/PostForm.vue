// 글 작성 폼 (맛집, 공지, 문의 게시판 공통)
<script setup>
import FileUpload from '@/components/FileUpload.vue';
import axios from 'axios';
import { ref, watch, onMounted } from 'vue';

const props = defineProps({
    modelValue: Boolean, // postList
    bbsType: String,
    mode: String, // 'create' 또는 'update'
    target: Object // 수정 대상 데이터 (update일 경우만 존재)
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

// 게시글 수정에 필요한 변수
const existingFiles = ref([]); // 기존 첨부파일
const deletedFileIds = ref([]); // 삭제된 파일 배열

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
    formData.append('useYn', 'Y');

    // 첨부파일들
    files.value.forEach(file => {
        formData.append('files', file);                        // 여러 파일 지원
    });

    console.log('dmdmm', formData.value);

    try { 
        
        let url = '/api/bbs/insert'; // 등록 기본

        if(props.mode === 'update'){
            url = '/api/bbs/update';
            formData.append('bbsId', props.target.bbsId); // 수정 시 ID 포함

            // 삭제된 파일 ID 목록 (String 배열 → JSON 문자열로 변환)
            if (deletedFileIds.value.length > 0) {
            formData.append('deletedFileIds', JSON.stringify(deletedFileIds.value));
            }
        }

        const res = await axios.post(url, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });

        if (res.data.result === 'success') {
            alert(`${props.mode === 'create' ? '등록' : '수정'} 성공!`);
            emit('submitPost'); // 부모에서 처리할 후속 동작
            closeDialog();       // 다이얼로그 닫기
        } else {
            alert(`${props.mode === 'create' ? '등록' : '수정'} 실패: ` + res.data.message);
        }
    } catch (e) {
        console.error(e);
        alert('에러 발생');
    }
};

// 기존 파일 삭제 시
const removeFile = (fileId) => {
  // 화면에서 제거
  existingFiles.value = existingFiles.value.filter(f => f.atchFileId !== fileId);

  // 백엔드에 전달할 삭제 ID 목록에 추가
  deletedFileIds.value.push(fileId);
};

watch(() => props.target, (newTarget) => {
  if (props.mode === 'update' && newTarget) {
    bbsTtl.value = newTarget.bbsTtl;
    bbsCn.value = newTarget.bbsCn;
    regNm.value = newTarget.regNm;
    isAnonymous.value = newTarget.regNm === '익명';
    existingFiles.value = newTarget.fileList || [];
  } else if (props.mode === 'create') {
    // create일 경우 값 초기화
    bbsTtl.value = '';
    bbsCn.value = '';
    regNm.value = '';
    isAnonymous.value = false;
  }
}, { immediate: true }); // mount 시에도 실행되게 함
</script>

<template>
    <v-dialog v-model="props.modelValue" max-width="600px" persistent>
        <v-card>
            <v-card-title> {{ props.mode === 'create' ? '게시글 등록' : '게시글 수정' }}</v-card-title>
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
                <!-- s: 파일 업로드 컴포넌트 -->
                <FileUpload
                    @files-selected="handleFilesSelected"
                    @remove-existing-file="removeFile"
                    :existingFiles="existingFiles"
                />
                
            </v-card-text>
            <v-card-actions>
                <v-spacer />
                <v-btn color="primary" @click="submitPost">
                    {{ props.mode === 'create' ? '등록' : '수정' }}
                </v-btn>
                <v-btn color="error" @click="closeDialog">닫기</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>
