package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {
    @Test
    @DisplayName("Word is guessed if all letters are correct")
    void wordIsGuessed() {
        var feedback = new Feedback(List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Word isn't guessed if one or more letters are incorrect")
    void wordIsNotGuessed() {
        var feedback = new Feedback(List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.ABSENT));
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Word is guessed when using the static 'correct' constructor")
    void wordIsGuessedUsingStaticConstructor() {
        assertTrue(Feedback.correct("woord").isWordGuessed());
    }

    @ParameterizedTest
    @DisplayName("Give hints based on previous hint and marks")
    @MethodSource("provideHintExamples")
    void giveHintAfterCreation(Hint expectedHint, Feedback feedback, Hint previousHint, String wordToGuess) {
        assertEquals(expectedHint, feedback.giveHint(previousHint, wordToGuess));
    }

    private static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of(
                        new Hint(List.of('b', 'a', 'a', 'r', 'd')),
                        new Feedback(List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)),
                        new Hint(List.of('.', '.', '.', '.', '.')),
                        "baard"
                ),
                Arguments.of(
                        new Hint(List.of('.', '.', '.', '.', '.')),
                        new Feedback(List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)),
                        new Hint(List.of('.', '.', '.', '.', '.')),
                        "kaart"
                ),
                Arguments.of(
                        new Hint(List.of('w', '.', '.', 'r', 'd')),
                        new Feedback(List.of(Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT)),
                        new Hint(List.of('.', '.', '.', '.', '.')),
                        "woord"
                ),
                Arguments.of(
                        new Hint(List.of('k', '+', '.', '.', '.')),
                        new Feedback(List.of(Mark.CORRECT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)),
                        new Hint(List.of('.', '.', '.', '.', '.')),
                        "kaart"
                )
        );
    }
}
