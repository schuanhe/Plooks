import { globalConfig } from "./config"
import { isURL } from "./util"

// 获取资源url
export const getResourceUrl = (originalUrl: string) => {
    // 如果本身就是一个url,或者前后端同源,直接返回
    if (isURL(originalUrl) || !globalConfig.domain) {
        return originalUrl;
    }

    // 前后端不同源
    return `http${globalConfig.https ? 's' : ''}://${globalConfig.domain}${originalUrl}`;
}