/**
 * 将时间字符串转换为格式为“yyyy-mm-dd”的日期字符串。
 * @param time 要转换的时间字符串。
 * @returns 格式为“yyyy-mm-dd”的日期字符串。
 */
export const timeStrToDateStr = (time: string) => {
    const date = new Date(time);
    const year = date.getFullYear()
    // 由于 getMonth 返回值会比正常月份小 1
    const month = date.getMonth() + 1;
    const day = date.getDate();

    return `${year}-${month}-${day}`
}


/**
 * 获取给定时间与当前时间之间的相对时间。
 * @param time 要获取相对时间的时间。
 * @returns 表示给定时间与当前时间之间的相对时间的字符串。
 */
export const relativeDate = (time: string) => {
    const originalTime = new Date(time);
    const currentTime = new Date();

    const originalYear = originalTime.getFullYear()
    const originalMonth = originalTime.getMonth() + 1;
    const originalDay = originalTime.getDay();

    if (currentTime.getFullYear() !== originalYear || currentTime.getMonth() + 1 != originalMonth) {
        return `${originalYear}-${originalMonth}-${originalDay}`;
    }

    switch (currentTime.getDay() - originalDay) {
        case 0:
            return "今天";
        case 1:
            return "昨天";
        case 2:
            return "前天";
        default:
            return `${originalYear}-${originalMonth}-${originalDay}`;
    }
}
