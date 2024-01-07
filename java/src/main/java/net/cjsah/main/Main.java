package net.cjsah.main;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.main.parse.passages.sql.TPassage;
import net.cjsah.sql.MybatisPlus;
import net.cjsah.sql.mapper.Passage2AMapper;
import net.cjsah.sql.mapper.Passage2Mapper;
import net.cjsah.sql.mapper.PassageTotalMapper;
import net.cjsah.sql.pojo.Passage;
import net.cjsah.sql.pojo.PassageTotal;
import net.cjsah.sql.pojo.PassageWord;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) {
        try (SqlSession session = MybatisPlus.session.openSession(true)) {
            PassageTotalMapper totalMapper = session.getMapper(PassageTotalMapper.class);
            Passage2AMapper tPsgMapper = session.getMapper(Passage2AMapper.class);
            Passage2Mapper oPsgMapper = session.getMapper(Passage2Mapper.class);


            long total = tPsgMapper.selectCount(new QueryWrapper<>());
            long parsed = 0;

            while (true) {
                PassageWord passage = tPsgMapper.selectOne(new QueryWrapper<>() {{
                    this.last("limit 1");
                }});
                if (passage == null) break;

                TPassage.parse(passage);
                PassageTotal passageTotal = new PassageTotal();
                passageTotal.from(passage);


                List<? extends Passage> list = oPsgMapper.selectList(new QueryWrapper<>() {{
                    this.eq("passage_id", passage.getPassageId());
                }});


                int insert = totalMapper.insert(passageTotal);
                if (insert != 0) {
                    tPsgMapper.deleteById(passage);
                    for (Passage psg : list) {
                        oPsgMapper.deleteById(psg);
                    }
                    parsed++;
                }
                log.info("解析进度 -> 共{}, 已解析{}", total, parsed);
            }
            log.info("解析完成");

        }
    }
}