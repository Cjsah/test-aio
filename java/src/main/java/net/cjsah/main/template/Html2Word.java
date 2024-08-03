package net.cjsah.main.template;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONObject;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Includes;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.data.UpdateReading;
import net.cjsah.data.WordMeaning;
import net.cjsah.data.WordNode;
import net.cjsah.main.resolver.AIEnglishImageRenderer;
import net.cjsah.util.HtmlUtil;
import net.cjsah.util.JsonUtil;
import net.cjsah.util.StreamUtil;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.ddr.poi.html.HtmlRenderConfig;
import org.ddr.poi.html.HtmlRenderPolicy;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Html2Word {

    @SneakyThrows
    public static void main(String[] args) throws IOException {
        File input = new File("./study-template-word.docx");
        File output = new File("./result.docx");
        File articleFile = new File("./article.json");

        String s = FileUtil.readUtf8String(articleFile);
        JSONObject json = JsonUtil.str2Obj(s, JSONObject.class);
        UpdateReading article = UpdateReading.fromJson(json.getJSONObject("data"));

        //        String content = HtmlUtil.ofContent(title);

        HtmlRenderConfig renderConfig = new HtmlRenderConfig();
        renderConfig.setCustomRenderers(List.of(new AIEnglishImageRenderer()));
        HtmlRenderPolicy htmlRenderPolicy = new HtmlRenderPolicy(renderConfig);
        Configure config = Configure.builder()
                .bind("tip", htmlRenderPolicy)
                .bind("word", htmlRenderPolicy)
                .bind("article", htmlRenderPolicy)
                .bind("answer", htmlRenderPolicy)
                .bind("twords", new WordTablePolicy())
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("type", "测试阅读");
        data.put("name", "测试学生");
        data.put("phone", "12345678900");
        data.put("grade", "0班");
        data.put("date", "2024-07-05");
        data.put("time", "12:00:00");
        data.put("ability", 10);
        data.put("vocabulary", 20);
        data.put("count", 30);

        data.put("tip", HtmlUtil.ofTip());
        data.put("word", HtmlUtil.ofWords(article.getWords()));
        data.put("article", HtmlUtil.ofArticle(article.getArticles(), StreamUtil.map(article.getWords(), WordNode::getWord), article.getOverWords()));
        data.put("answer", HtmlUtil.ofAnswer(article.getArticles()));
        data.put("twords", article.getMiddleWords().getWords());
        data.put("tcontent", Includes.ofLocal("test.docx").setRenderModel(data).create());

        long start = System.currentTimeMillis();
        try (
                XWPFTemplate wordTemplate = XWPFTemplate.compile(input, config).render(data);
                BufferedOutputStream outputStream = FileUtil.getOutputStream(output)
        ) {
            wordTemplate.write(outputStream);
            long end = System.currentTimeMillis();
            System.out.println(end - start);
        } catch (IOException e) {
            log.error("err", e);
        }
    }

    public static class WordTablePolicy extends DynamicTableRenderPolicy {

        @SuppressWarnings("unchecked")
        @Override
        public void render(XWPFTable table, Object o) throws Exception {
            if (null == o) return;
            List<WordMeaning> words = (List<WordMeaning>) o;

            int rows = words.size() / 4;
            int remain = words.size() % 4;

            for (int row = 0; row < 50; row++) {
                if (row == rows + Math.min(remain, 1)) break;
                XWPFTableRow tableRow = table.getRow(row);
                for (int col = 0; col < 4; col++) {
                    int index = col * rows + Math.min(remain, col) + row;
                    if (index >= words.size() || (row == rows && col >= remain)) break;
                    XWPFTableCell cell = tableRow.getCell(col);
                    WordMeaning word = words.get(index);
                    XWPFParagraph paragraph = cell.getParagraphs().get(0);
                    XWPFRun run = paragraph.insertNewRun(0);
                    CTFonts font = run.getCTR().addNewRPr().addNewRFonts();
                    font.setEastAsia("宋体");
                    font.setAscii("Times New Roman");
                    font.setHAnsi("Times New Roman");
                    run.setFontSize(9);
                    CTText text = run.getCTR().addNewT();
                    text.setStringValue(String.format("%03d.%s", index + 1, word.getWord()));
                }
            }
        }
    }
}
