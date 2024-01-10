create table `passage_total_r` (
    `id` bigint not null auto_increment comment '访问id',
    `create_time` datetime not null comment '创建时间',
    `passage_id` bigint unsigned not null comment '文章id',
    `word_count` int unsigned not null comment '单词数',
    `content` text not null comment '内容',
    `questions` text not null comment '问题',
    `file` varchar(255) not null comment '文件',
    `words` text not null comment '单词',
    `translate` text not null comment '翻译',
    `word_range` text comment '单词范围',
    `difficulty_range` text comment '难度范围',
    primary key (`id`)
) comment='处理中的文章';

