package net.cjsah.sql.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("lexicon_article_question")
public class Article implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    @TableId
    private Long id;
    private Long questionId;
    private String level1Word;
    private Integer level1WordNumber;
    private String level2Word;
    private Integer level2WordNumber;
    private String level3Word;
    private Integer level3WordNumber;
    private String level4Word;
    private Integer level4WordNumber;
    private String level5Word;
    private Integer level5WordNumber;
    private String level6Word;
    private Integer level6WordNumber;
    private String level7Word;
    private Integer level7WordNumber;
    private String level8Word;
    private Integer level8WordNumber;
    private String level9Word;
    private Integer level9WordNumber;
    private String level10Word;
    private Integer level10WordNumber;
    private String level11Word;
    private Integer level11WordNumber;
    private String level12Word;
    private Integer level12WordNumber;
    private String level13Word;
    private Integer level13WordNumber;
    private String level14Word;
    private Integer level14WordNumber;
    private String level15Word;
    private Integer level15WordNumber;
    private String level16Word;
    private Integer level16WordNumber;
    private String level17Word;
    private Integer level17WordNumber;
    private String level18Word;
    private Integer level18WordNumber;
    private String level19Word;
    private Integer level19WordNumber;
    private String level20Word;
    private Integer level20WordNumber;
}
