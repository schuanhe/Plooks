<template>
    <Artplayer @get-instance="getInstance" :option="option" :style="style" />
  </template>
  
  <script>
  import Artplayer from "./Artplayer2.vue";
  
  export default {
    data() {
      return {
        option: {
          url: "http://cdns.schuanhe.com//video/12/1693396292013.mp4",
        },
        style: {
          width: "600px",
          height: "400px",
          margin: "60px auto 0",
        },
      };
    },
    components: {
      Artplayer,
    },
    methods: {
      getInstance(art) {
        console.info(art);
      },
    },
  };
  </script>