package nl.hu.cisq1.lingo.trainer.domain;

import lombok.Getter;
import nl.hu.cisq1.lingo.trainer.domain.exception.ActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoActiveRoundException;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class Game {
    @Getter
    private final List<Round> rounds;

    public Game() {
        this.rounds = new ArrayList<>();
    }

    public void startNewRound(Word wordToBeGuessed) {
        if (this.activeRound() != null)
            throw new ActiveRoundException();

        // Note: Currently this method does not care about the word length, but it should alternate between the word lengths
        // This will probably be done in the service layer
        this.rounds.add(new Round(wordToBeGuessed));
    }

    public Feedback play(Word guess) {
        var round = this.activeRound();
        if (round == null)
            throw new NoActiveRoundException();

        return this.activeRound().continueRound(guess);
    }

    public Round activeRound() {
        return this.rounds.stream()
                .filter(Predicate.not(Round::isRoundFinished))
                .findFirst()
                .orElse(null);
    }

    public Integer getLetterLength(Integer roundNumber) {
        var index = (roundNumber - 1) % 3;
        return switch(index) {
            case 1 -> 6;
            case 2 -> 7;
            default -> 5;
        };
    }

    public Integer calculateScore() {
        return this.rounds.stream()
                .map(Round::getScore)
                .map(Score::calculate)
                .reduce(0, (total, element) -> total += element);
    }

    public static void main(String[] args) throws IOException {
        // Tiny implementation of the game, used for debugging purposes
        // Should eventually be removed, though it works fine :)
        var game = new Game();
        var words = List.of(
                new Word("banaan"),
                new Word("kaasje"),
                new Word("katoen"),
                new Word("appel"),
                new Word("gitaar"),
                new Word("groenten"),
                new Word("appelsap"),
                new Word("patat"),
                new Word("bezem"),
                new Word("stoel"),
                new Word("bankje"),
                new Word("zetmeel")
        );
        var random = new Random();
        var reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            var round = game.activeRound();
            if (round == null) {
                System.out.println("Starting a new game...");
                game.startNewRound(words.get(random.nextInt(words.size())));
                round = game.activeRound();
                System.out.printf("hint: %s\n", round.getCurrentHint());
            }

            System.out.println("Type your guess: ");
            var guess = reader.readLine();
            var feedback = game.play(new Word(guess));
            if (feedback.isWordGuessed()) {
                System.out.println("Word guessed!");
                System.out.printf("Current score: %s\n", game.calculateScore());
                continue;
            }

            System.out.printf("hint: %s\n", round.getCurrentHint());
        }
    }
}
