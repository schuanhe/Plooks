<template>
  <div ref="artRef"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, nextTick, watch, reactive } from 'vue';
import Artplayer from 'artplayer';
import artplayerPluginDanmuku from 'artplayer-plugin-danmuku'
// import Hls from 'swarmcloud-hls/dist/hls.min';
import Hls from 'hls.js';
import type { DanmukuType, ResourceType } from '@plooks/apis';
import type { OptionType } from '../types/player';
// import { HlsSwP2pEngine } from 'swarmcloud-hls';


const artRef = ref<HTMLDivElement | null>(null);
let instance: Artplayer | null = null;

// 定义组件的 props
const props = defineProps<{
  option: {
    resource: Array<ResourceType>,
    part: number,
  },
  danmuku: Array<DanmukuType>,
}>();

const emits = defineEmits(['get-instance']);

// 创建 Artplayer 实例
const createArtplayerInstance = () => {
  instance = new Artplayer({
    url: props.option.resource[props.option.part - 1].url,
    container: artRef.value as HTMLDivElement,
    playbackRate: false, // 播放速度
    pip: true, // 画中画
    fullscreen: true, // 全屏
    fullscreenWeb: true, // 全屏Web
    miniProgressBar: true, // 进度条
    lock: true, // 锁定
    setting: true, // 设置
    playsInline: true, // 内联播放
    //autoHeight: false, // 自动高度
    autoSize: false, // 自动尺寸
    //mini: true, // 迷你模式
    theme: '#409EFF', // 主题色
    customType: {
      m3u8: playM3u8, // 自定义播放类型
    },
    plugins: [
      artplayerPluginDanmuku({  // 弹幕
        // 弹幕数组
        danmuku: props.danmuku,
        speed: 5, // 弹幕持续时间，单位秒，范围在[1 ~ 10]
        opacity: 1, // 弹幕透明度，范围在[0 ~ 1]
        fontSize: 25, // 字体大小，支持数字和百分比
        color: '#FFFFFF', // 默认字体颜色
        mode: 0, // 默认模式，0-滚动，1-静止
        margin: [10, '25%'], // 弹幕上下边距，支持数字和百分比
        antiOverlap: true, // 是否防重叠
        useWorker: true, // 是否使用 web worker
        synchronousPlayback: false, // 是否同步到播放速度
        filter: (danmu) => danmu.text.length < 50, // 弹幕过滤函数，返回 true 则可以发送
        lockTime: 5, // 输入框锁定时间，单位秒，范围在[1 ~ 60]
        maxLength: 100, // 输入框最大可输入的字数，范围在[0 ~ 500]
        minWidth: 200, // 输入框最小宽度，范围在[0 ~ 500]，填 0 则为无限制
        maxWidth: 400, // 输入框最大宽度，范围在[0 ~ Infinity]，填 0 则为 100% 宽度
        theme: 'light', // 输入框自定义挂载时的主题色，默认为 dark，可以选填亮色 light
        beforeEmit: (danmu) => !!danmu.text.trim(), // 发送弹幕前的自定义校验，返回 true 则可以发送
        // 通过 mount 选项可以自定义输入框挂载的位置，默认挂载于播放器底部，仅在当宽度小于最小值时生效
        // mount: document.querySelector('.artplayer-danmuku'),
      }),
    ],
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
  if (instance) {
    instance.on('play', () => {
      console.log('play');
    });
    instance.on('pause', () => {
      console.log('pause');
    });
    instance.on('destroy', () => {
      console.log('destroy');
    });
  }
};



// 监听弹幕变化
watch(props.danmuku, (newVal) => {
  if (instance) {
    console.log('弹幕变化', newVal);
    
    instance.plugins.artplayerPluginDanmuku.config({
      danmuku: newVal,
    })
    // 重新加载弹幕
    instance.plugins.artplayerPluginDanmuku.load()
  }
});



// 销毁 Artplayer 实例
const destroyArtplayerInstance = () => {
  if (instance && instance.destroy) {
    instance.destroy(false);
  }
};




// 组件挂载时创建 Artplayer 实例
onMounted(() => {
  createArtplayerInstance(); // 创建 Artplayer 实例
  bindArtplayerEvents(); // 绑定播放器的各种事件
  nextTick(() => {
    // 如果需要，可以通过 $emit 发送 Artplayer 实例
    emits("get-instance", instance);
    console.log("更新示例");
  });
});



// 组件卸载时销毁 Artplayer 实例
onBeforeUnmount(() => {
  destroyArtplayerInstance();
});

</script>
