<template>
    <div style="height: 1024px; width: 1024px;">
      
      <div class="artplayer"
           ref="artRef" s>
      </div>
      <!-- <div class="artplayer-danmuku"></div> -->
    </div>
</template>
  
<script setup lang="ts">
// import type { ResourceType } from '@plooks/apis/src/types/resource-type';
import Artplayer from 'artplayer';
import type Option from 'artplayer/types/option';
import { onMounted, nextTick, ref } from 'vue';

// const props = defineProps<{
//   vid: number,
//   theme: string,
//   mobile?: boolean,
//   resources: Array<ResourceType>,
//   part: number,
// }>(); // withDefaults(defineProps<{
  const artRef = ref<HTMLDivElement | null>(null);

// 创建一个新的 Artplayer 实例
const createArt = () => {
  const option: Option = {
    container: artRef.value as HTMLDivElement,
    url: 'http://cdns.schuanhe.com//video/12/1693396292013.mp4',
    type: 'mp4',
    
  };

  console.log(option);
  const art = new Artplayer(option);
  console.log(art);
};

// 加载
// const loadPart = async () => {
//   createArt();
// }


//在
onMounted(() => {
  nextTick(() => {
    createArt();
  });
  
});



  
  </script>
  <style lang="less">
  // .artplayer {
  //   height: 100%;
  //   padding: 5px;
  // }
  .artplayer {
    height: "400px";
    width: "600px";
    margin: "60px auto 0";
  }
  .artplayer-danmuku {
    padding: 10px;
  }
  </style>