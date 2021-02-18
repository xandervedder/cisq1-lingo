package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.words.domain.Word;

public class Turn {
    private final Validator validator;
    private final Word guess;
    private final Word toBeGuessedWord;

    public Turn(Validator validator, Word guess, Word toBeGuessedWord) {
        this.validator = validator;
        this.guess = guess;
        this.toBeGuessedWord = toBeGuessedWord;
    }

    public Feedback doTurn() {
        return this.validator.validate(this.guess, this.toBeGuessedWord);
    }
}
