package net.cjsah.main;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.main.parse.passages.sql.OPassage;
import net.cjsah.sql.MybatisPlus;
import net.cjsah.sql.mapper.Passage2Mapper;
import net.cjsah.sql.mapper.PassageTotalMapper;
import net.cjsah.sql.pojo.Passage;
import net.cjsah.sql.pojo.PassageTotal;
import org.apache.ibatis.session.SqlSession;

@Slf4j
public class Main {

    public static void main(String[] args) {
        try (SqlSession session = MybatisPlus.session.openSession(true)) {
            PassageTotalMapper totalMapper = session.getMapper(PassageTotalMapper.class);
            Passage2Mapper psgMapper = session.getMapper(Passage2Mapper.class);


            long total = psgMapper.selectCount(new QueryWrapper<>());
            long parsed = 0;

            while (true) {
                Passage passage = psgMapper.selectOne(new QueryWrapper<>() {{
                    this.last("limit 1");
                }});
                if (passage == null) break;

                OPassage.parse(passage);
                PassageTotal passageTotal = new PassageTotal();
                passageTotal.from(passage);

                int insert = totalMapper.insert(passageTotal);
//                if (true) break;
                if (insert != 0) {
                    psgMapper.deleteById(passage);
                    parsed++;
                }
                log.info("解析进度 -> 共{}, 已解析{}", total, parsed);
            }
            log.info("解析完成");

        }
    }
}