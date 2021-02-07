package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {
    @Test
    @DisplayName("Word is guessed if all letters are correct")
    void wordIsGuessed() {
        var feedback = new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Word isn't guessed if one or more letters are incorrect")
    void wordIsNotGuessed() {
        var feedback = new Feedback("woord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.ABSENT));
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("The guess is valid when it doesn't contain any numbers")
    void guessIsValid() {
        var feedback = new Feedback("woord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.ABSENT));
        assertTrue(feedback.isGuessValid());
    }

    @Test
    @DisplayName("The guess is invalid when it contains one or more numbers")
    void guessIsInvalid() {
        var feedback = new Feedback("wo4rd", List.of(Mark.ABSENT, Mark.CORRECT, Mark.INVALID, Mark.CORRECT, Mark.ABSENT));
    }

    @Test
    @DisplayName("Exception is not expected when word length is equivalent to marks length")
    void guessIsEquivalentToMarksLength() {
        assertDoesNotThrow(
                () -> new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))
        );
    }

    @Test
    @DisplayName("Exception is expected when word length is not equivalent to marks length")
    void guessIsNotEquivalentToMarksLength() {
        assertThrows(
                InvalidFeedbackException.class,
                () -> new Feedback("woord", List.of(Mark.CORRECT))
        );
    }

    @Test
    @DisplayName("Word is guessed when using the static 'correct' constructor")
    void wordIsGuessedUsingStaticConstructor() {
        assertTrue(Feedback.correct("woord").isWordGuessed());
    }

    @Test
    @DisplayName("Word isn't guessed when using the static 'invalid' constructor")
    void wordIsNotGuessedUsingStaticConstructor() {
        assertFalse(Feedback.invalid("09248").isWordGuessed());
    }

    @Test
    @DisplayName("The guess is valid when using the static 'correct' constructor")
    void guessIsValidUsingStaticConstructor() {
        assertTrue(Feedback.correct("woord").isGuessValid());
    }

    @Test
    @DisplayName("The guess is invalid when using the static 'invalid' constructor")
    void guessIsInvalidUsingStaticConstructor() {
        assertFalse(Feedback.invalid("09248").isGuessValid());
    }
}
