import request from '../request';

// 请求数据
export const getAiMngeData = (q: string) => {
    return request.post('v1/aiMange/getAiData' , {q:q},{
        // 超时时间设置30s
        timeout: 30000,
    })
}