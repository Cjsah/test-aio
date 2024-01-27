package net.cjsah.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.cjsah.util.JsonUtil;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class WordNode {
    private final String word;
    private final String meaning;
    private final String englishPronunciation;
    private final String americaPronunciation;
    private final List<Sentence> sentence;

    public WordNode(String word) {
        this(word, "", "", "", null);
    }

    public static WordNode fromJson(JSONObject json) {
        return new WordNode(
                json.getString("word"),
                json.getString("meaning").replace("&", "&amp;").replace("<br>", ""),
                json.getString("englishPronunciation"),
                json.getString("americaPronunciation"),
                JsonUtil.jsonGetList(json, "sentence", Sentence::fromJson)
        );
    }
}
