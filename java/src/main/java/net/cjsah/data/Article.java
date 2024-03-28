package net.cjsah.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import net.cjsah.util.DocUtil;
import net.cjsah.util.JsonUtil;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class Article {
    private final long id;
    private final String title;
    private final String type;
    private final String knowledge;
    private final String parse;
    private final String answer;
    private final List<SubQuestion> questions;
    private final Integer wordCount;

    public static Article fromApiJson(JSONObject json) {
        return new Article(
                json.getLongValue("id"),
                json.getString("title"),
                json.getString("qtype"),
                json.getString("knowledges"),
                json.getString("parse"),
                json.getString("answer2"),
                JsonUtil.jsonGetList(json, "subquestions", SubQuestion::fromJson),
                json.getIntValue("wordNumber")
        );
    }
}
