package net.cjsah.sql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cjsah.sql.pojo.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}
