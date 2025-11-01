package org.example.base;

import org.example.observer.ObserverBus;
import org.example.strategy.AttackStrategy;

public abstract class GameCharacter {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int attack;
    protected int defense;
    protected int x, y;
    protected AttackStrategy attackStrategy;
    protected ObserverBus observerBus;

    public GameCharacter(String name, int health, int attack, int defense, ObserverBus observerBus) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.attack = attack;
        this.defense = defense;
        this.observerBus = observerBus;
        this.x = 0;
        this.y = 0;
    }

    public void setAttackStrategy(AttackStrategy strategy) {
        this.attackStrategy = strategy;
    }

    public void performAttack(GameCharacter target) {
        if (attackStrategy != null && health > 0 && target.health > 0) {
            int damage = attackStrategy.execute(this, target);
            target.takeDamage(damage);
        }
    }

    public void takeDamage(int damage) {
        int prevHealth = this.health;
        int actualDamage = Math.max(1, damage - defense / 2);
        this.health = Math.max(0, this.health - actualDamage);

        observerBus.notifyHealthChanged(this, prevHealth, this.health);

        if (this.health <= 0) {
            observerBus.notifyCharacterDeath(this);
        }
    }

    public void heal(int amount) {
        int prevHealth = this.health;
        this.health = Math.min(maxHealth, this.health + amount);
        observerBus.notifyHealthChanged(this, prevHealth, this.health);
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isAlive() { return health > 0; }
    public ObserverBus getObserverBus() {
        return observerBus;
    }

    public void setAttack(int attack) { this.attack = attack; }
    public void setDefense(int defense) { this.defense = defense; }
    public void setHealth(int health) {
        int prevHealth = this.health;
        this.health = health;
        observerBus.notifyHealthChanged(this, prevHealth, this.health);
    }
}
