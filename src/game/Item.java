package game;

public class Item{
	private String name;
	private String rank;
	private int range;
	private int weight;
	private int might;
	private int hit;
	private int crit;
	private int uses;

	public Item(String name, String rank, int range, int weight, int might, int hit, 
				int crit, int uses){
		this.name = name;
		this.rank = rank;
		this.range = range;
		this.weight = weight;
		this.might = might;
		this.hit = hit;
		this.crit = crit;
		this.uses = uses;
	}

	public String getName(){
		return name;
	}

	public String getRank(){
		return rank;
	}

	public int getRange(){
		return range;
	}
	
	public int getWeight(){
		return weight;
	}

	public int getMight(){
		return might;
	}

	public int getHit(){
		return hit;
	}
	
	public int getCrit(){
		return crit;
	}

	public int getUses(){
		return uses;
	}

	public String toString(){
		return "" + name + " " + uses;
	}
}