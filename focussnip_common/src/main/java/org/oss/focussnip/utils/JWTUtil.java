package org.oss.focussnip.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JWTUtil {

    private static final Logger log = LoggerFactory.getLogger(JWTUtil.class);

    public static String generTokenByRS256(String username) throws Exception{

        RSA256Key rsa256Key = SecretKeyUtil.getRSA256Key(); // 获取公钥/私钥
        Algorithm algorithm = Algorithm.RSA256(rsa256Key.getPublicKey(),rsa256Key.getPrivateKey());

        return sign(username, algorithm);
    }

    /**
     * 校验 token是否正确
     *
     * @param token  密钥
     * @param username 用户名
     * @return 是否正确
     */
    public static boolean verify(String token, String username) {
        try {
            RSA256Key rsa256Key = SecretKeyUtil.getRSA256Key(); // 获取公钥/私钥
            Algorithm algorithm = Algorithm.RSA256(rsa256Key.getPublicKey(),rsa256Key.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .withIssuer(Config.JWT_ISSUER)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt!=null;
        } catch (Exception e) {
            log.info("token is invalid{}", e.getMessage());
            return false;
        }
    }



    /**
     * 获得token中自定义用户信息
     * @param token
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(Config.JWT_CLIENT_USERNAME).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名
     *
     * @param username   用户名
     * @param algorithm     签名算法
     * @return 加密的token
     */
    private static String sign(String username, Algorithm algorithm) throws UnsupportedEncodingException {

        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        long currentTimes = System.currentTimeMillis();

        Date signDate = new Date(currentTimes);

        Date expireTime = new Date(currentTimes + Config.JWT_ACTIVE_TIME);

        return JWT.create()
                .withHeader(map)
                .withClaim(Config.JWT_CLIENT_USERNAME, username)
                .withSubject(username)
                .withIssuer(Config.JWT_ISSUER)
                .withExpiresAt(expireTime)
                .withIssuedAt(signDate)
                .sign(algorithm);
    }

}
