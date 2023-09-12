<template>
  <div ref="artRef"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, nextTick, watch } from 'vue';
import Artplayer from 'artplayer';
import Hls from 'hls.js';
import type { ResourceType } from '@plooks/apis';


const artRef = ref<HTMLDivElement | null>(null);
const instance = ref<Artplayer | null>(null);


// 定义组件的 props
const props = defineProps<{
  resource: Array<ResourceType>,
  part: number,
}>()

const emits = defineEmits(['get-instance']);

// 创建 Artplayer 实例
const createArtplayerInstance = () => {
  instance.value = new Artplayer({
    url: props.resource[props.part - 1].url,
    container: artRef.value as HTMLDivElement,
    playbackRate: false, // 播放速度
    autoPlayback: true, // 视频回放功能
    pip: true, // 画中画
    fullscreen: true, // 全屏
    fullscreenWeb: true, // 全屏Web
    miniProgressBar: true, // 进度条
    lock: true, // 锁定
    playsInline: true, // 内联播放
    //autoHeight: false, // 自动高度
    autoSize: false, // 自动尺寸
    //mini: true, // 迷你模式
    theme: '#409EFF', // 主题色
    customType: {
      m3u8: playM3u8, // 自定义播放类型
    },
  });
};


// m3u8 播放器
const playM3u8 = (video: HTMLVideoElement, url: string, art: Artplayer) => {
  if (Hls.isSupported()) {
    const hls = new Hls();
    hls.loadSource(url);
    hls.attachMedia(video);
    // 销毁 Artplayer 实例
    art.on('destroy', () => hls.destroy());
    hls.on(Hls.Events.MANIFEST_PARSED, () => {
      video.play();
    });
  } else if (video.canPlayType('application/vnd.apple.mpegurl')) {
    video.src = url;
    video.addEventListener('loadedmetadata', () => {
      video.play();
    });
  }

};

// 绑定播放器的各种事件
const bindArtplayerEvents = () => {

  if (instance.value) {
    instance.value.on('ready', () => {
    });
    instance.value.on('play', () => {
      console.log('play');      
    });
    instance.value.on('pause', () => {
      console.log('pause');
    });
    instance.value.on('destroy', () => {
      console.log('destroy');
    });
    // 视频播放完毕自动播放下一集
  }
};

// 销毁 Artplayer 实例
const destroyArtplayerInstance = () => {
  if (instance.value && instance.value.destroy) {
    instance.value.destroy(false);
  }
};

// 切换实例
const changeArtplayerInstance = () => {
    destroyArtplayerInstance();
    createArtplayerInstance();
    bindArtplayerEvents();
    nextTick(() => {
            emits("get-instance", instance.value);
            console.log("更新播放器成功");
    });
};
watch(() => props.part, (val) => {
  if (instance.value) {
    // 设置更新的url
    changeArtplayerInstance();
  }
});
// 组件挂载时创建 Artplayer 实例
onMounted(() => {
  changeArtplayerInstance()
});

// 组件卸载时销毁 Artplayer 实例
onBeforeUnmount(() => {
  destroyArtplayerInstance();
});

</script>
