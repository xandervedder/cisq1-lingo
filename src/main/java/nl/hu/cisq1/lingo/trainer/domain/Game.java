package nl.hu.cisq1.lingo.trainer.domain;

import lombok.Getter;
import nl.hu.cisq1.lingo.trainer.domain.exception.ActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.GameOverException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoActiveRoundException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Entity(name = "game")
public class Game {
    @Getter
    @Id
    @GeneratedValue
    private Long id;

    @Getter
    @Column
    private GameState state;

    @Getter
    @OneToMany(cascade = CascadeType.ALL)
    private final List<Round> rounds;

    public Game() {
        this.rounds = new ArrayList<>();
        this.state = GameState.CONTINUE;
    }

    public void startNewRound(String wordToBeGuessed) {
        if (this.activeRound() != null)
            throw new ActiveRoundException();
        else if (this.state.equals(GameState.LOST))
            throw new GameOverException();

        this.rounds.add(new Round(wordToBeGuessed));
    }

    public Round play(String guess) {
        var round = this.activeRound();
        if (round == null)
            throw new NoActiveRoundException();

        round.continueRound(guess);
        if (round.hasLost())
            this.state = GameState.LOST;

        return round;
    }

    public Round activeRound() {
        return this.rounds.stream()
                .filter(Predicate.not(Round::isRoundFinished))
                .findFirst()
                .orElse(null);
    }

    public Round lastRound() {
        return this.rounds.stream()
                .reduce((first, second) -> second)
                .orElse(new Round());
    }

    public Integer currentLetterLength() {
        var size = this.rounds.size();
        // The first round is an edge case
        var index = ((size == 0 ? 1 : size + 1) - 1) % 3;
        return switch(index) {
            case 1 -> 6;
            case 2 -> 7;
            default -> 5;
        };
    }

    public Integer calculateScore() {
        return this.rounds.stream()
                .filter(Round::isRoundFinished)
                .map(Round::getScore)
                .map(Score::calculate)
                .reduce(0, (total, element) -> total += element);
    }
}
