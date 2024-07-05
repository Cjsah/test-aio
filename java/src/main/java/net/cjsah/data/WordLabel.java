package net.cjsah.data;

import lombok.Data;

import java.util.List;

@Data
public class WordLabel {
    private final long id;
    private final List<WordMeaning> words;
}
