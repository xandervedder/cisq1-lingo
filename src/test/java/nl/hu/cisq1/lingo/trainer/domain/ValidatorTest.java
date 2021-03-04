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

    @ParameterizedTest
    @DisplayName("validiting should return the correct feedback")
    @MethodSource("provideArgumentsForValidation")
    void shouldReturnTheCorrectFeedbackAfterValidating(Feedback expected, String guess, String word) {
        assertEquals(expected, instance.validate(guess, word));
    }

    private static Stream<Arguments> provideArgumentsForValidation() {
        return Stream.of(
                Arguments.of(
                        new Feedback(List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.PRESENT, Mark.PRESENT)),
                        "banaan",
                        "banana"
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
                        new Feedback(List.of(Mark.CORRECT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT)),
                        "aaabbb",
                        "bbbaaa"
                ),
                Arguments.of(
                        new Feedback(List.of(Mark.CORRECT, Mark.PRESENT, Mark.ABSENT, Mark.PRESENT, Mark.CORRECT, Mark.PRESENT)),
                        "aaabab",
                        "bbbaaa"
                ),
                Arguments.of(
                        new Feedback(List.of(Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)),
                        "aaaaaa",
                        "bbbbbb"
                ),
                Arguments.of(
                        new Feedback(List.of(Mark.CORRECT, Mark.PRESENT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT)),
                        "gehoor",
                        "onmens"
                ),
                Arguments.of(
                        new Feedback(List.of(Mark.CORRECT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.CORRECT)),
                        "aabbcc",
                        "abcabc"
                ),
                Arguments.of(
                        new Feedback(List.of(Mark.CORRECT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT)),
                        "alianna",
                        "liniaal"
                ),
                Arguments.of(
                        new Feedback(List.of(Mark.CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)),
                        "heren",
                        "haren"
                ),
                Arguments.of(
                        new Feedback(List.of(Mark.CORRECT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.CORRECT, Mark.CORRECT)),
                        "eeaaae",
                        "aaeeae"
                )
        );
    }
}
