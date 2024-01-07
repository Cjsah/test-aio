package net.cjsah.sql.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class Passage implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    @TableId
    protected Long id;
    @Version
    protected Integer version;
    @TableField(fill = FieldFill.INSERT)
    protected Date createTime;
    protected Long passageId;
    protected Integer wordCount;
    protected Integer difficulty;
    protected String content;
    protected String questions;
    protected String answers;
    protected String file;
    protected String translate;

    public <T extends Passage> T copy(T passage) {
        passage.passageId = this.passageId;
        passage.wordCount = this.wordCount;
        passage.difficulty = this.difficulty;
        passage.content = this.content;
        passage.questions = this.questions;
        passage.answers = this.answers;
        passage.file = this.file;
        passage.translate = this.translate;
        return passage;
    }

}
