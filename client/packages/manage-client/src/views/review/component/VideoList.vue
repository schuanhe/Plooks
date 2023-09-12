<template>
    <!-- <video ref="playerRef" class="video" preload="auto" controls></video> -->
    <ArtplayerComNoDanmu class="video" 
        :resource="props.list"
        :vid="props.vid"
        :part="part"
        ></ArtplayerComNoDanmu>
    <div class="video-box">
        <n-scrollbar style="max-height: 300px;">
            <div class="video-item" :key="item.id" v-for="(item, index) in resources">
                <div class="item-left" @click="playVideo(index)">
                    <span>P{{ index + 1 }} {{ item.title }}</span>
                    <n-tag class="tag" :type="toTagType(item.status)">{{ toTagText(item.status) }}</n-tag>
                </div>
                <div v-show="item.status === reviewCode.WAITING_REVIEW" class="item-right">
                    <n-button text @click="reviewResource(item.id, index, reviewCode.AUDIT_APPROVED)">通过</n-button>
                    <n-button text
                        @click="reviewResource(item.id, index, reviewCode.WRONG_VIDEO_CONTENT)">不通过</n-button>
                </div>
            </div>
        </n-scrollbar>
    </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import type { ResourceType } from '@plooks/apis';
import { reviewResourceAPI } from "@plooks/apis";
import { NScrollbar, NTag, NButton, useNotification } from "naive-ui";
import { reviewCode, statusCode } from "@plooks/utils";
import { ArtplayerComNoDanmu } from '@plooks/components';

const props = defineProps<{
    list: Array<ResourceType>,
    vid: number
}>();

const part = ref(1);

const resources = ref(props.list);
const notification = useNotification();//通知

const toTagType = (state: number) => {
    switch (state) {
        case reviewCode.AUDIT_APPROVED:
            return "success";
        case reviewCode.VIDEO_PROCESSING:
        case reviewCode.SUBMIT_REVIEW:
        case reviewCode.WAITING_REVIEW:
            return "info";
        default:
            return "error";
    }
}

const toTagText = (state: number) => {
    switch (state) {
        case reviewCode.VIDEO_PROCESSING:
            return "视频处理中";
        case reviewCode.WAITING_REVIEW:
            return "等待审核";
        case reviewCode.AUDIT_APPROVED:
            return "审核通过";
        case reviewCode.WRONG_VIDEO_CONTENT:
            return "视频内容存在问题";
        case reviewCode.WRONG_VIDEO_INFO:
            return "视频信息存在问题";
        case reviewCode.PROCESSING_FAIL:
            return "视频处理失败";
        default:
            return "未知";
    }
}

//播放视频


const playVideo = (index: number) => {
    console.log("6666",index+ 1);
    part.value = index +1
}

//审核资源
const reviewResource = (id: number, index: number, status: number) => {
    reviewResourceAPI(id, status).then((res) => {
        if (res.data.code === statusCode.OK) {
            resources.value?.splice(index, 1);
        }
    }).catch((err) => {
        notification.error({
            title: '审核调用失败',
            content: "原因:" + err.response.data.msg,
            duration: 5000,
        })
    });
}
</script>

<style lang="less" scoped>
.video {
    width: 100%;
    height: 270px;
    background-color: black;
}

.video-box {
    width: 100%;
    padding-bottom: 30px;

    .video-item {
        height: 50px;
        padding: 0 10px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        border: 1px solid #efeff5;
        margin: 10px 10px;

        .item-left {
            cursor: pointer;

            .tag {
                margin-left: 10px;
            }
        }

        .item-right {
            width: 80px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
    }
}
</style>