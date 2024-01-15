package net.cjsah.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

@Data
public class Sentence {
    private final String english;
    private final String chinese;

    public static Sentence fromJson(JSONObject json) {
        return new Sentence(
                json.getString("englishSentence"),
                json.getString("chineseMeaning")
        );
    }

}
