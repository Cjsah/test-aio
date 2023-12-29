package net.cjsah.main.template;

import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;
import com.itextpdf.styledxmlparser.jsoup.nodes.Node;
import com.itextpdf.styledxmlparser.jsoup.nodes.TextNode;
import com.itextpdf.styledxmlparser.jsoup.select.Elements;
import net.cjsah.util.OfficeUtil;
import org.docx4j.TraversalUtil;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Tr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.IllegalFormatFlagsException;

public class TestWord {

    public static void main(String[] args) {
        TestWord template = new TestWord();
        template.generate("template2.docx");
    }

    private void generate(String file) {
        String path = "./math/template/";
        String templatePath = path + file;
        String resultPath = path + "result.docx";

        try {

            String passage = getPassage();

            Document doc = Jsoup.parse(passage);
            Element body = doc.body();


            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(templatePath));

            ClassFinder finder = new ClassFinder(Tbl.class);
            MainDocumentPart document = wordMLPackage.getMainDocumentPart();
            new TraversalUtil(document.getContent(), finder);

            System.out.printf("查找到%d个表格 %n", finder.results.size());

            Tbl table = (Tbl) finder.results.get(0);

            Tr tr = (Tr) table.getContent().get(0); // 复用

            finder = new ClassFinder(Tc.class);
            new TraversalUtil(tr.getContent(), finder);
            Tc tc = (Tc) finder.results.get(0);

            htmlToWord(body, tc, null, true);

            System.out.println(tc);

//            String template = XmlUtils.marshaltoString(tr);
//
//            tr = (Tr) XmlUtils.unmarshallFromTemplate(template, Collections.singletonMap("passage", passage));
//            table.getContent().add(tr);
//
            try (
                    FileOutputStream fos = new FileOutputStream(resultPath)
            ) {
                wordMLPackage.save(fos);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static P htmlToWord(Element element, ContentAccessor parent, P p, boolean first) {
        if (p == null) {
            p = new P();
            parent.getContent().add(p);
        }
        for (Node node : element.childNodes()) {
            if (node instanceof TextNode) {
                String value = ((TextNode) node).getWholeText();
                OfficeUtil.Word.appendString(p, value);
            } else if (node instanceof Element tagVal) {
                switch (tagVal.tagName()) {
                    case "br":
                        if (OfficeUtil.Word.notEmpty(p)) {
                            p = new P();
                            parent.getContent().add(p);
                        } else {
                            p.getContent().clear();
                        }
                        break;
                    case "strong":
                        p = htmlToWord(tagVal, parent, p, false);
                        break;
                    default:
                        System.out.println("未处理标签: " + tagVal.tagName());
                        break;
                }
            }else {
                throw new IllegalFormatFlagsException("未知标签: " + node.getClass());
            }
        }
        if (first && p.getContent().isEmpty()) {
            parent.getContent().remove(p);
        }
        return p;
    }



    private static String getPassage() {
        try (FileInputStream fis = new FileInputStream("./template")) {
            String value = new String(fis.readAllBytes());
            return "<html><body>" + value + "</body></html>";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
