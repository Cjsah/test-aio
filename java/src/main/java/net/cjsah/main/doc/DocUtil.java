package net.cjsah.main.doc;

import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;
import com.itextpdf.styledxmlparser.jsoup.nodes.TextNode;
import jakarta.xml.bind.JAXBElement;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTLanguage;
import org.docx4j.wml.CTVerticalAlignRun;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase;
import org.docx4j.wml.ParaRPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.RPrAbstract;
import org.docx4j.wml.STHint;
import org.docx4j.wml.STVerticalAlignRun;
import org.docx4j.wml.Text;

import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.IllegalFormatFlagsException;
import java.util.List;

@Slf4j
public class DocUtil {
    public static RPr genRpr(boolean bold, boolean italic) {
        return new RPr() {{
            this.rFonts = new RFonts() {{
                this.hint = STHint.DEFAULT;
                this.ascii = "Times New Roman";
                this.hAnsi = "Times New Roman";
            }};
            this.vertAlign = new CTVerticalAlignRun() {{
                this.val = STVerticalAlignRun.BASELINE;
            }};
            this.lang = new CTLanguage() {{
                this.val = "en-US";
            }};

            if (bold) {
                this.b = new BooleanDefaultTrue();
                this.bCs = new BooleanDefaultTrue();
            }
            if (italic) {
                this.i = new BooleanDefaultTrue();
                this.iCs = new BooleanDefaultTrue();
            }
        }};
    }

    public static P genP(String text) {
        P p = genP(false);
        R r = genR(text, false, false);
        p.getContent().add(r);
        return p;
    }

    public static P genP(boolean indent) {
        return new P() {{
           this.pPr = new PPr() {{
               this.rPr = new ParaRPr() {{
                   this.rFonts = new RFonts() {{
                       this.hint = STHint.DEFAULT;
                       this.ascii = "Times New Roman";
                       this.hAnsi = "Times New Roman";
                   }};
                   this.vertAlign = new CTVerticalAlignRun() {{
                       this.val = STVerticalAlignRun.BASELINE;
                   }};
                   this.lang = new CTLanguage() {{
                       this.val = "en-US";
                   }};
               }};
               if (indent) {
                   this.ind = new PPrBase.Ind() {{
                       this.firstLine = new BigInteger("420");
                       this.firstLineChars = new BigInteger("200");
                   }};
               }
           }};
        }};
    }

    public static void setBold(P p) {
        ParaRPr rPr = p.getPPr().getRPr();
        setBold(rPr);
        for (Object o : p.getContent()) {
            setBold(((R) o).getRPr());
        }
    }

    private static void setBold(RPrAbstract rPr) {
        RFonts font = rPr.getRFonts();
        rPr.setB(new BooleanDefaultTrue());
        rPr.setBCs(new BooleanDefaultTrue());
        rPr.setSz(new HpsMeasure() {{
            this.val = new BigInteger("24");
        }});
        rPr.setSzCs(new HpsMeasure() {{
            this.val = new BigInteger("24");
        }});
        font.setAscii("Times New Roman Bold");
        font.setHAnsi("Times New Roman Bold");
        font.setCs("Times New Roman Bold");
    }

    private static final QName namespace = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "t");

    public static R genR(String text, boolean bold, boolean italic) {
        return new R() {{
            this.rPr = genRpr(bold, italic);
            this.content.add(new JAXBElement<>(namespace, Text.class, new Text() {{
                this.value = text;
                this.space = "preserve";
            }}));
        }};
    }

    public static R genMark(int num) {
        return new R() {{
            this.rPr = genRpr(false, false);
            this.rPr.getVertAlign().setVal(STVerticalAlignRun.SUPERSCRIPT);
            this.content.add(new JAXBElement<>(namespace, Text.class, new Text() {{
                this.value = "[" + num + "]";
            }}));
        }};
    }

    public static String htmlToStr(String passage) {
        passage = "<html><body>" + passage.replace('\r', '\n') + "</body></html>";
        Element body = Jsoup.parse(passage).body();
        return DocUtil.htmlToStr(body);
    }

    public static String htmlToStr(Element element) {
        StringBuilder builder = new StringBuilder();
        for (com.itextpdf.styledxmlparser.jsoup.nodes.Node node : element.childNodes()) {
            if (node instanceof TextNode) {
                String value = ((TextNode) node).getWholeText();
                builder.append(value);
            } else if (node instanceof Element tag) {
                switch (tag.tagName()) {
                    case "br":
                        builder.append("\n");
                        break;
                    case "strong":
                    case "p":
                    case "u":
                    case "div":
                    case "span":
                        builder.append(htmlToStr(tag));
                        break;
                    default:
                        log.warn("未处理标签: {}", tag.tagName());
                        break;
                }
            }else {
                throw new IllegalFormatFlagsException("未知标签: " + node.getClass());
            }
        }
        return builder.toString();
    }

    public static List<P> parseHtml(String text, boolean indent) {
        text = htmlToStr(text);
        List<P> result = new ArrayList<>();
        for (String node : text.split("\n")) {
            node = node.trim();
            if (!node.isEmpty()) {
                P p = genP(indent);
                R r = genR(node, false, false);
                p.getContent().add(r);
                result.add(p);
            }
        }
        return result;
    }

}
