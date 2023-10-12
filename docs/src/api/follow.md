# 关注粉丝相关接口

## 通过用户ID获取关注列表

#### 请求URL
- ` http://域名/api/v1/follow/following/用户ID}/{page}/{page_size} `
  
#### 请求方式
- GET 

#### 请求参数
- 见url

内容数量 0 < 内容数量 < 30

#### 返回示例 

``` json
{
    "code": 200,
    "data": {
        "users": [
            {
                "uid": 1,
                "name": "user_1654250698886",
                "sign": "这个人很懒，什么都没有留下",
                "avatar": "",
                "spacecover": "",
                "gender": 0
            }
        ]
    },
    "mssage": "success"
}
```

#### 返回参数说明 

| 参数名     | 类型   | 说明                           |
| :--------- | :----- | ------------------------------ |
| uid        | int    | 用户ID                         |
| name       | string | 用户名                         |
| sign       | string | 个性签名                       |
| avatar     | string | 头像                           |
| spacecover | string | 用户空间封面图                 |
| gender     | int    | 用户性别，0：未知;1：男；2：女 |

#### 备注 
无


## 通过用户ID获取粉丝列表

#### 请求URL
- ` http://域名/api/v1/follow/follower/{用户ID}/{page_size}/{page} `
  
#### 请求方式
- GET 

#### 请求参数
- 见url

内容数量 0 < 内容数量 < 30

#### 返回示例 

``` json
{
    "code": 200,
    "data": {
        "users": [
            {
                "uid": 1,
                "name": "user_1654250698886",
                "sign": "这个人很懒，什么都没有留下",
                "avatar": "",
                "spacecover": "",
                "gender": 0
            }
        ]
    },
    "mssage": "success"
}
```

#### 返回参数说明 

| 参数名     | 类型   | 说明                           |
| :--------- | :----- | ------------------------------ |
| uid        | int    | 用户ID                         |
| name       | string | 用户名                         |
| sign       | string | 个性签名                       |
| avatar     | string | 头像                           |
| spacecover | string | 用户空间封面图                 |
| gender     | int    | 用户性别，0：未知;1：男；2：女 |

#### 备注 
无


## 通过用户ID获取关注和粉丝数据

#### 请求URL
- ` http://域名/api/v1/follow/count/{用户ID} `
  
#### 请求方式
- GET 

#### 返回示例 

``` json
{
    "code": 200,
    "data": {
        "followers": 1,
        "following": 0
    },
    "mssage": "success"
}
```

#### 返回参数说明 

| 参数名    | 类型 | 说明   |
| :-------- | :--- | ------ |
| followers | int  | 粉丝数 |
| following | int  | 关注数 |

#### 备注
无


## 获取关注状态

#### 请求URL
- ` http://域名/api/v1/follow/status/{被关注的人的id} `
  
#### 请求方式
- GET 

#### 请求头
- `Authorization': access_token `

#### 返回示例 

``` json
{
    "code": 200,
    "data": {
        "follow": true,
    },
    "mssage": "success"
}
```

#### 返回参数说明 

| 参数名 | 类型 | 说明     |
| :----- | :--- | -------- |
| follow | bool | 是否关注 |

#### 备注
无


##  关注用户

#### 请求URL
- ` http://域名/api/v1/follow/{uid} `
  
#### 请求方式
- POST 

####  请求头
- `"content-type": "application/json",`
- `Authorization': access_token`


### 返回示例 

``` json
  {
    "code": 200,
    "data": null,
    "mssage":"success"
  }
```

#### 备注
无


##  取消关注用户

#### 请求URL
- ` http://域名/api/v1/follow/{uid}
  
#### 请求方式
- DELETE

####  请求头
- `"content-type": "application/json",`
- `Authorization': access_token`


### 返回示例 

``` json
  {
    "code": 200,
    "data": null,
    "mssage":"success"
  }
```

#### 备注
无