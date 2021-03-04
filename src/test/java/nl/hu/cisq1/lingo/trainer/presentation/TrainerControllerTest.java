package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.GameState;
import nl.hu.cisq1.lingo.trainer.domain.Hint;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameIdDto;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GuessDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainerControllerTest {
    private GameIdDto gameIdDto;
    private GuessDto guessDto;

    private TrainerController instance;
    private TrainerService trainerService;
    private Game game;

    @BeforeEach
    void initializeBeforeEach() {
        this.trainerService = mock(TrainerService.class);
        this.instance = new TrainerController(this.trainerService);

        this.gameIdDto = new GameIdDto();
        this.gameIdDto.id = 1L;
        this.guessDto = new GuessDto();
        this.guessDto.guess = "banaan";
        this.guessDto.id = 1L;

        this.game = mock(Game.class);
        var round = mock(Round.class);

        when(round.isRoundFinished()).thenReturn(true);
        when(round.getCurrentHint()).thenReturn(new Hint());
        when(round.getTries()).thenReturn(0);
        when(this.trainerService.startNewGame()).thenReturn(this.game);
        when(this.trainerService.startNewRound(1L)).thenReturn(this.game);
        when(this.trainerService.continueRound(anyLong(), anyString())).thenReturn(this.game);
        when(this.trainerService.getGameById(1L)).thenReturn(this.game);
        when(this.trainerService.getScoreFromGameById(1L)).thenReturn(0);
        when(this.game.lastRound()).thenReturn(round);
        when(this.game.getId()).thenReturn(1L);
        when(this.game.getState()).thenReturn(GameState.CONTINUE);
        when(this.game.getRounds()).thenReturn(List.of());
    }

    @Test
    @DisplayName("should not return null when starting a game")
    void shouldNotReturnNullWhenStartingAGame() {
        assertNotNull(this.instance.startGame());
    }

    @Test
    @DisplayName("should return the correct game id after starting a game")
    void shouldReturnCorrectGameIdInDtoAfterStartingGame() {
        assertEquals(1L, this.instance.startGame().id);
    }

    @Test
    @DisplayName("should not return null when starting a new round")
    void shouldNotReturnNullWhenStartingARound() {
        assertNotNull(this.instance.startRound(this.gameIdDto));
    }

    @Test
    @DisplayName("should return the correct game id after starting a round")
    void shouldReturnCorrectGameIdInDtoAfterStartingRound() {
        assertEquals(1L, this.instance.startRound(this.gameIdDto).id);
    }

    @Test
    @DisplayName("should not return null when continuing round")
    void shouldNotReturnNullWhenContinuingRound() {
        assertNotNull(this.instance.continueRound(this.guessDto));
    }

    @Test
    @DisplayName("should return the correct game id after continuing a round")
    void shouldReturnCorrectGameIdInDtoAfterContinuingRound() {
        assertEquals(1L, this.instance.continueRound(this.guessDto).id);
    }

    @Test
    @DisplayName("should not return null when retrieving history of game")
    void shouldNotReturnNullWhenRetrievingHistoryOfGame() {
        assertNotNull(this.instance.historyFromGame(1L));
    }

    @Test
    @DisplayName("should have the correct amount of rounds in dto")
    void shouldHaveTheCorrectAmountOfRoundsInDto() {
        when(this.game.getRounds()).thenReturn(List.of(new Round(), new Round(), new Round()));
        assertEquals(3, this.instance.historyFromGame(1L).roundHistory.size());
    }

    @Test
    @DisplayName("should not return null when retrieving score from game by id")
    void shouldNotReturnNullWhenRetrievingScoreFromGameById() {
        assertNotNull(this.instance.scoreFromGame(1L));
    }

    @Test
    @DisplayName("should return the correct score in dto")
    void shouldHaveTheCorrectScoreInDto() {
        assertEquals(0, this.instance.scoreFromGame(1L).score);
    }
}
