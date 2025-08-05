package paintball;
import dataStructures.*;

/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * Interface of TeamClass
 */
public interface Team {
	
	static final int NOT_FOUND = -1;
	
	/**
	 * Get the name of the team.
	 * @return String that is the name of the team
	 */
	String getName();
	
	/**
	 * Starts an iterator of the team's bunkers.
	 * @return Iterator object of the team's bunkers
	 */
	Iterator<Bunker> getBunkers();
	
	/**
	 * Starts an iterator of the team's players.
	 * @return Iterator object of the team's players
	 */
	Iterator<Player> getPlayers();
	
	/**
	 * Gets the number of the team's bunkers.
	 * @return Number of bunkers in the team
	 */
	int getNumBunkers();	
	
	/**
	 * Checks by name if the bunker belongs to the team.
	 * @param name String that represents the name of the bunker
	 * @return True, if the bunker belongs to the team; False otherwise
	 */
	boolean doesBunkerExist(String name);
	
	/**
	 * Creates a player in the team, in the bunker given.
	 * @param player Player object that will integrate the team
	 * @param bunker Bunker object which is the bunker where the player will be created
	 */
	void createPlayer(Player player, Bunker bunker);
	
	/**
	 * Gets the number of players in the team.
	 * @return Number of players in the team.
	 */
	int getNumPlayers();
	
	/**
	 * Checks if there is a player of the team in the position given.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return True, if the player exists; False, otherwise
	 */
	boolean isPlayerInPosition(int x, int y);
	
	/**
	 * Gets the player in the position given.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return Player object, which is the player we want
	 * @pre: isPlayerInPosition(x, y)
	 */
	Player playerInPosition(int x, int y);
	
	/**
	 * Checks if there is a bunker in the position given.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return True, if the bunker is in the position; False, otherwise
	 */
	boolean isBunkerInPosition(int x, int y);
	
	/**
	 * Gets the bunker in the position given.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return Bunker object, which is the bunker we want
	 * @pre: isBunkerInPosition(x, y)
	 */
	Bunker bunkerInPosition(int x, int y);
	
	/**
	 * Removes the bunker given from the team's bunkers.
	 * @param bunker Bunker object which is the bunker we want to remove
	 * @pre: Bunker exists in team's bunkers
	 */
	void removeBunker(Bunker bunker);
	
	/**
	 * Adds the bunker given to the team's bunkers.
	 * @param bunker Bunker object which is the bunker we want to add
	 */
	void addBunker(Bunker bunker);
	
	/**
	 * Removes the player given from the team.
	 * @param player
	 * @pre: doesPlayerExist(player)
	 */
	void removePlayer(Player player);
	
	/**
	 * Checks if the player exists in the team.
	 * @param player Player object we want to check if it belongs to this team
	 * @return True, if the player exists; False, otherwise
	 */
	boolean doesPlayerExist(Player player);
	
	/**
	 * Gets a player by its index.
	 * @param index The position of the player in the array
	 * @return Player object which is the player we want
	 * @pre: index < getNumPlayers() && index >= 0
	 */
	
	Player getPlayerByIdx(int index);
}
