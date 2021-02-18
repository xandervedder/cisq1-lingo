package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.RoundFinishedException;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {
    private static final Word theWord = new Word("brood");
    private static final Word bogusWord = new Word("blalb");
    
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
}
