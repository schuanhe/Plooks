<template>
    <div class="select-partition">
        <n-select class="select" placeholder="选择分区" label-field="content" value-field="id" remote :options="partitions"
            @update:value="partitionChange" />
        <n-select v-if="showSubpartition" class="select" label-field="content" value-field="id" remote
            placeholder="选择二级分区" :options="subpartitions" @update:value="selectedPartition" />
    </div>
</template>

<script setup lang="ts">
import { onBeforeMount, ref } from "vue";
import type { PartitionType } from '@plooks/apis';
import { getPartitionAPI, } from '@plooks/apis';
import { NSelect, useNotification } from 'naive-ui';
import { statusCode } from "@plooks/utils";

const emits = defineEmits(["selected"]);
const notification = useNotification();

const partitionList = ref<Array<PartitionType>>([]);//所有分区
const partitions = ref<Array<PartitionType>>([]);//分区
const subpartitions = ref<Array<PartitionType>>([]);//二级分区
const showSubpartition = ref(false);//是否显示二级分区

//改变分区
const partitionChange = (parentId: number) => {
    subpartitions.value = partitionList.value.filter((item) => {
        return item.parentId === parentId;
    })
    showSubpartition.value = true;
}

const selectedPartition = (value: number) => {
    emits("selected", value);
}

onBeforeMount(() => {
    // 获取分区列表
    getPartitionAPI().then((res) => {
        if (res.data.code === statusCode.OK) {
            partitionList.value = res.data.data;
            partitions.value = partitionList.value.filter((item) => {
                return item.parentId === 0;
            })

            console.log(partitionList.value);
            console.log(partitions.value);
            
            
        }
    }).catch((err) => {
        notification.error({
            title: '获取分区失败',
            content: "原因:" + err.response.data.message,
            duration: 5000,
        })
    });
})
</script>

<style lang="less" scoped>
.select-partition {
    width: 330px;
    display: flex;
    justify-content: space-between;

    .select {
        width: 150px;
    }
}
</style>