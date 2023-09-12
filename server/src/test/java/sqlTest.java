import com.alibaba.fastjson.JSON;


import com.schuanhe.plooks.mapper.TestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = com.schuanhe.plooks.Application.class)
public class sqlTest {

    @Autowired
    private TestMapper testMapper;

    @Test
    public void teset() {

        String sql = getSql("查询20个用户id包含1的用户");
        System.out.println(sql);

        String sql1 = JSON.parseObject(sql).getString("sql");
        System.out.println(sql1);

        //String sql1 = "select * from user;";

        List<Map<String, Object>> maps = testMapper.findByCriteria(sql1);

        Object json = JSON.toJSON(maps);
        //List<Object> maps = testMapper.findByCriteria(sql1);
        System.out.println(json);

    }



    // 获取sql语句
    public static String getSql(String content) {

        try {
            // 创建URL对象
            URL url = new URL("https://xxxx/api/6-openai/plooks.php?msg=" + content);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法为GET
            connection.setRequestMethod("GET");
            // 获取响应码
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取响应数据
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // 处理响应数据，这里可以根据你的需求进行处理
                String responseData = response.toString();

                // 取出JSON.choices[0].message.content
                String sql = JSON.parseObject(responseData).getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                System.out.println(content);
                System.out.println("响应数据：" + responseData);
                return sql;
            } else {
                System.out.println("请求失败，响应码：" + responseCode);
            }

            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
