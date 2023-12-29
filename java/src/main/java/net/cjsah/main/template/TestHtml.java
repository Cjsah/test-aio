package net.cjsah.main.template;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

public class TestHtml {
    private static final Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
    private static final ConverterProperties props = new ConverterProperties();

    public static void main(String[] args) throws IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_32);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setObjectWrapper(builder.build());
        configuration.setTemplateLoader(new FileTemplateLoader(new File("./math/template/")));

        FontProvider fontProvider = new FontProvider(new FontSet(), "SimSun");
//        fontProvider.addFont("./math/template/font/simsun.ttc,0");
//        fontProvider.addFont("./math/template/font/simsun.ttc,1");
//        fontProvider.addFont("./math/template/font/simsunbd.otf");
//        fontProvider.addFont("./math/template/font/simhei.ttf");
//        fontProvider.addFont("./math/template/font/MSYH.ttc,0");
//        fontProvider.addFont("./math/template/font/MSYH.ttc,1");
//        fontProvider.addFont("./math/template/font/MSYHBD.ttc,0");
//        fontProvider.addFont("./math/template/font/MSYHBD.ttc,1");
//        fontProvider.addFont("./math/template/font/MSYHL.ttc,0");
//        fontProvider.addFont("./math/template/font/MSYHL.ttc,1");


//        System.out.println(fontProvider.addFont("./math/template/font/nerd.otf"));
        fontProvider.addStandardPdfFonts();
        props.setFontProvider(fontProvider);
        props.setCharset("utf-8");

        FontSet fontSet = fontProvider.getFontSet();

//        System.out.println(fontSet.get("微软雅黑"));
//        System.out.println(fontSet.get("MicrosoftYaHei"));
//        System.out.println(fontSet.get("Microsoft YaHei"));
//        System.out.println(fontSet.get("MicrosoftYaHeiUI"));
//        System.out.println(fontSet.get("Microsoft YaHei UI"));


        TestHtml template = new TestHtml();
        template.generate("test.html");

    }

    private void generate(String file) {
        try {
            freemarker.template.Template template = configuration.getTemplate(file);
            StringWriter writer = new StringWriter();
            template.process(new HashMap<>(), writer);

            String path = "./math/template/test.pdf";

            try (FileOutputStream fos = new FileOutputStream(path)) {
                PdfWriter pdfWriter = new PdfWriter(fos);
                PdfDocument pdf = new PdfDocument(pdfWriter);
                pdf.setDefaultPageSize(PageSize.A4);

                Document document = HtmlConverter.convertToDocument(writer.toString(), pdf, props);
                document.getRenderer().close();
                document.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
