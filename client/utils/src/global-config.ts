const title = "Plooks 一起看!"; //网站标题
const https = false; //是否启用https
const domain = "localhost:8080"; //域名
// const domain = "1.14.67.35:3311"
const icp = "icp备案信息"; //备案信息
const security = "公网安备信息" //公网安备信息

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