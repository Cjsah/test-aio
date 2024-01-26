package net.cjsah.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import net.cjsah.main.doc.DocUtil;

@Data
public class SubQuestion {
    private final long id;
    private final long parent;
    private final String title;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;

    public static SubQuestion fromJson(JSONObject json) {
        return new SubQuestion(
                json.getLongValue("id"),
                json.getLongValue("pid"),
                json.getString("title"),
                json.getString("optionA"),
                json.getString("optionB"),
                json.getString("optionC"),
                json.getString("optionD")
        );
    }

    public String getQuestion() {
        StringBuilder builder = new StringBuilder();
        builder.append(DocUtil.htmlToStr(this.title).trim());
        builder.append('\n');
        this.appendOption(builder, this.optionA, 'A');
        this.appendOption(builder, this.optionB, 'B');
        this.appendOption(builder, this.optionC, 'C');
        this.appendOption(builder, this.optionD, 'D');
        builder.append('\n');
        return builder.toString();
    }

    private void appendOption(StringBuilder builder, String option, char select) {
        if (!option.trim().isEmpty()) {
            option = DocUtil.htmlToStr(option).trim();
            builder.append(select);
            builder.append(". ");
            builder.append(option);
            builder.append('\n');
        }
    }
}
