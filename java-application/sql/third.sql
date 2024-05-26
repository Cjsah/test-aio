create table `video_file`
(
    `id`       bigint       not null auto_increment comment 'id',
    `path`     longtext     not null comment '路径',
    `title`    varchar(255) not null comment '标题',
    `size`     bigint       not null comment '视频文件大小',
    `time`     datetime     not null comment '视频创建时间',
    `duration` bigint       not null default 0 comment '时长(秒)',
    `type`     varchar(10)  not null default 0 comment '格式',
    `user`     varchar(255) not null comment '用户',
    primary key (`id`)
) comment ='视频文件表';

create table `video_audio`
(
    `id`      bigint      not null auto_increment comment 'id',
    `vid`     bigint      not null comment 'video id',
    `channel` tinyint     not null comment '音轨',
    `type`    varchar(10) not null comment '格式',
    `text`    longtext    not null comment '重要文字',
    primary key (`id`)
) comment ='视频音频表';
