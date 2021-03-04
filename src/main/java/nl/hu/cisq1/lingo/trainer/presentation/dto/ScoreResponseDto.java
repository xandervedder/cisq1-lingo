package nl.hu.cisq1.lingo.trainer.presentation.dto;

import lombok.Getter;

@Getter
public class ScoreResponseDto {
    private final Integer score;

    public ScoreResponseDto(Integer score) {
        this.score = score;
    }
}
