package net.cjsah.main.parse;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class TranslateModify {

    private static final Pattern PATTERN = Pattern.compile("Passage \\d\n");

    public static void main(String[] args) {
//        try (SqlSession session = MybatisPlus.session.openSession(true)) {
//            PassageTotalTMapper totalMapper = session.getMapper(PassageTotalTMapper.class);
//            PassageTotalRMapper totalRMapper = session.getMapper(PassageTotalRMapper.class);
//
//            long total = totalMapper.selectCount(new QueryWrapper<>());
//            long parsed = 0;
//
//            while (true) {
//                PassageTotalT passage = totalMapper.selectOne(new QueryWrapper<>() {{
//                    this.last("limit 1");
//                }});
//                if (passage == null) break;
//                if (totalRMapper.exists(new QueryWrapper<>() {{
//                    this.eq("passage_id", passage.getPassageId());
//                }})) {
//                    totalMapper.deleteById(passage);
//                    parsed++;
//                    log.info("解析进度 -> 共{}, 已解析{}", total, parsed);
//                    continue;
//                }
//
//                String translate = passage.getTranslate();
//                if (PATTERN.matcher(translate).find()) {
//                    translate = translate.substring(10);
//                }
//                passage.setTranslate(translate.trim());
//
//                PassageTotalR passageTotal = new PassageTotalR();
//                passageTotal.from(passage);
//
//                int insert = totalRMapper.insert(passageTotal);
////                if (true) break;
//                if (insert != 0) {
//                    totalMapper.deleteById(passage);
//                    parsed++;
//
//                }
//                log.info("解析进度 -> 共{}, 已解析{}", total, parsed);
//            }
//            log.info("解析完成");
//
//        }
    }
}