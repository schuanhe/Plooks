package com.schuanhe.plooks.utils;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JwtUtil {

    //默认效期为
    public static final Long JWT_TTL =  60 * 60 *1000L;// 12 * 60 * 60 *1000  12个小时
    //设置秘钥明文
    public static final String JWT_KEY = "schuanhePlooks";

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @return token
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间
        return builder.compact();
    }

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间 单位毫秒
     * @return token
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer("schuanhe")     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    /**
     * 创建token
     * @param id 唯一id
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return token
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2ODYyMzg1MDNlY2U0MTc4YjQ4OTU4MjU1NWI4ZGYyOSIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTY4MDMxOTA0NiwiZXhwIjoxNjgwMzIyNjQ2fQ.vxyTVsG00RhZTBaTiI6p2X_eldmPNNDVoIr7xO7kUgM";
        Claims claims = null;

        try {
             claims = parseJWT(token);
        } catch (ExpiredJwtException e) {
            System.out.println("token已过期");
            return;
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            System.out.println("token格式错误");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("token为空");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            System.out.println("token解析完成");
        }

        assert claims != null;
        System.out.println(claims.getSubject());
    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return secretKey
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }


    /**
     * 解析jwt
     * @param jwt token
     * @return Claims
     * @throws Exception 异常
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


    /**
     * 获取token中的数据
     * @param refreshToken 刷新token
     * @return  token
     */
    public static String getUserid(String refreshToken) throws Exception {
        Claims claims = parseJWT(refreshToken);
        return claims.getSubject();
    }

    /**
     * 通过token获取用户id，不抛出异常
     * @param token token
     * @return 用户id(错误返回0)
     */
    public static Integer getUseridNoException(String token) {
        try {
            String userid = getUserid(token);
            return Integer.valueOf(userid);
        } catch (Exception e) {
            return 0;
        }
    }
}