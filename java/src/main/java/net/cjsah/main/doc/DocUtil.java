package net.cjsah.main.doc;

import com.alibaba.fastjson2.JSONObject;
import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.nodes.Comment;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;
import com.itextpdf.styledxmlparser.jsoup.nodes.Node;
import com.itextpdf.styledxmlparser.jsoup.nodes.TextNode;
import com.itextpdf.styledxmlparser.jsoup.select.Elements;
import jakarta.xml.bind.JAXBElement;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.data.WordNode;
import net.cjsah.main.template.TestWord;
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
import org.docx4j.wml.U;
import org.docx4j.wml.UnderlineEnumeration;

import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IllegalFormatFlagsException;
import java.util.List;
import java.util.function.Consumer;

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

            if (bold) {     setBold(this);      }
            if (italic) {   setItalic(this);    }
        }};
    }

    public static RPr copyRpr(RPr old) {
        RPr rPr = genRpr(false, false);
        rPr.setB(old.getB());
        rPr.setBCs(old.getBCs());
        rPr.setI(old.getI());
        rPr.setICs(old.getICs());
        if (old.getU() != null) { setUnderline(rPr); }
        return rPr;
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
//        RFonts font = rPr.getRFonts();
        rPr.setB(new BooleanDefaultTrue());
        rPr.setBCs(new BooleanDefaultTrue());
//        rPr.setSz(new HpsMeasure() {{
//            this.val = new BigInteger("24");
//        }});
//        rPr.setSzCs(new HpsMeasure() {{
//            this.val = new BigInteger("24");
//        }});
//        font.setAscii("Times New Roman Bold");
//        font.setHAnsi("Times New Roman Bold");
//        font.setCs("Times New Roman Bold");
    }

    private static void setItalic(RPrAbstract rPr) {
//        RFonts font = rPr.getRFonts();
        rPr.setI(new BooleanDefaultTrue());
        rPr.setICs(new BooleanDefaultTrue());
//        rPr.setSz(new HpsMeasure() {{
//            this.val = new BigInteger("24");
//        }});
//        rPr.setSzCs(new HpsMeasure() {{
//            this.val = new BigInteger("24");
//        }});
//        font.setAscii("Times New Roman Bold");
//        font.setHAnsi("Times New Roman Bold");
//        font.setCs("Times New Roman Bold");
    }

    private static void setUnderline(RPrAbstract rPr) {
        rPr.setU(new U() {{
            this.val = UnderlineEnumeration.SINGLE;
        }});
    }

    @SuppressWarnings("HttpUrlsUsage")
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

    public static R genR(String text, RPr format, boolean bold, boolean italic) {
        return new R() {{
            this.rPr = copyRpr(format);
            if (bold) {     setBold(this.rPr);      }
            if (italic) {   setItalic(this.rPr);    }
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
        for (Node node : element.childNodes()) {
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

    public static List<P> parseText(String text, boolean indent) {
        List<P> result = new ArrayList<>();
        for (String node : text.split("\n")) {
            node = node.trim();
            P p = genP(indent);
            R r = genR(node, false, false);
            p.getContent().add(r);
            result.add(p);
        }
        return result;
    }

    public static List<P> parseHtml(String text, boolean indent) {
        text = htmlToStr(text);
        return parseText(text, indent);
    }

    public static ParseProgress parseHtmlNode(String text, List<WordNode> bolds, List<WordNode> italics) {
        text = "<html><body>" + text.replace('\r', '\n') + "</body></html>";
        Element body = Jsoup.parse(text).body();
        ParseProgress progress = new ParseProgress(true);
        parseHtmlNode(body, progress, genRpr(false, false), bolds, italics);
        return progress;
    }

    private static void parseHtmlNode(Element element, ParseProgress progress, RPr format, List<WordNode> bolds, List<WordNode> italics) {
        for (Node node : element.childNodes()) {
            if (node instanceof TextNode) {
                String value = ((TextNode) node).getWholeText();
                PassageNode passageNode = new PassageNode(value);
                List<PassageNode> results = new ArrayList<>() {{
                    this.add(passageNode);
                }};
                results = parsePassage(results, Collections.singletonList(new WordNode("\n")), word -> word.nextLine = true, false);
                System.out.println(results);
                System.out.println("===");
                if (!bolds.isEmpty()) {
                    results = parsePassage(results, bolds, word -> word.bold = true, true);
                }
                if (!italics.isEmpty()) {
                    results = parsePassage(results, italics, word -> word.italic = true, true);
                }

                System.out.println(results);
                for (PassageNode pnode : results) {
                    if (pnode.nextLine) {
                        progress.now = genP(true);
                        progress.nodes.add(progress.now);
                        continue;
                    }
                    System.out.println(pnode);
                    R r = DocUtil.genR(pnode.value, format, pnode.bold, pnode.italic);
                    progress.now.getContent().add(r);
                    if (pnode.italic) {
                        int num = 0;
                        for (JSONObject word : progress.overWords) {
                            if (pnode.wordNode.getWord().equals(word.getString("word"))) {
                                num = word.getIntValue("index");
                                break;
                            }
                        }
                        if (num == 0) {
                            num = progress.overWords.size() + 1;
                            JSONObject word = new JSONObject();
                            word.put("index", num);
                            word.put("word", pnode.wordNode.getWord());
                            word.put("symbol", pnode.wordNode.getAmericaPronunciation());
                            word.put("translate", pnode.wordNode.getMeaning());
                            progress.overWords.add(word);
                        }
                        r = DocUtil.genMark(num);
                        progress.now.getContent().add(r);
                    }
                }
                System.out.println("===");
            } else if (node instanceof Element tag) {
                switch (tag.tagName()) {
                    case "br":
                        progress.now = genP(true);
                        progress.nodes.add(progress.now);
                        break;
                    case "strong":
                    case "div":
                    case "span":
                        parseHtmlNode(tag, progress, format, bolds, italics);
                        break;
                    case "p":
                        progress.now = genP(true);
                        progress.nodes.add(progress.now);
                        parseHtmlNode(tag, progress, format, bolds, italics);
                        progress.now = genP(true);
                        progress.nodes.add(progress.now);
                        break;
                    case "u":
                        RPr rPr = copyRpr(format);
                        setUnderline(rPr);
                        parseHtmlNode(tag, progress, rPr, Collections.emptyList(), Collections.emptyList());
                        break;
                    case "table":

                        break;
                    default:
                        log.warn("未处理标签: {}", tag.tagName());
                        break;
                }
            } else if (!(node instanceof Comment)){
                log.warn("未知标签: {}", node.getClass());
            }
        }
    }

    private static void parseTable(Element table, ParseProgress progress, RPr format, List<WordNode> bolds, List<WordNode> italics) {
        Elements trs = table.getElementsByTag("tr");
        if (trs.isEmpty()) return;
        int cows = 0;
        for (Element tr : trs) {
            Elements tds = tr.getElementsByTag("td");
            if (tds.size() > cows) cows = tds.size();



        }


        System.out.println(table.html());
    }

    private static List<PassageNode> parsePassage(List<PassageNode> nodes, List<WordNode> words, Consumer<PassageNode> consumer, boolean letter) {
        List<PassageNode> results = new ArrayList<>();
        while (!nodes.isEmpty()) {
            PassageNode node = nodes.get(0);
            if (node.parsed) {
                results.add(node);
                nodes.remove(0);
                continue;
            }
            boolean noMatch = true;
            for (WordNode word : words) {
                int index = node.value.indexOf(word.getWord());
                if (index != -1 && (!letter || (notLetter(node.value, index - 1) && notLetter(node.value, index + word.getWord().length())))) {
                    nodes.remove(0);
                    node.substring(word.getWord().length() + index, node.value.length(), nodes);
                    node.substring(index, word.getWord().length() + index, nodes, part -> {
                        consumer.accept(part);
                        part.wordNode = word;
                        part.parsed = true;
                    });
                    node.substring(0, index, nodes);

                    noMatch = false;
                    break;
                }

            }
            if (noMatch) {
                nodes.remove(0);
                results.add(node);
            }

        }
        return results;
    }

    private static boolean notLetter(String value, int index) {
        if (index < 0 || index >= value.length()) return true;
        char c = value.charAt(index);
        return (c < 'a' || c > 'z') && (c < 'A' || c > 'Z');
    }

    @Data
    public static class ParseProgress {
        List<JSONObject> overWords = new ArrayList<>();
        List<P> nodes = new ArrayList<>();
        P now;

        public ParseProgress(boolean indent) {
            this.now = genP(indent);
            this.nodes.add(this.now);
        }
    }

    @Data
    static class PassageNode {
        String value;
        boolean bold;
        boolean italic;
        boolean parsed;
        boolean nextLine;
        WordNode wordNode;

        public PassageNode(String value) {
            this.value = value;
            this.bold = false;
            this.italic = false;
            this.parsed = false;
            this.nextLine = false;
        }

        public void substring(int from, int to, List<PassageNode> append) {
            this.substring(from, to, append, node -> {});
        }

        public void substring(int from, int to, List<PassageNode> append, Consumer<PassageNode> consumer) {
            if (from == to) return;
            PassageNode node = new PassageNode(this.value.substring(from, to));
            node.bold = this.bold;
            node.italic = this.italic;
            consumer.accept(node);
            append.add(0, node);
        }

    }

}
