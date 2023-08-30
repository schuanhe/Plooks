## redis数据备注


### login

| 备注 | 数据类型 | 路径 |
| --- | --- | --- |
| 登录错误次数 | int | login:error:{username} |

### user
| 备注 | 数据类型 | 路径 |
| --- | --- | --- |
| 用户邮箱验证码 | String | user:email:{email} |
| 用户信息 | User | user:info:{id} |
| 用户id | String | user:info:username:{username} |
| 用户id | String | user:token:{token} |

### captcha
| 备注 | 数据类型 | 路径 |
| --- | --- | --- |
| 验证码 | String | captcha:{uuid} |

### video
| 备注 | 数据类型 | 路径 |
| --- | --- | --- |
| 视频信息 | Video | video:info:{vid} |
| 视频信息(不带资源) | Video | video:info:no:{vid} |