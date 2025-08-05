package paintball;

/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * Interface of BunkerClass
 */
public interface Bunker extends MapElement {
	
	/**
	 * Gets the name of the bunker.
	 * @return String which is the name of the bunker
	 */
	String getName();
	
    /**
     * Checks if the bunker has a team (owner).
     * @return True, if the bunker has a team; False, otherwise
     */
    boolean hasTeam();
    
    /**
     * Checks if the bunker has a player inside.
     * @return True, if the bunker has a player inside; False, otherwise
     */
    boolean isOccupied();
    
    /**
     * Gets the number of coins in the bunker's treasure.
     * @return The number that represents the treasure
     */
    int getTreasure();
    
    /**
     * Adds one coin to the bunker's treasure.
     */
    void addCoin();
    
    /**
     * Occupies the bunker with the player given.
     * @param player Player object which is the player that will be inside the bunker
     */
    void occupyBunker(Player player);
    
    /**
     * Takes out the player inside the bunker.
     */
    void clearBunker();
    
    /**
     * Pays for the creation of a player, subtrating the value of cost to its treasure.
     * @param cost The value in coins to create a player
     */
    void payCreationCost(int cost);
}
