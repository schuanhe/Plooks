## redis数据备注


### login

| 备注 | 数据类型 | 路径 |
| --- | --- | --- |
| 登录错误次数 | int | login:error:{username} |

### user
| 备注 | 数据类型 | 路径 |
| --- | --- | --- |
| 用户邮箱验证码 | String | user:email:{email} |
| 用户认证信息 | UserDetailsImpl | user:info:{uid} |
| 用户信息 | User | user:info:user:{uid} |
| 用户id | String | user:info:username:{username} |
| 用户id | String | user:token:{token} |

### captcha
| 备注 | 数据类型 | 路径 |
| --- | --- | --- |
| 验证码 | String | captcha:{uuid} |

### video
| 备注 | 数据类型 | 路径 |
| --- | --- | --- |
| 视频info信息 | Video | video:info:{vid} |
| 视频作者信息 | User | video:info:author:{vid} |
| 视频资源信息 | List<Resources> | video:info:resources:{vid} |

#### video:danmuku
| 备注 | 数据类型 | 路径 | redis数据类型 |
| --- | --- | --- | --- |
| 弹幕列表 | List<Danmuku> | video:info:danmukus:{vid}:{part} | list |

#### video:list
| 备注 | 数据类型 | 路径 |
| --- | --- | --- |
| 视频列表(3分钟自动删除) | List<Video> | video:good:list:{pid}:{siz}:{page} |
| 视频列表数据(3分钟自动删除) | int | video:good:count:{pid} |

#### video:{uid}
| 备注 | 数据类型 | 路径 | redis数据类型 |
| --- | --- | --- | --- |
| 用户视频列表(3分钟自动删除) | List<Video> | video:list:{uid} | list |
| 用户视频数量 | int | video:count:{uid} | string |

### comments
| 备注 | 数据类型 | 路径 | redis数据类型 |
| --- | --- | --- | --- |
| 评论列表(有评论删除后三分钟删除) | List<Comment> | comments:comment:list:{vid} | list |
| 评论回复列表(有评论删除后三分钟删除) | List<Reply> | comments:reply:list:{cid} | list |


### 缓存刷新
| 备注 | 数据类型 | 路径 |
| --- | --- | --- |
| 视频评论缓存刷新 | boolean | refresh:comments:{vid} |
| 轮播图缓存刷新 | boolean | refresh:carousel |

