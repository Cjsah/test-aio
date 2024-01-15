package net.cjsah.main.template;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;
import com.itextpdf.styledxmlparser.jsoup.nodes.TextNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.data.Article;
import net.cjsah.data.WordNode;
import net.cjsah.main.doc.DocUtil;
import net.cjsah.util.JsonUtil;
import org.docx4j.TraversalUtil;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.R;
import org.docx4j.wml.Tbl;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IllegalFormatFlagsException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class TestWord {

    public static void main(String[] args) {
        TestWord template = new TestWord();
        template.generate("template.docx");
    }

    @SuppressWarnings("unchecked")
    private void generate(String file) {
        String path = "./math/template/";

        try {

            JSONObject context = new JSONObject();
            getContext(context);

            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(path, file));

            ClassFinder finder = new ClassFinder(Tbl.class);
            MainDocumentPart document = wordMLPackage.getMainDocumentPart();
            new TraversalUtil(document.getContent(), finder);

            Tbl wordsTable = (Tbl) finder.results.get(0);
            Tbl spellTable = (Tbl) finder.results.get(2);
            Tbl passageTable = (Tbl) finder.results.get(3);

            List<JSONObject> words = (List<JSONObject>) context.get("words");
            List<String> spells = (List<String>) context.get("spells");
            List<JSONObject> passages = (List<JSONObject>) context.get("passages");

            TableTemplate.parseWords(wordsTable, words);
            TableTemplate.parseSpell(spellTable, spells);
            TableTemplate.parsePassage(passageTable, passages);

//            if (true) return;

            try (FileOutputStream fos = new FileOutputStream(path + "result.docx")) {
                wordMLPackage.save(fos);

            }

        }catch (Exception e) {
            log.error("Error", e);
        }
    }

    private static void getContext(JSONObject context) {
        String jsonStr = FileUtil.readString(new File("article.json"), StandardCharsets.UTF_8);
        JSONObject json = JsonUtil.str2Obj(jsonStr, JSONObject.class);
        json = json.getJSONObject("data");
        JSONArray questions = json.getJSONArray("questions");
        JSONArray wordsArray = json.getJSONArray("words");
        JSONArray overWordsArray = json.getJSONArray("overWords");

        List<WordNode> studyWords = wordsArray.stream().parallel().map(it -> WordNode.fromJson((JSONObject) it)).toList();
        List<WordNode> overWords = overWordsArray.stream().parallel().map(it -> WordNode.fromJson((JSONObject) it)).toList();
        List<Article> articlesData = questions.stream().parallel().map(it -> Article.fromJson((JSONObject) it)).toList();

        List<JSONObject> words = new ArrayList<>();
        List<JSONObject> articles = new ArrayList<>();
        List<String> spells = new ArrayList<>();

        for (int i = 0; i < studyWords.size(); i++) {
            WordNode word = studyWords.get(i);
            if (null == word.getWord() || null == word.getMeaning()) continue;
            List<Map<String, String>> translateMap = Arrays.stream(word.getMeaning().split("(<br>|\\n)"))
                    .filter(meaning -> !meaning.trim().isEmpty() && !meaning.startsWith("*"))
                    .map(it -> it.replace("&", "&amp;"))
                    .peek(spells::add)
                    .map(it -> Collections.singletonMap("translate", it))
                    .collect(Collectors.toList());
            JSONObject map = new JSONObject();
            map.put("index", i + 1);
            map.put("word", word.getWord());
            map.put("symbol", word.getEnglishPronunciation());
            map.put("translates", translateMap);
            words.add(map);
        }
        for (int i = 0; i < articlesData.size(); i++) {
            Article article = articlesData.get(i);
            JSONObject map = new JSONObject();
            map.put("index", i + 1);
            map.put("num", article.getId());
            map.put("difficulty", -1); // TODO 未知数据
            map.put("count", -1); // TODO 未知数据
            String passage = "<html><body>" + article.getTitle() + "</body></html>";
            Element body = Jsoup.parse(passage).body();
            passage = htmlToStr(body);
            PassageNode passageNode = new PassageNode(passage);
            List<PassageNode> results = parsePassage(new ArrayList<>() {{
                this.add(passageNode);
            }}, studyWords, node -> node.bold = true);
            results = parsePassage(results, overWords, node -> node.italic = true);

            List<R> passages = new ArrayList<>();
            List<JSONObject> passageWords = new ArrayList<>();

            int index = 0;
            for (PassageNode node : results) {
                R value = DocUtil.genR(node.value, node.bold, node.italic);
                passages.add(value);
                if (node.italic) {
                    int num = 0;
                    for (JSONObject word : passageWords) {
                        if (node.wordNode.getWord().equals(word.getString("word"))) {
                            num = word.getIntValue("index");
                            break;
                        }
                    }
                    if (num == 0) {
                        num = ++index;
                    }
                    value = DocUtil.genMark(num);
                    passages.add(value);
                    if (num == index) {
                        JSONObject word = new JSONObject();
                        word.put("index", index);
                        word.put("word", node.wordNode.getWord());
                        word.put("symbol", node.wordNode.getAmericaPronunciation());
                        word.put("translate", node.wordNode.getMeaning());
                        passageWords.add(word);
                    }
                }
            }

            map.put("passages", passages);
            map.put("words", passageWords);
            articles.add(map);
        }

        context.put("words", words);
        context.put("spells", spells);
        context.put("passages", articles);
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

    private static List<PassageNode> parsePassage(List<PassageNode> nodes, List<WordNode> words, Consumer<PassageNode> consumer) {
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
                if (index != -1 && notLetter(node.value, index - 1) && notLetter(node.value, index + word.getWord().length())) {
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
    public static class Node {
        public final String index;
        public final String word;

        public Node(int index, String word) {
            this.index = String.format("%03d", index);
            this.word = word;
        }
    }

    @Data
    static class PassageNode {
        String value;
        boolean bold;
        boolean italic;
        boolean parsed;
        WordNode wordNode;

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
