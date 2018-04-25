package engine;

/**
 * Created by Micah on 4/24/2018.
 */
public class DummyStats implements IStats {

    @Override
    public int getMove() {
        return 5;
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public int getExp() {
        return 0;
    }

    @Override
    public int getHp() {
        return 20;
    }

    @Override
    public int getMaxhp() {
        return 20;
    }

    @Override
    public int getStrength() {
        return 0;
    }

    @Override
    public int getSkill() {
        return 0;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public int getLuck() {
        return 0;
    }

    @Override
    public int getDefense() {
        return 0;
    }

    @Override
    public int getResistance() {
        return 0;
    }

    @Override
    public int getConstitution() {
        return 0;
    }

    @Override
    public void setLevel(int level) {}

    @Override
    public void setExp(int exp) {}

    @Override
    public void setHp(int hp) {}

    @Override
    public void setMaxhp(int maxhp) {}

    @Override
    public void setStrength(int strength) {}

    @Override
    public void setSkill(int skill) {}

    @Override
    public void setSpeed(int speed) {}

    @Override
    public void setLuck(int luck) {}

    @Override
    public void setDefense(int defense) {}

    @Override
    public void setResistance(int resistance) {}

    @Override
    public void setConstitution(int constitution) {}

    @Override
    public void setMove(int move) {}
}
