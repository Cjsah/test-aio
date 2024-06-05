package net.cjsah.util;

import antlr.StringUtils;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
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
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTLanguage;
import org.docx4j.wml.CTVerticalAlignRun;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Drawing;
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
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.docx4j.wml.U;
import org.docx4j.wml.UnderlineEnumeration;

import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        P p = new P();
        p.setPPr(new PPr() {{
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
        }});
        return p;
    }

    public static void setBold(P p) {
        ParaRPr rPr = p.getPPr().getRPr();
        setBold(rPr);
        for (Object o : p.getContent()) {
            setBold(((R) o).getRPr());
        }
    }

    private static void setBold(RPrAbstract rPr) {
        rPr.setB(new BooleanDefaultTrue());
        rPr.setBCs(new BooleanDefaultTrue());
    }

    private static void setItalic(RPrAbstract rPr) {
        rPr.setI(new BooleanDefaultTrue());
        rPr.setICs(new BooleanDefaultTrue());
    }

    private static void setUnderline(RPrAbstract rPr) {
        rPr.setU(new U() {{
            this.val = UnderlineEnumeration.SINGLE;
        }});
    }

    @SuppressWarnings("HttpUrlsUsage")
    public static <T> JAXBElement<T> genJAXBElement(String part, Class<T> clazz, T data) {
        return new JAXBElement<>(
                new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", part),
                clazz,
                data
        );
    }


    public static R genR(String text, boolean bold, boolean italic) {
        return new R() {{
            this.rPr = genRpr(bold, italic);
            this.content.add(genJAXBElement("t", Text.class, new Text() {{
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
            this.content.add(genJAXBElement("t", Text.class, new Text() {{
                this.value = text;
                this.space = "preserve";
            }}));
        }};
    }

    public static R genMark(int num) {
        return new R() {{
            this.rPr = genRpr(false, false);
            this.rPr.getVertAlign().setVal(STVerticalAlignRun.SUPERSCRIPT);
            this.content.add(genJAXBElement("t", Text.class, new Text() {{
                this.value = "[" + num + "]";
            }}));
        }};
    }

    private static R genImage(ParseProgress progress, String url) {
        try {
            String link = "https://ai-english.shuhai777.cn";
            System.out.println(link + url);
            HttpRequest request = HttpRequest.get(link + url).timeout(10000);
            byte[] body = null;
            try (HttpResponse response = request.execute()) {
                body = response.bodyBytes();
            } catch (Exception e) {
                log.error("请求失败", e);
            }
            if (body == null) {
                log.warn("[{}]下载失败", url);
                return null;
            }
            R r = new R();
            r.setRPr(genRpr(false, false));
            Drawing drawing = new Drawing();
            JAXBElement<Drawing> jaxbElement = new JAXBElement<>(
                    new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "drawing"),
                    Drawing.class,
                    R.class,
                    drawing
            );
            r.getContent().add(jaxbElement);

            byte[] finalBody = body;
            progress.afters.add(new CustomConsumer(process -> true, wordPackage -> {
                BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordPackage, finalBody);
                String rldId = imagePart.getSourceRelationships().get(0).getId();
                int id = Integer.parseInt(rldId.substring(3));
                Inline inline = imagePart.createImageInline("img", "img", id, id, false, 7000);
                drawing.getAnchorOrInline().add(inline);
            }));
            return r;
        } catch (Exception e) {
            log.error("图片下载失败", e);
            return null;
        }
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

    public static ParseProgress parseHtmlNode(String text, boolean indent) {
        List<WordNode> nodes = Collections.emptyList();
        return startParse(text, nodes, nodes, indent);
    }

    public static ParseProgress parseHtmlNodeWithTrim(String text, boolean indent) {
        List<WordNode> nodes = Collections.emptyList();
        ParseProgress progress = startParse(text, nodes, nodes, indent);
        progress.nodes = DocUtil.trim(progress.nodes);
        return progress;
    }

    public static ParseProgress parseHtmlNode(String text, List<WordNode> bolds, List<WordNode> italics) {
        return startParse(text, bolds, italics, true);
    }

    private static ParseProgress startParse(String text, List<WordNode> bolds, List<WordNode> italics, boolean indent) {
        text = "<html><body>" + text.replaceAll("(\r| \r )", "") + "</body></html>";
        Element body = Jsoup.parse(text).body();
        ParseProgress progress = new ParseProgress(indent);
        parseHtmlNode(body, progress, genRpr(false, false), bolds, italics, indent);
        return progress;
    }

    private static void parseHtmlNode(Element element, ParseProgress progress, RPr format, List<WordNode> bolds, List<WordNode> italics, boolean indent) {
        for (Node node : element.childNodes()) {
            if (node instanceof TextNode) {
                String value = ((TextNode) node).getWholeText();
                PassageNode passageNode = new PassageNode(value);
                List<PassageNode> results = new ArrayList<>() {{
                    this.add(passageNode);
                }};
                results = parsePassage(results, Collections.singletonList(new WordNode("\n")), word -> word.nextLine = true, false);
                if (!bolds.isEmpty()) {
                    results = parsePassage(results, bolds, word -> word.bold = true, true);
                }
                if (!italics.isEmpty()) {
                    results = parsePassage(results, italics, word -> word.italic = true, true);
                }

                for (PassageNode pnode : results) {
                    if (pnode.nextLine) {
                        progress.now = genP(true);
                        progress.nodes.add(progress.now);
                        continue;
                    }
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
                            word.put("translate", pnode.wordNode.getMeaning().replaceAll("(<br>|\n)", " "));
                            progress.overWords.add(word);
                        }
                        r = DocUtil.genMark(num);
                        progress.now.getContent().add(r);
                    }
                }
            } else if (node instanceof Element tag) {
                RPr rPr;
                switch (tag.tagName()) {
                    case "br":
                        progress.now = genP(true);
                        progress.nodes.add(progress.now);
                        break;
                    case "strong":
                        rPr = copyRpr(format);
                        setBold(rPr);
                        parseHtmlNode(tag, progress, rPr, bolds, italics, indent);
                        break;
                    case "div":
                    case "span":
                    case "center":
                    case "bdo":
                        parseHtmlNode(tag, progress, format, bolds, italics, indent);
                        break;
                    case "p":
                        progress.now = genP(true);
                        progress.nodes.add(progress.now);
                        parseHtmlNode(tag, progress, format, bolds, italics, indent);
                        progress.now = genP(true);
                        progress.nodes.add(progress.now);
                        break;
                    case "u":
                        rPr = copyRpr(format);
                        setUnderline(rPr);
                        parseHtmlNode(tag, progress, rPr, Collections.emptyList(), Collections.emptyList(), indent);
                        break;
                    case "table":
                        parseTable(tag, progress, bolds, italics, indent);
                        break;
                    case "img":
                        String src = tag.attr("src");
                        R r = genImage(progress, src);
                        if (r != null) {
                            progress.now = genP(true);
                            progress.nodes.add(progress.now);
                            progress.now.getContent().add(r);
                            progress.now = genP(true);
                            progress.nodes.add(progress.now);
                        }
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

    private static void parseTable(Element table, ParseProgress progress, List<WordNode> bolds, List<WordNode> italics, boolean indent) {
        Elements trs = table.getElementsByTag("tr");
        if (trs.isEmpty()) return;
        int cows = 0;
        Tbl docTbl = new Tbl();
        for (Element tr : trs) {
            Tr docTr = new Tr();
            Elements tds = tr.getElementsByTag("td");
            if (tds.size() > cows) cows = tds.size();
            String width = String.valueOf(7152 / tds.size());
            for (Element td : tds) {
                Tc docTc = new Tc();
                docTc.setTcPr(new TcPr() {{
                    this.tcW = new TblWidth() {{
                        this.w = new BigInteger(width);
                        this.type = "dxa";
                    }};
                }});
                ParseProgress tdProgress = new ParseProgress(false);
                parseHtmlNode(td, tdProgress, DocUtil.genRpr(false, false), bolds, italics, indent);
                tdProgress.nodes = trim(tdProgress.nodes);
                tdProgress.nodes.stream().parallel().forEach(it -> {
                    if (it instanceof P) {
                        PPr pPr = ((P) it).getPPr();
                        PPrBase.Ind ind = pPr.getInd();
                        if (ind == null) {
                            ind = new PPrBase.Ind();
                            pPr.setInd(ind);
                        }
                        ind.setFirstLine(new BigInteger("315"));
                        ind.setFirstLineChars(new BigInteger("150"));
                    }
                });
                docTc.getContent().addAll(tdProgress.nodes);
                progress.afters.addAll(tdProgress.afters);
                JAXBElement<Tc> element = genJAXBElement("tc", Tc.class, docTc);
                docTr.getContent().add(element);
            }
            docTbl.getContent().add(docTr);
        }

        progress.nodes.add(docTbl);
        progress.now = genP(true);
        progress.nodes.add(progress.now);
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

    public static List<ContentAccessor> trim(List<ContentAccessor> list) {
        for (ContentAccessor node : list) {
            while (node instanceof P && !node.getContent().isEmpty()) {
                List<Object> content = node.getContent();
                Object obj = ((JAXBElement<?>) (((R) content.get(0)).getContent().get(0))).getValue();
                if (obj instanceof Text text) {
                    String value = text.getValue();
                    if (value.trim().isEmpty()) {
                        content.remove(0);
                    } else {
                        text.setValue(StringUtils.stripFront(value, " \t"));
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return list.stream().parallel().filter(it -> !it.getContent().isEmpty()).collect(Collectors.toList());
    }

    private static boolean notLetter(String value, int index) {
        if (index < 0 || index >= value.length()) return true;
        char c = value.charAt(index);
        return (c < 'a' || c > 'z') && (c < 'A' || c > 'Z');
    }

    @Data
    public static class ParseProgress {
        List<JSONObject> overWords = new ArrayList<>();
        List<ContentAccessor> nodes = new ArrayList<>();
        List<CustomConsumer> afters = new ArrayList<>();
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

    @Data
    public static class CustomConsumer {
        public final Predicate<Object> predicate;
        public final RunnableConsumer runner;
    }

    public interface RunnableConsumer {
        void run(WordprocessingMLPackage wordPackage) throws Exception;
    }

}
