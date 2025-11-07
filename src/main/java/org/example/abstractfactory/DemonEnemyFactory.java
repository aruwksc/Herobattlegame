package org.example.abstractfactory;

import org.example.observer.ObserverBus;
import org.example.strategy.MagicAttack;
import org.example.strategy.MeleeAttack;
import org.example.strategy.RangedAttack;
import org.example.base.GameCharacter;

public class DemonEnemyFactory implements EnemyFactory{
    @Override
    public GameCharacter createWarrior(ObserverBus observerBus) {
        GameCharacter enemy = new Enemy("Demon-warrior", 180, 35, 20, observerBus);
        enemy.setAttackStrategy(new MeleeAttack());
        return enemy;
    }

    @Override
    public GameCharacter createMage(ObserverBus observerBus) {
        GameCharacter enemy = new Enemy("Demon-mage", 120, 40, 10, observerBus);
        enemy.setAttackStrategy(new MagicAttack());
        return enemy;
    }

    @Override
    public GameCharacter createArcher(ObserverBus observerBus) {
        GameCharacter enemy = new Enemy("Demon-archer", 140, 38, 15, observerBus);
        enemy.setAttackStrategy(new RangedAttack());
        return enemy;
    }
}
