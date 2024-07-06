package net.cjsah.util;

import net.cjsah.data.WordNode;

import java.util.Arrays;
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

    public static String ofWords(List<WordNode> words) {
        if (words.isEmpty()) return "";
        StringBuilder builder = new StringBuilder("<ol>");
        for (WordNode word : words) {
            builder.append("<li><span><b>");
            builder.append(word.getWord());
            builder.append("</b>&nbsp;");
            builder.append(word.getEnglishPronunciation());
            Arrays.stream(word.getMeaning().split("(<br/>|\\n)"))
                    .filter(meaning -> !meaning.trim().isEmpty() && !meaning.startsWith("*"))
                    .map(it -> it.replace("&", "&amp;"))
                    .forEach(meaning -> {
                        builder.append("<br/>&nbsp;&nbsp;&nbsp;");
                        builder.append(meaning);
                    });
            builder.append("</span></li>");
        }
        builder.append("</ol>");
        return ofContent(builder.toString());
    }

}
