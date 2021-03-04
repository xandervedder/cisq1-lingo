package nl.hu.cisq1.lingo.trainer.presentation.dto.history;

import lombok.Getter;
import nl.hu.cisq1.lingo.trainer.domain.Round;

import java.util.List;

@Getter
public class RoundDto {
    private final String wordToGuess;
    private final Integer tries;
    private final List<String> guesses;

    public RoundDto(Round round) {
        this.wordToGuess = round.getToBeGuessedWord();
        this.tries = round.getTries();
        this.guesses = round.getGuesses();
    }
}
