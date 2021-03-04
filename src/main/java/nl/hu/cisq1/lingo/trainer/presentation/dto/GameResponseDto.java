package nl.hu.cisq1.lingo.trainer.presentation.dto;

import nl.hu.cisq1.lingo.trainer.domain.GameState;
import nl.hu.cisq1.lingo.trainer.domain.Hint;

public class GameResponseDto {
    public Long id;
    public Hint hint;
    public Boolean roundComplete;
    public Integer currentScore;
    public Integer tries;
    public GameState state;

    public GameResponseDto(Long id, Hint hint, Boolean roundComplete, Integer score, Integer tries, GameState state) {
        this.id = id;
        this.hint = hint;
        this.currentScore = score;
        this.tries = tries;
        this.roundComplete = roundComplete;
        this.state = state;
    }
}
