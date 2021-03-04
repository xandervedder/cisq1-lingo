package nl.hu.cisq1.lingo.trainer.domain;

public class Score {
    private final Integer numTurns;

    public Score(Integer numTurns) {
        this.numTurns = numTurns;
    }

    public Integer calculate() {
        // If the user didn't do any turns yet, then it should be obvious to not give them any points
        if (this.numTurns == 0) return 0;

        return 5 * (5 - this.numTurns) + 5;
    }
}
