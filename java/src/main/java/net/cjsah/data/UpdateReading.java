package net.cjsah.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import net.cjsah.util.JsonUtil;

import java.util.List;

@Data
public class UpdateReading {
    private final long id;
    private final List<WordNode> words;
    private final List<Article> articles;
    private final List<WordNode> overWords;
    private final WordLabel middleWords;

    public static UpdateReading fromJson(JSONObject json) {
        long id = json.getLongValue("paperId");
        List<WordNode> words = JsonUtil.jsonGetList(json, "studyWordList", WordNode::fromJson);
        List<WordNode> overWords = JsonUtil.jsonGetList(json, "overWords", WordNode::fromJson);
        List<Article> articles = JsonUtil.jsonGetList(json, "questions", Article::fromApiJson);
        List<WordMeaning> middleWords = JsonUtil.jsonGetList(json, "middleWordList", WordMeaning::fromJson);
        WordLabel wordLabel = new WordLabel(id, middleWords);
        return new UpdateReading(id, words, articles, overWords, wordLabel);
    }
}
