package net.cjsah.util;

import org.docx4j.wml.CTLanguage;
import org.docx4j.wml.CTVerticalAlignRun;
import org.docx4j.wml.Color;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STHint;
import org.docx4j.wml.STTheme;
import org.docx4j.wml.STVerticalAlignRun;
import org.docx4j.wml.Text;
import org.docx4j.wml.U;
import org.docx4j.wml.UnderlineEnumeration;

import java.math.BigInteger;

public final class OfficeUtil {

    public static final class Word {
        public static final RPr defaultRPr;

        static {
            defaultRPr = new RPr() {{
                this.rFonts = new RFonts() {{
                    this.hint = STHint.DEFAULT;
                    this.ascii = "Times New Roman";
                    this.hAnsi = "Times New Roman";
                    this.eastAsiaTheme = STTheme.MINOR_EAST_ASIA;
                    this.cstheme = STTheme.MINOR_EAST_ASIA;
                }};
                this.color = new Color() {{
                    this.val = "auto";
                }};
                this.sz = new HpsMeasure() {{
                    this.val = new BigInteger("21");
                }};
                this.szCs = new HpsMeasure() {{
                    this.val = new BigInteger("21");
                }};
                this.u = new U() {{
                    this.val = UnderlineEnumeration.NONE;
                }};
                this.vertAlign = new CTVerticalAlignRun() {{
                    this.val = STVerticalAlignRun.BASELINE;
                }};
                this.lang = new CTLanguage() {{
                    this.val = "en-US";
                    this.eastAsia = "zh-CN";
                }};
            }};
        }

        public static void appendString(ContentAccessor parent, String value) {
            R r = new R();
            r.setRPr(defaultRPr);
            parent.getContent().add(r);
            Text text = new Text();
            text.setValue(value);
            r.getContent().add(text);
        }

        public static boolean notEmpty(ContentAccessor content) {
            for (Object object : content.getContent()) {
                if (object instanceof ContentAccessor children) {
                    if (notEmpty(children)) return true;
                } else if (object instanceof Text text) {
                    if (!text.getValue().replace(" ", "").trim().isEmpty()) {
                        return true;
                    }
                }
            }
            return false;
        }

    }

}
