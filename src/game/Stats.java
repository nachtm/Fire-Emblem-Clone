package game;

public class Stats{
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
	private int[] stats;
	public static final String[] STAT_LOCATION = {"level", "exp","hp","maxhp","strength","skill",
						"speed","luck","defense","resistance","constitution","move"};
	//look at StatSheet if anything changes here!
	public Stats(int level, int exp, int hp, int str, int skl, int spd, int lck, int def, 
		int res, int con, int mov){
		this.level = level;
		this.exp = exp;
		this.hp = hp;
		this.maxhp = hp;
		strength = str;
		skill = skl;
		speed = spd;
		luck = lck;
		defense = def;
		resistance = res;
		constitution = con;
		move = mov;
		int[] temp = {level, exp, hp, maxhp, strength, skill, speed, luck, defense, resistance,
				constitution, move};
		stats = temp;
	}

	public void setStat(String stat, int value){
		for(int i = 0; i < STAT_LOCATION.length; i++){
			if(stat.equals(STAT_LOCATION[i])){
				stats[i] = value;
			}
		}
	}

	public int getStat(String stat){
		for(int i = 0; i < STAT_LOCATION.length; i++){
			if(stat.equals(STAT_LOCATION[i])){
				return stats[i];
			}
		} return -1;
	}
}