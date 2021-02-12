package nl.hu.cisq1.lingo.trainer.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidHintReplacementException;

import java.util.List;

@ToString
@EqualsAndHashCode
public class Hint {
    @Getter
    private List<Character> values;

    public Hint(List<Character> values) {
        this.values = values;
    }

    public void replaceWith(List<Character> values) {
        if (this.values.size() != values.size())
            throw new InvalidHintReplacementException();

        this.values = values;
    }
}
