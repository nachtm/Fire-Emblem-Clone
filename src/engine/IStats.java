package engine;

/**
 * Created by Micah on 4/24/2018.
 */
public interface IStats {

    int getMove();
    int getLevel();
    int getExp();
    int getHp();
    int getMaxhp();
    int getStrength();
    int getSkill();
    int getSpeed();
    int getLuck();
    int getDefense();
    int getResistance();
    int getConstitution();

    void setLevel(int level);
    void setExp(int exp);
    void setHp(int hp);
    void setMaxhp(int maxhp);
    void setStrength(int strength);
    void setSkill(int skill);
    void setSpeed(int speed);
    void setLuck(int luck);
    void setDefense(int defense);
    void setResistance(int resistance);
    void setConstitution(int constitution);
    void setMove(int move);
}
