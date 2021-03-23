package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.RoundFinishedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {
    private static final String theWord = "brood";
    private static final String bogusWord = "blalb";
    
    private Round instance = new Round(theWord);

    @BeforeEach
    void initialize() {
        this.instance = new Round(theWord);
    }

    @Test
    @DisplayName("round should throw exception when round is finished due to too many incorrect tries")
    void shouldThrowWhenRoundIsFinished() {
        IntStream.range(0, 5).forEach(n -> this.instance.continueRound(bogusWord));

        assertThrows(RoundFinishedException.class, () -> this.instance.continueRound(bogusWord));
        assertTrue(this.instance.isRoundFinished());
    }

    @Test
    @DisplayName("round should not throw exception when round isn't finished")
    void shouldNotThrowWhenRoundIsNotFinished() {
        assertDoesNotThrow(() -> this.instance.continueRound(bogusWord));
        assertFalse(this.instance.isRoundFinished());
    }

    @Test
    @DisplayName("round should throw exception when round is finished due to correct guess")
    void shouldNotBeAbleToContinueWhenTheRightWordWasGuessed() {
        this.instance.continueRound(theWord);

        assertThrows(RoundFinishedException.class, () -> this.instance.continueRound(bogusWord));
    }

    @Test
    @DisplayName("round should provide starting hint letter after initialization")
    void shouldProvideStartingHintLetterAfterCreation() {
        assertEquals(new Hint(List.of('b', '.', '.', '.', '.')), this.instance.getCurrentHint());
    }

    @ParameterizedTest
    @DisplayName("player has lost when exceeding max tries for current round")
    @MethodSource("triesProvider")
    void shouldLoseWhenTriesIsAtItsMax(boolean expected, int tries) {
        for (int i = 1; i <= tries; i++)
            this.instance.continueRound(bogusWord);

        assertEquals(expected, this.instance.hasLost());
    }

    static Stream<Arguments> triesProvider() {
        return Stream.of(
                Arguments.of(true, 5),
                Arguments.of(false, 1),
                Arguments.of(false, 3)
        );
    }

    @Test
    @DisplayName("revealWord should show construct the correct hint")
    void shouldConstructCorrectHint() {
        var marks = List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT);
        this.instance.revealWord();

        assertEquals(new Feedback(marks), this.instance.getCurrentFeedback());
        assertEquals(new Hint(List.of('b', 'r', 'o', 'o', 'd')), this.instance.getCurrentHint());
    }
}
