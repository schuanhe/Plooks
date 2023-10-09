<template>
  <div ref="artRef"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, nextTick, watch, reactive } from 'vue';
import Artplayer from 'artplayer';
import Hls from 'hls.js';
import type { AddDanmukuType, DanmukuType, ResourceType } from '@plooks/apis';
import { sendDanmukuAPI } from '@plooks/apis';
import { statusCode } from '@plooks/utils';


const artRef = ref<HTMLDivElement | null>(null);
const instance = ref<Artplayer | null>(null);


// 定义组件的 props
const props = withDefaults(defineProps<{
  resource: Array<ResourceType>,
  vid: number,
  part: number,
  isAdmin: boolean,
}>(), {
  isAdmin:false,
});


const emits = defineEmits(['get-instance','ready','play','pause','seek','switchPid']);

// 创建 Artplayer 实例
const createArtplayerInstance = () => {
  instance.value = new Artplayer({
    url: props.resource[props.part - 1].url,
    container: artRef.value as HTMLDivElement,
    // 直播模式
    isLive: !props.isAdmin,
    playbackRate: false, // 播放速度
    autoPlayback: true, // 视频回放功能
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
      emits("ready", "ready");
    });
    instance.value.on('play', () => {
      emits("play", "play");
    });
    instance.value.on('pause', () => {
      emits("pause", "pause");
    });
    instance.value.on('seek', () => {
      emits("seek", instance.value?.currentTime);
    });
    instance.value.on('restart', () => {
      emits("switchPid",props.part)
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
    console.log("当前房间信息",props);
    
    createArtplayerInstance();
    bindArtplayerEvents();
    nextTick(() => {
            emits("get-instance", instance.value);
    });
};

// 切换pid只用切换视频源
const switchPid = (pid:number) => {
  if (instance.value) {
    // 设置更新的url
    instance.value.switchUrl(props.resource[pid - 1].url);

  }
};
watch(() => props.vid, (val) => {
  if (instance.value) {
    // 设置更新的url
    changeArtplayerInstance();
  }
});

watch(() => props.part,(val)=>{
  if (instance.value) {
    // 设置更新的url
    switchPid(val)
  }
})
// 组件挂载时创建 Artplayer 实例
onMounted(() => {
  if(props.vid === 0) {
    console.log("vid为0,不创建播放器");
  }else{
    changeArtplayerInstance()
  }
});



// 组件卸载时销毁 Artplayer 实例
onBeforeUnmount(() => {
  destroyArtplayerInstance();
});

</script>
