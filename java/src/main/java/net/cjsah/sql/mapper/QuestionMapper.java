package net.cjsah.sql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cjsah.sql.pojo.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
}
