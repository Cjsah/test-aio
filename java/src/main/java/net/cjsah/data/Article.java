package net.cjsah.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import net.cjsah.main.doc.DocUtil;
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
    private final String questions;
    private final Integer wordCount;

    public static Article fromApiJson(JSONObject json) {
        List<SubQuestion> subQuestions = JsonUtil.jsonGetList(json, "subquestions", SubQuestion::fromJson);
        return new Article(
                json.getLongValue("id"),
                json.getString("title"),
                json.getString("qtype"),
                json.getString("knowledges"),
                DocUtil.htmlToStr(json.getString("parse")),
                DocUtil.htmlToStr(json.getString("answer2")),
                subQuestions.stream().parallel().map(SubQuestion::getQuestion).collect(Collectors.joining("")),
                json.getIntValue("wordNumber")
        );
    }
}
