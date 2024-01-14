UPDATE passage_2 t1
    JOIN (
        SELECT id, difficulty,
               (SELECT difficulty
                FROM passage_2
                WHERE id < t2.id
                ORDER BY id DESC
                LIMIT 1) AS previous_value
        FROM passage_2 t2
    ) t3 ON t1.id = t3.id
SET t1.difficulty = t3.previous_value
where t1.difficulty = 0 and t3.previous_value <> 0;


truncate passage_2a;
insert into passage_2a select * from passage_1a;

truncate passage_4;
insert into passage_4 select * from passage_3;

alter table passage_total add column `word_range` varchar(255) not null default '[]' comment '词汇量范围';


select * from passage_1 group by passage_id, id;

select distinct passage_id from passage_1;

SELECT id, difficulty,
       (SELECT difficulty
        FROM passage_2
        WHERE id = t2.id - 1
        LIMIT 1) AS previous_value
FROM passage_2 t2 limit 1000;

select * from passage_2 t1
                  JOIN (
    SELECT id, difficulty,
           (SELECT difficulty
            FROM passage_2
            WHERE id = t2.id - 1
            LIMIT 1) AS previous_value
    FROM passage_2 t2
) t3 ON t1.id = t3.id;

insert into passage_3 select * from passage_2;

insert into passage_2 select * from passage_1;
insert into passage_2a select * from passage_1a;



truncate passage_total_t;

select *, count(*) as c from passage_total_origin
group by id, passage_id
having c > 1;

insert into passage_total_b select * from passage_total_origin;

alter table passage_total_origin add column     `word_range` text comment '单词范围';
alter table passage_total_origin add column     `difficulty_range` text comment '难度范围';

update passage_total_origin set passage_total_origin.word_range = '[]', difficulty_range = '[]';

-- 创建临时表
CREATE TABLE tmp_table LIKE passage_total;

-- 选择并插入数据到临时表，并按照指定的列排序
SET @row_number = 0;
INSERT INTO passage_total
SELECT (@row_number:=@row_number + 1) AS id, t.create_time, t.passage_id, t.word_count, t.content, t.questions, t.file, t.words, t.translate, t.word_range, t.difficulty_range
FROM passage_total_origin t;

-- 删除原始表
DROP TABLE your_table;

-- 重命名临时表为原始表的名称
ALTER TABLE passage_total RENAME TO your_table;

truncate table passage_total_origin;
insert into passage_total_origin select * from passage_total;

INSERT INTO passage_total
SELECT t.passage_id as id, t.word_count, t.content, t.questions, t.words, t.translate, t.word_range, t.difficulty_range
FROM passage_total t;

CREATE TABLE passage_total_backup LIKE passage_total;
insert into passage_total_backup select * from passage_total;

