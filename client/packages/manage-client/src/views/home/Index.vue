<template>
    <div class="home" :style="initTheme()" v-title data-title="后台管理系统">
        <div class="home-menu">
            <span class="title">{{ globalConfig.title }}</span>
            <n-menu :default-value="defaultKey" :options="menuOptions" />
        </div>
        <div class="home-box">
            <header-bar></header-bar>
            <div class="home-router">
                <router-view></router-view>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { h, ref, onBeforeMount } from "vue";
import { getTheme } from '@/theme';
import { RouterLink, useRoute } from 'vue-router';
import { globalConfig,storageData } from '@plooks/utils';
import { NMenu } from "naive-ui";
import HeaderBar from '@/components/header-bar/Index.vue';

const route = useRoute();

const initTheme = () => {
    const theme = getTheme();

    return {
        "--title-color": theme.primaryColor
    }
}

const defaultKey = ref("");

const menuOptions:Array<any> = [];
if (storageData.get("user_info")?.role > 0) {
menuOptions.push(...[    
    {
        label: () =>
            h(
                RouterLink,
                {
                    to: {
                        name: "Review",
                    }
                },
                { default: () => '视频审核' }
            ),
        key: "review",
    },
    {
        label: () =>
            h(
                RouterLink,
                {
                    to: {
                        name: "Video",
                    }
                },
                { default: () => '视频管理' }
            ),
        key: "video",
    },])
}

// 审核的路由
if(storageData.get("user_info")?.role >= 2 ) {
    menuOptions.push(...[
    {
        label: () =>
            h(
                RouterLink,
                {
                    to: {
                        name: "User",
                    }
                },
                { default: () => '用户管理' }
            ),
        key: "user",
    },
    {
        label: () =>
            h(
                RouterLink,
                {
                    to: {
                        name: "Announce",
                    }
                },
                { default: () => '公告管理' }
            ),
        key: "announce",
    },
    {
        label: () =>
            h(
                RouterLink,
                {
                    to: {
                        name: "Carousel",
                    }
                },
                { default: () => '轮播图管理' }
            ),
        key: "carousel",
    },
    {
        label: () =>
            h(
                RouterLink,
                {
                    to: {
                        name: "Partition",
                    }
                },
                { default: () => '分区管理' }
            ),
        key: "partition",
    }
    ])
}

if(storageData.get("user_info")?.role == 3 ) {
    menuOptions.push(...[
    {
        label: () =>
            h(
                RouterLink,
                {
                    to: {
                        name: "AiMange",
                    }
                },
                { default: () => '智慧管理' }
            ),
        key: "AiMange",
    }
    ])
}



// 路由名称转key
const routeNameToKey = () => {
    switch (route.name) {
        case "Review":
            return "review";
        case "User":
            return "user";
        case "Video":
            return "video";
        case "Announce":
            return "announce";
        case "Carousel":
            return "carousel";
        case "Partition":
            return "partition";
        case "Config":
            return "config";
        default:
            return "";
    }
}

onBeforeMount(() => {
    defaultKey.value = routeNameToKey();
})
</script>

<style lang="less" scoped>
.home {
    display: flex;
    background: #f7f7f7;

    .home-box {
        width: calc(100% - 220px);
    }

    .home-menu {
        .title {
            display: block;
            height: 60px;
            font-size: 18px;
            color: var(--title-color);
            line-height: 60px;
            text-align: center;
            border-bottom: 1px solid rgba(60, 60, 67, .12);
        }

        width: 220px;
        height: 100vh;
        position: relative;
        background-color: #fff;
        box-shadow: 0 1px 4px rgba(0, 21, 41, .3);
    }

    .home-router {
        width: 100%;
        min-height: 500px;
        min-width: 1000px;
    }
}
</style>