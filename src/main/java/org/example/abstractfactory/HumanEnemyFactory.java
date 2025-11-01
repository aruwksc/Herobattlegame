package org.example.abstractfactory;

import org.example.observer.ObserverBus;
import org.example.strategy.MagicAttack;
import org.example.strategy.MeleeAttack;
import org.example.strategy.RangedAttack;
import org.example.base.GameCharacter;

public class HumanEnemyFactory implements EnemyFactory{
    @Override
    public GameCharacter createWarrior(ObserverBus observerBus) {
        GameCharacter enemy = new Enemy("Human-Warrior", 120, 20, 12, observerBus);
        enemy.setAttackStrategy(new MeleeAttack());
        return enemy;
    }
    @Override
    public GameCharacter createMage(ObserverBus observerBus) {
        GameCharacter enemy = new Enemy("Human-Mage", 70, 28, 5, observerBus);
        enemy.setAttackStrategy(new MagicAttack());
        return enemy;
    }

    @Override
    public GameCharacter createArcher(ObserverBus observerBus) {
        GameCharacter enemy = new Enemy("Human-Archer", 90, 25, 8, observerBus);
        enemy.setAttackStrategy(new RangedAttack());
        return enemy;
    }
}


