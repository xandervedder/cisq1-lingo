package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.application.exception.WordDoesNotExistInDatabaseException;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {
    private final SpringGameRepository gameRepository;
    private final WordService wordService;

    public TrainerService(SpringGameRepository gameRepository, WordService wordService) {
        this.gameRepository = gameRepository;
        this.wordService = wordService;
    }

    public Game startNewGame() {
       return this.startNewRound(new Game());
    }

    public Game startNewRound(Long gameId) {
        return this.startNewRound(this.getGameById(gameId));
    }

    private Game startNewRound(Game game) {
        var word = this.wordService.provideRandomWord(game.currentLetterLength());
        game.startNewRound(word);
        return this.gameRepository.save(game);
    }

    public Game continueRound(Long gameId, String guess) {
        if (!this.wordService.wordExists(guess))
            throw new WordDoesNotExistInDatabaseException();

        var game = this.getGameById(gameId);
        var round = game.play(guess);
        if (round.isRoundFinished())
            round.revealWord();

        return this.gameRepository.save(game);
    }

    public Integer getScoreFromGameById(Long gameId) {
        var game= this.getGameById(gameId);
        return game.calculateScore();
    }

    public Game getGameById(Long id) {
        return this.gameRepository.getOne(id);
    }
}
