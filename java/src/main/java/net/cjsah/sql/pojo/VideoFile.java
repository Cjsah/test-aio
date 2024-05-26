package net.cjsah.sql.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("video_file")
public class VideoFile implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    @TableId
    private Long id;

    private String path;
    private String title;
    private Long size;
    private Date time;
    private Long duration;
    private String type;
}
