package net.cjsah.main.parse.passages.sql;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.sql.MybatisPlus;
import net.cjsah.sql.mapper.Passage2Mapper;
import net.cjsah.sql.pojo.Passage;
import net.cjsah.util.JsonUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class OPassage {
    public static void main(String[] args) {
        try (SqlSession session = MybatisPlus.session.openSession(true)) {
            Passage2Mapper psgMapper = session.getMapper(Passage2Mapper.class);
            Passage passage = psgMapper.selectOne(new QueryWrapper<>() {{
                this.last("limit 1");
            }});
            OPassage.parse(passage);
        }
        log.info("解析完成");
    }

    private static final Pattern pattern = Pattern.compile("^\\d+\\..+");

    public static void parse(Passage passage) {
//        System.out.println(passage);
        String content = passage.getContent();
        String questions = passage.getQuestions();
        String answers = passage.getAnswers();

        content = content.substring(22, content.length() - 1);

        List<Question> resultQuestions = new ArrayList<>();
        Question now = null;
        for (String line : questions.split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String group = matcher.group();
                String[] split = group.split("\\.", 2);
                now = new Question(Integer.parseInt(split[0]), split[1].trim());
                resultQuestions.add(now);
            } else if (now != null) {
                now.options.add(line);
            }
        }

        for (String answerNode : Arrays.stream(answers.split("[ \n]")).filter(it -> !it.isEmpty()).toList()) {
            Matcher matcher = pattern.matcher(answerNode);
            if (matcher.find()) {
                String group = matcher.group();
                String[] split = group.split("\\.", 2);
                int num = Integer.parseInt(split[0]);
                for (Question question : resultQuestions) {
                    if (question.num == num) {
                        question.answer = split[1];
                        break;
                    }
                }
            }

        }

        questions = JsonUtil.obj2Str(resultQuestions);

        passage.setContent(content);
        passage.setQuestions(questions);
        passage.setAnswers("");
    }

    @Data
    static class Question {
        public final int num;
        public final String question;
        public final List<String> options= new ArrayList<>();
        public String answer = "";
    }

}
