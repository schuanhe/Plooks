<template>
    <div class="player-container">
        <!-- <w-player class="player" :key="playerKey" :options="options"></w-player> -->
        <artplayer-com class="player" :option="options" @getInstance="getInstance"></artplayer-com>

    </div>
</template>

<script setup lang="ts">
import { onBeforeMount, ref, watch } from 'vue';
import dashjs from "dashjs";
import { WPlayer } from 'vue-wplayer';
import ArtplayerCom from '../video-artplayer/ArtplayerCom.vue';
import 'vue-wplayer/dist/style.css';
import type { ResourceType, AddHistoryType, AddDanmakuType } from '@plooks/apis';
import { sendDanmakuAPI, getDanmakuAPI } from '@plooks/apis';
import { addHistoryAPI, getHistoryProgressAPI } from '@plooks/apis';
import type { OptionsType, QualityType, OptionType } from '../types/player';
import { getResourceUrl, statusCode } from '@plooks/utils';
import { getDefaultTime } from 'naive-ui/es/date-picker/src/utils';

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

const instance = ref<Artplayer | null>(null);

// 更新instance
const getInstance = (art: Artplayer) => {
    instance.value = art;
    console.log("父组件更新成功");
    
}

let startTime = 0;
const playerKey = ref(0);
let dash: dashjs.MediaPlayerClass;


const options: OptionType = {
    currentTime: startTime,
    resource: props.resources,
    part: props.part,
}


const options1: OptionsType = {
    resource: [],
    type: "dash",
    customType: (player: HTMLVideoElement, url: string) => {
        dash = dashjs.MediaPlayer().create();
        dash.initialize(player, url, false, startTime);
    },
    customQualityChange: (quality: string) => {
        const trackIndex = getTrackIndex(Number(quality));
        const tracks = dash.getTracksFor("video");
        if (trackIndex >= tracks.length) {
            dash.setCurrentTrack(tracks[tracks.length - 1])
        } else {
            dash.setCurrentTrack(tracks[trackIndex])
        }
    },
    mobile: props.mobile,
    theme: props.theme, // 主题
    danmaku: {
        open: true,
        data: [],
        send: (danmaku: AddDanmakuType) => {
            sendDanmaku(danmaku);
        }
    }
}

const getTrackIndex = (quality: number) => {
    switch (quality) {
        case 360:
            return 0;
        case 480:
            return 1;
        case 720:
            return 2;
        case 1080:
            return 3;
        default:
            return Number.MAX_SAFE_INTEGER
    }
}

// 加载
const loadPart = async (part: number) => {

    options.part = part;

    // loadResource(part);
    startTime = await getHistoryProgress();

    if (instance.value) {
        // 设置更新的url
        instance.value.switchUrl(props.resources[part - 1].url);
        
    }

    // await getDanmaku(part);
    playerKey.value = Date.now();
}

// const loadResource = (part: number) => {
//     let tmpResource: QualityType = {};

//     switch (props.resources[part - 1].quality) {
//         case 1080:
//             tmpResource[1080] = {
//                 name: "1080P",
//                 url: getResourceUrl(props.resources[part - 1].url),
//             }
//         case 720:
//             tmpResource[720] = {
//                 name: "720P",
//                 url: getResourceUrl(props.resources[part - 1].url),
//             }
//         case 480:
//             tmpResource[480] = {
//                 name: "480P",
//                 url: getResourceUrl(props.resources[part - 1].url),
//             }
//         case 360:
//             tmpResource[360] = {
//                 name: "360P",
//                 url: getResourceUrl(props.resources[part - 1].url),
//             }
//     }

//     options.resource = tmpResource;
// }

// 获取播放进度
const getHistoryProgress = async () => {
    const res = await getHistoryProgressAPI(props.vid);
    if (res.data.code === statusCode.OK) {
     
        if (res.data.data.progress.part === props.part) {

            //// 更新播放进度
            if (instance.value) {
                instance.value.seek = res.data.data.progress.time;
                console.log(7777777);
                
            }

            return res.data.data.progress.time;
        }
    }
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

const sendDanmaku = (danmakuForm: AddDanmakuType) => {
    danmakuForm.vid = props.vid;
    danmakuForm.part = props.part;
    sendDanmakuAPI(danmakuForm);
}

// const getDanmaku = async (part: number) => {
//     if (options.danmaku.data) options.danmaku.data = [];
//     const res = await getDanmakuAPI(props.vid, part);
//     if (res.data.code === statusCode.OK) {
//         const list = res.data.data.danmaku;
//         if (list) {
//             options.danmaku!.data = list;
//         }
//     }
// }

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