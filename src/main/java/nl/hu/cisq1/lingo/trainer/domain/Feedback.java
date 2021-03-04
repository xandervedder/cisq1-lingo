package nl.hu.cisq1.lingo.trainer.domain;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ToString
@EqualsAndHashCode
@Entity(name = "feedback")
public class Feedback {
    public static Feedback correct(String attempt) {
        return new Feedback(attempt.chars().mapToObj(i -> Mark.CORRECT).collect(Collectors.toList()));
    }

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @ElementCollection
    private List<Mark> marks;

    public Feedback() {}
    public Feedback(List<Mark> marks) {
        this.marks = marks;
    }

    public boolean isWordGuessed() {
        return this.marks.stream().allMatch(Mark.CORRECT::equals);
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
