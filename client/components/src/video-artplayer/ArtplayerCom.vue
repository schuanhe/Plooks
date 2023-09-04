<template>
  <div ref="artRef"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, nextTick, watch } from 'vue';
import Artplayer from 'artplayer';
import type { ResourceType } from '@plooks/apis';
import type { OptionType } from '../types/player';


const artRef = ref<HTMLDivElement | null>(null);
const instance = ref<Artplayer | null>(null);

// 定义组件的 props
const props = defineProps<{
  option: {
    resource: Array<ResourceType>,
    part: number,
  }
}>();

const emits = defineEmits(['get-instance']);

// const options: OptionType = {
//   currentTime: 0,
//   resource: optionProps.option.resource,
//   part: optionProps.option.part,
// }



// 创建 Artplayer 实例
const createArtplayerInstance = () => {
  instance.value = new Artplayer({
    url: props.option.resource[props.option.part - 1].url,
    container: artRef.value as HTMLDivElement,
  });
};


// 销毁 Artplayer 实例
const destroyArtplayerInstance = () => {
  if (instance.value && instance.value.destroy) {
    instance.value.destroy(false);
  }
};

// 组件挂载时创建 Artplayer 实例
onMounted(() => {
  createArtplayerInstance();
  nextTick(() => {
    // 如果需要，可以通过 $emit 发送 Artplayer 实例
    emits("get-instance", instance.value);
    console.log("更新示例");
  });
});

// 组件卸载时销毁 Artplayer 实例
onBeforeUnmount(() => {
  destroyArtplayerInstance();
});

</script>
