Feature: Lingo Trainer
  As a user, I want to be able to train for Lingo so that I improve my word guessing skills

  Scenario: Start a game
    When I start a game
    Then I should see a starting letter
    And  the word length should be "5"
    And  the score should be "0"

  Scenario Outline: Start a new round
    Given I am playing a game
    And   the round was won
    And   the last word <previous length> letters
    When  I start a new round
    Then  the word to guess has <next length> letter

    Examples:
      | previous length | next length |
      | 5               | 6           |
      | 6               | 7           |
      | 7               | 5           |

    # Failure path:
    Given I am playing a game
    And   the round was lost
    Then  I cannot start a new round

  Scenario Outline: Guessing a word
    Given I am playing a game
    And   the word is <word>
    Then  I guess the word with <guess>
    Then  I should recieve <feedback>

    Examples:
      | word  | guess   | feedback                                              |
      | BAARD | BERGEN  | INVALID, INVALID, INVALID, INVALID, INVALID, INVALID  |
      | BAARD | BONJE   | CORRECT, ABSENT, ABSENT, ABSENT, ABSENT               |
      | BAARD | BARST   | CORRECT, CORRECT, PRESENT, ABSENT, ABSENT             |
      | BAARD | DRAAD   | ABSENT, PRESENT, CORRECT, PRESENT, CORRECT            |
      | BAARD | BAARD   | CORRECT, CORRECT, CORRECT, CORRECT, CORRECT           |

  Scenario: Succesful guessing attempt
    Given I am playing a game
    And   I have guessed correctly
    Then  my score should increase
    And   the score should be "5"

  Scenario: Five unsuccesful attempts
    Given I am playing a game
    And   I have "5" unsuccesful attempts
    Then  the round is over

  Scenario: Word already guessed
    Given I am playing a game
    And   I have already guessed the word "word"
    Then  a new round will be started

  Scenario: Player can't guess if the player is eliminated
    Given I am playing a game
    And   I am eliminated
    Then  I should not be able to guess

  Scenario: Player can't start a new round if the player is still guessing
    Given I am playing a game
    And   I am still guessing a word
    Then  I should not be able to start a new round

  Scenario: Player can't start a new round if the player hasn't started a game
    Given I am not playing a game
    Then I should not be able to start a new round
