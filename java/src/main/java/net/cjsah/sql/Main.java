package net.cjsah.sql;

import lombok.extern.slf4j.Slf4j;
import net.cjsah.sql.mapper.TempMapper;
import org.apache.ibatis.session.SqlSession;

@Slf4j
public class Main {
    public static void main(String[] args) {

        try (SqlSession session = MybatisPlus.session.openSession(true)) {
            TempMapper mapper = session.getMapper(TempMapper.class);
        }
    }
}
