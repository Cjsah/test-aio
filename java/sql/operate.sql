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

