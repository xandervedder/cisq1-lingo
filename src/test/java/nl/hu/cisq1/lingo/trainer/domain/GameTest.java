package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.ActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.GameOverException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoActiveRoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private static final String theWord = "banaan";
    private static final String theWord2 = "stengel";
    private static final String incorrectWord = "banana";

    private Game instance = new Game();

    @BeforeEach
    void initialize() {
        this.instance = new Game();
    }

    @Test
    @DisplayName("game should add the new round correctly in its property")
    void shouldChangeTheSizeOfTheRoundsAfterStartingANewRound() {
        this.instance.startNewRound(theWord);
        assertEquals(1, this.instance.getRounds().size());
    }

    @Test
    @DisplayName("game should return null if there is no active game")
    void shouldReturnNullWhenThereIsNoActiveGame() {
        assertNull(this.instance.activeRound());
    }

    @Test
    @DisplayName("game should return active game if there is an active game")
    void shouldReturnActiveGameWhenThereIsOneActiveGame() {
        this.instance.startNewRound(theWord);
        assertNotNull(this.instance.activeRound());
    }

    @Test
    @DisplayName("game should throw an exception when trying to start a new round with one active")
    void shouldThrowWhenTryingToStartANewRoundWhenThereIsStillOneActive() {
        this.instance.startNewRound(theWord);
        assertThrows(ActiveRoundException.class, () -> this.instance.startNewRound(theWord));
    }

    @Test
    @DisplayName("game shouldn't throw an exception when trying to start a new round with none active")
    void shouldNotThrowWhenTryingToStartANewRoundWhenThereIsNotOneActive() {
        assertDoesNotThrow(() -> this.instance.startNewRound(theWord));
    }

    @Test
    @DisplayName("game should throw when trying to play a game without an active round")
    void shouldThrowExceptionWhenThereIsNotAnActiveRoundToPlay() {
        assertThrows(NoActiveRoundException.class, () -> this.instance.play(theWord));
    }

    @Test
    @DisplayName("game should throw when trying to start a new round after a player has lost")
    void shouldThrowGameOverExceptionWhenThePlayerIsGameOver() {
        this.instance.startNewRound(theWord);
        for (int i = 0; i < 5; i++)
            this.instance.play(incorrectWord);

        assertThrows(GameOverException.class, () -> this.instance.startNewRound(theWord));
    }

    @Test
    @DisplayName("game shouldn't throw when trying to start a game a player hasn't lost")
    void shouldNotThrowWhenTHePlayerIsNotGameOver() {
        this.instance.startNewRound(theWord);
        this.instance.play(theWord);

        assertDoesNotThrow(() -> this.instance.startNewRound(theWord));
    }

    @Test
    @DisplayName("game shouldn't throw when trying to play a game with an active round")
    void shouldNotThrowExceptionWhenThereIsAnActiveRoundToPlay() {
        this.instance.startNewRound(theWord);
        assertDoesNotThrow(() -> this.instance.play(theWord));
    }

    @ParameterizedTest
    @DisplayName("getLetterLength should return the correct letter length")
    @MethodSource("provideArgumentsForLetterLength")
    void shouldReturnCorrectLetterLengthBasedOnRoundNumber(Integer expected, List<String> wordsToBeGuessed, List<String> guesses) {
        wordsToBeGuessed.forEach(word -> {
            this.instance.startNewRound(word);
            guesses.forEach(this.instance::play);
        });

        assertEquals(expected, this.instance.currentLetterLength());
    }

    static Stream<Arguments> provideArgumentsForLetterLength() {
        return Stream.of(
                Arguments.of(5, List.of(), List.of()),
                Arguments.of(6, List.of(theWord), List.of(theWord)),
                Arguments.of(7, List.of(theWord, theWord), List.of(theWord)),
                Arguments.of(5, List.of(theWord, theWord, theWord), List.of(theWord)),
                Arguments.of(6, List.of(theWord, theWord, theWord, theWord), List.of(theWord)),
                Arguments.of(7, List.of(theWord, theWord, theWord, theWord, theWord), List.of(theWord)),
                Arguments.of(5, List.of(theWord, theWord, theWord, theWord, theWord, theWord), List.of(theWord))
        );
    }

    @Test
    @DisplayName("lastRound should return the last round in the list")
    void shouldReturnTheLastRoundInTheList() {
        this.instance.startNewRound(theWord);
        this.instance.play(theWord);
        this.instance.startNewRound(theWord2);

        assertEquals(new Round(theWord2).getToBeGuessedWord(), this.instance.lastRound().getToBeGuessedWord());
    }

    @Test
    @DisplayName("lastRound should return null when the list is empty")
    void shouldReturnNullIfListIsEmpty() {
        assertNull(this.instance.lastRound());
    }

    @ParameterizedTest
    @DisplayName("calculateScore should return the correct score based on the rounds that have passed")
    @MethodSource("provideArgumentsForCalculateScore")
    void shouldReturnCorrectScoreBasedOnTheRounds(Integer expectedScore, List<String> wordsToBeGuessed, List<String> guesses) {
        wordsToBeGuessed.forEach(word -> {
            this.instance.startNewRound(word);
            guesses.forEach(this.instance::play);
        });

        assertEquals(expectedScore, this.instance.calculateScore());
    }

    static Stream<Arguments> provideArgumentsForCalculateScore() {
        return Stream.of(
                Arguments.of(10, List.of(theWord), List.of(incorrectWord, incorrectWord, incorrectWord, theWord)),
                Arguments.of(20, List.of(theWord, theWord), List.of(incorrectWord, incorrectWord, incorrectWord, theWord)),
                Arguments.of(30, List.of(theWord, theWord, theWord), List.of(incorrectWord, incorrectWord, incorrectWord, theWord)),
                // Unfinished rounds case
                Arguments.of(0, List.of(theWord), List.of(incorrectWord, incorrectWord, incorrectWord))
        );
    }
}
