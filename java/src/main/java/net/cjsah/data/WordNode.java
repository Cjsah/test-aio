package net.cjsah.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import net.cjsah.util.JsonUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class WordNode {
    private final String word;
    private final String meaning;
    private final String englishPronunciation;
    private final String americaPronunciation;
    private final List<Sentence> sentence;
    private final List<String> meanings;

    public WordNode(String word) {
        this(word, "", "", "", null);
    }

    public WordNode(String word, String meaning, String englishPronunciation, String americaPronunciation, List<Sentence> sentence) {
        this.word = word;
        this.meaning = meaning;
        this.englishPronunciation = englishPronunciation;
        this.americaPronunciation = americaPronunciation;
        this.sentence = sentence;
        this.meanings = Arrays.stream(meaning.split("(<br/>|\\n)"))
                .filter(it -> !it.trim().isEmpty() && !it.startsWith("*"))
                .collect(Collectors.toList());
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

        meaning = meaning.replace("&", "&amp;").replaceAll("(<br/>|<br>)", "");

        return new WordNode(word, meaning, englishPronunciation, americaPronunciation, sentence);
    }
}
