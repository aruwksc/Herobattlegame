package org.example.factory;

import org.example.observer.ConsoleLogger;
import org.example.base.GameCharacter;
import org.example.observer.ObserverBus;
import org.example.strategy.MeleeAttack;

public class WarriorFactory implements HeroFactory {
    public GameCharacter createHero(String name, ObserverBus observerBus) {
        GameCharacter warrior = new Hero(name, 150, 25, 15, observerBus);
        warrior.setAttackStrategy(new MeleeAttack());
        ConsoleLogger.getInstance().log("Warrior Created" + name);
        return warrior;
    }
}
