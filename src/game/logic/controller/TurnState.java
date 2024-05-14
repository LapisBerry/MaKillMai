package game.logic.controller;

public enum TurnState {
    RollDice, // Player will roll dice in this state
    ResolveAction, // Player will resolve the action in this state (choosing what to do with the dice faces)
    EndTurn // Player will end the turn in this state
}
