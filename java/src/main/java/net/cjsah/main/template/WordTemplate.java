package net.cjsah.main.template;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.data.Article;
import net.cjsah.data.WordNode;
import net.cjsah.util.DocUtil;
import net.cjsah.util.JsonUtil;
import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Tbl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Slf4j
public class WordTemplate {
    private static final Pattern pattern = Pattern.compile("\\$\\{\\S+}");
    private static final Map<String, Replacer> FINDER = new HashMap<>();

    public static void main(String[] args) {
        WordTemplate template = new WordTemplate();
//        template.generate("study-all.docx");
        template.generate("study-template.docx");
    }

    private void generate(String file) {
        String path = "./";

        try {
            String s = FileUtil.readUtf8String(new File("template.json"));
                JSONObject json = JsonUtil.str2Obj(s, JSONObject.class);

            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(path, file));

            JSONObject context = new JSONObject();
            getContext(context, wordMLPackage);

            MainDocumentPart document = wordMLPackage.getMainDocumentPart();

            List<Object> content = document.getContent();

            List<Object> newList = new ArrayList<>();

            ClassFinder finder = new ClassFinder(Tbl.class);

            for (Object o : content) {
                String value = o.toString();

                if (pattern.matcher(value).find()) {
                    value = value.substring(2, value.length() - 1);
                    int emptyLines = (int) context.getOrDefault("allow-" + value, 0);
                    if (emptyLines >= 0) {
                        for (int i = 0; i < emptyLines; i++) {
                            newList.add(DocUtil.genP(""));
                        }
                        continue;
                    }
                    JSONArray array = json.getJSONArray(value);
                    if (array != null) {
                        List<Object> append = new ArrayList<>();
                        for (int i = 0; i < array.size(); i++) {
                            String node = array.getString(i);
                            Object object = XmlUtils.unmarshalString(node);
                            append.add(object);
                        }
                        Replacer replacer = FINDER.get(value);
                        if (replacer != null) {
                            finder.results.clear();
                            new TraversalUtil(append, finder);
                            replacer.replace(finder.results, context);
                        }
                        newList.addAll(append);
                    }
                } else {
                    newList.add(o);
                }
            }

            content.clear();
            content.addAll(newList);

            try (FileOutputStream fos = new FileOutputStream(path + "result.docx")) {
                wordMLPackage.save(fos);
            }

        }catch (Exception e) {
            log.error("Error", e);
        }
    }


    private static void getContext(JSONObject context, WordprocessingMLPackage wordMLPackage) {
        String jsonStr = FileUtil.readUtf8String(new File("article.json"));
        JSONObject json = JsonUtil.str2Obj(jsonStr, JSONObject.class);
        JSONArray questions = json.getJSONArray("questions");
        JSONArray wordsArray = json.getJSONArray("studyWordList");
        JSONArray overWordsArray = json.getJSONArray("overWords");

        List<WordNode> studyWords = wordsArray.stream().parallel().map(it -> WordNode.fromJson((JSONObject) it)).toList();
        List<WordNode> overWords = overWordsArray.stream().parallel().map(it -> WordNode.fromJson((JSONObject) it)).toList();
        List<Article> articlesData = questions.stream().parallel().map(it -> Article.fromApiJson((JSONObject) it)).toList();

        List<JSONObject> words = new ArrayList<>();
        List<JSONObject> articles = new ArrayList<>();
        List<String> spells = new ArrayList<>();

        for (int i = 0; i < studyWords.size(); i++) {
            WordNode word = studyWords.get(i);
            if (null == word.getWord() || null == word.getMeaning()) continue;
            List<Map<String, String>> translateMap = Arrays.stream(word.getMeaning().split("(<br/>|\\n)"))
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
            map.put("count", article.getWordCount()); // TODO 未知数据
            map.put("answers", DocUtil.parseHtml(article.getParse(), false));
            map.put("translate", Collections.emptyList()); // TODO 目前留空

            DocUtil.ParseProgress progress = DocUtil.parseHtmlNode(wordMLPackage, article.getTitle(), studyWords, overWords);

            List<ContentAccessor> passages = progress.getNodes();
            List<JSONObject> passageWords = progress.getOverWords();

            passages = DocUtil.trim(passages);

            passages.addAll(DocUtil.parseText(article.getQuestions(), false));

            map.put("passage", passages);
            map.put("words", passageWords);
            articles.add(map);
        }

        context.put("words", words);
        context.put("spells", spells);
        context.put("passages", articles);

//        context.put("allow-tip", -1);
//        context.put("allow-word", 9);
//        context.put("allow-article", -1);
//        context.put("allow-answer", 0);

//        context.put("allow-tip", -1);
//        context.put("allow-word", -1);
//        context.put("allow-article", 0);
//        context.put("allow-answer", 0);

        context.put("allow-tip", -1);
        context.put("allow-word", -1);
        context.put("allow-article", -1);
        context.put("allow-answer", 0);

//        context.put("allow-tip", 0);
//        context.put("allow-word", 3);
//        context.put("allow-article", 0);
//        context.put("allow-answer", -1);

    }

    static {
        FINDER.put("word", ((tables, context) -> {
            List<JSONObject> words = (List<JSONObject>) context.get("words");
            List<String> spells = (List<String>) context.get("spells");
            Tbl wordsTable = (Tbl) tables.get(0);
            Tbl spellTable = (Tbl) tables.get(2);
            WordTableTemplate.parseWords(wordsTable, words);
            WordTableTemplate.parseSpell(spellTable, spells);
        }));
        FINDER.put("article", ((tables, context) -> {
            List<JSONObject> passages = (List<JSONObject>) context.get("passages");
            Tbl passageTable = (Tbl) tables.get(0);
            WordTableTemplate.parsePassage(passageTable, passages);
        }));
        FINDER.put("answer", ((tables, context) -> {
            List<JSONObject> passages = (List<JSONObject>) context.get("passages");
            Tbl answerTable = (Tbl) tables.get(0);
            Tbl translateTable = (Tbl) tables.get(1);
            WordTableTemplate.parseAnswer(answerTable, passages);
            WordTableTemplate.parseTranslate(translateTable, passages);
        }));
    }

    interface Replacer {
        void replace(List<Object> tables, JSONObject context) throws JAXBException;
    }
}
