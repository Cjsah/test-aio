package net.cjsah.main.parse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
//        try (SqlSession session = MybatisPlus.session.openSession(true)) {
//            PassageTotalTMapper totalMapper = session.getMapper(PassageTotalTMapper.class);
//            Passage1Mapper psgMapper = session.getMapper(Passage1Mapper.class);
//
//            long total = psgMapper.selectCount(new QueryWrapper<>());
//            long parsed = 0;
//
//            while (true) {
//                Passage passage = psgMapper.selectOne(new QueryWrapper<>() {{
//                    this.last("limit 1");
//                }});
//                if (passage == null) break;
//
//                OPassage.parse(passage);
//                PassageTotalT passageTotal = new PassageTotalT();
//                passageTotal.from(passage);
//
//                int insert = totalMapper.insert(passageTotal);
////                if (true) break;
//                if (insert != 0) {
//                    List<? extends Passage> list = psgMapper.selectList(new QueryWrapper<>() {{
//                        this.eq("passage_id", passage.getPassageId());
//                    }});
//
//                    psgMapper.deleteById(passage);
//                    for (Passage tmp : list) {
//                        psgMapper.deleteById(tmp);
//                    }
//
//                    parsed += list.size() + 1;
//
//                }
//                log.info("解析进度 -> 共{}, 已解析{}", total, parsed);
//            }
//            log.info("解析完成");
//
//        }
    }
}