package nl.hu.cisq1.lingo.trainer.presentation.dto;

import lombok.Getter;
import nl.hu.cisq1.lingo.trainer.domain.GameState;
import nl.hu.cisq1.lingo.trainer.domain.Hint;

@Getter
public class GameResponseDto {
    private final Long id;
    private final Hint hint;
    private final Boolean roundComplete;
    private final Integer currentScore;
    private final Integer tries;
    private final GameState state;

    public GameResponseDto(Long id, Hint hint, Boolean roundComplete, Integer score, Integer tries, GameState state) {
        this.id = id;
        this.hint = hint;
        this.currentScore = score;
        this.tries = tries;
        this.roundComplete = roundComplete;
        this.state = state;
    }
}
