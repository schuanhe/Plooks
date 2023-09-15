<template>
    <div class="player-container">
        <artplayer-com class="player" 
        :resource="props.resources"
        :vid="props.vid"
        :part="props.part"
        @getInstance="getInstance"
        @autoPlayNext="autoPlayNext"
        ></artplayer-com>

    </div>
</template>

<script setup lang="ts">
import { ref, onBeforeUnmount } from 'vue';
import ArtplayerCom from '../video-artplayer/ArtplayerCom.vue';
import type { ResourceType, AddHistoryType } from '@plooks/apis';
import { addHistoryAPI } from '@plooks/apis';
import type Artplayer from 'artplayer';

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

const emits = defineEmits(['change']);

// 更新instance
const getInstance = (art: Artplayer) => {
    instance.value = art;    
}


// 视频播放完毕自动播放下一局
const autoPlayNext = () => {
    // 判断是否为最后一集
    if (props.part < props.resources.length) {
        // 播放下一集
        emits('change', props.part + 1);
    }
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