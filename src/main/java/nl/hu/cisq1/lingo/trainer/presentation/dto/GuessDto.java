package nl.hu.cisq1.lingo.trainer.presentation.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class GuessDto {
    @Positive
    public Long id;

    @NotEmpty
    @NotBlank
    public String guess;
}
