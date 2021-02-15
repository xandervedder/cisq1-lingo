package nl.hu.cisq1.lingo.trainer.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidHintReplacementException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ToString
@EqualsAndHashCode
public class Hint {
    private static final Character DOT = '.';
    @Getter
    private List<Character> values;

    public Hint(List<Character> values) {
        this.values = values;
    }

    public void replaceWith(List<Character> values) {
        if (this.values.size() != values.size())
            throw new InvalidHintReplacementException();

        this.values = IntStream.range(0, values.size())
                .mapToObj(index -> {
                    var currentCharacter = this.values.get(index);
                    var newCharacter = values.get(index);
                    if (currentCharacter == DOT) return newCharacter;
                    else return currentCharacter;
                })
                .collect(Collectors.toList());
    }
}
