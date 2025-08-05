package paintball;
import dataStructures.*;

/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * This is the top class, which communicates directly with Main, 
 * working as an intermediate between Main and all the other classes 
 * and packages. 
 */
public class GameClass implements Game {
	
	private Array<Bunker> bunkers; // Bunkers in the game
	private Array<Team> teams; // Teams in the game
	private Team currentTeam; // Team that has the turn (next team to play)
	private boolean gameInProgress; // Stores if the game is still running
	private Map map; // Map object, representing the map of the game
	
	/**
	 * Constructor of the GameClass.
	 * @param teamsNumber Number of initial teams
	 * @param bunkersNumber Number of initial bunkers
	 * @param mapWidth Map's maximum X coordinate
	 * @param mapHeight Map's maximum Y coordinate
	 */
	public GameClass(int teamsNumber, int bunkersNumber, int mapWidth, int mapHeight) {
		
		teams = new ArrayClass<Team>();
		bunkers = new ArrayClass<Bunker>();
		gameInProgress = true;
		map = new MapClass(mapWidth, mapHeight);
	}
		
    @Override
    public boolean isGameInProgress() {
    	return gameInProgress;
    }
    
    @Override
    public void endGame() {
    	gameInProgress = false;
    }
	
	@Override
	public void createBunker(int x, int y, int treasure, String name) {
		
		Bunker bunker = new BunkerClass(x, y, treasure, name);
		bunkers.insertLast(bunker);
		map.addElement(bunker, x, y);		
	}
	
	@Override
	public boolean canCreateBunker(int x, int y, int treasure, String name) {
		
		return isPositionValid(x, y) && treasure >= MIN_TREASURE && 
			   !doesBunkerExist(name) && !map.isPositionOccupied(x, y);
	}
	
	@Override
	public boolean isPositionValid(int x, int y) {
		
		return x >= MAP_XMIN && y >= MAP_YMIN && x <= map.mapWidth() && y <= map.mapHeight();
	}

	@Override
	public void createTeam(String teamName, String bunkerName) {
		
		Bunker bunker = getBunkerByName(bunkerName);
		Team team = new TeamClass(teamName, bunker);
		if (teams.size() == 0)
			currentTeam = team; 
		teams.insertLast(team);	
		bunker.assignTeam(team);
	}
	
	@Override
	public boolean canCreateTeam(String teamName, String bunkerName) {
		
		return !doesTeamExist(teamName) && doesBunkerExist(bunkerName) && 
			   !isBunkerClaimed(bunkerName);
	}
	
	@Override
	public Bunker getBunkerByName(String name) {
		
		return bunkers.get(searchBunkerIdxByName(name));
	}
	
	@Override
	public Team getCurrentTeamPlaying() {
		
		return currentTeam; 
	}
	
	@Override
	public boolean doesTeamExist(String name) {
	
		int i = 0;
		while (i < teams.size() && !name.equals(teams.get(i).getName()))
			i++;
		return i < teams.size();
	}
	
	/**
	 * Searches a bunker by its name, providing its index in the bunkers Array object.
	 * @param name Name of the bunker
	 * @return NOT_FOUND (-1) if the bunker does not exist in the game;
	 * 		   otherwise, returns its index
	 */
	private int searchBunkerIdxByName(String name) {

		int i = 0;
		while (i < bunkers.size() && !name.equals(bunkers.get(i).getName()))
			i++;
		if (i < bunkers.size())
			return i;
		else 
			return NOT_FOUND;
	}

	@Override
	public boolean doesBunkerExist(String name) {
		
		return searchBunkerIdxByName(name) != NOT_FOUND;
	}

	@Override
	public boolean isBunkerClaimed(String name) {
		
		return getBunkerByName(name).hasTeam();
	}

	@Override
	public boolean isBunkerOccupied(String name) {
		
		return getBunkerByName(name).isOccupied();
	}

	@Override
	public boolean hasEnoughTeams() {
		
		return teams.size() >= MIN_NUM_TEAMS;
	}

	@Override
	public int getMapHeight() {

		return map.mapHeight();
	}

	@Override
	public int getMapWidth() {
		
		return map.mapWidth();
	}

	@Override
	public Iterator<Bunker> bunkerIterator() {
		
		return bunkers.iterator();
	}
	
	@Override
	public Iterator<Bunker> currentTeamBunkersIterator() {
		
		return currentTeam.getBunkers();
	}
	
	@Override
	public Iterator<Player> currentTeamPlayersIterator() {
		
		return currentTeam.getPlayers();
	}

	@Override
	public Iterator<Team> teamIterator() {
		
		return teams.iterator();
	}
	
	@Override
	public Iterator<MapElement> mapElementIterator(int row) {
		
		return new ArrayIteratorClass<MapElement>(map.getMapRow(row), getMapWidth());
	}

	@Override
	public char getCorrespondingChar(MapElement elem) {
		
		if (elem == null || elem.getTeam() == null || !elem.getTeam().equals(currentTeam))
			return EMPTY_POS;
		
		if (elem instanceof Bunker) {
			Bunker bunker = (Bunker) elem;
			if(bunker.isOccupied())
				return OCCUPIED_BUNKER;
			
			return FREE_BUNKER;
		}
		return PLAYER_IN_POS;
	}

	@Override
	public int getNumberTeams() {
		
		return teams.size();
	}

	@Override
	public int getNumberBunkers() {
		
		return bunkers.size();
	}

	@Override
	public boolean isPlayerTypeValid(String type) {
		
		return type.equals(GREEN) || type.equals(BLUE) || type.equals(RED);
	}
	
	@Override
	public boolean hasEnoughCoins(String playerType, String bunkerName) {
		
		Bunker bunker = getBunkerByName(bunkerName);
		int cost = 0;
		switch (playerType) {
			case BLUE -> cost = BLUE_CREATION_COST;
			case RED -> cost = RED_CREATION_COST;
			case GREEN -> cost = GREEN_CREATION_COST; 
		}
		return bunker.getTreasure() >= cost;
	}

	@Override
	public void endOfTurn() {
		
		switchToNextTeamPlaying();
		generateCoins();
	}
	
	/**
	 * Switches the current team playing.
	 */
	private void switchToNextTeamPlaying() {
		
		int count = teams.size();
		int currentIndex = teams.searchIndexOf(currentTeam);
		if (currentIndex == count - 1) 
			currentTeam = teams.get(0); 
		else 
			currentTeam = teams.get(currentIndex + 1);
	}
	
	/**
	 * Adds a coin to every bunker in the game.
	 */
	private void generateCoins() {
		
		for (int i = 0; i < bunkers.size(); i++)
			bunkers.get(i).addCoin();
	}

	@Override
	public void createPlayer(String playerType, String bunkerName) {
		
		Bunker bunker = getBunkerByName(bunkerName);
		Player player = null;
		int creationCost = 0;
		
		switch (playerType) {
			case BLUE:
				player = new PlayerBlueClass(bunker.getX(), bunker.getY(), currentTeam);
				creationCost = BLUE_CREATION_COST;
				break;
			case RED:
				player = new PlayerRedClass(bunker.getX(), bunker.getY(), currentTeam);
				creationCost = RED_CREATION_COST;
				break;
			case GREEN:
				player = new PlayerGreenClass(bunker.getX(), bunker.getY(), currentTeam);
				creationCost = GREEN_CREATION_COST;
		}
		bunker.payCreationCost(creationCost);
		currentTeam.createPlayer(player, bunker);
	}

	@Override
	public int getCurrentTeamNumberOfPlayers() {
		return currentTeam.getNumPlayers();
	}
	
	@Override
	public boolean isDirectionValid(String direction) {
		
		return direction.equals(NORTH) || direction.equals(SOUTH) ||
			   direction.equals(WEST) || direction.equals(EAST);
	}

	@Override
	public boolean isPlayerOfCurrentTeamInPosition(int x, int y) {
		
		return currentTeam.isPlayerInPosition(x,y);
	}

	@Override
	public boolean isInvalidMove(int x, int y, int numMoves) {
	
		return !isPlayerRed(x,y) && numMoves == RED_MOVES;
	}
	
	/**
	 * Checks if the player with the position given is a RedClassObject or not
	 * (if it is a red player type or not).
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return True, if the player is red; False, otherwise
	 */
	private boolean isPlayerRed(int x, int y) {
		
		return currentTeam.playerInPosition(x,y) instanceof PlayerRedClass;
	}
	
	@Override
	public boolean isMovingOffMap(int x, int y, String direction) {
		
		int[] newPlayerPosition = simulateMovingToPosition(x, y, direction);
		return !isPositionValid(newPlayerPosition[0], newPlayerPosition[1]);
	}

	@Override
	public boolean isPositionOccupiedByTeammate(int x, int y, String direction) {
		
		int[] newPlayerPosition = simulateMovingToPosition(x, y, direction);
		return isPlayerOfCurrentTeamInPosition(newPlayerPosition[0], newPlayerPosition[1]);
	}

	@Override
	public int[] movePlayer(int x, int y, String direction) {
		
		Player player = currentTeam.playerInPosition(x, y); 
		int[] newPlayerPosition = simulateMovingToPosition(x, y, direction);
		int newX = newPlayerPosition[0]; 
		int newY = newPlayerPosition[1]; 
		
		if (isBunkerFromCurrentTeamInPosition(x, y)) { 
			Bunker bunker = currentTeam.bunkerInPosition(x, y); 
			bunker.clearBunker();
		} 
		else
			map.removeElement(x, y);

		player.changeX(newX);
		player.changeY(newY);
		
		return new int[] {newX, newY};
	}
	
	/**
	 * Performs an attack at the position (x, y) from the player
	 * with the position (initialX, initialY).
	 * @param x Position of attack in the X axis of the map
	 * @param y Position of attack in the Y axis of the map
	 * @param player Player object of the player attacking
	 * @param initialX Where in the X axis the player who attacks is
	 * @param initialY Where in the Y axis the player who attacks is
	 */
	private void attackInPosition(int x, int y, Player player, int initialX, int initialY) {
		
		Bunker bunker = null;
		if (doesBunkerExistInPosition(x, y) && isBunkerFromOtherTeamAbandoned(x, y)) {
			bunker = getBunkerInPosition(x,y);
			claimBunkerForCurrentTeam(bunker);
		}
		else if (doesBunkerExistInPosition(x, y) && isBunkerFree(x, y)) {
			bunker = getBunkerInPosition(x,y);
			claimFreeBunkerForCurrentTeam(bunker);
		}
		else if (isPlayerFromOtherTeamInPosition(x, y)) {
			fightAttack(x, y, player);
			aftermathOfAttack(x, y, player, bunker, initialX, initialY);
		}
		if (hasGameEnded()) 
			endGame();
	}
	
	private void aftermathOfAttack(int x, int y, Player player, Bunker bunker, 
								   int initialX, int initialY) {
		
		// checks if the player who attacked died or not during the attack
		if (isPlayerOfCurrentTeamInPosition(initialX, initialY)) {
			if (doesBunkerExistInPosition(x, y)) { 
				bunker = getBunkerInPosition(x,y);
				claimBunkerForCurrentTeam(bunker);	
			}
			else
				map.removeElement(x, y);	
		}
		else {
			if (isBunkerFromCurrentTeamInPosition(initialX, initialY)) {
				bunker = currentTeam.bunkerInPosition(initialX, initialY);
				bunker.clearBunker();
			}
			else 
				map.removeElement(initialX,  initialY);
		}
	}
	
	private int[] simulateMovingToPosition(int x, int y, String direction) {
		
		switch (direction) {
			case NORTH -> y--; 	
			case SOUTH -> y++;	
			case WEST -> x--;	
			case EAST -> x++;
		}
		return new int[] {x, y};
	}

	@Override
	public String getPlayerTypeFromCurrentTem(int x, int y) {
		
		return playerFromCurrentTeamInPosition(x, y).getPlayerType();
	}
	
	private Player playerFromCurrentTeamInPosition(int x, int y) {
		
		return currentTeam.playerInPosition(x, y);
	}

	@Override
	public void addPlayerToMap(int x, int y) {
		
		map.addElement(playerFromCurrentTeamInPosition(x,y), x, y); 
	}
	
	@Override
	public void fightMove(int x, int y) {
		
		Player currentTeamPlayer = playerFromCurrentTeamInPosition(x,y);
		fight(x, y, currentTeamPlayer);
	}
	
	private void fightAttack(int x, int y, Player player) {
		
		fight(x, y, player);
	}
	
	private void fight(int x, int y, Player currentTeamPlayer) {
		
		Player otherTeamPlayer = findPlayerFromOtherTeam(x,y);
		Player winningPlayer = winnerFromFight(currentTeamPlayer, otherTeamPlayer);
		
		if (winningPlayer.equals(currentTeamPlayer)) {
			Team otherTeam = teamWithPlayer(otherTeamPlayer);
			otherTeam.removePlayer(otherTeamPlayer);
			updateActiveTeams(otherTeam);
		}
		else {
			currentTeam.removePlayer(currentTeamPlayer);
			updateActiveTeams(currentTeam);
		}
	}
	
	private Team teamWithPlayer(Player player) {
		
		return player.getTeam();	
	}
	
	private Player findPlayerFromOtherTeam(int x, int y) {
		
		int i = 0;
		Player player = null;
		boolean found = false;
		while (i < teams.size() && !found) {
			Team team = teams.get(i);
			if (team.isPlayerInPosition(x, y) && !team.equals(currentTeam)) {
				found = true;
				player = team.playerInPosition(x,y);
			}
			i++;
		}
		return player;
	}
	
	@Override
	public boolean isPlayerFromOtherTeamInPosition(int x, int y) {
		
		return findPlayerFromOtherTeam(x, y) != null;
	}

	private Player winnerFromFight(Player player1, Player player2) {
		
		if (player1 instanceof PlayerRedClass && !(player2 instanceof PlayerGreenClass) ||
			player1 instanceof PlayerBlueClass && !(player2 instanceof PlayerRedClass) ||
			player1 instanceof PlayerGreenClass && !(player2 instanceof PlayerBlueClass))
			return player1;
		
		return player2;
	}
	
	@Override
	public boolean hasGameEnded() {
		
		return teams.size() < MIN_NUM_TEAMS;
	}

	private void updateActiveTeams(Team team) {
		
		int i = teams.searchIndexOf(team);
		if (team.getNumBunkers() == 0 && team.getNumPlayers() == 0)
			teams.removeAt(i);
	}
	
	@Override
	public boolean didCurrentTeamSurvive() {
		
		return teams.searchIndexOf(currentTeam) != NOT_FOUND;
	}
	
	@Override
	public String teamWinner() {
		
		return teams.get(0).getName();
	}

	@Override
	public void attack() {
		
		Player[] currentTeamPlayers = new Player[currentTeam.getNumPlayers()];
		for (int i = 0; i < currentTeam.getNumPlayers(); i++) {
			currentTeamPlayers[i] = currentTeam.getPlayerByIdx(i);
		}
		int i = 0;
		while(i < currentTeam.getNumPlayers() && !hasGameEnded()) {
			Player player = currentTeamPlayers[i];
			int initialX = player.getX();
			int initialY = player.getY();
			if (player instanceof PlayerRedClass)
				redAttack((PlayerRedClass)player, initialX, initialY);
			else if (player instanceof PlayerBlueClass)
				blueAttack((PlayerBlueClass)player, initialX, initialY);
			else if (player instanceof PlayerGreenClass)
				greenAttack((PlayerGreenClass)player, initialX, initialY);
			i++;
		}	
	}
	
	private void redAttack(PlayerRedClass player, int initialX, int initialY) {
		
		// attack stops if the player is eliminated or if he surpasses map height
		int x = initialX;
		int y = initialY;
		while (y <= getMapHeight() && isPlayerOfCurrentTeamInPosition(initialX, initialY)) {
			if (x > getMapWidth()) {
				y++;
				if (y == initialY)
					x = initialX + MOVE_EAST;
				else
					x = initialX;
			}
			attackInPosition(x, y, player, initialX, initialY);
			x += MOVE_EAST;			
		}
	}
	
	private void blueAttack(PlayerBlueClass player, int initialX, int initialY) {
		// attack stops if the player is eliminated or if he surpasses the limits
		// of the map twice in a row
		int factor = 1; 
		int iteration = 1;
		int limit = 0;
		while (limit < MAX_OFF_LIMIT_BLUE_MOVES && isPlayerOfCurrentTeamInPosition(initialX, initialY)) {
			int x = initialX; // goes back to original position in each iteration
			int y = initialY;
			if (iteration > MAX_OFF_LIMIT_BLUE_MOVES) {
				iteration = 1;
				factor++; 
			}
			int moveX = player.typeOfMovement(iteration);
			x += factor * moveX;
			
			if (isPositionValid(x, y)) {
				attackInPosition(x, y, player, initialX, initialY);
				limit = 0;
			}
			else 
				limit++;
			iteration++;
		}
	}
	
	private void greenAttack(PlayerGreenClass player, int initialX, int initialY) {
		// attack ends if the player is eliminated or if he surpasses the limits
		// of the map four times in a row
		int factor = 1;
		int iteration = 1;
		int limit = 0;
		while (limit < MAX_OFF_LIMIT_GREEN_MOVES && isPlayerOfCurrentTeamInPosition(initialX, initialY)) {
			int x = initialX; // goes back to original position in each iteration
			int y = initialY;
			if (iteration > MAX_OFF_LIMIT_GREEN_MOVES) {
				iteration = 1;
				factor++;
			}
			int[] directions = player.typeOfMovement(iteration);
			int moveX = directions[0];
			int moveY = directions[1];
			x += factor * moveX;
			y += factor * moveY;
			if (isPositionValid(x, y)) {
				attackInPosition(x, y, player, initialX, initialY);
				limit = 0;
			}
			else 
				limit++;
			iteration++;
		}
	}
	
	private boolean isBunkerFromOtherTeam(int x, int y) {
		
		Bunker bunker = getBunkerInPosition(x, y);
		return teamWithBunker(bunker) != null && !currentTeam.equals(teamWithBunker(bunker));
	}
	
	@Override
	public boolean isBunkerFromOtherTeamAbandoned(int x, int y) {
		
		Bunker bunker = getBunkerInPosition(x, y); 
		return !bunker.isOccupied() && isBunkerFromOtherTeam(x,y);
	}
	
	@Override
	public boolean isBunkerFree(int x, int y) {
		
		Bunker bunker = getBunkerInPosition(x, y);
		return !bunker.hasTeam();
	}
	
	@Override
	public boolean doesBunkerExistInPosition(int x, int y) {
		
		return getBunkerInPosition(x,y) != null;
	}
	
	@Override
	public void claimBunker(int x, int y) {
		
		Bunker bunker = getBunkerInPosition(x,y); 
		claimBunkerForCurrentTeam(bunker);
		bunker.occupyBunker(playerFromCurrentTeamInPosition(x,y));
	}
	
	@Override
	public void claimFreeBunker(int x, int y) {
		
		Bunker bunker = getBunkerInPosition(x,y);
		claimFreeBunkerForCurrentTeam(bunker);	
		bunker.occupyBunker(playerFromCurrentTeamInPosition(x,y)); 
	}
	
	private void claimBunkerForCurrentTeam(Bunker bunker) {
		
		Team otherTeam = teamWithBunker(bunker);
		bunker.clearBunker();
		otherTeam.removeBunker(bunker);
		updateActiveTeams(otherTeam);
		
		currentTeam.addBunker(bunker);
		bunker.assignTeam(currentTeam);
	}
	
	private void claimFreeBunkerForCurrentTeam(Bunker bunker) {
		
		currentTeam.addBunker(bunker);
		bunker.assignTeam(currentTeam);
	}
	
	private Bunker getBunkerInPosition(int x, int y) {
		
		Bunker bunker = null;
		int i = 0;
		while (i < bunkers.size() && bunker == null) {
			Bunker tempBunker = bunkers.get(i);
			if (tempBunker.getX() == x && tempBunker.getY() == y) 
				bunker = tempBunker;
			i++;	
		}
		return bunker;
	}
	
	//@pre: doesBunkerExistCoord(x,y) && isBunkerFree(x,y)
	private Team teamWithBunker(Bunker bunker) {
		
		return bunker.getTeam();	
	}
	
	@Override
	public boolean isBunkerFromCurrentTeamInPosition(int x, int y) {
		
		return currentTeam.isBunkerInPosition(x,y);
	}
	
	@Override
	public int getCurrentTeamNumberOfBunkers() {
		
		return currentTeam.getNumBunkers();
	}
	
	@Override
	public boolean doesBunkerBelongToCurrentTeam(String name) {
		
		return currentTeam.doesBunkerExist(name);
	}
	
	@Override
	public void reoccupyBunker(int x, int y) {
		
		Bunker bunker = getBunkerInPosition(x,y);
		bunker.occupyBunker(playerFromCurrentTeamInPosition(x,y));
	}
}
