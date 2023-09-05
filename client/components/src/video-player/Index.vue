<template>
    <div class="player-container">
        <!-- <w-player class="player" :key="playerKey" :options="options"></w-player> -->
        <artplayer-com class="player" :danmuku="danmuku" :option.sync="option" @getInstance="getInstance"></artplayer-com>

    </div>
</template>

<script setup lang="ts">
import { onBeforeMount, ref, watch, reactive } from 'vue';
import dashjs from "dashjs";
import ArtplayerCom from '../video-artplayer/ArtplayerCom.vue';
import type { ResourceType, AddHistoryType, AddDanmukuType,DanmukuType } from '@plooks/apis';
import { sendDanmukuAPI, getDanmukuAPI } from '@plooks/apis';
import { addHistoryAPI, getHistoryProgressAPI } from '@plooks/apis';
import type { OptionsType, QualityType, OptionType } from '../types/player';
import { getResourceUrl, statusCode } from '@plooks/utils';

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

//cs 弹幕
let danmuku = ref<Array<DanmukuType>>([]);
let instance: Artplayer | null = null;

// 更新instance
const getInstance = (art: Artplayer) => {
    instance = art;    
}

let startTime = 0;
const playerKey = ref(0);
let dash: dashjs.MediaPlayerClass;


const option = ref<OptionType>({
    currentTime: 0,
    resource: props.resources,
    part: props.part,
    Danmuku: danmuku.value,
});

// 加载
const loadPart = async (part: number) => {
    
    if (instance) {
        // 设置更新的url
        instance.switchUrl(props.resources[part - 1].url);
        // instance1.plugins.artplayerPluginDanmuku.option
        getDanmuku();
        getHistoryProgress();
    }

    // await getDanmuku(part);
    playerKey.value = Date.now();
}

/**
 * 监听props.part的变化，当其变化时调用loadPart方法
 * @param {any} val props.part的新值
 */
watch(() => props.part, (val) => {
    loadPart(val);
});

// 上传历史记录
const uploadHistory = async () => {
    const history: AddHistoryType = {
        vid: props.vid,
        part: props.part,
        time: dash.time()
    }
    await addHistoryAPI(history);
}

const sendDanmuku = (DanmukuForm: AddDanmukuType) => {
    DanmukuForm.vid = props.vid;
    DanmukuForm.part = props.part;
    sendDanmukuAPI(DanmukuForm);
}

const getDanmuku = async () => {
    const res = await getDanmukuAPI(props.vid, props.part);
    if (res.data.code === statusCode.OK) {
        const list = res.data.data.Danmuku;
        // 设置弹幕
        danmuku = list;
        console.log(list);
        
    }
}

// 获取播放进度
const getHistoryProgress = async () => {
    const res = await getHistoryProgressAPI(props.vid);
    if (res.data.code === statusCode.OK) {
     
        console.log("?");
        
        if (res.data.data.progress.part === props.part&&instance) {
            // 更新播放进度
            instance.seek = res.data.data.progress.time;
            console.log(instance.seek);
            console.log(res.data.data.progress.time);
        }
    }
}

onBeforeMount(() => {
    loadPart(props.part);
    window.addEventListener("unload", uploadHistory);
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