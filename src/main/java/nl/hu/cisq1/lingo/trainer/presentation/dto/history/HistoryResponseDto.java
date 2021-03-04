package nl.hu.cisq1.lingo.trainer.presentation.dto.history;

import lombok.Getter;
import nl.hu.cisq1.lingo.trainer.domain.Game;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class HistoryResponseDto {
    private final List<RoundDto> roundHistory;

    public HistoryResponseDto(Game game) {
        this.roundHistory = game.getRounds().stream()
                .map(RoundDto::new)
                .collect(Collectors.toList());
    }
}
