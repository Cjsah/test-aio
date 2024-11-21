package net.cjsah.sql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cjsah.sql.pojo.TempNode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TempMapper extends BaseMapper<TempNode> {
}
