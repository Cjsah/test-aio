package net.cjsah.sql;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class MybatisMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", date(), metaObject);
        this.setFieldValByName("updateTime", date(), metaObject);
        this.setFieldValByName("time", date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", date(), metaObject);
    }

    public static Date date() {
        LocalDateTime ldt = LocalDateTime.now();
        final ZoneId zoneId = ZoneId.systemDefault();
        final ZonedDateTime zdt = ldt.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

}
