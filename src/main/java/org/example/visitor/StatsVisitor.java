package org.example.visitor;

import org.example.abstractfactory.Enemy;
import org.example.factory.Hero;

public class StatsVisitor implements CharacterVisitor{
    private StringBuilder stats = new StringBuilder();

    @Override
    public void visit(Hero hero) {
        stats.append("=== HERO ===\n");
        stats.append("Name").append(hero.getName()).append("\n");
        stats.append("Health").append(hero.getHealth()).append("\n");
        stats.append("Attack").append(hero.getAttack()).append("\n");
        stats.append("Defense").append(hero.getDefense()).append("\n");
    }
    @Override
    public void visit(Enemy enemy) {
        stats.append("=== ENEMY ===\n");
        stats.append("Name").append(enemy.getName()).append("\n");
        stats.append("Health").append(enemy.getHealth()).append("\n");
        stats.append("Attack").append(enemy.getAttack()).append("\n");
        stats.append("Defense").append(enemy.getDefense()).append("\n");
    }
    public String getStats(){
        return stats.toString();
    }
    public void reset() {
        stats = new StringBuilder();
    }

}
