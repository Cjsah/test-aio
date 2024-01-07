package net.cjsah.main;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.sql.MybatisPlus;
import net.cjsah.sql.mapper.Passage1Mapper;
import net.cjsah.sql.mapper.Passage2Mapper;
import net.cjsah.sql.pojo.Passage;
import net.cjsah.sql.pojo.Passage1;
import net.cjsah.sql.pojo.Passage2;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) {
        try (SqlSession session = MybatisPlus.session.openSession(true)) {
            Passage1Mapper mapper = session.getMapper(Passage1Mapper.class);
            Passage2Mapper newMapper = session.getMapper(Passage2Mapper.class);


            long total = mapper.selectCount(new QueryWrapper<>());
            long parsed = 0;

            while (true) {
                Passage1 passage = mapper.selectOne(new QueryWrapper<>() {{
                    this.last("limit 1");
                }});
                if (passage == null) break;
                List<Passage1> list = mapper.selectList(new QueryWrapper<>() {{
                    this.ne("id", passage.getId());
                    this.eq("passage_id", passage.getPassageId());
                }});

                int sum = list.stream().parallel().mapToInt(Passage::getDifficulty).sum() + passage.getDifficulty();
                int average = sum / (list.size() + 1);

                Passage2 newPassage = passage.copy(new Passage2());
                newPassage.setDifficulty(average);

                int insert = newMapper.insert(newPassage);
                if (insert != 0) {
                    mapper.deleteById(passage);
                    for (Passage1 passage1 : list) {
                        mapper.deleteById(passage1);
                    }
                    parsed += list.size() + 1;
                }
                log.info("解析进度 -> 共{}, 已解析{}", total, parsed);
            }
            log.info("解析完成");

        }



    }
}