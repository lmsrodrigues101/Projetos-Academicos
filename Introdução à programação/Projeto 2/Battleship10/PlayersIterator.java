
/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * This class is an iterator of the players, sorted by their scores in 
 * descending order and, in case of ties, by lexicographical order.
 */
public class PlayersIterator {
	
	// Instance variables
	private Player[] players;
	private int size;
	private int nextIndex;
	
	// Constructor
	/**
	 * @param players, array of players to iterate through
	 * @param size, the number of players in the array "players"
	 * @pre: players != null && size <= players.length
	 */
	public PlayersIterator(Player[] players, int size) {
		
		this.players = players;
		this.size = size;
		nextIndex = 0;		
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
	 * @return the next player on the array
	 * @pre: hasNext()
	 */
	public Player next() {
		
		return players[nextIndex++];
	}
}
