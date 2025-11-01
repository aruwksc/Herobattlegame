package org.example.decorator;

import org.example.base.GameCharacter;

public class ShieldDecorator extends CharacterDecorator {
    private static final int DEFENSE_BONUS = 10;

    public ShieldDecorator(GameCharacter character) {
        super(character);
        character.setDefense(character.getDefense() + DEFENSE_BONUS);
    }
}