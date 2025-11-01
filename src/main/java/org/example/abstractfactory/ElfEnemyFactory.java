package org.example.abstractfactory;

import org.example.observer.ObserverBus;
import org.example.strategy.MagicAttack;
import org.example.strategy.MeleeAttack;
import org.example.strategy.RangedAttack;
import org.example.base.GameCharacter;

public class ElfEnemyFactory implements EnemyFactory{
    @Override
    public GameCharacter createWarrior(ObserverBus observerBus) {
        GameCharacter enemy = new Enemy("Elf-Warrior", 100, 22, 10, observerBus);
        enemy.setAttackStrategy(new MeleeAttack());
        return enemy;
    }

    @Override
    public GameCharacter createMage(ObserverBus observerBus) {
        GameCharacter enemy = new Enemy("Elf-Mage", 80, 32, 6, observerBus);
        enemy.setAttackStrategy(new MagicAttack());
        return enemy;
    }

    @Override
    public GameCharacter createArcher(ObserverBus observerBus) {
        GameCharacter enemy = new Enemy("Elf-Archer", 95, 30, 9, observerBus);
        enemy.setAttackStrategy(new RangedAttack());
        return enemy;
    }
}
