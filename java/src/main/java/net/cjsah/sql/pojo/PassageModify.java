package net.cjsah.sql.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("passage_modify")
public class PassageModify implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    @TableId
    protected Long id;
    protected String title;
    protected String answer;
    protected String translate;
    protected Integer difficulty;
    protected Integer abilityStart;
    protected Integer abilityEnd;

}
