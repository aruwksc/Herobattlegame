package org.example.observer;

import org.example.base.GameCharacter;

public interface GameObserver {
    void onHealthChanged(GameCharacter character, int oldHealth, int newHealth);
    void onBattleEvent(String event);
    void onCharacterDeath(GameCharacter character);
}