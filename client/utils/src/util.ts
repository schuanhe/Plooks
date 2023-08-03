/**
 * 生成一个指定范围内的随机整数
 * @param min - 最小值
 * @param max - 最大值
 * @returns 一个随机整数
 */
export const getRandom = (min: number, max: number) => {
    return Math.ceil(Math.random() * (max - min) + min);
}

/**
 * 防抖函数，用于减少函数的执行次数
 * @param fn - 需要执行的函数
 * @param delay - 延迟时间，默认为1000ms
 * @returns 一个新的函数
 */
export const debounce = (fn: Function, delay = 1000) => {
    let timer: number | null = null;
    return function () {
        if (timer) {
            clearTimeout(timer)
        }
        timer = window.setTimeout(() => {
            fn(arguments);
            timer = null
        }, delay);
    }
}

/**
 * 节流函数，用于减少函数的执行次数
 * @param fn - 需要执行的函数
 * @param delay - 延迟时间，默认为1000ms
 * @returns 一个新的函数
 */
export const thorttle = (fn: Function, delay = 1000) => {
    let timer: number | null = null;
    return function () {
        if (!timer) {
            timer = window.setTimeout(function () {
                fn(arguments);

                timer = null;
            }, delay);
        }
    };
};

/**
 * 判断一个字符串是否为URL
 * @param url - 需要判断的字符串
 * @returns 如果是URL则返回true，否则返回false
 */
export const isURL = (url: string) => {
    const reg = /http(s)?:\/\/((([\w-]+\.)+[\w-]+)|(localhost))(:\d+)?(\/[\w- ./?%&=#]*)?/;
    return reg.test(url)}

