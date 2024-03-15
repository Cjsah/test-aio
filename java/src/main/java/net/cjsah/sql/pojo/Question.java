package net.cjsah.sql.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("questions")
public class Question implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    @TableId
    private Long id;
    private String parse;
}
