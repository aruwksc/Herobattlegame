package org.example.abstractfactory;

import org.example.base.GameCharacter;
import org.example.observer.ObserverBus;

public interface EnemyFactory {
    GameCharacter createWarrior(ObserverBus observerBus);
    GameCharacter createMage(ObserverBus observerBus);
    GameCharacter createArcher(ObserverBus observerBus);
}