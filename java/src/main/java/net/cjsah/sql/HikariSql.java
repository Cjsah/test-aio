package net.cjsah.sql;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.util.JsonUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
public class HikariSql {
    private static final HikariDataSource dataSource;
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://192.168.0.10:3306/passage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&serverTimezone=GMT%2B8&autoReconnect=true");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("mysql");
        dataSource.setIdleTimeout(60000);
        dataSource.setAutoCommit(true);
        dataSource.setMaximumPoolSize(20);
        dataSource.setMinimumIdle(1);
        dataSource.setMaxLifetime(600000);
        dataSource.setConnectionTimeout(30000);
        dataSource.setConnectionTestQuery("SELECT 1");
    }

    public static boolean insert(String filename, long id, int count, int difficulty, String content, String questions, String answers, String translate) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into `passage_1`(`create_time`, `passage_id`, `word_count`, `difficulty`, `content`, `questions`, `answers`, `file`, `translate`) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, date());
            preparedStatement.setLong(2, id);
            preparedStatement.setInt(3, count);
            preparedStatement.setInt(4, difficulty);
            preparedStatement.setString(5, content);
            preparedStatement.setString(6, questions);
            preparedStatement.setString(7, answers);
            preparedStatement.setString(8, filename);
            preparedStatement.setString(9, translate);
            preparedStatement.execute();
            return true;
        }
    }

    public static boolean insertWords(String filename, long id, int count, int difficulty, String content, String questions, String answers, String translate, String words) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into `passage_1a`(`create_time`, `passage_id`, `word_count`, `difficulty`, `content`, `questions`, `answers`, `file`, `translate`, `words`) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, date());
            preparedStatement.setLong(2, id);
            preparedStatement.setInt(3, count);
            preparedStatement.setInt(4, difficulty);
            preparedStatement.setString(5, content);
            preparedStatement.setString(6, questions);
            preparedStatement.setString(7, answers);
            preparedStatement.setString(8, filename);
            preparedStatement.setString(9, translate);
            preparedStatement.setString(10, words);
            preparedStatement.execute();
            return true;
        }
    }

    public static boolean update(long id, int wordCount) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select word_range wr from passage_total where passage_id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String range = resultSet.getString("wr");
                List<Integer> list = JsonUtil.str2List(range, int.class);
                boolean change = false;
                if (list.isEmpty()) {
                    list.add(wordCount);
                    list.add(wordCount);
                    change = true;
                } else if (wordCount < list.get(0)) {
                    list.set(0, wordCount);
                    change = true;
                } else if (wordCount > list.get(1)) {
                    list.set(1, wordCount);
                    change = true;
                }
                range = JsonUtil.obj2Str(list);
                resultSet.close();
                preparedStatement.close();

                if (change) {
                    preparedStatement = connection.prepareStatement("update passage_total set word_range = ? where passage_id = ?");
                    preparedStatement.setString(1, range);
                    preparedStatement.setLong(2, id);
                    preparedStatement.execute();
                }
                return true;
            }

            return false;

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
