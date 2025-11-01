package org.example.decorator;

import org.example.base.GameCharacter;
import org.example.observer.ObserverBus;

public abstract class CharacterDecorator extends GameCharacter {
    protected GameCharacter decoratedCharacter;

    public CharacterDecorator(GameCharacter character) {
        super(character.getName(), character.getHealth(), character.getAttack(),
                character.getDefense(), character.getObserverBus());
        this.decoratedCharacter = character;
    }
}