package org.example.visitor;

import org.example.abstractfactory.Enemy;
import org.example.factory.Hero;

public interface CharacterVisitor {
    void visit(Hero hero);
    void visit(Enemy enemy);
}

