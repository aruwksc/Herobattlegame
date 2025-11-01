package org.example.abstractfactory;

import org.example.observer.ObserverBus;
import org.example.strategy.MagicAttack;
import org.example.strategy.MeleeAttack;
import org.example.strategy.RangedAttack;
import org.example.base.GameCharacter;

public class OrcEnemyFactory implements EnemyFactory{
    @Override
    public GameCharacter createWarrior(ObserverBus observerBus) {
        GameCharacter enemy = new Enemy("Orc-warrior", 160, 28, 18, observerBus);
        enemy.setAttackStrategy(new MeleeAttack());
        return enemy;
    }

    @Override
    public GameCharacter createMage(ObserverBus observerBus) {
        GameCharacter enemy = new Enemy("orc-mage", 90, 30, 8, observerBus);
        enemy.setAttackStrategy(new MagicAttack());
        return enemy;
    }

    @Override
    public GameCharacter createArcher(ObserverBus observerBus) {
        GameCharacter enemy = new Enemy("orc-archer", 110, 26, 12, observerBus);
        enemy.setAttackStrategy(new RangedAttack());
        return enemy;
    }
}
