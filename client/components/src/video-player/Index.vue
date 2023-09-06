<template>
    <div class="player-container">
        <!-- <w-player class="player" :key="playerKey" :options="options"></w-player> -->
        <artplayer-com class="player" 
        :resource="props.resources"
        :vid="props.vid"
        :part="props.part"
        @getInstance="getInstance"></artplayer-com>

    </div>
</template>

<script setup lang="ts">
import { onBeforeMount, ref, onBeforeUnmount } from 'vue';
import dashjs from "dashjs";
import ArtplayerCom from '../video-artplayer/ArtplayerCom.vue';
import type { ResourceType, AddHistoryType, AddDanmukuType,DanmukuType } from '@plooks/apis';
import { sendDanmukuAPI, getDanmukuAPI } from '@plooks/apis';
import { addHistoryAPI, getHistoryProgressAPI } from '@plooks/apis';
import type { OptionsType, QualityType, OptionType } from '../types/player';
import { getResourceUrl, statusCode } from '@plooks/utils';
import Artplayer from 'artplayer';

const props = withDefaults(defineProps<{
    vid: number, // 视频id
    theme: string, // 主题
    mobile?: boolean, // 是否为移动端
    resources: Array<ResourceType>, // 视频资源
    part: number, // 当前播放的集数
}>(), {
    part: 1,
    mobile: false
})

const instance = ref<Artplayer | null>(null)

// 更新instance
const getInstance = (art: Artplayer) => {
    instance.value = art;    
}




// 上传历史记录
const uploadHistory = async () => {
    const history: AddHistoryType = {
        vid: props.vid,
        part: props.part,
        time: instance.value?.currentTime || 0,
    }
    await addHistoryAPI(history);
}

//
onBeforeUnmount(() => {
  // 执行你的上传历史数据操作或清理操作
  uploadHistory();
});
</script>

<style lang="less" scoped>
.player-container {
    height: 0;
    width: 100%;
    padding-bottom: 56.25%;
    position: relative;
    margin-bottom: 40px;

    .player {
        width: 100%;
        height: 100%;
        position: absolute;
        background-color: black;
    }
}
</style>