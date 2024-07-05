package net.cjsah.main.template;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONObject;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.data.UpdateReading;
import net.cjsah.util.JsonUtil;
import org.ddr.poi.html.HtmlRenderPolicy;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Html2Word {

    public static void main(String[] args) throws IOException {
        File input = new File("./study-template.docx");
        File output = new File("./result.docx");

        String s = FileUtil.readUtf8String(new File("article.json"));
        JSONObject json = JsonUtil.str2Obj(s, JSONObject.class);
        UpdateReading article = UpdateReading.fromJson(json);

        //        String content = HtmlUtil.ofContent(title);


        HtmlRenderPolicy htmlRenderPolicy = new HtmlRenderPolicy();
        Configure config = Configure.builder()
                .bind("article", htmlRenderPolicy)
                .bind("tip", htmlRenderPolicy)
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

        data.put("tip", "<p style=\"text-indent: 24px;\">请逐字逐句翻译，确保看懂每一句话，看懂文章再做题，不要为了做题而读文章，不要跳略读，不要先看题再看文章。</p>" +
                "<p style=\"text-indent: 24px;\">我们的目标是在高于80%正确率的前提下，阅读文章每分钟在80个单词以上。</p>    " +
                "<p style=\"text-indent: 24px;\">经过刻意学习后，你一定能实现这个目标。当目标达成时，英语学习对于你来说轻松无比，拿高分顺理成章。</p>");





//        data.put("", null);
//        data.put("", null);
//        data.put("", null);
//        data.put("", null);
//        data.put("", null);
//        data.put("", null);
//        data.put("", null);
//        data.put("article", content);

        try (
                XWPFTemplate template = XWPFTemplate.compile(input, config).render(data);
                BufferedOutputStream outputStream = FileUtil.getOutputStream(output)
        ) {
            template.write(outputStream);
        } catch (IOException e) {
            log.error("err", e);
        }

    }

}
