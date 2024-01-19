package net.cjsah.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import net.cjsah.main.doc.DocUtil;
import org.docx4j.wml.P;

import java.util.ArrayList;
import java.util.List;

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

    public List<P> getQuestion() {
        List<P> question = new ArrayList<>();
        question.add(DocUtil.genP(DocUtil.htmlToStr(this.title).trim()));
        this.appendOption(question, this.optionA, 'A');
        this.appendOption(question, this.optionB, 'B');
        this.appendOption(question, this.optionC, 'C');
        this.appendOption(question, this.optionD, 'D');
        question.add(DocUtil.genP(""));
        return question;
    }

    private void appendOption(List<P> question, String option, char select) {
        if (!option.trim().isEmpty()) {
            option = DocUtil.htmlToStr(option).trim();
            question.add(DocUtil.genP(select + ". " + option));
        }
    }
}
