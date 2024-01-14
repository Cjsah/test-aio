package net.cjsah.main.parse;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.sql.MybatisPlus;
import net.cjsah.sql.mapper.PassageModifyMapper;
import net.cjsah.sql.mapper.PassageTotalMapper;
import net.cjsah.sql.pojo.PassageModify;
import net.cjsah.sql.pojo.PassageTotal;
import net.cjsah.util.JsonUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ChangeSql {
    public static void main(String[] args) {
        try (SqlSession session = MybatisPlus.session.openSession(true)) {
            PassageTotalMapper totalMapper = session.getMapper(PassageTotalMapper.class);
            PassageModifyMapper modifyMapper = session.getMapper(PassageModifyMapper.class);

            long total = totalMapper.selectCount(new QueryWrapper<>());
            long parsed = 0;

            while (true) {
                PassageTotal passage = totalMapper.selectOne(new QueryWrapper<>() {{
                    this.last("limit 1");
                }});
                if (passage == null) break;
                List<Question> questions = JsonUtil.str2List(passage.getQuestions(), JSONObject.class)
                        .stream().parallel()
                        .map(Question::fromJson)
                        .toList();

                StringBuilder builder = new StringBuilder();
                builder.append(passage.getContent());
                builder.append("\n\n");

                for (int i = 0; i < questions.size(); i++) {
                    Question question = questions.get(i);
                    question.num = i + 1;
                    String options = String.join("\n", question.options);
                    question.question = String.format("%d. %s\n%s", question.num, question.question, options);
                    builder.append(question.question);
                    builder.append("\n\n");
                }

                builder.setLength(builder.length() - 2);

                String answer = String.join("\n", questions.stream().parallel().map(it -> it.num + ". " + it.answer).toList());

                List<Integer> abilities = JsonUtil.str2List(passage.getWordRange(), int.class);
                if (abilities.isEmpty()) {
                    abilities = Collections.singletonList(0);
                }

                PassageModify modified = new PassageModify();
                modified.setTitle(builder.toString());
                modified.setAnswer(answer);
                modified.setTranslate(passage.getTranslate());
                modified.setDifficulty(passage.getDifficultyAverage());
                modified.setAbilityStart(abilities.get(0));
                modified.setAbilityEnd(abilities.get(abilities.size() - 1));

//                if (true) break;

                int insert = modifyMapper.insert(modified);
//                if (true) break;
                if (insert != 0) {
                    totalMapper.deleteById(passage);
                    parsed ++;
                }
                log.info("解析进度 -> 共{}, 已解析{}", total, parsed);
            }
            log.info("解析完成");

        }

    }

    @Data
    static class Question {
        public int num;
        public String question;
        public String answer = "";
        public final List<String> options= new ArrayList<>();

        public static Question fromJson(JSONObject json) {
            Question question = new Question();
            question.options.addAll(json.getList("options", String.class));
            question.num = json.getIntValue("num");
            question.question = json.getString("question");
            question.answer = json.getString("answer");
            return question;
        }

    }

}
