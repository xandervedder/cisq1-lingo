package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidHintReplacementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HintTest {
    @Test
    @DisplayName("hint should be replaced correctly")
    void replaceHint() {
        var hint = new Hint(List.of('.', '.', '.', '.', '.'));
        hint.replaceWith(List.of('.', '.', '.', '.', 'd'));
        assertEquals(hint, new Hint(List.of('.', '.', '.', '.', 'd')));
    }

    @Test
    @DisplayName("replaceWith method throws if the size between the hints differ")
    void replaceHintWithException() {
        var hint = new Hint(List.of('.', '.', '.', '.', '.'));
        assertThrows(InvalidHintReplacementException.class, () -> hint.replaceWith(List.of('.')));
    }

    @Test
    @DisplayName("replaceWith method doesn't throw if the size is equal between the hints")
    void replaceHintWithoutException() {
        var hint = new Hint(List.of('.', '.', '.', '.', '.'));
        assertDoesNotThrow(() -> hint.replaceWith(List.of('.', '.', '.', '.', '.')));
    }
}
