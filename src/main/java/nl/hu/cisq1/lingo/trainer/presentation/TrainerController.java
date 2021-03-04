package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.presentation.dto.*;
import nl.hu.cisq1.lingo.trainer.presentation.dto.history.HistoryResponseDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
public class TrainerController {
    private final TrainerService service;

    public TrainerController(TrainerService service) {
        this.service = service;
    }

    @PostMapping("/game/start")
    public GameResponseDto startGame() {
        return this.buildResponseDto(this.service.startNewGame());
    }

    @PostMapping("/round/start")
    public GameResponseDto startRound(@RequestBody GameIdDto dto) {
        return this.buildResponseDto(this.service.startNewRound(dto.id));
    }

    @PostMapping("/round/continue")
    public GameResponseDto continueRound(@RequestBody GuessDto dto) {
        return this.buildResponseDto(this.service.continueRound(dto.id, dto.guess));
    }

    private GameResponseDto buildResponseDto(Game game) {
        var round = game.lastRound();
        return new GameResponseDto(
                game.getId(),
                round.getCurrentHint(),
                round.isRoundFinished(),
                game.calculateScore(),
                round.getTries(),
                game.getState()
        );
    }

    @GetMapping("/game/history/{id}")
    public HistoryResponseDto historyFromGame(@PathVariable Long id) {
        var game = this.service.getGameById(id);
        return new HistoryResponseDto(game);
    }

    @GetMapping("/score/{id}")
    public ScoreResponseDto scoreFromGame(@PathVariable Long id) {
        return new ScoreResponseDto(this.service.getScoreFromGameById(id));
    }
}
