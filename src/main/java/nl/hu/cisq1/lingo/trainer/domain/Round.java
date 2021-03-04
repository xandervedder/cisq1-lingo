package nl.hu.cisq1.lingo.trainer.domain;

import lombok.Getter;
import nl.hu.cisq1.lingo.trainer.domain.exception.RoundFinishedException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "round")
public class Round {
    private static final Integer MAX_TRIES = 5;
    private static final Validator VALIDATOR = new Validator();

    @Id
    @GeneratedValue
    private Long id;

    @Getter
    @OneToOne(cascade = CascadeType.ALL)
    private Hint currentHint;

    @Getter
    @OneToMany(cascade = CascadeType.ALL)
    private List<Feedback> feedbackList;

    @Getter
    @Column(name = "theWord")
    private String toBeGuessedWord;

    @Getter
    @ElementCollection
    @CollectionTable(name = "guess", joinColumns = @JoinColumn(name = "round_id"))
    private List<String> guesses;

    @Getter
    @Column(name = "tries")
    private int tries;

    @Getter
    @Transient
    private Feedback currentFeedback;

    public Round() {}
    public Round(String toBeGuessedWord) {
        this.feedbackList = new ArrayList<>(5);
        this.toBeGuessedWord = toBeGuessedWord;
        this.guesses = new ArrayList<>(5);

        var startingHints = toBeGuessedWord.chars()
                .mapToObj(index -> '.')
                .collect(Collectors.toList());
        // Starting hint
        startingHints.set(0, toBeGuessedWord.charAt(0));

        this.currentHint = new Hint(startingHints);
    }

    public void continueRound(String guess) {
        if (this.isRoundFinished())
            throw new RoundFinishedException();

        this.guesses.add(guess);
        this.currentFeedback = VALIDATOR.validate(guess, this.toBeGuessedWord);
        this.feedbackList.add(this.currentFeedback);
        this.tries++;

        var hint = this.currentFeedback.giveHint(this.currentHint, this.toBeGuessedWord);
        this.currentHint.replaceWith(hint.getValues());
    }

    public Score getScore() {
        return new Score(this.feedbackList.size());
    }

    public boolean isRoundFinished() {
        return this.hasLost() || this.feedbackList.stream().anyMatch(Feedback::isWordGuessed);
    }
    
    public boolean hasLost() {
        return this.feedbackList.size() >= MAX_TRIES;
    }

    public void revealWord() {
        this.currentFeedback = VALIDATOR.validate(this.toBeGuessedWord, this.toBeGuessedWord);
        this.currentHint = this.currentFeedback.giveHint(this.currentHint, this.toBeGuessedWord);
    }
}
