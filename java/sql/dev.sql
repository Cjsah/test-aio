create table `passage_total` (
    `id` bigint not null auto_increment comment '访问id',
    `create_time` datetime not null comment '创建时间',
    `passage_id` bigint unsigned not null comment '文章id',
    `word_count` int unsigned not null comment '单词数',
    `difficulty` int unsigned not null comment '难度值',
    `content` text not null comment '内容',
    `questions` text not null comment '问题',
    `file` varchar(255) not null comment '文件',
    `words` text not null comment '单词',
    `translate` text not null comment '翻译',
    primary key (`id`)
) comment='处理中的文章-表格';

