package org.example.strategy;

import org.example.base.GameCharacter;

public interface AttackStrategy {
    int execute(GameCharacter attacker, GameCharacter target);
    String getAttackName();
}