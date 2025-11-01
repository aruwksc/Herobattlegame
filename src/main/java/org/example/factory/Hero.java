package org.example.factory;

import org.example.base.GameCharacter;
import org.example.observer.ObserverBus;

public class Hero extends GameCharacter {
    public Hero(String name, int health, int attack, int defense, ObserverBus observerBus) {
        super(name, health, attack, defense, observerBus);
    }
}