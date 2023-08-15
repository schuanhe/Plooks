# 用户相关接口

## 用户注册

#### 请求URL
- ` http://域名/api/v1/user/register `
  
#### 请求方式
- POST 

####  请求头
- `"content-type": "application/json",`

#### 参数

| 参数名   | 必选 | 类型   | 说明   |
| :------- | :--- | :----- | ------ |
| username | 是   | string | 用户名 |
| email    | 是   | string | 邮箱   |
| password | 是   | string | 密码   |
| code     | 是   | string | 验证码 |

#### 返回示例 

``` json
  {
    "code": 200,
    "data": null,
    "message":"ok"
  }
```

#### 备注
无

## 用户登录（账号）

#### 请求URL
- ` http://域名/api/v1/user/login `
  
#### 请求方式
- POST 

####  请求头
- `"content-type": "application/json",`

#### 参数

| 参数名   | 必选 | 类型   | 说明 |
| :------- | :--- | :----- | ---- |
| user.username    | 是   | string | 账号 |
| user.password | 是   | string | 密码 |
| uuid     | 否   | string | uuid |
| code     | 否   | string | 验证码 |

#### 返回示例 
登录成功返回值
```json
  {
    "code": 200,
    "data": {
      "access_token": "",
      "refresh_token": "", 
    },
    "message":"ok"
  }
```

连续登录失败三次会返回值
```json
  {
    "code": -1,
    "data": null,
    "message":"需要人机验证"
  }
```

#### 返回参数说明 

| 参数名        | 类型   | 说明                                |
| :------------ | :----- | ----------------------------------- |
| access_token  | string | 请求使用的token，5分钟内有效        |
| refresh_token | string | 获取access_token的token，14天内有效 |

#### 备注
同一账号连续登录失败三次会返回需要人机验证，此时需要带上uuid和code才可以继续登录

## 用户登录（邮箱）

#### 请求URL
- ` http://域名/api/v1/user/login/email `
  
#### 请求方式
- POST
####  请求头
- `"content-type": "application/json",`

#### 参数

| 参数名 | 必选 | 类型   | 说明   |
| :----- | :--- | :----- | ------ |
| email  | 是   | string | 邮箱   |
| code   | 是   | string | 验证码 |

#### 返回示例 
登录成功返回值
```json
  {
    "code": 200,
    "data": {
      "access_token": "",
      "refresh_token": "" 
    },
    "message":"ok"
  }
```

连续登录失败三次会返回值
```json
  {
    "code": -1,
    "data": null,
    "message":"需要人机验证"
  }
```

#### 返回参数说明 

| 参数名        | 类型   | 说明                                |
| :------------ | :----- | ----------------------------------- |
| access_token  | string | 请求使用的token，5分钟内有效        |
| refresh_token | string | 获取access_token的token，14天内有效 |

#### 备注
同一邮箱连续登录失败三次会返回需要人机验证，此时需要等待30分钟或者调用人机验证接口并通过滑块验证才可以继续登录


## 发送登录邮箱验证码

#### 请求URL
- ` http://域名/api/v1/user/login/email/code `
  
#### 请求方式
- POST 

####  请求头
- `"content-type": "application/json",`

#### 参数

| 参数名 | 必选 | 类型   | 说明     |
| :----- | :--- | :----- | -------- |
| user.email  | 是   | string | 目标邮箱 |
| uuid   | 是   | string | uuid     |
| code   | 是   | string | 验证码   |

#### 返回示例 

```json
  {
    "code": 200,
    "data": null,
    "message":"ok"
  }
```

#### 备注 
无

## 发送注册邮箱验证码

#### 请求URL
- ` http://域名/api/v1/user/register/email `

#### 请求方式
- POST

####  请求头
- `"content-type": "application/json",`

#### 参数

| 参数名 | 必选 | 类型   | 说明     |
| :----- | :--- | :----- | -------- |
| user.email  | 是   | string | 目标邮箱 |
| uuid   | 是   | string | uuid     |
| code   | 是   | string | 验证码   |

#### 返回示例

```json
  {
    "code": 200,
    "data": null,
    "message":"ok"
  }
```

#### 备注
无

## 通过用户ID获取用户信息

#### 请求URL
- ` http://域名/api/v1/user/info/{用户ID} `
  
#### 请求方式
- GET 

#### 返回示例 

``` json
{
    "code": 200,
    "message": "success",
    "data": {
        "username": "schuanhe",
        "nickname": "默认昵称",
        "gender": 0,
        "sign": "这个人很懒，什么都没有留下",
        "uid": 1
    }
}
```


#### 返回参数说明 

| 参数名     | 类型   | 说明                           |
| :--------- | :----- | ------------------------------ |
| uid        | int    | 用户ID                         |
| nickname       | string | 用户昵称         |
| sign       | string | 个性签名                       |
| avatar     | string | 头像                           |
| spacecover | string | 用户空间封面图                 |
| gender     | int    | 用户性别，0：未知;1：男；2：女 |

#### 备注
无

## 通过用户名获取用户id

#### 请求URL
- ` http://域名/api/v1/user/id/{用户名} `
  
#### 请求方式
- GET 

#### 请求参数
- 见url

#### 返回示例 

``` json
{
    "code": 200,
    "message": "success",
    "data": {
        "uid": 1
    }
}
```

#### 返回参数说明 

| 参数名 | 类型 | 说明   |
| :----- | :--- | ------ |
| uid    | int  | 用户ID |

#### 备注
无

## 修改密码验证

#### 请求URL
- ` http://域名/api/v1/user/modify/email/code `
  
#### 请求方式
- Post

#### 请求参数
```json
{
    "user": {
      "email": "邮箱"
    },
    "code": "验证码",
    "uuid": "uuid"
}
```

#### 返回示例 

``` json
  {
    "code": 200,
    "data": null,
    "message": "success"
}
```

#### 备注
无


## 修改密码

#### 请求URL
- ` http://域名/api/v1/user/modify `
  
#### 请求方式
- POST 

####  请求头
- `"content-type": "application/json",`

#### 参数

| 参数名   | 必选 | 类型   | 说明       |
| :------- | :--- | :----- | ---------- |
| email    | 是   | string | 邮箱       |
| password | 是   | string | 新密码     |
| code     | 是   | string | 邮箱验证码 |

#### 返回示例 

```json
  {
    "code": 200,
    "data": null,
    "message": "success"
  }
```

#### 备注 
需要先调用[修改密码验证](#修改密码验证)接口 


## 刷新token

#### 请求URL
- ` http://域名/api/v1/user/token/refresh`
  
#### 请求方式
- GET 

#### 请求头
- `Authorization': refresh_token `
- `"content-type": "application/json"`

#### 返回示例 

``` json
  {
    "code": 200,
    "data": {
      "token": "" 
    },
    "message": "success"
}
```

#### 返回参数说明 

| 参数名 | 类型   | 说明         |
| :----- | :----- | ------------ |
| token  | string | access_token |

#### 备注
需要refresh_token


## 用户获取个人信息

#### 请求URL
- ` http://域名/api/v1/user/info `
  
#### 请求方式
- GET 

#### 请求头
- `Authorization': access_token `
- `"content-type": "application/json"`

#### 返回示例 

``` json
  {
    "code": 200,
    "data": {
      "user_info": {
        "uid": 1,
        "nickname": "昵称",
        "sign": "个性签名",
        "email": "1****1@qq.com",
        "avatar": "",
        "spacecover": "",
        "gender": 1,
        "role": 0,
        "birthday":"",
        "created_at": ""
      }
    },
    "message": "success"
}
```

#### 返回参数说明 

| 参数名     | 类型   | 说明                           |
| :--------- | :----- | ------------------------------ |
| uid        | int    | 用户ID                         |
| name       | string | 用户名                         |
| sign       | string | 个性签名                       |
| email      | string | 用户邮箱（脱敏后数据）         |
| avatar     | string | 头像                           |
| spacecover | string | 用户空间封面图                 |
| gender     | int    | 用户性别，0：未知;1：男；2：女 |
| role       | int    | [用户身份](/api/#用户身份)     |
| birthday   | time   | 生日                           |
| created_at | time   | 注册时间                       |

#### 备注
无

 
## 用户修改个人信息

#### 请求URL
- ` https://域名/api/v1/user/info `
  
#### 请求方式
- PUT 

#### 请求头
- `Authorization': access_token `
- `"content-type": "application/json"`

#### 参数

| 参数名   | 必选 | 类型               | 说明                 |
| :------- | :--- | :----------------- | -------------------- |
| avatar   | 是   | string             | 用户头像             |
| name     | 是   | string             | 用户名               |
| birthday | 是   | string（日期格式） | 生日（默认1970-1-1） |
| gender   | 否   | int                | 性别（默认为0未知）  |
| sign     | 否   | string             | 个性签名             |

#### 返回示例 

``` json
{
  "code": 200,
  "data": null,
  "massage": "success"
}
```

#### 备注
无


## 用户修改空间封面图

#### 请求URL
- ` https://域名/api/v1/user/cover `
  
#### 请求方式
- PUT 

#### 请求头
- `Authorization': access_token `
- `"content-type": "application/json"`

#### 参数

| 参数名     | 必选 | 类型   | 说明      |
| :--------- | :--- | :----- | --------- |
| spacecover | 是   | string | 封面图url |

#### 返回示例 

``` json
{
  "code": 200,
  "data": null,
  "massage": "success"
}
```

#### 备注
封面图url必须为调用图片上传接口返回的url

