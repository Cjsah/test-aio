package net.cjsah.sql.pojo;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName(value = "student", autoResultMap = true)
public class Student implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    @TableId
    private Long id;
    @TableLogic
    protected Boolean delFlag;

    private Long stuManageId;
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private JSONObject subjects;
}
