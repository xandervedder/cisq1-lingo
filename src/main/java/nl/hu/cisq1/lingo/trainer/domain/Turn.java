package nl.hu.cisq1.lingo.trainer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity(name = "turn")
public class Turn {
    @Id
    private Integer id;

    @Transient
    private Validator validator;

    @Column(name = "guess")
    private String guess;

    // TODO: We have to figure out if this class is needed at all..
    @Column(name = "theWord")
    private String toBeGuessedWord;

    public Turn() {}
    public Turn(Validator validator, String guess, String toBeGuessedWord) {
        this.validator = validator;
        this.guess = guess;
        this.toBeGuessedWord = toBeGuessedWord;
    }

    public Feedback doTurn() {
        return this.validator.validate(this.guess, this.toBeGuessedWord);
    }
}
