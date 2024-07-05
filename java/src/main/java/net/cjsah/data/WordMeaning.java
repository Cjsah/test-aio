package net.cjsah.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class WordMeaning {
    private final String word;
    private final String meaning;
    private List<TranslateOption> meanings;

    public static WordMeaning fromJson(JSONObject json) {
        String word = json.getString("word");
        String meaning = json.getString("meaning");
        return new WordMeaning(word, meaning == null ? "" : meaning);
    }
}
