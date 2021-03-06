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
        var newHints = List.of('.');
        assertThrows(IncompatibleLengthException.class, () -> hint.replaceWith(newHints));
    }

    @Test
    @DisplayName("replaceWith method doesn't throw if the size is equal between the hints")
    void replaceHintWithoutException() {
        var hint = new Hint(List.of('.', '.', '.', '.', '.'));
        assertDoesNotThrow(() -> hint.replaceWith(List.of('.', '.', '.', '.', '.')));
    }

    @ParameterizedTest
    @DisplayName("replaceWith method should keep the previous hint to prevent it from overwriting")
    @MethodSource("provideHintDiffArguments")
    void replaceHintShouldKeepDiffBetweenHints(Hint expected, Hint initial, List<Character> newHints) {
        initial.replaceWith(newHints);
        assertEquals(expected, initial);
    }

    private static Stream<Arguments> provideHintDiffArguments() {
        return Stream.of(
                // Add a letter to the hint while retaining previous letters (in the new hint)
                Arguments.of(
                        new Hint(List.of('b', 'a', 'n', 'a', '.', '.', 'n')),
                        new Hint(List.of('b', 'a', 'n', 'a', '.', '.', '.')),
                        List.of('b', 'a', 'n', 'a', '.', '.', 'n')
                ),
                // Add a letter to the hint while not retaining (some of) previous letters (in the new hint)
                Arguments.of(
                        new Hint(List.of('b', 'a', 'n', 'a', '.', '.', 'n')),
                        new Hint(List.of('b', 'a', 'n', 'a', '.', '.', '.')),
                        List.of('b', 'a', 'n', '.', '.', '.', 'n')
                ),
                // Add a letter to the hint while not retaining any previous letters (in the new hint)
                Arguments.of(
                        new Hint(List.of('b', 'a', 'n', 'a', '.', '.', 'n')),
                        new Hint(List.of('b', 'a', 'n', 'a', '.', '.', '.')),
                        List.of('.', '.', '.', '.', '.', '.', 'n')
                )
        );
    }
}
