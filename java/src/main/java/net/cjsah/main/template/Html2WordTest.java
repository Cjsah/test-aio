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
public class Html2WordTest {

    public static void main(String[] args) throws IOException {
        File input = new File("./study-template.docx");
        File output = new File("./result.docx");

        String html = FileUtil.readUtf8String(new File("./text.html"));

        HtmlRenderPolicy htmlRenderPolicy = new HtmlRenderPolicy();
        Configure config = Configure.builder()
                .bind("article", htmlRenderPolicy)
                .build();

        StringBuilder builder = new StringBuilder();

        builder.append("<table><tr><td>");
        builder.append(html);
        builder.append("</td></tr></table>");


        Map<String, Object> data = new HashMap<>();
        data.put("article", HtmlUtil.html(html));

        try (
                XWPFTemplate wordTemplate = XWPFTemplate.compile(input, config).render(data);
                BufferedOutputStream outputStream = FileUtil.getOutputStream(output)
        ) {
            wordTemplate.write(outputStream);
        } catch (IOException e) {
            log.error("err", e);
        }

    }

}
