package net.cjsah.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

@Data
public class Article {
    private final long id;
    private final String title;
    private final String type;
    private final String knowledge;
    private final String parse;
    private final String answer;

    public static Article fromJson(JSONObject json) {
        return new Article(
                json.getIntValue("id"),
                json.getString("title"),
                json.getString("qtype"),
                json.getString("knowledges"),
                json.getString("parse"),
                json.getString("answer2")
        );
    }
}
