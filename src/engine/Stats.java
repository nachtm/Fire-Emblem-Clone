package engine;

/**
 * Created by Micah on 4/24/2018.
 */
public class Stats implements IStats {
    private int level;
    private int exp;
    private int hp;
    private int maxhp;
    private int strength;
    private int skill;
    private int speed;
    private int luck;
    private int defense;
    private int resistance;
    private int constitution;
    private int move;
    private int aid;

    public Stats(int level, int exp, int hp, int maxhp, int strength, int skill, int speed, int luck, int defense,
                 int resistance, int constitution, int move, int aid) {
        this.level = level;
        this.exp = exp;
        this.hp = hp;
        this.maxhp = maxhp;
        this.strength = strength;
        this.skill = skill;
        this.speed = speed;
        this.luck = luck;
        this.defense = defense;
        this.resistance = resistance;
        this.constitution = constitution;
        this.move = move;
        this.aid = aid;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getExp() {
        return exp;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public int getMaxhp() {
        return maxhp;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    @Override
    public int getSkill() {
        return skill;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public int getLuck() {
        return luck;
    }

    @Override
    public int getDefense() {
        return defense;
    }

    @Override
    public int getResistance() {
        return resistance;
    }

    @Override
    public int getConstitution() {
        return constitution;
    }

    @Override
    public int getAid() {
        return aid;
    }

    @Override
    public int getMove() {
        return move;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setMaxhp(int maxhp) {
        this.maxhp = maxhp;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public void setMove(int move) {
        this.move = move;
    }

    @Override
    public void setAid(int aid) {
        this.aid = aid;
    }
}
