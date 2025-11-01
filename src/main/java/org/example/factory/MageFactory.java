package org.example.factory;

import org.example.observer.ConsoleLogger;
import org.example.observer.ObserverBus;
import org.example.strategy.MagicAttack;
import org.example.base.GameCharacter;

public class MageFactory implements HeroFactory {
    public GameCharacter createHero(String name, ObserverBus observerBus) {
        GameCharacter mage = new Hero(name, 80, 35, 5, observerBus);
        mage.setAttackStrategy(new MagicAttack());
        ConsoleLogger.getInstance().log("Mage Created" + name);
        return mage;
    }
}
