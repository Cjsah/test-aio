package net.cjsah.main.template;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.xml.bind.JAXBElement;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.data.Article;
import net.cjsah.data.SubQuestion;
import net.cjsah.data.WordNode;
import net.cjsah.main.doc.DocUtil;
import net.cjsah.util.JsonUtil;
import org.docx4j.TraversalUtil;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

            Tbl answerTable = (Tbl) finder.results.get(6);
            Tbl translateTable = (Tbl) finder.results.get(7);

            List<JSONObject> words = (List<JSONObject>) context.get("words");
            List<String> spells = (List<String>) context.get("spells");
            List<JSONObject> passages = (List<JSONObject>) context.get("passages");

            TableTemplate.parseWords(wordsTable, words);
            TableTemplate.parseSpell(spellTable, spells);
            TableTemplate.parsePassage(passageTable, passages);
            TableTemplate.parseAnswer(answerTable, passages);
            TableTemplate.parseTranslate(translateTable, passages);

//            if (true) return;

            try (FileOutputStream fos = new FileOutputStream(path + "result.docx")) {
                wordMLPackage.save(fos);
            }

        }catch (Exception e) {
            log.error("Error", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void getContext(JSONObject context) {
        String jsonStr = FileUtil.readString(new File("article.json"), StandardCharsets.UTF_8);
        JSONObject json = JsonUtil.str2Obj(jsonStr, JSONObject.class);
        json = json.getJSONObject("data");
        JSONArray questions = json.getJSONArray("questions");
        JSONArray wordsArray = json.getJSONArray("words");
        JSONArray overWordsArray = json.getJSONArray("overWords");
        JSONArray subQuestions = json.getJSONArray("subquestions");

        List<WordNode> studyWords = wordsArray.stream().parallel().map(it -> WordNode.fromJson((JSONObject) it)).toList();
        List<WordNode> overWords = overWordsArray.stream().parallel().map(it -> WordNode.fromJson((JSONObject) it)).toList();
        List<Article> articlesData = questions.stream().parallel().map(it -> Article.fromApiJson((JSONObject) it)).toList();

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
            map.put("count", -1); // TODO 未知数据
            map.put("answers", DocUtil.parseHtml(article.getParse(), false));
            map.put("translate", Collections.emptyList()); // TODO 目前留空

            DocUtil.ParseProgress progress = DocUtil.parseHtmlNode(article.getTitle(), studyWords, overWords);

            List<P> passages = progress.getNodes();
            List<JSONObject> passageWords = progress.getOverWords();

            for (P node : passages) {
                while (!node.getContent().isEmpty()) {
                    Text text = ((JAXBElement<Text>) ((R) node.getContent().get(0)).getContent().get(0)).getValue();
                    String value = text.getValue();
                    if (value.trim().isEmpty()) {
                        node.getContent().remove(0);
                    } else {
                        text.setValue(value.stripLeading());
                        break;
                    }
                }

            }

            passages = passages.stream().parallel().filter(it -> !it.getContent().isEmpty()).collect(Collectors.toList());
            passages.addAll(DocUtil.parseText(article.getQuestions(), false));

            map.put("passage", passages);
            map.put("words", passageWords);
            articles.add(map);
        }

        context.put("words", words);
        context.put("spells", spells);
        context.put("passages", articles);
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


}
