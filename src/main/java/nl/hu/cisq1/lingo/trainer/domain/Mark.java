package nl.hu.cisq1.lingo.trainer.domain;

public enum Mark {
    ABSENT,     // Letter is not in the word
    CORRECT,    // Happens when the letter is on the right place
    PRESENT,    // Letter is not on the right spot, but is in the word
}
