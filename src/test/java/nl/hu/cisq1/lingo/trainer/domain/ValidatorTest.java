package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.IncompatibleLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
    private final static Validator instance = new Validator();
    private final static String correctWord = "brood";

    @Test
    @DisplayName("throw exception when words do not equal in size")
    void shouldThrowWhenLengthsAreNotEqual() {
        var word = "banaan";
        assertThrows(IncompatibleLengthException.class, () -> instance.validate(word, correctWord));
    }

    @Test
    @DisplayName("don't throw exception when words are equal in size")
    void shouldNotThrowWhenLengthsAreEqual() {
        assertDoesNotThrow(() -> instance.validate("baard", correctWord));
    }

    @Test
    @DisplayName("validating correct guess should return feedback that is correct")
    void shouldReturnFullyCorrectFeedbackAfterValidatingARightGuess() {
        assertTrue(instance.validate("brood", correctWord).isWordGuessed());
    }

    @Test
    @DisplayName("validating invalid guess should return feedback that is invalid")
    void shouldReturnFullyInvalidFeedbackAfterValidatingInvalidGuess() {
        assertFalse(instance.validate("12395", correctWord).isGuessValid());
    }

    @ParameterizedTest
    @DisplayName("validiting should return the correct feedback")
    @MethodSource("provideArgumentsForValidation")
    void shouldReturnTheCorrectFeedbackAfterValidating(Feedback expected, String guess, String word) {
        assertEquals(expected, instance.validate(guess, word));
    }

    private static Stream<Arguments> provideArgumentsForValidation() {
        return Stream.of(
                Arguments.of(
                        new Feedback(List.of(Mark.CORRECT, Mark.INVALID, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)),
                        "k3uis",
                        "kruis"
                ),
                Arguments.of(
                        new Feedback(List.of(Mark.CORRECT, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.CORRECT)),
                        "k-*-s",
                        "kruis"
                ),
                Arguments.of(
                        new Feedback(List.of(Mark.CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)),
                        "kouis",
                        "kruis"
                ),
                Arguments.of(
                        new Feedback(List.of(Mark.CORRECT, Mark.PRESENT, Mark.CORRECT, Mark.CORRECT, Mark.PRESENT)),
                        "ksuir",
                        "kruis"
                ),
                Arguments.of(
                        new Feedback(List.of(Mark.CORRECT, Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.CORRECT, Mark.CORRECT)),
                        "kaasje",
                        "kastje"
                ),
                Arguments.of(
                        new Feedback(List.of(Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT)),
                        "aaabbb",
                        "bbbaaa"
                ),
                Arguments.of(
                        new Feedback(List.of(Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.CORRECT, Mark.PRESENT)),
                        "aaabab",
                        "bbbaaa"
                ),
                Arguments.of(
                        new Feedback(List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)),
                        "aaaaaa",
                        "bbbbbb"
                )
        );
    }

    @Test
    @DisplayName("isFullyInvalid should return true when giving digits")
    void isFullyInvalidMethodShouldReturnTrueWhenGivingAllDigits() {
        assertTrue(instance.isFullyInvalid("12345"));
    }

    @Test
    @DisplayName("isFullyInvalid should return false when giving some digits")
    void isFullyInvalidMethodShouldReturnFalseWhenGivingSomeDigits() {
        assertFalse(instance.isFullyInvalid("sp4gh3tti"));
    }

    @Test
    @DisplayName("isFullyInvalid should return false when giving no digits")
    void isFullyInvalidMethodShouldReturnFalseWhenGivingNoDigits() {
        assertFalse(instance.isFullyInvalid("spaghetti"));
    }

    @Test
    @DisplayName("isLetterInWord should return true when there is one letter that matches")
    void isLetterInWordShouldReturnTrueWhenThereIsALetterThatMatches() {
        assertTrue(instance.isLetterInWord('b', correctWord));
    }

    @Test
    @DisplayName("isLetterInWord should return true when there are multiple letters that match")
    void isLetterInWordShouldReturnTrueWhenThereAreLettersThatMatch() {
        assertTrue(instance.isLetterInWord('o', correctWord));
    }

    @Test
    @DisplayName("isLetterInWord should return false when there aren't any letters that match")
    void isLetterInWordShouldReturnFalseWhenThereAreNotAnyLettersThatMatch() {
        assertFalse(instance.isLetterInWord('a', correctWord));
    }
}
