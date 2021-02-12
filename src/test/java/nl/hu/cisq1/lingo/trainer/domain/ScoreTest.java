package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {
    @ParameterizedTest
    @DisplayName("score should be calculated correctly")
    @MethodSource("provideArgumentsForCalculation")
    void shouldCalculateCorrectlyBasedOnTurn(Integer expectedScore, Integer numTurn) {
        var score = new Score();
        score.calculate(numTurn);
        assertEquals(expectedScore, score.getValue());
    }

    private static Stream<Arguments> provideArgumentsForCalculation() {
        return Stream.of(
                Arguments.of(30, 0),
                Arguments.of(25, 1),
                Arguments.of(20, 2),
                Arguments.of(15, 3),
                Arguments.of(10, 4),
                Arguments.of(5, 5)
        );
    }
}
