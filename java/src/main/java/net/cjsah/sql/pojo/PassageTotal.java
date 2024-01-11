package net.cjsah.sql.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("passage_total")
public class PassageTotal  implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    @TableId
    protected Long id;
    protected Integer wordCount;
    protected String content;
    protected String questions;
    protected String translate;
    protected String words;
    protected String wordRange;
    protected String difficultyRange;
    protected Integer wordAverage;
    protected Integer difficultyAverage;

}
