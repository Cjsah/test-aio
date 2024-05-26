package net.cjsah.sql.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@TableName("video_file")
public class VideoFile implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    @TableId(type = IdType.AUTO)
    private Long id;

    private String path;
    private String title;
    private Long size;
    private Date time;
    private Long duration;
    private String type;

    public VideoFile(String path, String title, String type, long size, long duration, Date time) {
        this.path = path;
        this.title = title;
        this.size = size;
        this.time = time;
        this.duration = duration;
        this.type = type;
    }
}
