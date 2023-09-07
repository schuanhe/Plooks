<template>
  <div ref="artRef"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, nextTick, watch, reactive } from 'vue';
import Artplayer from 'artplayer';
import artplayerPluginDanmuku from 'artplayer-plugin-danmuku'
import Hls from 'hls.js';
import type { AddDanmukuType, DanmukuType, ResourceType } from '@plooks/apis';
import { getDanmukuAPI, getHistoryProgressAPI, sendDanmukuAPI } from '@plooks/apis';
import { statusCode } from '@plooks/utils';


const artRef = ref<HTMLDivElement | null>(null);
const instance = ref<Artplayer | null>(null);


// 定义组件的 props
const props = defineProps<{
  resource: Array<ResourceType>,
  vid: number,
  part: number,
}>()


const emits = defineEmits(['get-instance']);

// 创建 Artplayer 实例
const createArtplayerInstance = () => {
  instance.value = new Artplayer({
    url: props.resource[props.part - 1].url,
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
        danmuku: function () {
                return new Promise((resolve, reject)=>{
                    getDanmukuAPI(props.vid,props.part).then((res)=>{
                        if (res.data.code === statusCode.OK) {
                            const list = res.data.data.Danmuku;
                            resolve(list);
                        }else{
                            reject(res.data.message);
                        }
                    })
                })
        },
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

  if (instance.value) {
    instance.value.on('ready', () => {
      //播放器准备完成,则设置播放进度
      getHistoryProgress()
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
    
    // TODO: 修复播放完毕自动播放下一集

    // 弹幕事件绑定(关闭ts校验)
    //@ts-ignore
    instance.value.on('artplayerPluginDanmuku:emit', (danmu) => {
      //判断danmu.text是否存在
      const danmuku = danmu as DanmukuType
      const addDanmu:AddDanmukuType = {
        ...props,
        ...danmuku 
      }
      sendDanmu(addDanmu)
    });
  }
};




// 获取播放进度
const getHistoryProgress = async () => {
    const res = await getHistoryProgressAPI(props.vid);
    if (res.data.code === statusCode.OK) {
        if (res.data.data.progress.part === props.part&&instance) {
            // 更新播放进度
            if (instance.value) {
              instance.value.seek = res.data.data.progress.time;
              instance.value.notice.show = '根据历史记录，已为您跳转到上次播放进度';
            }
        }
    }
}

// 发送弹幕
const sendDanmu = async (danmu:AddDanmukuType)=>{
  console.log(danmu);
  const res = await sendDanmukuAPI(danmu);
    if (res.data.code === statusCode.OK) {
      // 发送弹幕成功
    }
}


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
