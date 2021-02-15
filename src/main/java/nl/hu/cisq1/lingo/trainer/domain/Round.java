package nl.hu.cisq1.lingo.trainer.domain;

import lombok.Getter;
import nl.hu.cisq1.lingo.trainer.domain.exception.RoundFinishedException;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Round {
    private static final Integer MAX_TRIES = 5;
    private static final Validator VALIDATOR = new Validator();

    private final List<Feedback> feedbackList;
    private final List<Turn> turns;
    private final Word toBeGuessedWord;

    @Getter
    private final Hint currentHint;

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
