
/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * This class is a filtered iterator of the players still alive,
 * in the order that they were added to the game.
 */
public class PlayersInGameIterator {
	
	// Instance variables
	private Player[] players;
	private int size;
	private int nextIndex;
	
	// Constructor
	/**
	 * @param players, array of players to iterate through
	 * @param sizePlayer, number of players in the array "players"
	 * @pre: players != null && sizePlayer <= players.length
	 */
	public PlayersInGameIterator(Player[] players, int sizePlayer) {
		
		this.players = players;
		size = sizePlayer;
		nextIndex = 0;
		advance();
	}
	
	// Methods
	/**
	 * @return true, if there are still elements in the array to show;
	 * false, if otherwise
	 */
	public boolean hasNext() {
		return nextIndex < size;
	}
	
	/**
	 * It returns the next player on the array that is still alive
	 * and searcher for the next player that fulfills that criterion.
	 * 
	 * @return the next player alive
	 * @pre: hasNext()
	 */
	// pre: hasNext()
	public Player next() {
		Player player = players[nextIndex++];
		advance();
		return player;
	}
	
	/**
	 * It searches for the next player alive (the next player that
	 * fulfills the criterion).
	 */
	private void advance() { 
		while (nextIndex < size && !criterion(players[nextIndex]))
			nextIndex++;
	}
	
	/**
	 * @param player, the player we want to know if it is still playing or not
	 * @return true, if the player given is still alive;
	 * false, if it is not alive
	 */
	private boolean criterion(Player player) { 
		
		return player.stillPlaying(); 
	}		
}
