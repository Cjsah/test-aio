package net.cjsah.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import net.cjsah.util.DocUtil;

@Data
public class SubQuestion {
    private final String title;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;

    public static SubQuestion fromJson(JSONObject json) {
        return new SubQuestion(
                json.getString("title"),
                json.getString("optionA"),
                json.getString("optionB"),
                json.getString("optionC"),
                json.getString("optionD")
        );
    }

    public DocUtil.ParseProgress parse() {
        DocUtil.ParseProgress progress = DocUtil.parseHtmlNodeWithTrim(this.title, false);
        appendOption(progress, this.optionA, 'A');
        appendOption(progress, this.optionB, 'B');
        appendOption(progress, this.optionC, 'C');
        appendOption(progress, this.optionD, 'D');
        return progress;
    }

    private static void appendOption(DocUtil.ParseProgress progress, String option, char select) {
        DocUtil.ParseProgress trim = DocUtil.parseHtmlNodeWithTrim(select + ". " + option, false);
        progress.getNodes().addAll(trim.getNodes());
        progress.getAfters().addAll(trim.getAfters());
    }
}
