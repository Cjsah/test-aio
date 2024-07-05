package net.cjsah.util;

import java.util.List;
import java.util.stream.Collectors;

public class HtmlUtil {
    public static String ofContent(String content) {
        return "<html><body>" + content + "</body></html>";
    }

    public static String ofIndent(List<String> tips) {
        String content = tips.stream()
                .map(it -> "<p style=\"text-indent: 24px;\">" + it + "</p>")
                .collect(Collectors.joining());
        return ofContent(content);
    }

//    public static String ofWord(String content, String tag) {
//
//
//
//    }

}
