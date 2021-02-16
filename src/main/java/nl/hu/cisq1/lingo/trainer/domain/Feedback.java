package nl.hu.cisq1.lingo.trainer.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ToString
@EqualsAndHashCode
public class Feedback {
    public static Feedback correct(String attempt) {
        return new Feedback(attempt.chars().mapToObj(i -> Mark.CORRECT).collect(Collectors.toList()));
    }

    public static Feedback invalid(String attempt) {
        return new Feedback(attempt.chars().mapToObj(i -> Mark.INVALID).collect(Collectors.toList()));
    }

    private final List<Mark> marks; // Validating is done by another class

    public Feedback(List<Mark> marks) {
        this.marks = marks;
    }

    public boolean isWordGuessed() {
        return this.marks.stream().allMatch(Mark.CORRECT::equals);
    }

    public boolean isGuessValid() {
        return this.marks.stream().allMatch(Predicate.not(Mark.INVALID::equals));
    }

    public Hint giveHint(Hint previousHint, String wordToGuess) {
        var letters = wordToGuess.toCharArray();
        var hints = IntStream.range(0, this.marks.size())
                // Doing for-loops the cool way... B)
                .mapToObj(index -> switch(this.marks.get(index)) {
                    case CORRECT -> letters[index];
                    case PRESENT -> '+';
                    default -> '.';
                })
                .collect(Collectors.toList());

        previousHint.replaceWith(hints);
        return previousHint;
    }
}
