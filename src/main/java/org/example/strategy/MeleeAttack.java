package org.example.strategy;

import org.example.base.GameCharacter;

public class MeleeAttack implements AttackStrategy {
    @Override
    public int execute(GameCharacter attacker, GameCharacter target) {
        int damage = (int)(attacker.getAttack() * (0.8 + Math.random() * 0.4));
        return damage;
    }

    @Override
    public String getAttackName() {
        return "Melee Attack";
    }
}