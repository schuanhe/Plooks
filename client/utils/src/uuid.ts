/**
 * 生成一个唯一的 UUID 字符串。
 * 
 * @returns 一个 UUID 字符串。
 */
export const createUuid = () =>{
    const tmp_url = URL.createObjectURL(new Blob());
    const uuid = tmp_url.split('/').pop() as string;
    URL.revokeObjectURL(tmp_url);
    return uuid;
}
