package org.example.builder;

import org.example.base.GameCharacter;
import org.example.factory.Hero;
import org.example.observer.ObserverBus;

public class CharacterBuilder {
    private String name;
    private int health = 100;
    private int attack = 20;
    private int defense = 10;
    private ObserverBus observerBus;

    public CharacterBuilder(ObserverBus observerBus) {
        this.observerBus = observerBus;
    }

    public CharacterBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CharacterBuilder setHealth(int health) {
        this.health = health;
        return this;
    }

    public CharacterBuilder setAttack(int attack) {
        this.attack = attack;
        return this;
    }

    public CharacterBuilder setDefense(int defense) {
        this.defense = defense;
        return this;
    }

    public GameCharacter build() {
        GameCharacter hero = new Hero(name, health, attack, defense, observerBus);
        return hero;
    }
}