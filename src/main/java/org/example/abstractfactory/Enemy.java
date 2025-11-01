package org.example.abstractfactory;

import org.example.base.GameCharacter;
import org.example.observer.ObserverBus;

public class Enemy extends GameCharacter {
    public Enemy(String name, int health, int attack, int defense, ObserverBus observerBus) {
        super(name, health, attack, defense, observerBus);
    }
}