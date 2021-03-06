package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.application.exception.GameNotFoundException;
import nl.hu.cisq1.lingo.trainer.application.exception.WordDoesNotExistInDatabaseException;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exception.ActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.GameOverException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(CiTestConfiguration.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrainerServiceIntegrationTest {
    private static final String UNAVAILABLE_WORD = "plank";
    private static final String AVAILABLE_WORD = "pizza"; // Available in the CiTestConfiguration
    private static final String NONEXISTING_WORD = "¯\\_(ツ)_/¯";
    private static final Long NONEXISTING_ID = -1L;

    // Does not have a round attached
    private Game testGame1;
    // Has a round attached to it at creation
    private Game testGame2;
    // Has a round attached to it at creation, used for a test with an exception
    private Game testGame3;

    @Autowired
    private TrainerService service;

    @Autowired
    private SpringGameRepository repository;

    @BeforeAll
    void initialize() {
        this.testGame1 = this.repository.save(new Game());

        var gameWithRound = new Game();
        gameWithRound.startNewRound(UNAVAILABLE_WORD);
        this.testGame2 = this.repository.save(gameWithRound);

        var gameWithUnavailableWord = new Game();
        gameWithUnavailableWord.startNewRound(UNAVAILABLE_WORD);
        this.testGame3 = this.repository.save(gameWithUnavailableWord);
    }

    @Test
    @DisplayName("should not return null when starting a new game")
    void shouldNotReturnNullWhenStartingANewGame() {
        assertNotNull(this.service.startNewGame());
    }

    @Test
    @DisplayName("should throw when trying to start a new round while there already is an active round")
    void shouldThrowWhenTryingToStartANewRoundWhileThereAlreadyIsAnActiveRound() {
        var id = this.testGame2.getId();
        assertThrows(ActiveRoundException.class,
                () -> this.service.startNewRound(id));
    }

    @Test
    @DisplayName("should not throw when trying to start a new round when there isn't an active round")
    void shouldNotThrowWhenTryingToStartANewRoundWhenThereIsNotAnActiveRound() {
        var id = this.testGame1.getId();
        assertDoesNotThrow(() -> this.service.startNewRound(id));
    }

    @Test
    @DisplayName("should throw when giving a guess that isn't an existing word")
    void shouldThrowWhenGivingAGuessThatIsNotAnExistingWord() {
        var id = this.testGame1.getId();
        assertThrows(WordDoesNotExistInDatabaseException.class,
                () -> this.service.continueRound(id, NONEXISTING_WORD));
    }

    @Test
    @DisplayName("should throw when trying to continue a game that is over")
    void shouldThrowWhenTryingToContinueAGameThatIsOver() {
        IntStream.range(0, 5).forEach(i -> this.testGame3.play(AVAILABLE_WORD));
        assertThrows(GameOverException.class, () -> this.testGame3.startNewRound(AVAILABLE_WORD));
    }

    @Test
    @DisplayName("should reveal word when round is finished after max tries (5)")
    void shouldRevealWordWhenRoundIsFinishedAfterMaxTries() {
        for (int tries = 1; tries < 4; tries++)
            this.service.continueRound(this.testGame2.getId(), AVAILABLE_WORD);

        var hint = this.service.continueRound(this.testGame2.getId(), AVAILABLE_WORD).lastRound().getCurrentHint();
        assertEquals(List.of('p', '.', '.', '.', '+'), hint.getValues());
    }

    @Test
    @DisplayName("should return the correct score from specific game")
    void shouldReturnTheCorrectScoreFromSpecificGame() {
        assertEquals(0, this.service.getScoreFromGameById(testGame1.getId()));
    }

    @Test
    @DisplayName("should find game by existing game id")
    void shouldFindGameByExistingGameId() {
        assertNotNull(this.service.getGameById(this.testGame1.getId()));
    }

    @Test
    @DisplayName("should throw when game isn't found")
    void shouldThrowWhenGameIsNotFound() {
        assertThrows(GameNotFoundException.class, () -> this.service.getGameById(NONEXISTING_ID));
    }
}
