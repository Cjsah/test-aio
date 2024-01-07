package net.cjsah.main.parse;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.main.parse.passages.Passage2;
import net.cjsah.sql.MybatisPlus;
import net.cjsah.sql.mapper.Passage1AMapper;
import net.cjsah.sql.mapper.Passage2AMapper;
import net.cjsah.sql.pojo.Passage1A;
import net.cjsah.sql.pojo.Passage2A;
import net.cjsah.sql.pojo.PassageWord;
import net.cjsah.util.StringUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ParseSql {
    static int totalCount = 0;
    static int completeCount = 0;

    public static void main(String[] args) {
        try (SqlSession session = MybatisPlus.session.openSession(true)) {
            Passage1AMapper mapper1 = session.getMapper(Passage1AMapper.class);
            Passage2AMapper mapper2 = session.getMapper(Passage2AMapper.class);


            long total = mapper1.selectCount(new QueryWrapper<>());
            long parsed = 0;

            while (true) {
                Passage1A passage = mapper1.selectOne(new QueryWrapper<>() {{
                    this.last("limit 1");
                }});
                if (passage == null) break;
                if (passage.getPassageId() == 0) {
                    mapper1.deleteById(passage);
                    continue;
                }

                List<Passage1A> list = mapper1.selectList(new QueryWrapper<>() {{
                    this.eq("passage_id", passage.getPassageId());
                }});

                int sum = list.stream().parallel().mapToInt(PassageWord::getDifficulty).sum();
                int average = sum / list.size();

                Passage2A newPassage = passage.copy(new Passage2A());
                newPassage.setDifficulty(average);

                int insert = mapper2.insert(newPassage);
                if (insert != 0) {
                    for (Passage1A passage1 : list) {
                        mapper1.deleteById(passage1);
                    }
                    parsed += list.size();
                }
                log.info("解析进度 -> 共{}, 已解析{}", total, parsed);
            }
            log.info("解析完成");

        }


    }

}
