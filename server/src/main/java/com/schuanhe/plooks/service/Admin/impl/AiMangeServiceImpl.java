package com.schuanhe.plooks.service.Admin.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.schuanhe.plooks.mapper.AiMangeMapper;
import com.schuanhe.plooks.service.Admin.AiMangeService;
import com.schuanhe.plooks.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AiMangeServiceImpl implements AiMangeService {

    @Value("${plooks.ai.url}")
    private String aiUrl;  // 请求地址

    @Autowired
    private AiMangeMapper aiMangeMapper;

    @Autowired
    private RedisCache redisCache;


    @Override
    public Map<String, Object> getAiData(String q) throws Exception{
        log.info("[AI数据]问题为：{}",q);
        String aiSql = getSql(q);
        Map<String,Object> data = new HashMap<>();
        JSONObject AiJsonObject = JSON.parseObject(aiSql);
        String sqlString = AiJsonObject.getString("sql");
        List<?> columns = AiJsonObject.getObject("columns", List.class);
        List<Map<String, Object>> maps = aiMangeMapper.findByCriteria(sqlString);
        data.put("sql",sqlString);
        data.put("columns",columns);
        data.put("data",maps);
        log.info("[AI数据]返回数据为：{}",data);

        return data;
    }


    public String getSql(String content) {
        try {
            // 创建URL对象
            URL url = new URL(this.aiUrl + content);
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
                JSONObject jsonObject = JSON.parseObject(responseData);
                String sql = jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                System.out.println("响应数据：" + jsonObject);

                redisCache.setCacheList("logs:ai:",jsonObject);
                return sql;
            } else {
                log.error("AiMangeServiceImpl.getSql() q:{} error: {}", content, responseCode);
            }
            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {
            log.error("AiMangeServiceImpl.getSql() q:{} error: {}", content, e.getMessage());
        }
        return null;
    }


}
