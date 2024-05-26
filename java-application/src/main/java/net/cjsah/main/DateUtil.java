package net.cjsah.main;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static ZonedDateTime nowDate() {
        LocalDateTime ldt = LocalDateTime.now();
        return ldt.atZone(ZoneId.of("GMT+8"));
    }

    public static Date now() {
        return Date.from(nowDate().toInstant());
    }

    public static Date afterDays(Date now, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public static Date afterDays(int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now());
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}
