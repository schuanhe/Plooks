import request from '../request';
import type { AxiosProgressEvent } from 'axios';
import type { UploadOptions } from "../types/upload-options-type";
import { statusCode } from '@plooks/utils';

// 上传图片
export const uploadFileAPI = ({
    name,
    file,
    action,
    onProgress,
    onFinish,
    onError,
}: UploadOptions) => {
    const formData = new FormData();
    formData.append(name, file)
    request.post(action, formData, {
        // 文件上传60分钟超时 3600000 = 1000*60*60
        timeout: 3600000,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        onUploadProgress: (progressEvent: AxiosProgressEvent) => {
            if (!progressEvent.total) {
                onProgress(0);
                return;
            }
            onProgress(Math.floor(progressEvent.loaded / progressEvent.total * 100));
        }
    }).then((res) => {
        if (res.data.code === statusCode.OK) {
            onFinish(res.data);
        } else {
            onError(res.data);
        }
    }).catch((err) => {
        onError(err);
    })
}
