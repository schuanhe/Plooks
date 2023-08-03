import request from '../request';
import { storageData } from "@plooks/utils" 
// import { storageData } from "@plooks/utils";

// 获取access_token
export const getAccessToken = async () => {
    const res = await request.get('v1/user/token/refresh', {
        headers: {
            Authorization: `${storageData.get('refresh_token')}`
        }
    });

    return res
}