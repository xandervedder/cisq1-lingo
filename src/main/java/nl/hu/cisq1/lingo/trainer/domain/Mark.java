package nl.hu.cisq1.lingo.trainer.domain;

public enum Mark {
    ABSENT,     // Letter is not in the word
    CORRECT,    // Happens when the letter is on the right place
    INVALID,    // TODO: remove this constant, since it is not being used
    PRESENT,    // Letter is not on the right spot, but is in the word
}
