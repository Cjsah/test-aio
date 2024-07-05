package net.cjsah.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class TranslateOption {
    private final String translate;
    private final boolean need;
    private boolean selected;

    public TranslateOption(String translate, boolean need) {
        this.translate = translate;
        this.need = need;
        this.selected = false;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TranslateOption that = (TranslateOption) object;
        return Objects.equals(translate, that.translate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translate);
    }
}
