package net.cjsah.main.template;

import cn.hutool.core.util.IdUtil;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Passage {
    private static final Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
    private static final ConverterProperties props = new ConverterProperties();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) throws IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_32);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setObjectWrapper(builder.build());
        configuration.setTemplateLoader(new FileTemplateLoader(new File("./math/template/")));

        FontProvider fontProvider = new FontProvider(new FontSet(), "SimSun");
        System.out.println(fontProvider.addFont("./math/template/font/simsunbd.otf"));
        fontProvider.addStandardPdfFonts();
        props.setFontProvider(fontProvider);
        props.setCharset("utf-8");

        Record record = new Record(1, "Test Pdf");

        Passage template = new Passage();
        template.generate(record);

    }

    public void generate(Record record) {
        try {
            Map<String, Object> context = new HashMap<>();

            context.put("name", "测试学生");
            context.put("type", "测试");
            context.put("number", String.format("%07d", 123456));

            LocalDateTime now = LocalDateTime.now(ZoneId.of("GMT"));
            context.put("vocabulary", 100);
            context.put("difficulty", 200);
            context.put("count", 1);
            context.put("phone", "123****4567");
            context.put("grade", "*年*班");
            context.put("date", now.format(DATE_FORMATTER));
            context.put("time", now.format(TIME_FORMATTER));
            context.put("week", now.getDayOfWeek().name());

            System.out.println(this.generate(record, "name.xml", context));
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private String generate(Record record, String file, Map<String, Object> context) throws IOException, TemplateException {
        String uuid = IdUtil.fastUUID();
        try (
                StringWriter writer = new StringWriter();
                OutputStream os = new FileOutputStream("./math/template/output.pdf")
        ) {
            Template template = configuration.getTemplate(file);
            template.process(context, writer);

//            IConverter converter = LocalConverter.builder().build();
//            converter.convert(new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8)))
//                    .as(DocumentType.DOCX)
//                    .to(os)
//                    .as(DocumentType.PDF)
//                    .execute();
//            // 关闭
//            converter.shutDown();
            writer.close();
        }

        return uuid;
    }

    @Data
    static class Record {
        private Long id;
        private String name;

        public Record(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
    @Data
    public static class Node {
        public final String index;
        public final String word;

        public Node(int index, String word) {
            this.index = String.format("%03d", index);
            this.word = word;
        }
    }

}
