package net.cjsah.main;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String text = "${a},${a},${b},${c}";
        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");

        Pattern PATTERN = Pattern.compile("\\$\\{[a-zA-Z0-9]+}");

        Matcher matcher = PATTERN.matcher(text);
        System.out.println(matcher.replaceAll(result -> {
            String key = result.group();
            key = key.substring(2, key.length() -1);
            return map.getOrDefault(key, "{" + key + "}");
        }));

    }
}
