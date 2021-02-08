package nl.hu.cisq1.lingo.trainer.domain;

public enum Mark {
    ABSENT,     // Letter is not in the word
    CORRECT,    // Happens when the letter is on the right place
    INVALID,    // Only happens when length is too long
    PRESENT,    // Letter is not on the right spot, but is in the word
}
