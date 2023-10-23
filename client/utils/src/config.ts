// 获取环境变量或设置默认值
const title = "Plooks 一起看!"; // 网站标题
const https =  false; // 是否启用https
// 去除警告
// @ts-ignore
// const domain = import.meta.env.VITE_BASE_DOMAIN; // 域名
const domain = "localhost:2023"; // 域名
// const domain = "43.138.25.64:2023"; // 域名
const icp = "icp备案信息"; // 备案信息
const security = "公网安备信息"; // 公网安备信息

//上传文件大小限制，需要先修改后端大小限制
const maxImgSize = 5;//上传图片最大大小(单位M)
const maxVideoSize = 500;//上传视频最大大小(单位M)

export const globalConfig = {
    title: title,
    https: https,
    domain: domain,
    icp: icp,
    security: security,
    maxImgSize: maxImgSize,
    maxVideoSize: maxVideoSize,
}