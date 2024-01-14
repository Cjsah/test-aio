package net.cjsah.main.doc;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;
import com.itextpdf.styledxmlparser.jsoup.nodes.Node;
import com.itextpdf.styledxmlparser.jsoup.nodes.TextNode;
import jakarta.xml.bind.JAXBElement;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.util.JsonUtil;
import net.cjsah.util.OfficeUtil;
import org.docx4j.TraversalUtil;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Tr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.IllegalFormatFlagsException;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class Main {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        String jsonStr = FileUtil.readString(new File("article.json"), StandardCharsets.UTF_8);
        JSONObject json = JsonUtil.str2Obj(jsonStr, JSONObject.class);
        json = json.getJSONObject("data");
        JSONArray questions = json.getJSONArray("questions");
        JSONArray words = json.getJSONArray("words");
        JSONArray overWords = json.getJSONArray("overWords");

        String article = questions.getJSONObject(0).getString("title");
        article = "<html><body>" + article + "</body></html>";

        Document doc = Jsoup.parse(article);
        Element body = doc.body();

        article = htmlToStr(body);

        PassageNode passage = new PassageNode(article);

        List<String> bold = words.stream().parallel().map(it -> ((JSONObject) it).getString("word")).toList();
        List<String> italic = overWords.stream().parallel().map(it -> ((JSONObject) it).getString("word")).toList();

        List<PassageNode> results = parsePassage(new ArrayList<>() {{
            this.add(passage);
        }}, bold, node -> node.bold = true);
        results = parsePassage(results, italic, node -> node.italic = true);

        String path = "test.docx";
        String resultPath = "result.docx";

        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(path));
            ClassFinder finder = new ClassFinder(Tbl.class);
            MainDocumentPart document = wordMLPackage.getMainDocumentPart();
            new TraversalUtil(document.getContent(), finder);

            log.info("查找到{}个表格", finder.results.size());

            Tbl table = (Tbl) finder.results.get(0);
            Tr tr = (Tr) (table).getContent().get(0);
            JAXBElement<Tc> element = (JAXBElement<Tc>) tr.getContent().get(0);
            P p = (P) element.getValue().getContent().get(0);
            p.getContent().clear();

            int num = 0;
            for (PassageNode node : results) {
                R value = DocUtil.genR(node.value, node.bold, node.italic);
                p.getContent().add(value);
                if (node.italic) {
                    value = DocUtil.genMark(++num);
                    p.getContent().add(value);
                }
            }

            try (
                    FileOutputStream fos = new FileOutputStream(resultPath)
            ) {
                wordMLPackage.save(fos);
            }

        } catch (Docx4JException | IOException e) {
            log.error("读取失败", e);
        }

    }

    public static String htmlToStr(Element element) {
        StringBuilder builder = new StringBuilder();
        for (Node node : element.childNodes()) {
            if (node instanceof TextNode) {
                String value = ((TextNode) node).getWholeText();
                builder.append(value);
            } else if (node instanceof Element tagVal) {
                switch (tagVal.tagName()) {
                    case "br":
                        builder.append("\n");
                        break;
                    case "strong":
                    case "p":
                        builder.append(htmlToStr(tagVal));
                        break;
                    default:
                        System.out.println("未处理标签: " + tagVal.tagName());
                        break;
                }
            }else {
                throw new IllegalFormatFlagsException("未知标签: " + node.getClass());
            }
        }
        return builder.toString();
    }

    private static List<PassageNode> parsePassage(List<PassageNode> nodes, List<String> words, Consumer<PassageNode> consumer) {
        List<PassageNode> results = new ArrayList<>();
        while (!nodes.isEmpty()) {
            PassageNode node = nodes.get(0);
            if (node.parsed) {
                results.add(node);
                nodes.remove(0);
                continue;
            }
            boolean noMatch = true;
            for (String word : words) {
                int index = node.value.indexOf(word);
                if (index != -1 && notLetter(node.value, index - 1) && notLetter(node.value, index + word.length())) {
                    nodes.remove(0);
                    node.substring(word.length() + index, node.value.length(), nodes);
                    node.substring(index, word.length() + index, nodes, part -> {
                        consumer.accept(part);
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
    static class PassageNode {
        String value;
        boolean bold;
        boolean italic;
        boolean parsed;

        public PassageNode(String value) {
            this.value = value;
            this.bold = false;
            this.italic = false;
            this.parsed = false;
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
