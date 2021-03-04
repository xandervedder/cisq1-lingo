package nl.hu.cisq1.lingo.trainer.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidHintReplacementException;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ToString
@EqualsAndHashCode
@Entity(name = "hint")
public class Hint {
    private static final Character DOT = '.';
    private static final Character PLUS = '+';

    @Id
    @GeneratedValue
    private Long id;

    @Getter
    @ElementCollection
    private List<Character> values;

    public Hint() {}
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
                    if (currentCharacter.equals(DOT) || currentCharacter.equals(PLUS)) return newCharacter;
                    else return currentCharacter;
                })
                .collect(Collectors.toList());
    }
}
