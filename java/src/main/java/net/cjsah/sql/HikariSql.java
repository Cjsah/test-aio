package net.cjsah.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Properties;

@Slf4j
public class HikariSql {
    private static final HikariDataSource dataSource;
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        Properties properties = new Properties();
        properties.setProperty("jdbcUrl", "jdbc:mysql://192.168.0.10:3306/passage");
        properties.setProperty("username", "root");
        properties.setProperty("password", "mysql");
        properties.setProperty("poolName", "Hikari");
        properties.setProperty("maximumPoolSize", "20");
        properties.setProperty("minimumIdle", "1");
        properties.setProperty("connectionTimeout", "30000");
        properties.setProperty("idleTimeout", "600000");
        properties.setProperty("maxLifetime", "1800000");
        HikariConfig hikariConfig = new HikariConfig(properties);
        dataSource = new HikariDataSource(hikariConfig);
    }

    public static void insert(String filename, long id, int count, String content, String questions, String answers) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("insert into `passage_1`(`create_time`, `passage_id`, `word_count`, `content`, `questions`, `answers`, `file`) values (?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, date());
            preparedStatement.setLong(2, id);
            preparedStatement.setInt(3, count);
            preparedStatement.setString(4, content);
            preparedStatement.setString(5, questions);
            preparedStatement.setString(6, answers);
            preparedStatement.setString(7, filename);
            preparedStatement.execute();
        } catch (SQLException e) {
            log.error("Insert Error", e);
        }
    }

    private static String date() {
        LocalDateTime ldt = LocalDateTime.now();
        final ZoneId zoneId = ZoneId.of("GMT+8");
        final ZonedDateTime zdt = ldt.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return FORMAT.format(date);
    }
}
