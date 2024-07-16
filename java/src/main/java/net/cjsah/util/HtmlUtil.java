package net.cjsah.util;

import lombok.Data;
import net.cjsah.data.Article;
import net.cjsah.data.SubQuestion;
import net.cjsah.data.WordNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Html 解析
 * <p/>
 * 字号:
 * <ul>
 *     <li>20 -> 小三</li>
 *     <li>16 -> 小四</li>
 * </ul>
 */
public class HtmlUtil {

    public static String indent(String content) {
        return "<p style=\"text-indent: 24px;\">" + content + "</p>";
    }

    public static String ofTip() {
        return indent("请逐字逐句翻译，确保看懂每一句话，看懂文章再做题，不要为了做题而读文章，不要跳略读，不要先看题再看文章。") +
                indent("我们的目标是在高于80%正确率的前提下，阅读文章每分钟在80个单词以上。") +
                indent("经过刻意学习后，你一定能实现这个目标。当目标达成时，英语学习对于你来说轻松无比，拿高分顺理成章。");
    }


    public static String ofWords(List<WordNode> words) {
        if (words.isEmpty()) return "";
        StringBuilder builder = new StringBuilder();
        subTitle(builder, "生词表");
        List<String> spells = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            WordNode word = words.get(i);
            // <span><span>${index}.</span> <b>${word}</b> ${pronunciation}</span><br/>
            builder.append("<p><span style=\"color:#595959;\">");
            builder.append(i + 1);
            builder.append(".</span> <b>");
            builder.append(word.getWord());
            builder.append("</b> ");
            builder.append(word.getEnglishPronunciation());
            builder.append("</p>");
            spells.addAll(word.getMeanings());
            for (String meaning : word.getMeanings()) {
                builder.append("<p style=\"display:block;text-indent: 24px\">");
                builder.append(meaning);
                builder.append("</p>");
            }
        }
        if (spells.isEmpty()) return builder.toString();
        subTitle(builder, "单词速记");
        addTip(builder, "请在今天学习的单词列表中找到对应单词，写在横线上，边抄边读，可以默写，默写后在单词列表中找到并核对，书写时保持工整。");
        int count = 0;
        spell:
        for (int i = 0; i < 4; i++) {
            Collections.shuffle(spells);
            for (String translate : spells) {
                builder.append("<p style=\"text-align:right;margin:12px 0;\">");
                builder.append(translate);
                builder.appendCodePoint(160);
                builder.append(++count);
                builder.append(".__________");
                builder.append("</p>");
                if (count >= 120 && i != 0) {
                    break spell;
                }
            }
        }
        return builder.toString();
    }

    public static String ofArticle(List<Article> articles, List<String> words, List<WordNode> overWords) {
        if (articles.isEmpty()) return "";
        StringBuilder builder = new StringBuilder();
        subTitle(builder, "智能阅读训练");
        addTip(builder, "我们进行的是刻意练习，不是简单的做阅读题，我们要实现的目标是流利性阅读，并在流利性阅读中提升学生的词汇和阅读能力。所以我们要做到以下几点：");
        addTip(builder, "第一遍阅读中不认识的单词和句子，尽量猜测词义，并加以标记。");
        addTip(builder, "读完全文后，要查询生词和句子的意思，并抄在右侧批注区，不要在原文上注释。复习时对照所划的单词、句子以及批注强化学习即可。");
        addTip(builder, "流利性阅读训练的目的是提高英语语感，所以切勿为了做题而做题。需要逐字逐句的搞懂文章的意思。");
        addTip(builder, "阅读速度是很重要的学习指标，请在理解词句的基础上尽量提高阅读速度。");
        addTip(builder, "☆阅读计时从此处开始，请按顺序完成阅读，并注意记录时间。", false);
        for (int articleIndex = 0; articleIndex < articles.size(); articleIndex++) {
            Article article = articles.get(articleIndex);
            // <p><b>Passage ${index}</b> 编号:${id}, 字数:${wordCount}, 阅读开始时间：_____点_____分</p>
            builder.append("<p><b style=\"font-size:16px;\">Passage ");
            builder.append(articleIndex + 1);
            builder.append("</b> 编号:");
            builder.append(article.getId());
            builder.append(", 字数:");
            builder.append(article.getWordCount());
            builder.append(", 阅读开始时间：_____点_____分");
            builder.append("<table style=\"margin-left:6px;\"><tr><td style=\"width:160%\">");
            //content
            ParseResult parseResult = resolvePassage(article.getTitle(), words, overWords);
            builder.append(parseResult.passage);
            System.out.println(article.getId());
            System.out.println(parseResult.passage);
            builder.append("<br/>");
            for (SubQuestion question : article.getQuestions()) {
                builder.append("<br/>");
                builder.append(question.getTitle());
                builder.append("<br/>");
                if (addOptionCheckFail(builder, 'A', question.getOptionA())) continue;
                if (addOptionCheckFail(builder, 'B', question.getOptionB())) continue;
                if (addOptionCheckFail(builder, 'C', question.getOptionC())) continue;
                addOptionCheckFail(builder, 'D', question.getOptionD());
            }
            builder.append("<br/></td><td>");
            // sider word
            for (int wordIndex = 0; wordIndex < parseResult.words.size(); wordIndex++) {
                WordNode word = parseResult.words.get(wordIndex);
                builder.append("<br/><p>");
                builder.append(wordIndex + 1);
                builder.append(". ");
                builder.append(word.getWord());
                builder.appendCodePoint(160);
                builder.append(word.getEnglishPronunciation());
                builder.append("</p>");
                if (word.getMeanings().isEmpty()) continue;
                builder.append("<p>");
                for (String meaning : word.getMeanings()) {
                    builder.append(meaning);
                    builder.appendCodePoint(160);
                }
                builder.append("</p><br/>");
            }
            builder.append("</td></tr></table><br/>");
        }
        return builder.toString();
    }

    public static String ofAnswer(List<Article> articles) {
        if (articles.isEmpty()) return "";
        StringBuilder builder = new StringBuilder();
        subTitle(builder, "答案");
        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            // <p><b>Passage ${index}</b></p>
            builder.append("<p><b style=\"font-size:16px;\">Passage ");
            builder.append(i + 1);
            builder.append("</b></p><p>");
            builder.append(article.getAnswer());
            builder.append("</p><p>");
            builder.append(article.getParse());
            builder.append("</p>");
        }
        // TODO 暂时留空
//        subTitle(builder, "全文翻译");
//        for (int i = 0; i < articles.size(); i++) {
//            Article article = articles.get(i);
//            // <p><b>Passage ${index}</b></p>
//            builder.append("<p><b style=\"font-size:16px;\">Passage ");
//            builder.append(i + 1);
//            builder.append("</b></p><p>");
//            builder.append(article.getAnswer());
//            builder.append("</p><p>");
//            builder.append(article.getParse());
//            builder.append("</p>");
//        }
        return builder.toString();
    }

    private static void addTip(StringBuilder builder, String tip) {
        addTip(builder, tip, true);
    }

    private static void addTip(StringBuilder builder, String tip, boolean indent) {
        builder.append("<p style=\"font-size:12px;color:#595959;");
        if (indent) builder.append("text-indent:24px;");
        builder.append("\">");
        builder.append(tip);
        builder.append("</p>");
    }

    private static void subTitle(StringBuilder builder, String name) {
        builder.append("<p style=\"font-size:20px;text-align:center;margin:12px 0;\">");
        builder.append(name);
        builder.append("</p>");
    }

    private static boolean addOptionCheckFail(StringBuilder builder, char option, String text) {
        if (text == null || text.isEmpty()) return true;
        builder.append(option);
        builder.append(". ");
        builder.append(text);
        builder.append("<br/>");
        return false;
    }

    public static ParseResult resolvePassage(String passage, List<String> studyWords, List<WordNode> overWords) {
        List<WordNode> nodes = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        StringBuilder wordBuilder = new StringBuilder();
        boolean append = true;
        for (int i = 0; i < passage.length(); i++) {
            int c = passage.codePointAt(i);
            if (StringUtil.isLetter(c)) {
                if (append) {
                    wordBuilder.appendCodePoint(c);
                } else {
                    builder.appendCodePoint(c);
                }
            } else {
                if (wordBuilder.length() > 0) {
                    String word = wordBuilder.toString();
                    wordBuilder.setLength(0);
                    WordNode node;
                    if (studyWords.contains(word)) {
                        builder.append("<b>");
                        builder.append(word);
                        builder.append("</b>");
                    } else if ((node = StreamUtil.find(overWords, it -> word.equals(it.getWord()))) != null){
                        builder.append("<i>");
                        builder.append(word);
                        builder.append("</i>");
                        int num = nodes.indexOf(node);
                        if (num == -1) {
                            num = nodes.size();
                            nodes.add(node);
                        }
                        builder.append("<sup>[");
                        builder.append(num + 1);
                        builder.append("]</sup>");
                    } else {
                        builder.append(word);
                    }
                }
                builder.appendCodePoint(append && c == ' ' ? 160 : c);
                switch (c) {
                    case '<':
                        append = false;
                        break;
                    case '>':
                        append = true;
                        break;
                }
            }

        }
        return new ParseResult(builder.toString(), nodes);
    }

    @Data
    public static final class ParseResult {
        private final String passage;
        private final List<WordNode> words;
    }

}
