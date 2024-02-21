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
        dataSource.setJdbcUrl("jdbc:mysql://s.shuhai777.com:3307/passage?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&serverTimezone=GMT%2B8&autoReconnect=true");
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

    public static boolean update(long id, int wordCount, int difficulty) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select word_range wr, difficulty_range dif from passage_total where passage_id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String wordRange = resultSet.getString("wr");
                String difficultyRange = resultSet.getString("dif");
                List<Integer> wordList = JsonUtil.str2List(wordRange, int.class);
                List<Integer> difficultyList = JsonUtil.str2List(difficultyRange, int.class);
                wordList.add(wordCount);
                difficultyList.add(difficulty);
                wordRange = JsonUtil.obj2Str(wordList);
                difficultyRange = JsonUtil.obj2Str(difficultyList);
                resultSet.close();
                preparedStatement.close();
                preparedStatement = connection.prepareStatement("update passage_total set word_range = ? , difficulty_range = ? where passage_id = ?");
                preparedStatement.setString(1, wordRange);
                preparedStatement.setString(2, difficultyRange);
                preparedStatement.setLong(3, id);
                preparedStatement.execute();
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
