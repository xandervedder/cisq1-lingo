package nl.hu.cisq1.lingo.trainer.presentation.dto.history;

import nl.hu.cisq1.lingo.trainer.domain.Game;

import java.util.List;
import java.util.stream.Collectors;

public class HistoryResponseDto {
    public List<RoundDto> roundHistory;

    public HistoryResponseDto(Game game) {
        this.roundHistory = game.getRounds().stream()
                .map(RoundDto::new)
                .collect(Collectors.toList());
    }
}
