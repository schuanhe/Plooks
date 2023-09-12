import { ref } from 'vue';
import { useMessage } from 'naive-ui';
import { statusCode } from '@plooks/utils';
import { getFollowStatusAPI, followAPI, unfollowAPI } from "@plooks/apis";

export default function useUserFollow() {
    const isFollow = ref(false);//是否关注

    const message = useMessage();//通知

    const getFollowStatus = (fid: number) => {
        getFollowStatusAPI(fid).then((res) => {
            if (res.data.code === statusCode.OK) {
                isFollow.value = res.data.data.follow;
            }
        })
    }

    //关注
    const follow = (id: number) => {
        followAPI(id).then((res) => {
            if (res.data.code === statusCode.OK) {
                message.success('关注成功');
                isFollow.value = true;
            } else {
                message.error(res.data.message);
            }
        })
    }

    //取关
    const unfollow = (id: number) => {
        unfollowAPI(id).then((res) => {
            if (res.data.code === statusCode.OK) {
                message.success('取关成功');
                isFollow.value = false;
            } else {
                message.error(res.data.message);
            }
        })
    }

    return {
        isFollow,
        follow,
        unfollow,
        getFollowStatus,
    }
}

