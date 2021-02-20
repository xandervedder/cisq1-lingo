package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.words.domain.Word;

import javax.persistence.*;

@Entity(name = "turn")
public class Turn {
    @Id
    private Integer id;

    @Transient
    private Validator validator;

    @ManyToOne
    private Word guess;

    @ManyToOne
    private Word toBeGuessedWord;

    public Turn() {}
    public Turn(Validator validator, Word guess, Word toBeGuessedWord) {
        this.validator = validator;
        this.guess = guess;
        this.toBeGuessedWord = toBeGuessedWord;
    }

    public Feedback doTurn() {
        return this.validator.validate(this.guess, this.toBeGuessedWord);
    }
}
