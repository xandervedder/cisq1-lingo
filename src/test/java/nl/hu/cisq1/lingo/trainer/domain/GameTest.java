package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.ActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoActiveRoundException;
import nl.hu.cisq1.lingo.words.domain.Word;
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
    @DisplayName("game shouldn't throw when trying to play a game with an active round")
    void shouldNotThrowExceptionWhenThereIsAnActiveRoundToPlay() {
        this.instance.startNewRound(theWord);
        assertDoesNotThrow(() -> this.instance.play(theWord));
    }

    @ParameterizedTest
    @DisplayName("getLetterLength should return the correct letter length")
    @MethodSource("provideArgumentsForLetterLength")
    void shouldReturnCorrectLetterLengthBasedOnRoundNumber(Integer expected, Integer roundNumber) {
        assertEquals(expected, this.instance.getLetterLength(roundNumber));
    }

    private static Stream<Arguments> provideArgumentsForLetterLength() {
        return Stream.of(
                Arguments.of(5, 1),
                Arguments.of(6, 2),
                Arguments.of(7, 3),
                Arguments.of(5, 10),
                Arguments.of(6, 11),
                Arguments.of(7, 12)
        );
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

    private static Stream<Arguments> provideArgumentsForCalculateScore() {
        return Stream.of(
                Arguments.of(10, List.of(theWord), List.of(incorrectWord, incorrectWord, incorrectWord, theWord)),
                Arguments.of(20, List.of(theWord, theWord), List.of(incorrectWord, incorrectWord, incorrectWord, theWord)),
                Arguments.of(30, List.of(theWord, theWord, theWord), List.of(incorrectWord, incorrectWord, incorrectWord, theWord))
        );
    }
}
