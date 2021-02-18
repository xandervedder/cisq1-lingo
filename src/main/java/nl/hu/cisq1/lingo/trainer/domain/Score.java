package nl.hu.cisq1.lingo.trainer.domain;

public class Score {
    private final Integer numTurns;

    public Score(Integer numTurns) {
        this.numTurns = numTurns;
    }

    public Integer calculate() {
        return 5 * (5 - this.numTurns) + 5;
    }
}
