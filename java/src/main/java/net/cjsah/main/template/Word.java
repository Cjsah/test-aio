package net.cjsah.main.template;

import cn.hutool.core.util.IdUtil;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateException;
import lombok.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Word {
    private static final Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
    private static final ConverterProperties props = new ConverterProperties();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_32);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setObjectWrapper(builder.build());
        configuration.setTemplateLoader(new FileTemplateLoader(new File("./math/template/")));

        FontProvider fontProvider = new FontProvider(new FontSet(), "SimSun");
        System.out.println(fontProvider.addFont("./math/template/font/simsunbd.otf"));

        fontProvider.addFont("./math/template/font/nerd.otf");
        fontProvider.addStandardPdfFonts();
        props.setFontProvider(fontProvider);
        props.setCharset("utf-8");

        Record record = new Record(1, "Test Pdf");

        Word template = new Word();
        template.generate(record);

    }

    public void generate(Record record) {
        try {
            List<String> words = new ArrayList<>();
            for (int i = 1; i <= 200; i++) {
                words.add("word" + i);
            }

            Map<String, Object> context = new HashMap<>();

            context.put("name", "测试学生最多个十一个字");
            context.put("type", "测试");
            context.put("number", String.format("%07d", 123456));

            LocalDateTime now = LocalDateTime.now(ZoneId.of("GMT"));
            context.put("time", now.format(FORMATTER));

            int length = words.size() / 4;
            int remain = words.size() % 4;

            List<Node[]> list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                Node[] node = new Node[4];
                for (int j = 0; j < 4; j++) {
                    int index = j * length + java.lang.Math.min(remain, j);
                    node[j] = new Node(index + i + 1, words.get(index + i));
                }
                list.add(node);
            }
            if (remain > 0) {
                Node[] node = new Node[remain];
                for (int i = 0; i < remain; i++) {
                    int index = (i + 1) * length + i;
                    node[i] = new Node(index + 1, words.get(index));
                }
                list.add(node);
            }

            context.put("words", list);

            System.out.println(this.generate(record, "word.ftl", context));
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private String generate(Record record, String file, Map<String, Object> context) throws IOException, TemplateException {
        freemarker.template.Template template = configuration.getTemplate(file);
        StringWriter writer = new StringWriter();
        template.process(context, writer);

        String uuid = IdUtil.fastUUID();
        String filename = String.format("No.%07d-%s.", record.getId(), record.getName());
        String path = "./math/template/" + filename;

        try (FileOutputStream fos = new FileOutputStream(path + "html")) {
            fos.write(writer.toString().getBytes());
        }
        try (FileOutputStream fos = new FileOutputStream(path + "pdf")) {
            PdfWriter pdfWriter = new PdfWriter(fos);
            PdfDocument pdf = new PdfDocument(pdfWriter);
            pdf.setDefaultPageSize(PageSize.A4);

            Document document = HtmlConverter.convertToDocument(writer.toString(), pdf, props);
            document.getRenderer().close();
            document.close();
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
