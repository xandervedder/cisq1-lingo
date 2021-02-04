Feature: Lingo Trainer
  As a user, I want to be able to train for Lingo so that I improve my word guessing skills

  Scenario: Start a game
    When I start a game
    Then I should see a starting letter
    And  the word length should be "5"
    And  the score should be "0"
