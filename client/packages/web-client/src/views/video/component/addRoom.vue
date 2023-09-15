<template>
    <div class="login-bg">
        <div class="login-card">
            <div class="card-head">
                <span> 创建一起看房间 </span>
                <n-icon class="close-icon" @click="closeCard">
                    <close></close>
                </n-icon>
            </div>
            <n-form :rules="rules" :label-width="80">
                <n-form-item  label="是否创建私密房间">
                    <n-switch v-model:value="isPublic" @update:value="handleChange" />
                </n-form-item>
                <n-form-item v-if="addRommInfo.isPublic" label="密码" path="password">
                    <n-input v-model:value="password" placeholder="请输入密码" />
                </n-form-item>
            </n-form>
            <div class="save-btn">
                <n-button type="primary" @click="addRoom()"> 进入房间 </n-button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { onBeforeMount, ref } from 'vue';
import { Close } from '@plooks/icons';
import { statusCode } from '@plooks/utils';
import { addRoomApi } from '@plooks/apis';
import { useRouter } from 'vue-router';
import type { roomInfoType } from '@plooks/apis'
import {
    NIcon, NButton, NInput,NForm, NFormItem,
    NSwitch, type FormRules, useNotification
} from 'naive-ui';

const notification = useNotification();
const router = useRouter();

const emits = defineEmits(['close']);
const props = defineProps<{
    vid: number,
    pid: number
}>();

const password = ref<string>("");
const isPublic = ref<boolean>(true);

const addRommInfo = ref<roomInfoType>({
    isPublic: true,
    vid: props.vid,
    pid: props.pid,
    password: ""
})

const handleChange = (value: boolean) => {
    addRommInfo.value.isPublic = value;
}

const addRoom = () =>{
    if(addRommInfo.value.isPublic){
        if(addRommInfo.value.password == ""){
            notification.error({
                title: "非公开房间需要设置密码",
                duration: 3000,
            });
            return;
        }
    }
    addRommInfo.value.password = password.value;
    addRoomApi(addRommInfo.value).then((res) => {
        if (res.data.code === statusCode.OK) {
            // 通过路由跳转
            router.push({
                name: 'WatchRoom',
                params: {
                    roomId: res.data.data.rid
                }
            })

            emits('close', 1);
        }else{
            notification.error({
                title: res.data.message,
                duration: 3000,
            });
        }
    })
}

const rules: FormRules = {
    password: [
        { required: true, message: '请输入密码' },
    ]
}

const closeCard = () => {
    emits('close', 0);
}


onBeforeMount(() => {

})
</script>

<style lang="less" scoped>
.login-bg {
    top: 0;
    left: 0;
    z-index: 999;
    position: fixed;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.45);
}

.login-card {
    position: absolute;
    top: 50%;
    left: 50%;
    margin: -220px 0 0 -163px;
    width: 330px;
    height: 250px;
    padding: 6px 12px;
    background-color: #fff;
    border-radius: 10px;
    animation: fadein .3s ease-in;
    box-shadow: 16px 16px 50px -12px rgba(0, 0, 0, 0.8);
}

.form-class {
    padding: 20px;
}

.card-head {
    display: flex;
    height: 30px;
    align-items: center;
    justify-content: space-between;

    // 关闭按钮
    .close-icon {
        font-size: 26px;
        color: #adadad;
        cursor: pointer;

        &:hover {
            color: #999;
        }
    }

}

.collention-item {
    height: 36px;
    padding: 0 6px;
    line-height: 36px;

    span {
        float: right;
        font-size: 12px;
        color: #999;
    }
}

.add-collection {
    width: 100%;

    button {
        width: 100%;
    }
}

.save-btn {
    width: 100%;
    text-align: center;

    button {
        width: 160px;
        margin-top: 10px;
    }
}
</style>