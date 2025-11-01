package org.example.strategy;

import org.example.base.GameCharacter;

public class MagicAttack implements AttackStrategy {
    @Override
    public int execute(GameCharacter attacker, GameCharacter target) {
        int damage = (int)(attacker.getAttack() * (1.0 + Math.random() * 0.5));
        return damage;
    }

    @Override
    public String getAttackName() {
        return "Magic Attack";
    }
}