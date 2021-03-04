package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.application.exception.WordDoesNotExistInDatabaseException;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Hint;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {
    private static final String theWord = "banaan";

    private TrainerService instance;
    private WordService wordService;
    private Game game;

    @BeforeEach
    void initialize() {
        // Reset the test doubles to their initial state
        this.wordService = mock(WordService.class);
        this.game = mock(Game.class);
        var gameRepository = mock(SpringGameRepository.class);

        this.instance = new TrainerService(gameRepository, wordService);

        when(this.wordService.provideRandomWord(anyInt())).thenReturn(theWord);
        when(this.wordService.wordExists(theWord)).thenReturn(true);
        when(this.game.getId()).thenReturn(1L);
        when(this.game.play(anyString())).thenReturn(new Round(theWord));
        when(gameRepository.save(isA(Game.class))).thenReturn(this.game);
        when(gameRepository.getOne(anyLong())).thenReturn(this.game);
    }

    @Test
    @DisplayName("should not return null when starting a game")
    void shouldNotReturnNullWhenStartingANewGame() {
        assertNotNull(this.instance.startNewGame());
    }

    @Test
    @DisplayName("should return the correct game id")
    void shouldReturnTheCorrectGameIdWhenStartingANewGame() {
        assertEquals(1L, this.instance.startNewGame().getId());
    }

    @Test
    @DisplayName("should not return null when starting a new round")
    void shouldNotReturnNullWhenStartingANewRound() {
        assertNotNull(this.instance.startNewRound(1L));
    }

    @Test
    @DisplayName("should return the correct game id")
    void shouldReturnTheCorrectGameIdWhenStartingANewRound() {
        assertEquals(1L, this.instance.startNewRound(1L).getId());
    }

    @Test
    @DisplayName("should not return null when continuing a round")
    void shouldNotReturnNullWhenContinuingARound() {
        assertNotNull(this.instance.continueRound(1L, theWord));
    }

    @Test
    @DisplayName("should return the correct game id")
    void shouldReturnTheCorrectGameIdWhenContinuingARound() {
        assertEquals(1L, this.instance.continueRound(1L, theWord).getId());
    }

    @Test
    @DisplayName("should throw when the word doesn't exist")
    void shouldThrowWhenWordDoesNotExist() {
        when(this.wordService.wordExists("12345")).thenReturn(false);

        this.instance.startNewGame();
        assertThrows(WordDoesNotExistInDatabaseException.class, () -> this.instance.continueRound(1L, "12345"));
    }

    @Test
    @DisplayName("should not throw when the word exists")
    void shouldNotThrowWhenTheWordExists() {
        this.instance.startNewGame();
        assertDoesNotThrow(() -> this.instance.continueRound(1L, theWord));
    }

    @Test
    @DisplayName("should return the correct score by gameId")
    void shouldReturnTheCorrectScoreByGameId() {
        when(this.game.calculateScore()).thenReturn(0);

        assertEquals(0, this.instance.getScoreFromGameById(1L));
    }

    @Test
    @DisplayName("should reveal the word when the round is finished")
    void shouldRevealWordWhenRoundIsFinished() {
        var hint = new Hint(List.of('b', 'a', 'n', 'a', 'a', 'n'));
        var round = mock(Round.class);

        when(round.isRoundFinished()).thenReturn(true);
        when(round.getCurrentHint()).thenReturn(hint);
        when(this.game.play(anyString())).thenReturn(round);
        when(this.game.lastRound()).thenReturn(round);

        assertEquals(hint, this.instance.continueRound(1L, theWord).lastRound().getCurrentHint());
    }
}
