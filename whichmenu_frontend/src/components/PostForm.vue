// 글 작성 폼 (맛집, 공지, 문의 게시판 공통)
<script setup>
import FileUpload from '@/components/FileUpload.vue';
import { ref } from 'vue';

const props = defineProps({
    modelValue: Boolean,
});
const emit = defineEmits(['update:modelValue', 'submitPost']);

const postContent = ref('');

// 닫기 버튼 클릭 시
const closeDialog = () => {
    emit('update:modelValue', false);
};

// 등록 버튼 클릭 시
const submitPost = () => {
    if (!postContent.value.trim()) return;
    emit('submitPost', postContent.value);
    postContent.value = '';
    closeDialog(); // 등록 후 닫기
};
</script>

<template>
    <v-dialog v-model="props.modelValue" max-width="600px" persistent>
        <v-card>
            <v-card-title>게시글 작성</v-card-title>
            <v-card-text>
                <v-textarea
                    v-model="postContent"
                    label="내용 작성"
                    placeholder="내용을 입력해주세요"
                    auto-grow
                />
                <FileUpload />
            </v-card-text>
            <v-card-actions>
                <v-spacer />
                <v-btn color="primary" @click="submitPost">등록</v-btn>
                <v-btn color="error" @click="closeDialog">닫기</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>
