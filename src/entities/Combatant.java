package entities;

/**
 * Interface defining combat behaviors.
 * Demonstrates the "Interface" and "Polymorphism" concepts.
 */
public interface Combatant {
    void takeDamage(int amount);
    void heal(int amount);
    int getAttackDamage();
    boolean isAlive();
    int getHp();
    int getMaxHp();
}
