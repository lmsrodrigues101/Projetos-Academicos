package paintball;

/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * This is the class that creates objects of type Bunker.
 * It is one of the subclasses of AbstractMapElement.
 */
public class BunkerClass extends AbstractMapElement implements Bunker {
	
	private int treasure; // Number of coins in the bunker
	private String name; // Name of the bunker
	private Player playerInside; // Player inside the bunker
	
	/**
	 * Constructor of Bunkerclass 
	 * @param x Position of the bunker in the X axis of the map
	 * @param y Position of the bunker in the Y axis of the map
	 * @param treasure Number of initial coins in the treasure's bunker
	 * @param name String which is the name of the bunker
	 */
	public BunkerClass(int x, int y, int treasure, String name) {
		
		super(x, y);
		this.name = name;
		this.treasure = treasure;
		playerInside = null;
	}
	
	@Override
    public String getName() {
    	
    	return name;
    }
	
	@Override
	public boolean hasTeam() {
		
		return getTeam() != null;
	}	
	
	@Override
	public int getTreasure() {
		
		return treasure;
	}

	@Override
	public void addCoin() {
		
		treasure++;		
	}
	
	@Override
	public boolean isOccupied() {
		
		return playerInside != null;
	}

	@Override
	public void occupyBunker(Player player) {
		
		playerInside = player;
	}

	@Override
	public void clearBunker() {
		
		playerInside = null;
	}

	@Override
	public void payCreationCost(int cost) {
		
		treasure -= cost;
	}	

}
