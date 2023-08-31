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

#### video:list
| 备注 | 数据类型 | 路径 |
| --- | --- | --- |
| 视频列表(3分钟自动删除) | List<Video> | video:good:list:{pid}:{siz}:{page} |
| 视频列表数据(3分钟自动删除) | int | video:good:count:{pid} |

