package net.cjsah.sql.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("passage_total_t")
public class PassageTotalT implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    @TableId
    protected Long id;
    @TableField(fill = FieldFill.INSERT)
    protected Date createTime;
    protected Long passageId;
    protected Integer wordCount;
    protected Integer difficulty;
    protected String content;
    protected String questions;
    protected String file;
    protected String translate;
    protected String words;

    public <T extends PassageWord> void from(T passage) {
        this.passageId = passage.passageId;
        this.wordCount = passage.wordCount;
        this.difficulty = passage.difficulty;
        this.content = passage.content;
        this.questions = passage.questions;
        this.file = passage.file;
        this.translate = passage.translate;
        this.words = passage.words;
    }

    public <T extends Passage> void from(T passage) {
        this.passageId = passage.passageId;
        this.wordCount = passage.wordCount;
        this.difficulty = passage.difficulty;
        this.content = passage.content;
        this.questions = passage.questions;
        this.file = passage.file;
        this.translate = passage.translate;
        this.words = "";
    }

}
