package nl.hu.cisq1.lingo.trainer.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.application.exception.GameNotFoundException;
import nl.hu.cisq1.lingo.trainer.application.exception.WordDoesNotExistInDatabaseException;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exception.ActiveRoundException;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameIdDto;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GuessDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
class TrainerControllerIntegrationTest {
    private static final String STARTING_GAME_DTO_REPRESENTATION = "{\"id\":null,\"hint\":{\"values\":[\"b\",\".\",\".\",\".\",\".\",\".\"]},\"roundComplete\":false,\"currentScore\":0,\"tries\":0,\"state\":\"CONTINUE\"}";
    private static final String SUCCESFUL_GAME_DTO_REPRESENTATION = "{\"id\":null,\"hint\":{\"values\":[\"b\",\"a\",\"n\",\"a\",\"a\",\"n\"]},\"roundComplete\":true,\"currentScore\":25,\"tries\":1,\"state\":\"CONTINUE\"}";
    private static final String ROUND_HISTORY_DTO_REPRESENTATION = "{\"roundHistory\":[{\"wordToGuess\":\"banaan\",\"tries\":1,\"guesses\":[\"banaan\"]}]}";
    private static final String SCORE_DTO_REPRESENTATION = "{\"score\":25}";

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private TrainerService service;
    private GameIdDto gameIdDto;
    private GuessDto guessDto;
    private Game gameWithSuccesfullRound;

    @BeforeEach
    void initialize() {
        this.gameIdDto = new GameIdDto();
        this.gameIdDto.id = 1L;

        this.guessDto = new GuessDto();
        this.guessDto.id = 1L;
        this.guessDto.guess = "banaan";

        this.gameWithSuccesfullRound = new Game();
        this.gameWithSuccesfullRound.startNewRound("banaan");
        this.gameWithSuccesfullRound.play("banaan");

        this.service = mock(TrainerService.class);
        var controller = new TrainerController(service);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        var game = new Game();
        game.startNewRound("banaan");
        when(this.service.startNewGame()).thenReturn(game);
        when(this.service.startNewRound(1L)).thenReturn(game);
    }

    @Test
    @DisplayName("should start a game correctly")
    void shouldStartAGameCorrectly() throws Exception {
        var request = MockMvcRequestBuilders.post("/trainer/game/start");

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(STARTING_GAME_DTO_REPRESENTATION));
    }

    @Test
    @DisplayName("should start a new round correctly")
    void shouldStartARoundCorrectly() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/trainer/round/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(this.gameIdDto));

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(STARTING_GAME_DTO_REPRESENTATION));
    }

    @Test
    @DisplayName("should give status code 405 when starting a round in a game where round is already started")
    void shouldGiveStatusCode405WhenStartingARoundInAGameWhereRoundIsAlreadyStarted() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/trainer/round/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(this.gameIdDto));

        when(this.service.startNewRound(1L)).thenThrow(ActiveRoundException.class);

        this.mockMvc.perform(request)
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("should continue round correctly")
    void shouldContinueRoundCorrectly() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/trainer/round/continue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(this.guessDto));

        when(this.service.continueRound(1L, this.guessDto.guess)).thenReturn(this.gameWithSuccesfullRound);

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(SUCCESFUL_GAME_DTO_REPRESENTATION));
    }

    @Test
    @DisplayName("should give status code 400 when word doesn't exist")
    void shouldGiveAHttpStatusCodeOf400WhenWordDoesNotExist() throws Exception {
        var word = "wordThatDoesNotExist";
        this.guessDto.guess = word;

        var request = MockMvcRequestBuilders
                .post("/trainer/round/continue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(this.guessDto));

        when(this.service.continueRound(1L, word)).thenThrow(WordDoesNotExistInDatabaseException.class);

        this.mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should give history of game correctly")
    void shouldGiveHistoryOfGameCorrectly() throws Exception {
        var request = MockMvcRequestBuilders.get("/trainer/game/history/1");

        when(this.service.getGameById(1L)).thenReturn(this.gameWithSuccesfullRound);

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(ROUND_HISTORY_DTO_REPRESENTATION));
    }

    @Test
    @DisplayName("should give score of game correctly")
    void shouldGiveScoreOfGameCorrectly() throws Exception {
        var request = MockMvcRequestBuilders.get("/trainer/score/1");

        when(this.service.getScoreFromGameById(1L)).thenReturn(this.gameWithSuccesfullRound.calculateScore());

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(SCORE_DTO_REPRESENTATION));
    }

    @ParameterizedTest
    @DisplayName("should give status code 404 when game doesn't exist")
    @MethodSource("urlProvider")
    void shouldGiveStatusCodeWhenGameDoesNotExist(String url) throws Exception {
        var request = MockMvcRequestBuilders.get(url);

        when(this.service.getGameById(-1L)).thenThrow(GameNotFoundException.class);
        when(this.service.getScoreFromGameById(-1L)).thenThrow(GameNotFoundException.class);

        this.mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    static Stream<Arguments> urlProvider() {
        return Stream.of(
                Arguments.of("/trainer/game/history/-1"),
                Arguments.of("/tainer/game/score/-1")
        );
    }
}
