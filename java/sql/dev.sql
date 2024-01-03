
drop table if exists `passage_1`;
create table `passage_1` (
    `id` bigint not null auto_increment comment '访问id',
    `version` tinyint unsigned not null default 0 comment '逻辑删除',
    `create_time` datetime not null comment '创建时间',
    `passage_id` bigint unsigned not null comment '文章id',
    `word_count` int unsigned not null comment '单词数',
    `difficulty` int unsigned not null comment '难度值',
    `content` text not null comment '内容',
    `questions` text not null comment '问题',
    `answers` text not null comment '答案',
    `file` varchar(255) not null comment '文件',
    `translate` text not null comment '翻译',
    primary key (`id`)
) comment='未处理的文章';