package net.cjsah.sql.pojo;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import lombok.Data;
import net.cjsah.sql.pojo.enums.QuestionType;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName(value = "question", autoResultMap = true)
public class Question implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    @TableId
    private Long id;
    private QuestionType questionType;
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private JSONObject data;
}
