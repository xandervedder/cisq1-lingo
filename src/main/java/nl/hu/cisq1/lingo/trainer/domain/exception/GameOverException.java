package nl.hu.cisq1.lingo.trainer.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class GameOverException extends RuntimeException {
}
