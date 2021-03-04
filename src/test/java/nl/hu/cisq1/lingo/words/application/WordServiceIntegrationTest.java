package nl.hu.cisq1.lingo.words.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This integration test integrates between the service layer,
 * the data layer and the framework.
 * In a dev environment, we test against the actual database.
 *
 * In continuous integration pipelines, we should not
 * use the actual database as we don't have one.
 * We want to replace it with an in-memory database.
 *
 * Set the profile to CI, so that application-ci.properties is loaded
 * and an import script is run.
 **/
@SpringBootTest
@Import(CiTestConfiguration.class)
class WordServiceIntegrationTest {
    @Autowired
    private WordService service;

    @Test
    @DisplayName("provides random 5, 6 and 7 letter words")
    void providesRandomWord() {
        for (int wordLength = 5; wordLength <= 7; wordLength++) {
            String randomWord = this.service.provideRandomWord(wordLength);
            assertEquals(wordLength, randomWord.length());

            // Printing is not necessary in most tests
            // (done here for verification of student configuration)
            System.out.println("Random word: " + randomWord);
        }
    }

    @ParameterizedTest
    @DisplayName("returns a boolean whether or not the guess exists")
    @MethodSource("provideGuesses")
    void testIfGuessExists(String guess, boolean expected) {
        assertEquals(expected, this.service.wordExists(guess));
    }

    static Stream<Arguments> provideGuesses() {
        return Stream.of(
                Arguments.of("pizza", true),
                Arguments.of("aaaaaa", false),
                Arguments.of("ababab", false),
                Arguments.of("012345", false)
        );
    }
}
