package nl.hu.cisq1.lingo.trainer.presentation.dto.history;

import nl.hu.cisq1.lingo.trainer.domain.Round;

import java.util.List;

public class RoundDto {
    public String wordToGuess;
    public Integer tries;
    public List<String> guesses;

    public RoundDto(Round round) {
        this.wordToGuess = round.getToBeGuessedWord();
        this.tries = round.getTries();
        this.guesses = round.getGuesses();
    }
}
