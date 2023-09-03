<template>
  <div ref="artRef"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount,nextTick } from 'vue';
import Artplayer from 'artplayer';

const artRef = ref<HTMLDivElement | null>(null);
const instance = ref<Artplayer | null>(null);

// 定义组件的 props
const optionProps = defineProps<{
option: {
  url: string;
};
}>();

// 创建 Artplayer 实例
const createArtplayerInstance = () => {
instance.value = new Artplayer({
  ...optionProps.option,
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
  // $emit("get-instance", instance.value);
});
});

// 组件卸载时销毁 Artplayer 实例
onBeforeUnmount(() => {
destroyArtplayerInstance();
});
</script>
