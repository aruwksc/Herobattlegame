package org.example.decorator;

import org.example.base.GameCharacter;

public class PowerBoostDecorator extends CharacterDecorator {
    private static final int ATTACK_BONUS = 15;

    public PowerBoostDecorator(GameCharacter character) {
        super(character);
        character.setAttack(character.getAttack() + ATTACK_BONUS);
    }
}