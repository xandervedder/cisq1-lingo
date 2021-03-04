package nl.hu.cisq1.lingo.trainer.presentation.dto;

import javax.validation.constraints.Positive;

public class GameIdDto {
    @Positive
    public Long id;
}
