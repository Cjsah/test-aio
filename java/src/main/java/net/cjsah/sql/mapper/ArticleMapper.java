package net.cjsah.sql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cjsah.sql.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
