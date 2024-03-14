package net.cjsah.main.parse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SortValue {

    public static void main(String[] args) {
//        try (SqlSession session = MybatisPlus.session.openSession(true)) {
//            PassageTotalMapper mapper = session.getMapper(PassageTotalMapper.class);
//
//            long total = mapper.selectCount(new QueryWrapper<>());
//            long parsed = 0;
//
//            while (true) {
//                PassageTotal passage = mapper.selectOne(new QueryWrapper<>() {{
//                    this.eq("word_average", 0);
//                    this.last("limit 1");
//                }});
//                if (passage == null) break;
//
//                String wordRange = passage.getWordRange();
//                String difficultyRange = passage.getDifficultyRange();
//                List<Integer> wordRangeList = JsonUtil.str2List(wordRange, int.class);
//                List<Integer> difficultyRangeList = JsonUtil.str2List(difficultyRange, int.class);
//
//                int sum = wordRangeList.stream().mapToInt(it -> it).sum();
//                int average = sum / wordRangeList.size();
//                passage.setWordAverage(average);
//
//                sum = difficultyRangeList.stream().mapToInt(it -> it).sum();
//                average = sum / difficultyRangeList.size();
//                passage.setDifficultyAverage(average);
//
//                mapper.updateById(passage);
//                parsed++;
////                if (true) break;
//                log.info("解析进度 -> 共{}, 已解析{}", total, parsed);
//            }
//            log.info("解析完成");
//
//        }
    }
}