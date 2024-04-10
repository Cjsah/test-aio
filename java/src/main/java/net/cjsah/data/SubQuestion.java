package net.cjsah.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import net.cjsah.util.DocUtil;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;

import java.util.List;

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
        List<ContentAccessor> nodes = progress.getNodes();
        nodes.addAll(DocUtil.parseHtmlNodeWithTrim("A. " + this.optionA, false).getNodes());
        nodes.addAll(DocUtil.parseHtmlNodeWithTrim("B. " + this.optionB, false).getNodes());
        nodes.addAll(DocUtil.parseHtmlNodeWithTrim("C. " + this.optionC, false).getNodes());
        nodes.addAll(DocUtil.parseHtmlNodeWithTrim("D. " + this.optionD, false).getNodes());
        nodes.add(DocUtil.genP(""));
        return progress;
    }

    private static List<P> appendOption(String option, char select) {
        return DocUtil.parseText(select + ". " + option, false);
    }
}
