package nl.hu.cisq1.lingo.trainer.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WordDoesNotExistInDatabaseException extends RuntimeException {
}
