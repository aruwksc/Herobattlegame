package org.example.factory;

import org.example.observer.ConsoleLogger;
import org.example.observer.ObserverBus;
import org.example.strategy.RangedAttack;
import org.example.base.GameCharacter;

public class ArcherFactory implements HeroFactory{
    public GameCharacter createHero(String name, ObserverBus observerBus) {
        GameCharacter archer = new Hero(name, 100, 30, 10, observerBus);
        archer.setAttackStrategy(new RangedAttack());
        ConsoleLogger.getInstance().log("Archer Created" + name);
        return archer;
    }
}
