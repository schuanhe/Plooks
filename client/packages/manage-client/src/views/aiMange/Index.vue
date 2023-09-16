<template>
    <n-spin :show="loading">
    <n-card class="card">
        <div class="info-header">
            <n-card title="智慧数据(ai生成有风险,仅供毕业答辩)">
            <n-input style="max-width: 40%;" v-model:value="question" placeholder="直接说出你想要的数据吧"></n-input>
            <n-button style="margin-left: 5%; margin-right: 5%;" type="primary" @click="getAiData"> 智能获取 </n-button>
            <n-input type="textarea" v-if="sql != ''" :disabled="true" v-model:value="sql" placeholder="执行的sql" style="max-width: 40%;">{{ sql }}</n-input>
  </n-card>
        </div>
        <n-scrollbar style="max-height: 600px">
        <n-table class="table" striped>
            <thead class="table-head">
                <tr>
                    <th v-for="column in columns" :key="column.key"> {{ column.title }} </th>
                </tr>
            </thead>
            
            <tbody class="table-body">  
                <tr :key="item.id" v-for="item in tableData">
                    <template v-for="column in columns" :key="column.key">
                        <!-- 头像 -->
                        <td v-if="column.key == 'avatar'"> <common-avatar :url="item.avatar"></common-avatar> </td>
                        <!-- 性别 -->
                        <td v-else-if="column.key == 'gender'"> 
                            <n-tag  v-if="item[column.key] == 1" type="success"> 男 </n-tag> 
                            <n-tag v-else type="error"> 女 </n-tag>
                        </td>
                        <!-- 视频封面 -->
                        <td v-else-if="column.key == 'cover'">
                            <img class="cover" :src="getResourceUrl(item.cover)" alt="视频封面">
                        </td>
                        <!-- 状态 -->
                        <td v-else-if="column.key == 'status'"> 
                            <n-tag  v-if="item[column.key] == 0" type="success"> 正常 </n-tag> 
                            <n-tag  v-else-if="item[column.key] == 1" type="error"> 封禁 </n-tag>
                            <n-tag  v-else-if="item[column.key] == 100" type="warning"> 创建成功 </n-tag>
                            <n-tag  v-else-if="item[column.key] == 500" type="info"> 待审核 </n-tag>
                            <n-tag  v-else-if="item[column.key] == 2100" type="error"> 审核不通过 </n-tag>
                        </td>
                        <!-- 普通表格 -->
                        <td v-else> {{ item[column.key] }} </td>
                    </template>
                </tr>
            </tbody>
            
        </n-table>
        </n-scrollbar>
    </n-card>
</n-spin>
</template>

<script setup lang="ts">

import { onBeforeMount, ref } from 'vue';
import { CommonAvatar } from '@plooks/components';
import { getAiMngeData } from "@plooks/apis";
import { NTable, NButton, NCard, useNotification,NScrollbar, NInput, NTag, NSpin } from 'naive-ui';
import { getResourceUrl, statusCode } from '@plooks/utils';


const notification = useNotification();//通知

export interface columnType {
    title: string;
    key: string;
}


const columns = ref(Array<columnType>());

const tableData = ref(Array<any>());

const sql = ref("");

// 问题字符串
const question = ref("");

// 加载图标
const loading = ref(false);


const getAiData = () => {
    loading.value = true;
    getAiMngeData(question.value).then((res:any) => {
        if (res.data.code === statusCode.OK) {
            sql.value = res.data.data.sql;
            columns.value = res.data.data.columns;
            tableData.value = res.data.data.data;
            console.log(res.data.data);
        }else{
            notification.error({
                title: res.data.message,
                duration: 5000,
            })
        }
        loading.value = false;
    }).catch(() => {
        loading.value = false;
        notification.error({
            title: '获取数据失败',
            duration: 5000,
        })
    });
    // const data = {
    //     "sql":"SELECT * FROM user WHERE id LIKE '%1%' LIMIT 20","columns":[{"title":"用户id","key":"id"},{"title":"用户名","key":"username"},{"title":"昵称","key":"nickname"},{"title":"邮箱","key":"email"},{"title":"头像","key":"avatar"},{"title":"性别","key":"gender"},{"title":"生日","key":"birthday"},{"title":"签名","key":"sign"},{"title":"状态","key":"status"},{"title":"角色","key":"role"},{"title":"创建时间","key":"created_at"},{"title":"更新时间","key":"updated_at"},{"title":"删除时间","key":"deleted_at"}]
    //     ,"data":[{"birthday":"2000-05-14T00:00:00","role":3,"gender":1,"space_cover":"http://cdns.schuanhe.com//image/1693999233801.jpg","sign":"这个人很懒，什么都没有留下66777777777777777777777","created_at":"2023-08-02T20:00:25","avatar":"http://cdns.schuanhe.com//image/1694341836552.jpg","password":"$2a$10$CT10ufGLV9bR/BSkk9KPTejJAz41CeSwly8q9jlsYqxjMm2XQ6w2W","updated_at":"2023-09-15T22:27:33","nickname":"幻鹤666","id":1,"email":"schuanhe666@qq.com","username":"schuanhe","status":"0"},{"birthday":"1970-01-01T00:00:00","role":0,"gender":0,"sign":"这个人很懒，什么都没有留下","created_at":"2023-08-14T12:05:22","avatar":"http://cdns.schuanhe.com//image/1693477312286.jpg","password":"$2a$10$w/3g5PYYDs0sBLfLT30TKepeHdPVXs4nMHi.6fsCVSDiMeFtXZPI2","updated_at":"2023-09-15T22:27:33","nickname":"553","id":10,"email":"asd@qq.com","username":"asd","status":"0"},{"birthday":"1970-01-01T00:00:00","password":"$2a$10$hFXs9wypLLBl.eag0t5UcOextqH/b01RyjpI8PjV32XASHePJpoqa","role":0,"updated_at":"2023-09-15T22:27:33","gender":0,"nickname":"用户UDXw","sign":"这个人很懒，什么都没有留下","created_at":"2023-09-15T12:32:11","id":11,"email":"2369551660@QQ.COM","username":"hq123","status":"0"},{"birthday":"2001-11-04T00:00:00","role":0,"gender":1,"space_cover":"http://cdns.schuanhe.com//image/1694782379604.jpg","sign":"“烟灭酒半杯 流着眼泪笑着说自己没事”","created_at":"2023-09-15T12:33:03","avatar":"http://cdns.schuanhe.com//image/1694782655371.jpg","password":"$2a$10$etLc8kXaWgJ.MblYBbtX2OYZnAGKXqrY4ccbEcVqJJpSEXUUntVXy","updated_at":"2023-09-15T21:07:55","nickname":"Rylan","id":12,"email":"971221816@qq.com","username":"Rylan","status":"0"},{"birthday":"1970-01-01T00:00:00","password":"$2a$10$H0IetzOPm0UYhFb8TATrhug.O.mVB2XUnS9eOkN9BZ3LMGkC2UQ.y","role":0,"updated_at":"2023-09-15T12:41:20","gender":0,"nickname":"用户mmIH","sign":"这个人很懒，什么都没有留下","created_at":"2023-09-15T12:41:20","id":13,"email":"qin20021@qq.com","username":"20160314","status":"0"},{"birthday":"1970-01-01T00:00:00","password":"$2a$10$U5ZJXyL0tdqMnYC3qcnRQOYA7Z2t7ih3W1FU0qQa/v7CVeM5vrfO.","role":0,"updated_at":"2023-09-15T12:43:03","gender":0,"nickname":"用户kNsU","sign":"这个人很懒，什么都没有留下","created_at":"2023-09-15T12:43:03","id":14,"email":"1669103407@qq.com","username":"1669103407","status":"0"},{"birthday":"1970-01-01T00:00:00","password":"$2a$10$HDi3RHXP81GRC5XcfidFie13pLUOMHBVyr6SEoDgO816joFCbBbvS","role":0,"updated_at":"2023-09-15T13:01:39","gender":0,"nickname":"用户PD3w","sign":"这个人很懒，什么都没有留下","created_at":"2023-09-15T13:01:39","id":15,"email":"2739595445@qq.com","username":"2739595445","status":"0"},{"birthday":"1970-01-01T00:00:00","password":"$2a$10$eMOE5frLb.sUkc.OZ1W3oeN1nICLd1TX.Lghsa8RXpRUIHApmbY.G","role":0,"updated_at":"2023-09-15T13:06:48","gender":0,"nickname":"用户ltcA","sign":"这个人很懒，什么都没有留下","created_at":"2023-09-15T13:06:48","id":16,"email":"2540627176@qq.com","username":"Scall01","status":"0"},{"birthday":"1970-01-01T00:00:00","password":"$2a$10$0w8laPuUDisvsRAjX41/cujxZMbapi2O38Y9WvibDE1lDxNp21RkS","role":0,"updated_at":"2023-09-15T13:38:58","gender":0,"nickname":"用户G5DZ","sign":"这个人很懒，什么都没有留下","created_at":"2023-09-15T13:38:58","id":17,"email":"2473535093@qq.com","username":"SHABOYI","status":"0"}]

    // }

    // // 生成表格
    // columns.value = data.columns
    // tableData.value = data.data
    // sql.value = data.sql
}

onBeforeMount(() => {
    
})
</script>

<style lang="less" scoped>
.card {
    margin: 10px;
    width: calc(100% - 20px);
    border-radius: 10px;

    .info-header {
        display: flex;  //这里是flex布局
        height: 160px; // 这里是高度
        align-items: center; //这里是垂直居中
        justify-content: space-between; //这里是水平居中
        margin-bottom: 20px; //

        .search {
            width: 300px;
        }
    }

    .table {
        .table-head {
            text-align: center;
        }

        .table-body {
            text-align: center;

            .cover {
                height: 60px;
                width: 100px;
            }

            .btn-list {
                button {
                    margin: 0 6px;
                }
            }
        }
    }
}

.pagination {
    margin-top: 20px;
}
</style>