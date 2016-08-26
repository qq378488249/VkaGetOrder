package cc.chenghong.vkagetorder.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * Created by 何成龙 on 2016/7/8.
 */
public class StringUtils {

    public final static String REG_DIGIT = "[0-9]*";
    public final static String REG_CHAR = "[a-zA-Z]*";
    public final static String EMPTY = "";

    /**
     * 判断是否为空或者值是否为null
     *
     * @param obj
     * @return true为空或者null，false不为空
     */
    public static boolean isEmpty(Object... obj) {
        if (obj == null)
            return true;
        for (Object object : obj) {
            if (object == null)
                return true;
            if (object.toString().trim().length() == 0)
                return true;
        }
        return false;
    }

    /**
     * 判断对象不为空是否成立
     *
     * @param obj
     * @return true为非空或者非null，false为空或为null
     */
    public static boolean noEmpty(Object... obj) {
        if (obj == null)
            return false;
        for (Object object : obj) {
            if (object == null)
                return false;
            if (object.toString().trim().length() == 0)
                return false;
        }
        return true;
    }

    public static boolean isBlankEmpty(Object obj) {
        if (obj == null || "".equals(obj) || "".equals(obj.toString().trim()) || "null".equalsIgnoreCase(obj.toString()))
            return true;

        return false;
    }

    /**
     * 是否空,或者为空串,或者为"null"
     *
     * @author guoyu
     */
    public static boolean isBlankEmpty(Object... objs) {
        if (objs == null || objs.length == 0)
            return true;
        for (Object obj : objs) {
            if (isBlankEmpty(obj)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNotBlank(String pattern) {
        return !isBlankEmpty(pattern);
    }

    public static boolean isBlank(String pattern) {
        return isBlankEmpty(pattern);
    }

    public static String formatCountNames(String nameList) {
        String[] names = nameList.split(",");

        Map<String, Integer> nameCount = new HashMap<String, Integer>();
        for (String name : names) {
            if (StringUtils.isEmpty(name)) continue;
            if (nameCount.containsKey(name)) {
                Integer count = nameCount.get(name) + 1;
                nameCount.put(name, count);
            } else {
                nameCount.put(name, 1);
            }
        }

        StringBuilder newNames = new StringBuilder();
        for (String key : nameCount.keySet()) {
            if (StringUtils.isEmpty(key)) continue;
            Integer count = nameCount.get(key);
            String splitChar = newNames.length() > 0 ? "," : "";
            newNames.append(splitChar).append(key).append("x").append(count);
        }

        return newNames.toString();
    }


    public static boolean isDigit(String str) {
        return str.matches(REG_DIGIT);
    }

    public static boolean isChar(String str) {
        return str.matches(REG_CHAR);
    }

    public static Boolean isNotEmpty(Object... obj) {
        Boolean r = StringUtils.isEmpty(obj);
        return !r;
    }

    public static boolean isNumeric(String str) {
        if (isBlankEmpty(str)) return false;
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 过滤空白字符
     *
     * @author guoyu
     */
    public static String replaceBlank(String str) {
        if (str != null && !"".equals(str)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            String strNoBlank = m.replaceAll("");
            return strNoBlank;
        } else {
            return str;
        }
    }

    public static void main(String[] args) {
        System.out.println(replaceBlank("MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKZMkymRTMIBhmVs\n" +
                "sFc+EpydeSNtwNWATVJwDPuodAbY49+ylaeMVRxtBLp1h/gQfIERLMrB5e81q9/D\n" +
                "JVG9uK+kHuqcyEC5B9pkoqOb7PJhEPuQJlB3R4eyDuiUFEUwNaD9IBc9bNU5UgD5\n" +
                "tly5GJQtFDu3B+rAtqlHbihZGKnxAgMBAAECgYEAg9MpyQasMRIiZl4NP2suN8ZA\n" +
                "08vZ06S8JoA6WneDWjYuFRKsvT9p7HmjCOfIG+V9vJZPyn5/9eq2z08TU0OwlQvE\n" +
                "tsAqi1qwFaAB3iWGUiASImeblw73JtagINBIIroL0s1W+2w2jLIaL2eiPh62D129\n" +
                "HxS6FkvHAlp7SURJwB0CQQDPrperi4kmvDv73adLlar83f5zbB9XkG8HDHMWtOi+\n" +
                "1T49oucJjui+IBx5muS99ZtiLIU+FJ6wSTgi6F4zR0XHAkEAzP0+j9beTqAyVsaj\n" +
                "+4XQJMDlFf7+jsy+q1H5tBDGFl5gP7RV6JdW6/TdfTnwH70j3ykcBMxabdxKVuF5\n" +
                "3g4yhwJBAKDgf078jSa7Y7/saN6tNsIg+S3SEMlU8hzEsRZi13SUXV2DpnMKCBLX\n" +
                "bxdoPE9GRbKcCC8Z6/9lCJ5J4EbarBsCQQC7VSP7Z625xQS78MSjc+KH9BBnTaVs\n" +
                "8we8oSnFuR6OCqmDDlcEPGofMKPJcU1UdBF4O6VzbR2nEI6PX3dzyx25AkBUetWs\n" +
                "KmcPO7Tq4rBFcJar2Lfs9Avo3FqD3hF6Sjz1mMB5/Ip+1sJypnheoMA0gIYKghg/\n" +
                "EcK1LMtp+tHF2ja2"));
        System.out.println(replaceBlank("MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKZMkymRTMIBhmVssFc+EpydeSNtwNWATVJwDPuodAbY49+ylaeMVRxtBLp1h/gQfIERLMrB5e81q9/DJVG9uK+kHuqcyEC5B9pkoqOb7PJhEPuQJlB3R4eyDuiUFEUwNaD9IBc9bNU5UgD5tly5GJQtFDu3B+rAtqlHbihZGKnxAgMBAAECgYEAg9MpyQasMRIiZl4NP2suN8ZA08vZ06S8JoA6WneDWjYuFRKsvT9p7HmjCOfIG+V9vJZPyn5/9eq2z08TU0OwlQvEtsAqi1qwFaAB3iWGUiASImeblw73JtagINBIIroL0s1W+2w2jLIaL2eiPh62D129HxS6FkvHAlp7SURJwB0CQQDPrperi4kmvDv73adLlar83f5zbB9XkG8HDHMWtOi+1T49oucJjui+IBx5muS99ZtiLIU+FJ6wSTgi6F4zR0XHAkEAzP0+j9beTqAyVsaj+4XQJMDlFf7+jsy+q1H5tBDGFl5gP7RV6JdW6/TdfTnwH70j3ykcBMxabdxKVuF53g4yhwJBAKDgf078jSa7Y7/saN6tNsIg+S3SEMlU8hzEsRZi13SUXV2DpnMKCBLXbxdoPE9GRbKcCC8Z6/9lCJ5J4EbarBsCQQC7VSP7Z625xQS78MSjc+KH9BBnTaVs8we8oSnFuR6OCqmDDlcEPGofMKPJcU1UdBF4O6VzbR2nEI6PX3dzyx25AkBUetWsKmcPO7Tq4rBFcJar2Lfs9Avo3FqD3hF6Sjz1mMB5/Ip+1sJypnheoMA0gIYKghg/EcK1LMtp+tHF2ja2"));
    }

    /**
     * 获取字节数
     *
     * @param s
     * @return
     */
    public static int getByteCount(String s) {
        s = s.replaceAll("[^\\x00-\\xff]", "**");
        int length = s.length();
        return length;
    }

}
