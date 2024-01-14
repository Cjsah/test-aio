create table `passage_modify` (
    `id` bigint not null auto_increment comment 'id',
    `title` longtext not null comment '内容',
    `answer2` longtext not null comment '问题',
    `parse` longtext not null comment '翻译',
    `difficulty` int unsigned not null comment '难度值',
    `ability_start` int unsigned not null comment '能力值-start',
    `ability_end` int unsigned not null comment '能力值-end',
    primary key (`id`)
) comment='处理中的文章';

