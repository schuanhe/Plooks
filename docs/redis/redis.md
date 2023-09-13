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
| 用户id | String | user:info:nickname:{nickname} |
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
| 视频作者uid | Integer | video:uid:{vid} |
| 视频点击次数 | int | video:clicks:{vid}|

#### video:danmuku
| 备注 | 数据类型 | 路径 | redis数据类型 |
| --- | --- | --- | --- |
| 弹幕列表 | List<Danmuku> | video:info:danmukus:{vid}:{part} | list |

#### video:list
| 备注 | 数据类型 | 路径 |
| --- | --- | --- |
| 视频列表(3分钟自动删除) | List<Video> | video:good:list:{pid}:{siz}:{page} |
| 视频列表数据(3分钟自动删除) | int | video:good:count:{pid} |
| 用户搜索关键词 | List<Video> | video:good:search:{keyword} |

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


### message
| 备注 | 数据类型 | 路径 | redis数据类型 |
| --- | --- | --- | --- |
| 用户回复消息 | List<Message.ReplyMessages> | message:reply:{uid} | list |


### whispers
| 备注 | 数据类型 | 路径 | redis数据类型 |
| --- | --- | --- | --- |
| 用户私信列表(每个用户仅最近一条) | Map<Integer,Objet> | message:whispers:{uid} | Hash  |


### 缓存刷新
| 备注 | 数据类型 | 路径 |
| --- | --- | --- |
| 视频评论缓存刷新 | boolean | refresh:comments:{vid} |
| 轮播图缓存刷新 | boolean | refresh:carousel |
| 视频点击次数缓存刷新(60s) | boolean | refresh:video:clicks:{vid} |
| 用户搜索视频限制(3s) | boolean | refresh:search:{uid} |
