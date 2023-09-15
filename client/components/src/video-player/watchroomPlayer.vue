<template>
    <div class="player-container">
        <artplayer-com class="player" 
        ref="artRef"
        :resource="props.resources"
        :vid="props.vid"
        :part="props.part"
        :isAdmin="props.isAdmin"
        @getInstance="getInstance"
        @ready="ready"
        @play="play"
        @pause="pause"
        @seek="seek"
        @switchPid="switchPid"
        ></artplayer-com>

    </div>
</template>

<script setup lang="ts">
import { ref, onBeforeUnmount } from 'vue';
import ArtplayerCom from '../video-artplayer/ArtplayerComRoom.vue';
import type { ResourceType, AddHistoryType, roomInfoType } from '@plooks/apis';
import { addHistoryAPI } from '@plooks/apis';
import type Artplayer from 'artplayer';

const props = withDefaults(defineProps<{
    vid: number, // 视频id
    theme: string, // 主题
    resources: Array<ResourceType>, // 视频资源
    part: number, // 当前播放的集数
    isAdmin: boolean, // 是否是管理员
}>(), {
    vid: 0,
    part: 1,
    isAdmin: false,
})

const instance = ref<Artplayer | null>(null)

const emits = defineEmits(['change','ready','play','pause','seek','switchPid','getInstance']);

// 更新instance
const getInstance = (art: Artplayer) => {
    instance.value = art;
    emits("getInstance",instance.value)
}

//
const ready = () => {
    emits("ready","ready")
}

const play = () => {
    emits("play","play")
}

const pause = () => {
    emits("pause","pause")
}

const seek = (seek:number) => {
    emits("seek",seek)
}

const switchPid = (pid:number) => {
    emits("switchPid",pid)
}

onBeforeUnmount(() => {

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