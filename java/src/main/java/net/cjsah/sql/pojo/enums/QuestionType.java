package net.cjsah.sql.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionType {
    EMPTY, // 0
    FORMULA_SHOW, // 1
    FORMULA_MEM, // 2
    EXAMPLE_QUESTIONS, // 3
    EXERCISES; // 4

    @EnumValue
    private final int index = this.ordinal();

}
