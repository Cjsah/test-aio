package net.cjsah.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.cjsah.util.JsonUtil;

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
        String word = json.getString("word");
        String meaning = json.getString("meaning");
        String englishPronunciation = json.getString("englishPronunciation");
        String americaPronunciation = json.getString("americaPronunciation");
        List<Sentence> sentence = JsonUtil.jsonGetList(json, "sentence", Sentence::fromJson);

        if (meaning == null) meaning = "";
        if (englishPronunciation == null) englishPronunciation = "";
        if (americaPronunciation == null) americaPronunciation = "";

        meaning = meaning.replace("&", "&amp;").replace("<br/>", "");

        return new WordNode(word, meaning, englishPronunciation, americaPronunciation, sentence);
    }
}
