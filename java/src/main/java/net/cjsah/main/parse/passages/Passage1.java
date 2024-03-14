package net.cjsah.main.parse.passages;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.sql.HikariSql;
import net.cjsah.util.StringUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Passage1 {
    final List<Passage> passages = new ArrayList<>();
    String answer = "";
    Passage current = null;
    Stage stage = Stage.WAIT_START;

    public static void main(String[] args) {
        File file = new File("passage/test.docx");
        try (FileInputStream fis = new FileInputStream(file); XWPFDocument document = new XWPFDocument(fis)) {
            new Passage1().parse(document, file.getPath());
        } catch (Exception e) {
            log.error("err", e);
        }

    }
    public boolean parse(XWPFDocument document, String filename) throws Exception {
        this.answer = "";
        this.current = new Passage();
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (String s : paragraph.getParagraphText().split("\n")) {
                String trim = s.trim();
                if (trim.isEmpty()) continue;
                Line line = new Line(s, trim);
                stage = stage.next.apply(line, stage);
                if (stage == Stage.COMPLETE) {
                    passages.add(current);
                    current = new Passage();
                    stage = Stage.COMPLETED;
                }
                stage.parse.accept(this, line);
            }
        }
        if (passages.isEmpty()) {
            throw new Exception("Empty Passage");
        }
        for (Passage psg : passages) {
//            HikariSql.insert(filename, psg.id, psg.wordCount, 0, psg.content, psg.questions, this.answer, psg.translate);
        }
        return true;
    }

    private void getIndex(int index) {
        for (Passage passage : passages) {
            if (passage.index == index) {
                current = passage;
                return;
            }
        }
        current = null;
    }

    @Data
    static class Passage {
        int index;
        int id;
        int wordCount;
        int difficulty;
        String content = "";
        String questions = "";
        String words = "";
        String translate = "";

    }

    @Data
    static class Line {
        final String origin;
        final String trim;
    }

    static final Pattern PASSAGE_PATTERN = Pattern.compile("Passage \\d");
    static final Pattern COUNT_PATTERN = Pattern.compile("单词数：\\d+");
    static final Pattern ID_PATTERN = Pattern.compile("编号：\\d+");
    static final Pattern DIFFICULTY_PATTERN = Pattern.compile("本次文章词汇难度值：\\d+");

    private static Stage parseFirstStage(Line line, Stage now) {
        if (line.trim.startsWith("答案")) {
            return Stage.ANSWER;
        } else if (line.trim.startsWith("全文参考翻译")) {
            return Stage.WAIT_TRANSLATE;
        } else if (now == Stage.COMPLETED && line.trim.startsWith("Passage")) {
            return Stage.MSG;
        } else {
            return now;
        }
    }

    static final Pattern regex = Pattern.compile("Passage \\d");
    static final Pattern markRegex = Pattern.compile("\t*郑重提示.*");

    @RequiredArgsConstructor
    enum Stage {
        COMPLETED(Passage1::parseFirstStage, (passage, line) -> {}),

        COMPLETE((next, now) -> COMPLETED, (passage, line) -> {}),

        TRANSLATE((next, now) -> now, (passage, line) -> {
            Matcher matcher = regex.matcher(line.trim);
            if (matcher.find()) {
                int index = Integer.parseInt(matcher.group().split(" ")[1]);
                passage.getIndex(index);
            }
            if (passage.current == null) return;
            passage.current.translate += line.origin + "\n";
        }),

        WAIT_TRANSLATE((next, now) -> next.trim.startsWith("Passage ") ? TRANSLATE : now, (passage, line) -> {}),

        ANSWER((next, now) -> next.trim.startsWith("全文参考翻译") ? WAIT_TRANSLATE : now, (passage, line) -> {
            passage.answer += line.origin + "\n";
        }),

        QUESTION((next, now) -> next.trim.startsWith("完成时间") ? COMPLETE : now, (passage, line) -> {
            passage.current.questions += line.origin + "\n";
        }),

        WORD((next, now) -> StringUtil.match(next.trim, markRegex) ? QUESTION : now, (passage, line) -> {
            passage.current.words += line.origin + "\n";
        }),

        CONTENT((next, now) -> next.trim.startsWith("其他单词释义") ? WORD : StringUtil.match(next.trim, markRegex) ? QUESTION : now, (passage, line) -> {
            passage.current.content += line.origin + "\n";
        }),

        MSG((next, now) -> next.trim.startsWith("开始时间") ? CONTENT: now, (passage, line) -> {
            Matcher matcher = PASSAGE_PATTERN.matcher(line.trim);
            if (matcher.find()) {
                passage.current.index = Integer.parseInt(matcher.group().split(" ")[1]);
            }
            matcher = COUNT_PATTERN.matcher(line.trim);
            if (matcher.find()) {
                passage.current.wordCount = Integer.parseInt(matcher.group().split("：")[1]);
            }
            matcher = ID_PATTERN.matcher(line.trim);
            if (matcher.find()) {
                passage.current.id = Integer.parseInt(matcher.group().split("：")[1]);
            }
        }),

        WAIT_PASSAGE((next, now) -> next.trim.startsWith("Passage") ? MSG : now, (passage, line) -> {}),

        WAIT_START((next, now) -> now, (passage, line) -> {
            Matcher matcher = DIFFICULTY_PATTERN.matcher(line.trim);
            if (matcher.find()) {
                passage.current.difficulty = Integer.parseInt(matcher.group().split("：")[1]);
                passage.stage = WAIT_PASSAGE;
            }
        });

        final BiFunction<Line, Stage, Stage> next;
        final BiConsumer<Passage1, Line> parse;
    }

}
