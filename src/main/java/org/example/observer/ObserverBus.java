package org.example.observer;

import org.example.base.GameCharacter;
import java.util.ArrayList;
import java.util.List;

public class ObserverBus {
    private List<GameObserver> observers = new ArrayList<>();

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    public void notifyHealthChanged(GameCharacter character, int oldHealth, int newHealth) {
        for (GameObserver observer : observers) {
            observer.onHealthChanged(character, oldHealth, newHealth);
        }
    }

    public void notifyBattleEvent(String event) {
        for (GameObserver observer : observers) {
            observer.onBattleEvent(event);
        }
    }

    public void notifyCharacterDeath(GameCharacter character) {
        for (GameObserver observer : observers) {
            observer.onCharacterDeath(character);
        }
    }
}