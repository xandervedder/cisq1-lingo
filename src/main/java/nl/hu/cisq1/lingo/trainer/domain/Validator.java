package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.IncompatibleLengthException;

import java.util.ArrayList;

public class Validator {
    public Feedback validate(String guess, String word) {
        if (guess.length() != word.length()) throw new IncompatibleLengthException();
        if (guess.equals(word)) return Feedback.correct(guess);

        var marks = new ArrayList<Mark>();
        var unprocessedChars = new ArrayList<Character>();
        for (int i = 0; i < word.length(); i++) {
            var wordLetter = word.charAt(i);
            if (guess.charAt(i) == wordLetter) {
                marks.add(Mark.CORRECT);
                continue;
            }
            unprocessedChars.add(wordLetter);
            marks.add(Mark.ABSENT);
        }

        for (int i = 0; i < word.length(); i++) {
            var guessLetter = guess.charAt(i);
            if (unprocessedChars.contains(guessLetter) && marks.get(i) == Mark.ABSENT) {
                unprocessedChars.remove((Character)guessLetter);
                marks.set(i, Mark.PRESENT);
            }
        }
        // The first letter should always be available
        marks.set(0, Mark.CORRECT);

        return new Feedback(marks);
    }
}
