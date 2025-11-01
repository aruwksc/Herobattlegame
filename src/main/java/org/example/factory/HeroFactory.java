package org.example.factory;

import org.example.base.GameCharacter;
import org.example.observer.ObserverBus;

public interface HeroFactory {
    GameCharacter createHero(String name, ObserverBus observerBus);

}
