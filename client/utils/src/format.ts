/**
 * 判断是否是邮箱
 * @param str 邮箱
 */
export const isEmail = (str: string) => {
    const reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
    return reg.test(str);
}


/**
 * 判断是否是手机号
 * @param str 手机号 
 */
export const isPhone = (str: string) => {
    const reg = /^1[3|4|5|6|7|8|9][0-9]{9}$/;
    return reg.test(str);
}