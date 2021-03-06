import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Jwt {

    /**
     * 参数列表：
     * args[0] command：encode 编码，decode 解码。
     * args[1] secret：秘钥。
     * args[2] claim1=value1：数据项1=数据项1的值。
     * ...
     * args[N] claimN[=valueN]：数据项N=数据项N的值。注意，"=数据项N的值"是可选的，当不传值时会使用默认值。
     * <p>
     * shell script example:
     * java -jar jwt.jar \
     * encode irVWQwBj7hEAvUMEEXTBCC0g1QMy1kgUUphIQcDjRwdiQU1DeHPiLpuNFEqieavK \
     * issuer=sunnymix.com \
     * subject=sunnymix.com \
     * audience=Login \
     * accountId=123 \
     * accountType=STAFF \
     * name=Sunny
     *
     * @param args 参数列表
     */
    public static void main(String[] args) {
        Map<String, Object> argMap = _parseArgMap(args);
        if (argMap == null) {
            System.out.println("args not enough");
            return;
        }
        String command = argMap.get(command_key).toString();
        argMap.remove(command_key);
        if (command_encode.equals(command)) {
            encode(argMap);
        } else if (command_decode.equals(command)) {
            decode(argMap);
        }
    }

    private static void encode(Map<String, Object> argMap) {
        JwtBuilder jwt = Jwts.builder();
        jwt.setId(UUID.randomUUID().toString().replace("-", ""));

        jwt.setIssuer(argMap.getOrDefault(issuer_key, "example.org").toString());
        argMap.remove(issuer_key);

        jwt.setSubject(argMap.getOrDefault(subject_key, "example.org").toString());
        argMap.remove(subject_key);

        jwt.setAudience(argMap.getOrDefault(audience_key, "Login").toString());
        argMap.remove(audience_key);

        String secret = argMap.getOrDefault(secret_key, "").toString();
        argMap.remove(secret_key);

        SignatureAlgorithm signAlgorithm = SignatureAlgorithm.HS512;
        Key signKey = new SecretKeySpec(secret.getBytes(), signAlgorithm.getJcaName());
        jwt.signWith(signKey, signAlgorithm);

        int expiry = Integer.parseInt(argMap.getOrDefault(expiry_key, "7").toString());
        argMap.remove(expiry_key);

        long nowMillis = System.currentTimeMillis();
        Date nowDate = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + (expiry * day_millis));
        jwt.setIssuedAt(nowDate).setExpiration(expiryDate).setNotBefore(nowDate);

        jwt.addClaims(argMap);
        String jwtString = jwt.compact();
        System.out.println(jwtString);
    }

    private static void decode(Map<String, Object> argMap) {

    }

    private static final String command_key = "command";
    private static final String command_encode = "encode";
    private static final String command_decode = "decode";

    private static final String secret_key = "secret";

    private static final String kvGap = "=";

    private static final String issuer_key = "issuer";
    private static final String subject_key = "subject";
    private static final String audience_key = "audience";
    private static final String expiry_key = "expiry";

    private static final long day_millis = 1000 * 60 * 60 * 24;

    private static boolean _isNumber(String v) {
        return v.matches("-?\\d+(\\.\\d+)?");
    }

    private static Map<String, Object> _parseArgMap(String[] args) {
        if (args.length < 2) {
            return null;
        }
        Map<String, Object> argMap = new HashMap<>();
        argMap.put(command_key, args[0]);
        argMap.put(secret_key, args[1]);
        for (int i = 2; i < args.length; i++) {
            String arg = args[i];
            String[] kv = arg.split(kvGap);
            if (kv.length > 0) {
                String k = kv[0];
                Object v = null;
                if (kv.length > 1) {
                    String vStr = kv[1];
                    if (_isNumber(vStr)) {
                        v = Long.parseLong(vStr);
                    } else {
                        v = vStr;
                    }
                }
                argMap.put(k, v);
            }
        }
        return argMap;
    }

}
