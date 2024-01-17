package net.cjsah.main.doc;

import jakarta.xml.bind.JAXBElement;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTLanguage;
import org.docx4j.wml.CTVerticalAlignRun;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase;
import org.docx4j.wml.ParaRPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STHint;
import org.docx4j.wml.STVerticalAlignRun;
import org.docx4j.wml.Text;

import javax.xml.namespace.QName;
import java.math.BigInteger;

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

    public static P genP() {
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
               this.ind = new PPrBase.Ind() {{
                   this.firstLine = new BigInteger("420");
                   this.firstLineChars = new BigInteger("200");
               }};
           }};
        }};
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
}
