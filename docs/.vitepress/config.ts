import { defineConfig } from 'vitepress';
// @ts-ignore
import path from 'path';

export default defineConfig({
    title: 'Plooks一起看网站文档',
    lang: 'zh-CN',
    base: '/Plooks/',
    description: '基于SpringBoot + Vue的一起看',
    head: [
        [
            'meta',
            {
                name: 'keywords',
                content: 'Plooks, java, vue, vue3, SpringBoot'
            }
        ],
        ['link', { rel: 'icon', type: 'image/svg+xml', href: '/logo.svg' }],
        [
            'meta',
            {
                name: 'viewport',
                content: 'width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no'
            }
        ],
        ['link', { rel: 'icon', href: '/favicon.ico' }]
    ],
    srcDir: `${path.resolve(process.cwd())}/src`,
    themeConfig: {
        editLink: {
            text: '为此页提供修改建议',
            pattern: 'https://github.com/schuanhe/plooks/tree/main/docs/src/:path'
        },
        socialLinks: [
            { icon: 'github', link: 'https://github.com/schuanhe/plooks' },
        ],
        footer: {
            message: '根据  AGPL-3.0 许可证发布',
            copyright: 'Copyright © 2022-2023'
        },
        nav: [
            { text: '项目指南', link: '/guide/', activeMatch: '/guide/' },
            { text: '接口文档', link: '/api/', activeMatch: '/api/' },
            { text: '赞助', link: '/other/donate' }
        ],
        sidebar: {
            '/guide/': [
                {
                    text: '项目指南',
                    items: [
                        {
                            text: '开始',
                            link: '/guide/'
                        },
                        {
                            text: '后端部署',
                            link: '/guide/api'
                        },
                        {
                            text: '前端部署',
                            link: '/guide/web'
                        },
                        {
                            text: '常见问题解答',
                            link: '/guide/qa'
                        },
                        {
                            text: '相关截图',
                            link: '/guide/screenshot'
                        }
                    ]
                }
            ],
            '/api/': [
                {
                    text: '接口文档',
                    items: [
                        {
                            text: '开始',
                            link: '/api/'
                        },
                        {
                            text: '用户相关接口',
                            link: '/api/user'
                        },
                        {
                            text: '验证相关接口',
                            link: '/api/captcha'
                        },
                        {
                            text: '文件上传相关接口',
                            link: '/api/upload'
                        },
                        {
                            text: '分区相关接口',
                            link: '/api/partition'
                        },
                        {
                            text: '视频相关接口',
                            link: '/api/video'
                        },
                        {
                            text: '视频资源接口',
                            link: '/api/resource'
                        },
                        {
                            text: '点赞收藏接口',
                            link: '/api/archive'
                        },
                        {
                            text: '收藏夹接口',
                            link: '/api/collection'
                        },
                        {
                            text: '评论回复接口',
                            link: '/api/comment'
                        },
                        {
                            text: '关注粉丝接口',
                            link: '/api/follow'
                        },
                        {
                            text: '消息接口',
                            link: '/api/message'
                        },
                        {
                            text: '历史记录接口',
                            link: '/api/history'
                        },
                        {
                            text: '弹幕接口',
                            link: '/api/danmaku'
                        },
                        {
                            text: '轮播图相关接口',
                            link: '/api/carousel'
                        },
                        {
                            text: 'Redis配置',
                            link: '/api/redis'
                        },
                    ]
                }
            ]
        }
    }
});