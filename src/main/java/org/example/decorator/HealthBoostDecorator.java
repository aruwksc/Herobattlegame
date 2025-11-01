package org.example.decorator;

import org.example.base.GameCharacter;

public class HealthBoostDecorator extends CharacterDecorator {
    private static final int HEALTH_BONUS = 50;

    public HealthBoostDecorator(GameCharacter character) {
        super(character);
        character.heal(HEALTH_BONUS);
    }
}