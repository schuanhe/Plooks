<template>
    <div class="avatar-cropper">
        <div class="card-left">
            <pic-cropper ref="cropperRef" :key="props.file?.name" :file="props.file" :minHeight="60" :minWidth="60"
                @change="imgChange"></pic-cropper>
        </div>
        <div class="card-right">
            <span class="title">头像预览</span>
            <img :src="previewURL" alt="图像预览" />
            <n-button type="primary" @click="uploadAvatar">裁剪并上传</n-button>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import PicCropper from './PicCropper.vue';
import { NButton } from 'naive-ui';
import { uploadFileAPI } from "@plooks/apis";

const props = defineProps<{
    file?: File // 用于接收文件的属性
}>();

const cropperRef = ref<InstanceType<typeof PicCropper> | null>(null); // 用于引用PicCropper组件的实例

const previewURL = ref(""); // 用于预览图片的URL
const imgChange = (url: string) => {
    previewURL.value = url;
}

const emits = defineEmits(["stateChange","initUrl"]) // 用于定义自定义事件
const uploadAvatar = async () => { // 用于上传头像
    emits("initUrl")
    if (cropperRef.value) {
        const file = await cropperRef.value.getFile(); // 获取裁剪后的文件
        await uploadFileAPI({
            name: "image",
            action: "v1/upload/image",
            file: file,
            onProgress: () => { },
            onError: () => {
                emits("stateChange", "error") // 触发自定义事件，传递错误状态
            },
            onFinish: (data?: any) => {
                emits("stateChange", "success", data) // 触发自定义事件，传递成功状态和数据
            },
        })
    }
}

</script>

<style lang="less" scoped>
.avatar-cropper {
    box-sizing: border-box;
    padding: 10px;
    display: flex;
    height: 280px;

    .card-left {
        width: 260px;
        text-align: center
    }

    .card-right {
        width: 200px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        flex-direction: column;

        img {
            width: 120px;
            height: 120px;
        }
    }
}
</style>