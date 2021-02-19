package nl.hu.cisq1.lingo.trainer.domain;

import lombok.Getter;
import nl.hu.cisq1.lingo.trainer.domain.exception.ActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoActiveRoundException;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Game {
    @Getter
    private final List<Round> rounds;

    public Game() {
        this.rounds = new ArrayList<>();
    }

    public void startNewRound(Word wordToBeGuessed) {
        if (this.activeRound() != null)
            throw new ActiveRoundException();

        // Note: Currently this method does not care about the word length, but it should alternate between the word lengths
        // This will probably be done in the service layer
        this.rounds.add(new Round(wordToBeGuessed));
    }

    public Feedback play(Word guess) {
        var round = this.activeRound();
        if (round == null)
            throw new NoActiveRoundException();

        return this.activeRound().continueRound(guess);
    }

    public Round activeRound() {
        return this.rounds.stream()
                .filter(Predicate.not(Round::isRoundFinished))
                .findFirst()
                .orElse(null);
    }

    public Integer getLetterLength(Integer roundNumber) {
        var index = (roundNumber - 1) % 3;
        return switch(index) {
            case 1 -> 6;
            case 2 -> 7;
            default -> 5;
        };
    }

    public Integer calculateScore() {
        return this.rounds.stream()
                .map(Round::getScore)
                .map(Score::calculate)
                .reduce(0, (total, element) -> total += element);
    }
}
