package nl.hu.cisq1.lingo.trainer.domain;

import lombok.Getter;
import nl.hu.cisq1.lingo.trainer.domain.exception.RoundFinishedException;
import nl.hu.cisq1.lingo.words.domain.Word;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "round")
public class Round {
    private static final Integer MAX_TRIES = 5;
    private static final Validator VALIDATOR = new Validator();

    @Id
    private Integer id;

    @Getter
    @Transient
    private Hint currentHint;

    @OneToMany
    private List<Feedback> feedbackList;

    @OneToMany
    private List<Turn> turns;

    @OneToOne
    private Word toBeGuessedWord;

    public Round() {}
    public Round(Word toBeGuessedWord) {
        this.feedbackList = new ArrayList<>(5);
        this.turns = new ArrayList<>(5);
        this.toBeGuessedWord = toBeGuessedWord;

        var startingHints = toBeGuessedWord.getValue().chars()
                .mapToObj(index -> '.')
                .collect(Collectors.toList());
        startingHints.set(0, toBeGuessedWord.getValue().toCharArray()[0]);

        this.currentHint = new Hint(startingHints);
    }

    public Feedback continueRound(Word guess) {
        if (this.isRoundFinished())
            throw new RoundFinishedException();

        var turn = new Turn(VALIDATOR, guess, this.toBeGuessedWord);
        this.turns.add(turn);

        var feedback = turn.doTurn();
        this.feedbackList.add(feedback);

        var hint = feedback.giveHint(this.currentHint, this.toBeGuessedWord.getValue());

        // TODO: you can probably remove this:
        this.currentHint.replaceWith(hint.getValues());

        return feedback;
    }

    public Score getScore() {
        return new Score(this.turns.size());
    }

    public boolean isRoundFinished() {
        return this.turns.size() >= MAX_TRIES || this.feedbackList.stream().anyMatch(Feedback::isWordGuessed);
    }
}
