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
import lombok.extern.slf4j.Slf4j;
import net.cjsah.data.QuestionData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Html2Word {

    private static final Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
    private static final ConverterProperties props = new ConverterProperties();


    public static void main(String[] args) throws IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_32);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setObjectWrapper(builder.build());
        configuration.setTemplateLoader(new FileTemplateLoader(new File("./template/")));

        System.out.println("===1");
        FontProvider fontProvider = new FontProvider(new FontSet(), "Times-Roman");
        System.out.println(fontProvider.addFont("./font/simsun.ttc,0"));
        System.out.println(fontProvider.addFont("./font/simsunbd.otf"));
        System.out.println(fontProvider.addFont("./font/nerd.otf"));
        System.out.println(fontProvider.addStandardPdfFonts());
        props.setFontProvider(fontProvider);
        props.setCharset("utf-8");

        System.out.println("===2");
        MathRecord record = new MathRecord(1, "Test Pdf 标题测试");

        Html2Word template = new Html2Word();
        template.generate(record);
    }

    private void generate(MathRecord record) {
        try {
            List<PdfData> formulaShow = new ArrayList<>();
            List<PdfData> formulaMem = new ArrayList<>();
            List<PdfData> exampleQuestions = new ArrayList<>();
            List<PdfData> exercises = new ArrayList<>();


            formulaShow.add(new PdfData(QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")));
            formulaShow.add(new PdfData(QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")));
            formulaShow.add(new PdfData(QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")));
            formulaShow.add(new PdfData(QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")));
            formulaShow.add(new PdfData(QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")));
            formulaShow.add(new PdfData(QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")));
            formulaShow.add(new PdfData(QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")));
            formulaShow.add(new PdfData(QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")));
            formulaShow.add(new PdfData(QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")));
            formulaShow.add(new PdfData(QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")));
            formulaShow.add(new PdfData(QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")));
            formulaShow.add(new PdfData(QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")));
            formulaShow.add(new PdfData(QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")));
            formulaShow.add(new PdfData(QuestionData.Node.of("test text")));

            formulaMem.add(new PdfData(
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d"),
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")
            ));
            formulaMem.add(new PdfData(
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d"),
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")
            ));
            formulaMem.add(new PdfData(
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d"),
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")
            ));
            formulaMem.add(new PdfData(
                    QuestionData.Node.of("aaa"),
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")
            ));
            formulaMem.add(new PdfData(
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d"),
                    QuestionData.Node.of("bbb")
            ));
            formulaMem.add(new PdfData(
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d"),
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")
            ));
            formulaMem.add(new PdfData(
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d"),
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")
            ));

            exampleQuestions.add(new PdfData(
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d"),
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d")
            ));
            exampleQuestions.add(new PdfData(
                    new QuestionData.Node(false, "aaa"),
                    new QuestionData.Node(false, "bbb")
            ));

            exercises.add(new PdfData(
                    1,
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d"),
                    QuestionData.Node.of("英语"),
                    QuestionData.Node.of("提示词")
            ));
            exercises.add(new PdfData(
                    2,
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d"),
                    QuestionData.Node.of("英语"),
                    QuestionData.Node.of("提示词")
            ));
            exercises.add(new PdfData(
                    3,
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d"),
                    QuestionData.Node.of("英语"),
                    QuestionData.Node.of("提示词")
            ));
            exercises.add(new PdfData(
                    4,
                    QuestionData.Node.image("177c6531dbd0400781da7add70e5fa3d"),
                    QuestionData.Node.of("英语"),
                    QuestionData.Node.of("提示词")
            ));
            exercises.add(new PdfData(
                    5,
                    QuestionData.Node.of("汉语"),
                    QuestionData.Node.of("英语"),
                    QuestionData.Node.of("提示词")
            ));
            exercises.add(new PdfData(
                    6,
                    QuestionData.Node.of("汉语"),
                    QuestionData.Node.of("英语"),
                    QuestionData.Node.of("提示词")
            ));
            exercises.add(new PdfData(
                    7,
                    QuestionData.Node.of("汉语"),
                    QuestionData.Node.of("英语"),
                    QuestionData.Node.of("提示词")
            ));
            exercises.add(new PdfData(
                    8,
                    QuestionData.Node.of("汉语"),
                    QuestionData.Node.of("英语"),
                    QuestionData.Node.of("提示词")
            ));
            exercises.add(new PdfData(
                    9,
                    QuestionData.Node.of("汉语"),
                    QuestionData.Node.of("英语"),
                    QuestionData.Node.of("提示词")
            ));
            exercises.add(new PdfData(
                    10,
                    QuestionData.Node.of("汉语"),
                    QuestionData.Node.of("英语"),
                    QuestionData.Node.of("提示词")
            ));

            Map<String, Object> context = new HashMap<>();
            context.put("record", record.getName());
            context.put("name", "Test Student");
            context.put("type", "测试卷");
            context.put("number", String.format("%07d", record.getId()));

            context.put("formula_show", formulaShow);
            context.put("formula_mem", formulaMem);
            context.put("example_questions", exampleQuestions);
            context.put("exercises", exercises);
            context.put("url", "http://localhost:8899");

            System.out.println(this.generate(record, "math-question.ftl", context, true));
            System.out.println(this.generate(record, "math-answer.ftl", context, false));
        } catch (IOException | TemplateException e) {
            log.error("Err", e);
        }
    }

    private String generate(MathRecord record, String file, Map<String, Object> context, boolean question) throws IOException, TemplateException {
        freemarker.template.Template template = configuration.getTemplate(file);
        StringWriter writer = new StringWriter();
        template.process(context, writer);

        String filename = String.format("No.%07d-%s-%s.", record.getId(), record.getName(), question ? "试题" : "答案");
        String path = "./" + filename;

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
        return filename;
    }

    @Data
    static class MathRecord {
        private Long id;
        private String name;

        private Long studentId;
        private Integer requireAccuracy;
        private Integer studyTime;
        private String questionFile;
        private String answerFile;

        public MathRecord(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }


    @Data
    public static class PdfData {
        private final int num;
        private final QuestionData.Node question;
        private final QuestionData.Node answer;
        private final QuestionData.Node tip;

        public PdfData(int num, QuestionData.Node question, QuestionData.Node answer, QuestionData.Node tip) {
            this.num = num;
            this.question = question;
            this.answer = answer;
            this.tip = tip;
        }

        public PdfData(int num, QuestionData.Node question, QuestionData.Node answer) {
            this.num = num;
            this.question = question;
            this.answer = answer;
            this.tip = QuestionData.EMPTY;
        }

        public PdfData(QuestionData.Node question, QuestionData.Node answer) {
            this.question = question;
            this.answer = answer;
            this.num = 0;
            this.tip = QuestionData.EMPTY;
        }

        public PdfData(int num, QuestionData.Node question) {
            this.num = num;
            this.question = question;
            this.answer = QuestionData.EMPTY;
            this.tip = QuestionData.EMPTY;
        }

        public PdfData(QuestionData.Node question) {
            this.question = question;
            this.answer = QuestionData.EMPTY;
            this.tip = QuestionData.EMPTY;
            this.num = 0;
        }


    }

}
