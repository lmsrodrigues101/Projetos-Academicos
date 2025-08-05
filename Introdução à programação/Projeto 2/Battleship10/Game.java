
/** 
 * @authors Gustavo Sousa, Leandro Rodrigues
 * This class is the interface of the game. Each of its instances will have 
 * an array of Player objects and, depending on which command is inputed by 
 * the user, the Main class will call the methods created 
 * within Game and interact with the instance created in Main. 
 * From there, the instance of Game will create a "bridge" between
 * Main and all the other classes.
 */
public class Game {
	
	// Constants
	public static final char WATER = '.';
	public static final char DOWN = '*';
	private static final int MISS = -30;
	private static final int HIT = 100;
	private static final int DUPLICATE_FACTOR = 2;
	
	// Instance variables
	private Player[] players;
	private int sizePlayers;
	private int currentPlayerIdx;
	
	// Constructor
	/**
	 * @param numPlayers, number of players
	 * @pre: numPlayers >= 2              
	 */
	public Game(int numPlayers) {
		
		players = new Player[numPlayers];
		sizePlayers = 0;
		currentPlayerIdx = 0;
	}
	
	// Methods
	/**
	 * Stores a player to the variable "players".
	 * 
	 * @param name, player's name
	 * @param fleet, player's fleet
	 * @pre: name != null && fleet != null && !doesPlayerExist(name)
	 */
	public void addPlayer(String name, char[][] fleet) {

		players[sizePlayers++] = new Player(name, fleet);
	}
	
	/**
	 * @return iterator of players, sorted by their scores in descending
	 * order and, in case of ties, by lexicographical order
	 */
	public PlayersIterator iterator() {
		
		Player[] aux = copyPlayers();
		sortPlayers(aux);   
		return new PlayersIterator(aux, sizePlayers);
	}
	
	/**
	 * @return iterator of players not eliminated, in the order by which 
	 * they were added to the game
	 */
	public PlayersInGameIterator filtIterator() {
		
		return new PlayersInGameIterator(players, sizePlayers);
	}
	
	/**
	 * Sorts a players' copy by their scores in descending
	 * order and, in case of ties, by lexicographical order.
	 * 
	 * @param playersAux, copy of the array "players"
	 */
	private void sortPlayers(Player[] playersAux) {
		 
		for (int i = 0; i < playersAux.length - 1; i++) {
			int maxIndex = i;
			for (int j = i + 1; j < playersAux.length; j++) {
				if (playersAux[j].compareTo(playersAux[maxIndex]) > 0) {
					maxIndex = j;
				}
			}
			Player temp = playersAux[i];
			playersAux[i] = playersAux[maxIndex];
			playersAux[maxIndex] = temp;
		}
	}
	
	/**
	 * @return a copy of the array "players"
	 */
	private Player[] copyPlayers() {
		
		Player[] aux = new Player[sizePlayers];
		for (int i = 0; i < sizePlayers; i++) 
			aux[i] = players[i];
		
		return aux;
	}
	
	/**
	 * Checks if the game is over: if there is only 1 player
	 * alive, the game is over; else, it is not over.
	 */
	public boolean isGameOver() {
		
		int sum = 0;
		int i = 0;
		while (i < sizePlayers && sum <= 1) {
			if (players[i].stillPlaying())
				sum++;
			i++;
		}
		return sum == 1;
	}
	
	/**
	 * @return the index in "players" of the player that has the turn
	 */
	private int getNextPlayerIdx() { 
		
		return currentPlayerIdx;
	}
	
	/**
	 * @return the name of the player that has the turn
	 */
	public String getNextPlayerName() {
		
		return players[currentPlayerIdx].getName();
	}
	
	/**
	 * Passes the turn to the next player alive.
	 */
	private void switchPlayer() {
		
		do {
			if (currentPlayerIdx == sizePlayers - 1) 	
				currentPlayerIdx = 0;
			else
				currentPlayerIdx++;
		} while (!players[currentPlayerIdx].stillPlaying());
	}
	
	/**
	 * @param name, the name given to check if it belongs to any player
	 * @return true, if the player with the name given exists;
	 * false, if it does not exist
	 * @pre: name != null
	 */
	public boolean doesPlayerExist(String name) {
		
		int i = 0;
		while (i < sizePlayers && !players[i].getName().equals(name))
			i++;
		
		return i < sizePlayers;
	}
	
	/**
	 * @param name, the name of the player we want
	 * @return the player with the name given
	 */
	private Player getPlayer(String name) {
		
		int i = 0;
		while (!players[i].getName().equals(name))
			i++;
		
		return players[i];
	}
	
	/**
	 * @param name, the name of the player we want
	 * @return the points of the player with the name given
	 * @pre: doesPlayersExist(name)
	 */
	public int getPlayersPoints(String name) {
		
		return getPlayer(name).getPoints();
	}
	
	/**
	 * @param name, the name of the player we want
	 * @return the fleet of the player with the name given
	 * @pre: doesPlayersExist(name)
	 */
	public char[][] getPlayersFleet(String name) {
		
		return getPlayer(name).getFleet();
	}
	
	/**
	 * @param name, the name of the player we want to check if is alive
	 * @return true, if the player with the name given
	 * is still playing; false, if otherwise
	 * @pre: doesPlayersExist(name)
	 */
	public boolean isPlayerStillPlaying(String name) {
		
		return getPlayer(name).stillPlaying();
	}
	
	/**
	 * @return the name of the player who won the game
	 * @pre: isGameOver()
	 */
	public String winner() {

		int winnerIdx = 0;
		while (winnerIdx < sizePlayers && !players[winnerIdx].stillPlaying())
			winnerIdx++;

		Player[] aux = copyPlayers();
		sortPlayers(aux);

		if (aux[0].getPoints() == aux[1].getPoints()) 
			return players[winnerIdx].getName();
		
		return aux[0].getName();
	}
	
	/**
	 * @param name, the name of the player whose fleet
	 * the player with the turn wanted to shoot
	 * @return true, if the shot is self-inflicted;
	 * false, if it is not
	 * @pre: name != null && !isGameOver() 
	 */
	public boolean selfInflicted(String name) {
		
		return getNextPlayerName().equals(name);
	}
	
	/**
	 * @param row, the row from the fleet matrix that got shot
	 * @param col, the column from the fleet matrix that got shot
	 * @param name, the name of the player that got shot
	 * @return true, if the shot made was inside the fleet matrix's bounds;
	 * false, if otherwise
	 * @pre: name != null && !isGameOver() && !selfInflicted(name) 
	 * && doesPlayerExist(name)
	 */
	public boolean isShotValid(int row, int col, String name) {

		char[][] fleet = getPlayersFleet(name);
		return 1 <= row && row <= fleet.length && 1 <= col && col <= fleet[0].length;
	}
	
	/**
	 * Performs a valid shot to the fleet of the player with the name given.
	 * 
	 * @param row, the row from the fleet matrix that got shot
	 * @param col, the column from the fleet matrix that got shot
	 * @param name, the name of the player that got shot
	 * @pre: name != null && !isGameOver() && !selfInflicted(name)
	 * && isShotValid(row, col, name) && doesPlayerExist(name)
	 */
	public void shoot(int row, int col, String name) {
		
		Player playerShot = getPlayer(name);
		char[][] fleet = getPlayersFleet(name);
		int points = 0;
		
		if (fleet[row - 1][col - 1] == DOWN) 
            points = playerShot.sunkenBoat(row, col) * MISS; 
           
        else if (fleet[row - 1][col - 1] != WATER) 
            points = playerShot.sinkBoat(row, col) * HIT;  
		
		Player playerWhoShot = players[getNextPlayerIdx()];
		int currentPoints = playerWhoShot.getPoints();
		currentPoints += points;
		
		playerWhoShot.updatePoints(currentPoints);
		
		if (isGameOver()) 
			duplicatePoints(playerWhoShot);
		else
			switchPlayer();
	}	
	
	/**
	 * Duplicates the points of the player given as input.
	 * 
	 * @param player, the player which points will be duplicated
	 */
	private void duplicatePoints(Player player) {
		
		player.updatePoints(player.getPoints() * DUPLICATE_FACTOR);
	}
}
