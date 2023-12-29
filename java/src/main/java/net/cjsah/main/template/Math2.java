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
import net.cjsah.data.QuestionData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Math2 {

    private static final Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
    private static final ConverterProperties props = new ConverterProperties();


    public static void main(String[] args) throws IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_32);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setObjectWrapper(builder.build());
        configuration.setTemplateLoader(new FileTemplateLoader(new File("./math/template/")));

        FontProvider fontProvider = new FontProvider(new FontSet(), "SimSun");
//        System.out.println(fontProvider.addFont("./math/template/font/simsun-b.ttc,6"));
        System.out.println(fontProvider.addFont("./math/template/font/simsun.ttc,0"));
        System.out.println(fontProvider.addFont("./math/template/font/simsunbd.otf"));
        System.out.println(fontProvider.addFont("./math/template/font/nerd.otf"));
        fontProvider.addStandardPdfFonts();
        props.setFontProvider(fontProvider);
        props.setCharset("utf-8");

        MathRecord record = new MathRecord(1, "Test Pdf 标题测试");

        Math2 template = new Math2();
        template.generate(record, true);

        System.out.println(record.questionFile);
        System.out.println(record.answerFile);
    }

    public void generate(MathRecord record, boolean genAnswer) {
        try {
            List<PdfData> formulaShow = new ArrayList<>();
            List<PdfData> formulaMem = new ArrayList<>();
            List<PdfData> exampleQuestions = new ArrayList<>();
            List<PdfData> exercises = new ArrayList<>();


            formulaShow.add(new PdfData(
                    0,
                    new QuestionData.Node(false, "ae913d52-c459-48a2-b6b4-4e5a7b67f02a"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaShow.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaShow.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaShow.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaShow.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaShow.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaShow.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaShow.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaShow.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaShow.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaShow.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaShow.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaShow.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaShow.add(new PdfData(
                    0,
                    new QuestionData.Node(false, "test text"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaMem.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    new QuestionData.Node(false, "answer1"),
                    ""
            ));
            formulaMem.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    new QuestionData.Node(false, "answer2"),
                    ""
            ));
            formulaMem.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    QuestionData.EMPTY,
                    ""
            ));
            formulaMem.add(new PdfData(
                    0,
                    new QuestionData.Node(false, "bbb"),
                    new QuestionData.Node(false, "answer1"),
                    ""
            ));
            formulaMem.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    new QuestionData.Node(false, "answer1"),
                    ""
            ));
            formulaMem.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    new QuestionData.Node(false, "answer1"),
                    ""
            ));
            formulaMem.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    new QuestionData.Node(false, "answer1"),
                    ""
            ));

            exampleQuestions.add(new PdfData(
                    0,
                    new QuestionData.Node(true, "ae913d52-c459-48a2-b6b4-4e5a7b67f02a"),
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    "ccc"
            ));
            exampleQuestions.add(new PdfData(
                    0,
                    new QuestionData.Node(false, "aaa"),
                    new QuestionData.Node(false, "bbb"),
                    "ccc"
            ));
            exercises.add(new PdfData(
                    10,
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    new QuestionData.Node(true, "f38694d3-56e2-4b16-b0dd-f8e37e28f6b3"),
                    "ccc"
            ));
            exercises.add(new PdfData(
                    2,
                    new QuestionData.Node(false, "aaa"),
                    new QuestionData.Node(false, "bbb"),
                    "ccc"
            ));
            exercises.add(new PdfData(
                    3,
                    new QuestionData.Node(false, "aaa"),
                    new QuestionData.Node(false, "bbb"),
                    "ccc<br/>ccc<br/>ccc<br/>ccc<br/>ccc<br/>ccc"
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

            record.setQuestionFile(this.generate(record, "english-question.ftl", context, true));
            if (genAnswer) {
                record.setAnswerFile(this.generate(record, "english-answer.ftl", context, false));

            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private String generate(MathRecord record, String file, Map<String, Object> context, boolean question) throws IOException, TemplateException {
        freemarker.template.Template template = configuration.getTemplate(file);
        StringWriter writer = new StringWriter();
        template.process(context, writer);

        String uuid = IdUtil.fastUUID();
        String filename = String.format("No.%07d-%s-%s.", record.getId(), record.getName(), question ? "试题" : "答案");
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
        private final String input;
    }

}
