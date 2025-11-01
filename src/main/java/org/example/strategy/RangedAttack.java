package org.example.strategy;

import org.example.base.GameCharacter;

public class RangedAttack implements AttackStrategy {
    @Override
    public int execute(GameCharacter attacker, GameCharacter target) {
        int damage = (int)(attacker.getAttack() * (0.6 + Math.random() * 0.6));
        return damage;
    }

    @Override
    public String getAttackName() {
        return "Ranged Attack";
    }
}