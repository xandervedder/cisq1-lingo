package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.IncompatibleLengthException;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Validator {
    public Feedback validate(Word guess, Word word) {
        if (!guess.getLength().equals(word.getLength()))
            throw new IncompatibleLengthException();

        if (guess.equals(word))
            return Feedback.correct(guess.getValue());

        if (this.isFullyInvalid(guess))
            return Feedback.invalid(guess.getValue());

        var letters = guess.getValue().toCharArray();
        var wordLetters = word.getValue().toCharArray();

        // The first *real* step is to validate three different Marks:
        // 1. INVALID - If the character is a digit or not a letter
        // 2. CORRECT - If the character is in the right spot and is equal
        // 3. ABSENT - If the character is not in the letter or is to be processed in the next step
        var initialMarks = IntStream.range(0, guess.getLength())
                .mapToObj(index -> {
                    var letter = letters[index];
                    var actualLetter = wordLetters[index];
                    if (Character.isDigit(letter) || !Character.isLetter(letter)) return Mark.INVALID;
                    else if (letter == actualLetter) return Mark.CORRECT;
                    else return Mark.ABSENT;
                })
                .collect(Collectors.toList());

        // Before we can continue to the next step, we need to filter the remaining characters
        var leftOverWordLettersBuilder = new StringBuilder();
        var leftOverLettersBuilder = new StringBuilder();
        for (int i = 0; i < initialMarks.size(); i++) {
            // We only want the ones marked as ABSENT for the next step
            if (initialMarks.get(i) != Mark.ABSENT) continue;
            leftOverWordLettersBuilder.append(wordLetters[i]);
            leftOverLettersBuilder.append(letters[i]);
        }

        // In this step we check if the leftovers of the guess are equal to the leftovers of the to be guessed word
        var leftOverWordLetters = leftOverWordLettersBuilder.toString();
        var leftOverLetters = leftOverLettersBuilder.toString();
        var leftOverLettersArray = leftOverLetters.toCharArray();
        var presentMarks = IntStream.range(0, leftOverWordLetters.length())
                .mapToObj(index -> this.isLetterInWord(leftOverLettersArray[index], leftOverWordLetters) ? Mark.PRESENT : Mark.ABSENT)
                .collect(Collectors.toList());

        return new Feedback(
                // The last step is going of the marks again, but this time replace the ABSENT entries with whatever
                // is in the newly generated list from the previous step
                initialMarks.stream().map(initialMark -> {
                    if (initialMark != Mark.ABSENT || presentMarks.size() == 0) return initialMark;

                    var presentMark = presentMarks.get(0);
                    presentMarks.remove(0);
                    return presentMark;
                }).collect(Collectors.toList())
        );
    }

    public boolean isFullyInvalid(Word guess) {
        return guess.getValue().chars().allMatch(Character::isDigit);
    }

    public boolean isLetterInWord(Character letter, Word word) {
        return this.isLetterInWord(letter, word.getValue());
    }

    public boolean isLetterInWord(Character letter, String word) {
        return word.chars().anyMatch(character -> (char) character == letter);
    }
}
