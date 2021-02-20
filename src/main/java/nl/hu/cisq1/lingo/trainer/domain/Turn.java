package nl.hu.cisq1.lingo.trainer.domain;

import javax.persistence.*;

@Entity(name = "turn")
public class Turn {
    @Id
    @GeneratedValue
    private Long id;

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
