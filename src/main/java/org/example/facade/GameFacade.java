package org.example.facade;

import org.example.base.GameCharacter;
import org.example.factory.*;
import org.example.abstractfactory.*;
import org.example.decorator.*;
import org.example.observer.ObserverBus;
import org.example.strategy.*;

public class GameFacade {
    private ObserverBus observerBus;
    private GameCharacter hero;
    private GameCharacter enemy;
    private boolean gameStarted = false;

    public GameFacade() {
        this.observerBus = new ObserverBus();
    }

    public void createHero(String type, String name) {
        HeroFactory factory;
        switch (type.toLowerCase()) {
            case "warrior":
                factory = new WarriorFactory();
                break;
            case "mage":
                factory = new MageFactory();
                break;
            case "archer":
                factory = new ArcherFactory();
                break;
            default:
                factory = new WarriorFactory();
        }
        hero = factory.createHero(name, observerBus);
        hero.move(100, 300);
    }

    public void createEnemy(String race, String type) {
        EnemyFactory factory;
        switch (race.toLowerCase()) {
            case "human":
                factory = new HumanEnemyFactory();
                break;
            case "elf":
                factory = new ElfEnemyFactory();
                break;
            case "orc":
                factory = new OrcEnemyFactory();
                break;
            case "demon":
                factory = new DemonEnemyFactory();
                break;
            default:
                factory = new HumanEnemyFactory();
        }

        switch (type.toLowerCase()) {
            case "warrior":
                enemy = factory.createWarrior(observerBus);
                break;
            case "mage":
                enemy = factory.createMage(observerBus);
                break;
            case "archer":
                enemy = factory.createArcher(observerBus);
                break;
            default:
                enemy = factory.createWarrior(observerBus);
        }
        enemy.move(600, 300);
    }

    public void heroAttack() {
        if (hero != null && enemy != null && hero.isAlive() && enemy.isAlive()) {
            hero.performAttack(enemy);
        }
    }

    public void enemyAttack() {
        if (hero != null && enemy != null && hero.isAlive() && enemy.isAlive()) {
            enemy.performAttack(hero);
        }
    }

    public void changeHeroAttackStrategy(AttackStrategy strategy) {
        if (hero != null) {
            hero.setAttackStrategy(strategy);
        }
    }

    public void applyShieldBuff() {
        if (hero != null) {
            new ShieldDecorator(hero);
        }
    }

    public void applyPowerBuff() {
        if (hero != null) {
            new PowerBoostDecorator(hero);
        }
    }

    public void applyHealthBuff() {
        if (hero != null) {
            new HealthBoostDecorator(hero);
        }
    }

    public void moveHero(int dx, int dy) {
        if (hero != null) {
            hero.move(dx * 5, dy * 5);
        }
    }

    public GameCharacter getHero() {
        return hero;
    }

    public GameCharacter getEnemy() {
        return enemy;
    }

    public ObserverBus getObserverBus() {
        return observerBus;
    }

    public void resetGame() {
        hero = null;
        enemy = null;
        gameStarted = false;
    }

    public void setGameStarted(boolean started) {
        this.gameStarted = started;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
}