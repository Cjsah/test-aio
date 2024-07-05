package net.cjsah.main.template;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONObject;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.data.UpdateReading;
import net.cjsah.util.HtmlUtil;
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
        File templateFile = new File("./template.json");
        File articleFile = new File("./article.json");

        String s = FileUtil.readUtf8String(templateFile);
        JSONObject template = JsonUtil.str2Obj(s, JSONObject.class);

        s = FileUtil.readUtf8String(articleFile);
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

        data.put("tip", HtmlUtil.ofIndent(template.getList("tip", String.class)));





//        data.put("", null);
//        data.put("", null);
//        data.put("", null);
//        data.put("", null);
//        data.put("", null);
//        data.put("", null);
//        data.put("", null);
//        data.put("article", content);

//        try (
//                XWPFTemplate template = XWPFTemplate.compile(input, config).render(data);
//                BufferedOutputStream outputStream = FileUtil.getOutputStream(output)
//        ) {
//            template.write(outputStream);
//        } catch (IOException e) {
//            log.error("err", e);
//        }

    }

}
