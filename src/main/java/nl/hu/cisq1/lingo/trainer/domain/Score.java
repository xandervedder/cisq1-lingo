package nl.hu.cisq1.lingo.trainer.domain;

import lombok.Getter;

public class Score {
    @Getter
    private Integer value;

    public Score() {
        this.value = 0;
    }

    public void calculate(Integer numTurns) {
        this.value = 5 * (5 - numTurns) + 5;
    }
}
