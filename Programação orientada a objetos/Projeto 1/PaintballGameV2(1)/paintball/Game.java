package paintball;
import dataStructures.*;

/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * Top class' interface of the game
 */

public interface Game {
	
	// Constants
	static final int NOT_FOUND = -1;
	static final int MIN_NUM_TEAMS = 2;
	static final int MAP_XMIN = 1;  
	static final int MAP_YMIN = 1;  
	static final int MIN_TREASURE = 1;
	
	// Constants for representing map elements
	static final char EMPTY_POS = '.';
	static final char FREE_BUNKER = 'B';
	static final char OCCUPIED_BUNKER = 'O';
	static final char PLAYER_IN_POS = 'P';
	
	// Constants for "Types of players"
	static final String GREEN = "green";
	static final String RED = "red";
	static final String BLUE = "blue";
	static final int BLUE_GREEN_MOVES = 1;
	static final int RED_MOVES = 3;
	static final int RED_CREATION_COST = 4;
	static final int GREEN_CREATION_COST = 2;
	static final int BLUE_CREATION_COST = 2;
	static final int MAX_OFF_LIMIT_GREEN_MOVES = 4;
	static final int MAX_OFF_LIMIT_BLUE_MOVES = 2;
	
	// Constants for directions
	static final String SOUTH = "south";
	static final String NORTH = "north";
	static final String WEST = "west";
	static final String EAST = "east";
	static final int MOVE_SOUTH = 1;
	static final int MOVE_NORTH = -1;
	static final int MOVE_WEST = -1;
	static final int MOVE_EAST = 1;
	
	/**
	 * Checks if the game running has ended or not.
	 * @return true if the game is in progress (has not ended),
	 * 		   false otherwise
	 */
    boolean isGameInProgress();
    
    /**
     * Creates a new bunker.
     * @param x X coordinate of the bunker in the map
     * @param y Y coordinate of the bunker in the map
     * @param treasure Number of coins the bunker has
     * @param name Name of the bunker
     * @pre canCreateBunker(x, y, treasure, name)
     */
    void createBunker(int x, int y, int treasure, String name);
    
    /**
     * Checks if the bunker with the parameters given can be created.
     * @param x X coordinate of the bunker in the map
     * @param y Y coordinate of the bunker in the map
     * @param treasure Number of coins the bunker has
     * @param name Name of the bunker
     * @return True, if the bunker can be created; False, otherwise
     */
    boolean canCreateBunker(int x, int y, int treasure, String name); 
    
    /**
     * Checks if the team with the parameters given can be created.
     * @param teamName Name of the team
     * @param bunkerName Name of the team's bunker
     * @return True, if the team can be created; False, otherwise
     */
    boolean canCreateTeam(String teamName, String bunkerName);
    	
    /**
     * Creates a new team.
     * @param teamName Name of the new team
     * @param bunkerName Bunker assigned to the new team
     */
    void createTeam(String teamName, String bunkerName);
    
    /**
     * Search for a bunker by its name.
     * @param name Name of the bunker
     * @return Bunker object, which is the bunker with the name given
     * @pre: doesBunkerExist(name)
     */
    Bunker getBunkerByName(String name);
    
    /**
     * Get the team currently playing
     * @return Team object, which is the current team
     */
    Team getCurrentTeamPlaying();
    
    /**
     * Checks if a team with the name given exists.
     * @param name Name of the team
     * @return True, if the team exists; False, otherwise
     */
    boolean doesTeamExist(String name);
    
    /**
     * Checks if a bunker with the name given exists.
     * @param name Name of the bunker
     * @return True, if the bunker exists; False, otherwise
     */
    boolean doesBunkerExist(String name);
    
    /**
     * Checks if the bunker with the name given has a team (owner).
     * @param name Name of the bunker
     * @return True, if the bunker has an owner; False, otherwise
     * @pre: doesBunkerExist(name)
     */
    boolean isBunkerClaimed(String name);
    
    /**
     * Gets the number of teams alive.
     * @return The number of teams
     */
    int getNumberTeams();
    
    /**
     * Gets the number of total bunkers in game.
     * @return The number of bunkers
     */
    int getNumberBunkers();
    
    /**
     * Ends the game.
     */
    void endGame();
    
    /**
     * Checks if there are enough teams to start the game.
     * @return True, if there are enough teams to start; False, otherwise
     */
    boolean hasEnoughTeams();
    
    /**
     * Gets the maximum value an element of the map can take in the Y axis
     * @return the maximum value in the Y axis
     */
    int getMapHeight();
    
    /**
     * Gets the maximum value an element of the map can take in the Y axis
     * @return the maximum value in the Y axis
     */
    int getMapWidth();
    
    /**
     * Starts an iterator of all bunkers in the game.
     * @return The iterator of bunkers
     */
    Iterator<Bunker> bunkerIterator();
    
    /**
     * Starts an iterator of the teams in the game.
     * @return The iterator of teams
     */
    Iterator<Team> teamIterator();
    
    /**
     * Starts an iterator of only the current team's bunkers.
     * @return Iterator object, which is the iterator of the current team's bunkers
     */
    Iterator<Bunker> currentTeamBunkersIterator();
    
    /**
     * Starts an iterator of the current team's players.
     * @return Iterator object, which is the iterator of the current team's players
     */
    Iterator<Player> currentTeamPlayersIterator();
    
    /**
     * Starts an iterator of the elements in a row of the map.
     * @param row The row which is going to be iterate through
     * @return Iterator object, which is the iterator of the map's row given as input
     */
	Iterator<MapElement> mapElementIterator(int row);
	
	/**
	 * Gets the character corresponding to the map's element given as input.
	 * @param elem MapElement object, which can be an occupied bunker, empty bunker, player or null
	 * @return A char corresponding to the element given
	 */
	char getCorrespondingChar(MapElement elem);
	
	/**
	 * Checks if the String given is one of the players types of the game.
	 * @param type String that is going to be checked has a player type
	 * @return True, if the String given is a type of player; False, otherwise
	 */
	boolean isPlayerTypeValid(String type);
	
	/**
	 * Checks if the bunker given by the name below belongs to the current team.
	 * @param name String representing the name of the bunker
	 * @return True, if the name given is a current team's bunker; False, otherwise
	 */
	boolean doesBunkerBelongToCurrentTeam(String name);
	
	/**
	 * Checks if the bunker given by name has enough coins to create a player with the player type given.
	 * @param playerType String that represents a player type
	 * @param bunkerName String with the name of the bunker
	 * @return True, if it has enough coins to create the player; False, otherwise
	 * @pre: doesBunkerExist(name) 
	 */
	boolean hasEnoughCoins(String playerType, String bunkerName);
	
	/**
	 * Switches the current team playing
	 */
	void endOfTurn();
	
	/**
	 * Checks if the bunker has a player inside.
	 * @param bunkerName String with the name of the bunker
	 * @return True, if the bunker has a player inside; False, otherwise
	 * @pre: doesBunkerExist(bunkerName)
	 */
	boolean isBunkerOccupied(String bunkerName);
	
	/**
	 * Creates a player.
	 * @param playerType String with the player type of the player to be created
	 * @param bunkerName String with the name of the bunker where the player will be created
	 * @pre: doesBunkerExist(bunkerNname) && isPlayerTypeValid(playerType)
	 */
	void createPlayer(String playerType, String bunkerName);
	
	/**
	 * Gets the number of players in the current team.
	 * @return The number of players in current team
	 */
	int getCurrentTeamNumberOfPlayers();

	/**
	 * Gets the number of bunkers of the current team.
	 * @return The number of bunkers of current team
	 */
	int getCurrentTeamNumberOfBunkers();
	
	/**
	 * Checks if the coordinates given are inside the map's limits.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return True, if the position is inside the map; False, otherwise
	 */
	boolean isPositionValid(int x, int y);
	
	/**
	 * Checks if there is a player of the current team in the position given.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return True, if there is a current team player in the position given; False, otherwise
	 * @pre: isPositionValid(x,y)
	 */
	boolean isPlayerOfCurrentTeamInPosition(int x, int y);
	
	/**
	 * Checks if the player in the position tried to do more moves than what is supposed to.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return True, if the player is not red and tried to do 3 moves
	 * @pre: isPositionValid(x,y)
	 */
	boolean isInvalidMove(int x, int y, int numMoves);
	
	/**
	 * Checks if the String given is a valid direction of the game.
	 * @param direction String which represents a direction of movement
	 * @return True, if the direction is valid; False, otherwise
	 */
	boolean isDirectionValid(String direction);
	
	/**
	 * Checks if the player in the position given is trying to move off the map.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @param direction String that represents the direction of the move
	 * @return True, if the player would move off the map; False, otherwise
	 * @pre: isDirectionValid(direction)
	 */
	boolean isMovingOffMap(int x, int y, String direction);
	

	/**
	 * Simulates a move with the direction given and
	 * checks if there is another player in the current team in the position simulated.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @param direction String that represents the direction of the move
	 * @return True, if the position is occupied by a teammate; False, otherwise
	 * @pre: isDirectionValid(direction) && isPositionValid(x,y)
	 */
	boolean isPositionOccupiedByTeammate(int x, int y, String direction);
	
	/**
	 * Changes the position of a player from the current team in the position given.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @param direction String that represents the direction of the move
	 * @return Player's new coordinates
	 * @pre: isDirectionValid(direction) && isPlayerFromCurrentTeamAlive(x, y) &&
	 * 		 isPositionValid(x,y)
	 */
	int[] movePlayer(int x, int y, String direction);
	
	/**
	 * Gets the player's type of the player in the position given.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return String that is the type of the player given by coordinates
	 * @pre: isPlayerFromCurrentTeamAlive(x, y) && isPositionValid(x,y)
	 */
	String getPlayerTypeFromCurrentTem(int x, int y);
	
	/**
	 * Checks if the bunker given by its position belongs
	 * to another team and if it is not occupied.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return True, if the bunker is abandoned and belong to another team; False, otherwise
	 * @pre: doesBunkerExist() && isPositionValid(x,y)
	 */
	boolean isBunkerFromOtherTeamAbandoned(int x, int y);
	
	/**
	 * Checks if the bunker in position given exists.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return True, if the bunker in the position exists; False, otherwise
	 * @pre: isPositionValid(x,y)
	 */
	boolean doesBunkerExistInPosition(int x, int y);
	
	/**
	 * Claims the bunker from another team, occupying it with a player.
	 * @param x  Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @pre: isPositionValid(x,y) && doesBunkerExistInPosition(x, y) &&
	 * 	     isPlayerOfCurrentTeamInPosition(x, y)
	 */
	void claimBunker(int x, int y);
	
	/**
	 * Claims a bunker without a team, occupying it with a player.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @pre: isPositionValid(x,y) && doesBunkerExistInPosition(x, y) &&
	 * 	     isPlayerOfCurrentTeamInPosition(x, y)
	 */
	void claimFreeBunker(int x, int y);
	
	/**
	 * Checks if the bunker in the position given is without a team (owner).
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return True, if the bunker is free; False, otherwise
	 * @pre: isPositionValid(x,y) && doesBunkerExistInPosition(x, y)
	 */
	boolean isBunkerFree(int x, int y);
	
	/**
	 * Adds a player with the position given to the map matrix. 
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @pre: isPositionValid(x,y)
	 */
	void addPlayerToMap(int x, int y); 
	
	/**
	 * Checks if the current team has a bunker in the position given.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return True, if the bunker from current team is in the position; False; otherwise
	 * @pre: isPositionValid(x,y)
	 */
	boolean isBunkerFromCurrentTeamInPosition(int x, int y);
	
	/**
	 * Makes a player occupy a bunker from the current team, without players inside.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @pre: isPositionValid(x,y)
	 */
	void reoccupyBunker(int x, int y);
	
	/**
	 * Performs a fight that originated from a "move" command, between two players in 
	 * the same position, given as input
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @pre: isPlayerFromCurrentTeamAlive(x,y) && isPlayerFromOtherTeamInPosition(x,y) &&
	 * 		 isPositionValid(x,y)
	 */
	void fightMove(int x, int y);
	
	/**
	 * Checks if there is any player from other teams in the position given.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return True, if there is a player from other teams in that position; False, otherwise
	 * @pre: isPositionValid(x,y)
	 */
	boolean isPlayerFromOtherTeamInPosition(int x, int y);
	
	/**
	 * Checks if the game has ended.
	 * @return True, if it has ended; False, otherwise
	 */
	boolean hasGameEnded();
	
	/**
	 * Gets the name of the winning team.
	 * @return String which is the name of the team
	 * @pre: hasGameEnded()
	 */
	String teamWinner();
	
	/**
	 * Performs the actions of the command attack.
	 */
	void attack();
	
	/**
	 * Checks if the current team is still in the game.
	 * @return True, if the team is still alive; False, otherwise
	 */
	boolean didCurrentTeamSurvive();

}