import java.util.Scanner;
import dataStructures.*;
import paintball.*;

/**
 * Main program for the Paintball game.
 * @authors:
 */
public class Main {

    // User commands
    private static final String BYE = "Bye.";
    private static final String QUIT_COMMAND = "quit";
    private static final String HELP_COMMAND = "help";
    private static final String GAME_COMMAND = "game";
    private static final String STATUS_COMMAND = "status";
    private static final String MAP_COMMAND = "map";
    private static final String BUNKERS_COMMAND = "bunkers";
    private static final String PLAYERS_COMMAND = "players";
    private static final String CREATE_COMMAND = "create";
    private static final String MOVE_COMMAND = "move";
    private static final String ATTACK_COMMAND = "attack";  
    private static final String INVALID_COMMAND = "Invalid command.";
    
    // Feedback given by the program
    private static final String GAME = "game - Create a new game\n";
    private static final String HELP = "help - Show available commands\n";
    private static final String QUIT = "quit - End program execution";
    private static final String MOVE = "move - Move a player\n";
    private static final String CREATE = "create - Create a player in a bunker\n";
    private static final String ATTACK = "attack - Attack with all players of the current team\n";
    private static final String STATUS = "status - Show the current state of the game\n";
    private static final String MAP = "map - Show the map of the current team\n";
    private static final String BUNKERS = "bunkers - List the bunkers " + 
    									   "of the current team, by the order they were seized\n";
    private static final String PLAYERS = "players - List the active players " + 
    									  "of the current team, by the order they were created\n";
    private static final String PROMPT = "> ";
    private static final String TEAM_NOT_CREATED = "Team not created.";
    private static final String BUNKER_NOT_CREATED = "Bunker not created.";
    private static final String FATAL_ERROR = "FATAL ERROR: Insufficient number of teams.";
    private static final String PLAYER_IN_POSITION = "%s player in position (%d, %d)\n";
    private static final String BUNKER_SEIZED = "Bunker seized.";
    private static final String PLAYER_ELIMINATED = "Player eliminated.";
    private static final String WINNER = "Winner is %s.\n";
    private static final String NUM_PLAYERS = "%d players:\n";
    private static final String NUM_BUNKERS = "%d bunkers:\n";
    private static final String NUM_TEAMS = "%d teams:\n";
    
    public static void main (String[] args) {
    	
    	// Command interpreter before the game is active.
		Scanner in = new Scanner(System.in);
		Game game;
		String command;
		do {
			System.out.print(PROMPT);
			command = in.next().toLowerCase();
			switch(command) {
			case GAME_COMMAND:
				game = executeGame(in);
				command = executeInGameCommands(in, game, command);
				break;
				
			case HELP_COMMAND:
				System.out.println(GAME+HELP+QUIT);
				break;
				
			case QUIT_COMMAND:
				System.out.println(BYE);
				break;
			
			default:
				in.nextLine();
				System.out.println(INVALID_COMMAND);
			}
		} while (!command.equals(QUIT_COMMAND));
		
		in.close();
	}
    
    /**
     * Command interpreter with an active game.
     * @param in Scanner object to read user input
     * @param game Game object which is going to execute actions depending on the command given
     * @param command String given by the user as input 
     * @return The command given as input
     */
    private static String executeInGameCommands(Scanner in, Game game, String command) {
    	
    	do {
			if (game.isGameInProgress()) {
				System.out.print("" + game.getCurrentTeamPlaying().getName() + PROMPT);
				command = in.next().toLowerCase();
				if (command.equals(GAME_COMMAND))
					game = executeGame(in);
				else
					executeCommand(in, command, game);	
			}
		} while (game.isGameInProgress() && !command.equals(QUIT_COMMAND));
    	
    	return command;
    }
    
    /**
     * Executes the command given.
     * @param in Scanner object to read user input
     * @param command String given by the user as input
     * @param game Game object which is going to execute actions depending on the command given
     */
    private static void executeCommand(Scanner in, String command, Game game) {

		switch(command) {

		case HELP_COMMAND:
			System.out.println(GAME+MOVE+CREATE+ATTACK+STATUS+MAP+BUNKERS+PLAYERS+HELP+QUIT);
            break;
		case STATUS_COMMAND:
            executeStatus(game);
            break;
		case MAP_COMMAND:
            executeMap(game);
            break;
		case BUNKERS_COMMAND:
            executeBunkers(game);
            break;
		case PLAYERS_COMMAND:
            executePlayers(game);
            break;
        case CREATE_COMMAND:
            executeCreate(game, in);
            break;
        case MOVE_COMMAND:
            executeMove(game, in);
            break;
        case ATTACK_COMMAND:
            executeAttack(game);
            break;
        case QUIT_COMMAND:
        	System.out.println(BYE);
			break;  
		default:
			in.nextLine();
			System.out.println(INVALID_COMMAND);
		}
	}	
    
    /**
     * Executes "status" command.
     * @param game Game object which is active
     */
    private static void executeStatus(Game game) {
    	System.out.println(game.getMapWidth() + " " + game.getMapHeight());
    	System.out.printf(NUM_BUNKERS, game.getNumberBunkers());
        Iterator<Bunker> itBunker = game.bunkerIterator();
        while(itBunker.hasNext()) {
        	Bunker bunker = itBunker.next();
        	System.out.print(bunker.getName());
        	if (bunker.hasTeam())
        		System.out.println(" (" + bunker.getTeamName() + ")");
        	else
        		System.out.println(" (without owner)");
        }
        int count = game.getNumberTeams();
        System.out.printf(NUM_TEAMS, count);
        Iterator<Team> itTeam = game.teamIterator();
        while(itTeam.hasNext()){
        	Team team = itTeam.next();
        	System.out.print(team.getName());
        	if (count > 1)
        		System.out.print("; ");     
        	count--;
        }
        System.out.println();	
    }
    
    /**
     * Executes "map" command.
     * @param game Game object which is active
     */
    private static void executeMap(Game game) {
    	
    	int numRows = game.getMapHeight(); // y
    	int numCols = game.getMapWidth(); // x
        System.out.println(numCols + " " + numRows);
        System.out.print("**");
        for (int i = 1; i <= numCols; i++) {
        	System.out.print(i);
        	if (i != numCols)
        		System.out.print(" ");
        	else
        		System.out.println();
        }
        for (int i = 0; i < numRows; i++) {
        	Iterator<MapElement> it = game.mapElementIterator(i);
        	System.out.print(i+1);
        	while(it.hasNext()) {
        		MapElement elem = it.next();
        		System.out.print(" " + game.getCorrespondingChar(elem));
        	}
        	System.out.println();
        }
    }
    
    /**
     * Executes "bunkers" command.
     * @param game Game object which is active
     */
    private static void executeBunkers(Game game) {
    	
    	int numBunkers = game.getCurrentTeamNumberOfBunkers();
        
        if (numBunkers == 0)
        	System.out.println("Without bunkers.");
        else {
        	System.out.printf(NUM_BUNKERS, numBunkers);
        	Iterator<Bunker> it = game.currentTeamBunkersIterator();
        	while(it.hasNext()) {
        		Bunker bunker = it.next();
        		System.out.println(bunker.getName() + " with " + bunker.getTreasure() +
        					   	   " coins in position (" + bunker.getX() + 
        					   	   ", " + bunker.getY() + ")");
        	}
        }
    }
    
    /**
     * Executes "players" command.
     * @param game Game object which is active
     */
    private static void executePlayers(Game game) {

    	int numPlayers = game.getCurrentTeamNumberOfPlayers();
    	if (numPlayers == 0)
        	System.out.println("Without players.");
        else {
        	System.out.printf(NUM_PLAYERS, numPlayers);
        	Iterator<Player> it = game.currentTeamPlayersIterator();
        	while(it.hasNext()) {
        		Player player = it.next();
        		System.out.printf(PLAYER_IN_POSITION, player.getPlayerType(), 
        						   player.getX(), player.getY());
        	}
        }
    }
    
    /**
     * Executes "create" command.
     * @param game Game object which is active
     */
    private static void executeCreate(Game game, Scanner in) {
    	
    	String playerType = in.next().trim();
    	String bunkerName = in.nextLine().trim();
    	
    	if(!game.isPlayerTypeValid(playerType))
    		System.out.println("Non-existent player type.");
    	else if(!game.doesBunkerExist(bunkerName))
    		System.out.println("Non-existent bunker.");
    	else if(!game.doesBunkerBelongToCurrentTeam(bunkerName))
    		System.out.println("Bunker illegally invaded.");
    	else if(game.isBunkerOccupied(bunkerName))
    		System.out.println("Bunker not free.");
    	else if(!game.hasEnoughCoins(playerType, bunkerName))
    		System.out.println("Insufficient coins for recruitment.");
    	else {
    		game.createPlayer(playerType, bunkerName);
    		System.out.println(playerType + " player created in " + bunkerName);
    	}
    	game.endOfTurn();		
    }

    /**
     * Executes "attack" command.
     * @param game Game object which is active
     */
    private static void executeAttack(Game game) {
        
    	game.attack();
    	if (game.didCurrentTeamSurvive()) 
    		executeMap(game);
    	else 
    		System.out.println("All players eliminated.");
    	game.endOfTurn();
    	if (game.hasGameEnded()) {
			System.out.printf(WINNER, game.teamWinner());
			game.endGame();
    	}	
    }
    
    /**
     * Executes "map" command.
     * @param game Game object which is active
     * @param in Scanner object to read user input
     */
    private static void executeMove(Game game, Scanner in) {
        
    	int x = in.nextInt();
    	int y = in.nextInt();
    	String[] directions = in.nextLine().trim().split(" ");
    	int numMoves = directions.length;
    	int i = 0;
    	while (i < numMoves) {
    		if (!game.isPositionValid(x,y)) {
    	    	System.out.println("Invalid position.");
    	    	i = numMoves;
    		}
    		else if (!game.isDirectionValid(directions[i])) {
    			System.out.println("Invalid direction.");
    			i++;
    		}
    		else if (!game.isPlayerOfCurrentTeamInPosition(x,y)) {
    			System.out.println("No player in that position.");
    			i = numMoves;
    		}
    		else if (game.isInvalidMove(x, y, numMoves)) {
    			System.out.println("Invalid move.");
    			i = numMoves;
    		}
    		else if (game.isMovingOffMap(x, y, directions[i])) {
    			System.out.println("Trying to move off the map.");
    			i++;
    		}
    		else if (game.isPositionOccupiedByTeammate(x, y, directions[i])) {
    			System.out.println("Position occupied.");
    			i++;
    		}
    		else {
    			int[] newPlayerPosition = game.movePlayer(x, y, directions[i]);
    			x = newPlayerPosition[0];
    			y = newPlayerPosition[1];
    			if (game.doesBunkerExistInPosition(x, y) && game.isBunkerFromOtherTeamAbandoned(x, y)) {
    				game.claimBunker(x, y);
    				System.out.println(BUNKER_SEIZED);
    				System.out.printf(PLAYER_IN_POSITION, game.getPlayerTypeFromCurrentTem(x, y), x, y);
    				i++;
    			}
    			else if (game.doesBunkerExistInPosition(x, y) && game.isBunkerFree(x, y)) {
    				game.claimFreeBunker(x, y);
    				System.out.println(BUNKER_SEIZED);
    				System.out.printf(PLAYER_IN_POSITION, game.getPlayerTypeFromCurrentTem(x, y), x, y);
    				i++;
    			}	
    			else if (game.doesBunkerExistInPosition(x, y) && game.isBunkerFromCurrentTeamInPosition(x, y)) {
    				game.reoccupyBunker(x, y);
    				System.out.printf(PLAYER_IN_POSITION, game.getPlayerTypeFromCurrentTem(x, y), x, y);
    				i++;
    			}
    			else if (game.isPlayerFromOtherTeamInPosition(x, y)) 
    				i = fightBetweenPlayers(game, x, y, i, numMoves);
    			else {
    				game.addPlayerToMap(x, y);
    				System.out.printf(PLAYER_IN_POSITION, game.getPlayerTypeFromCurrentTem(x, y), x, y);
    				i++;
    			}
    		}
    		if (game.hasGameEnded() && i == numMoves) {
    			System.out.printf(WINNER, game.teamWinner());
    			game.endGame();
    		}
		}
    	game.endOfTurn();
    }
    
    /**
     * Makes a fight between two players after a "move" command was executed.
     * @param game Game object which is the game active
     * @param x X coordinate of the players in the map
     * @param y Y coordinate of the players in the map
     * @param iterationStep If the player is red, this iterationStep could go up to 3; otherwise,
     *        it can only get to 1
     * @param numMoves The number of maximum moves a player can make (depending on player type)
     * @return the new iteration step, depending on what was accomplished by the "move" command
     */
    private static int fightBetweenPlayers(Game game, int x, int y, int iterationStep, int numMoves) {
    	game.fightMove(x, y);
		if (game.doesBunkerExistInPosition(x, y)) { 
			if(!game.isPlayerOfCurrentTeamInPosition(x, y)) {
				System.out.println(PLAYER_ELIMINATED);
				iterationStep = numMoves;
			} 
			else {
				game.claimBunker(x, y);
				System.out.println("Won the fight and bunker seized.");
				System.out.printf(PLAYER_IN_POSITION, game.getPlayerTypeFromCurrentTem(x, y), x, y);
				iterationStep++;
			}
		}
		else { 
			if(!game.isPlayerOfCurrentTeamInPosition(x, y)) {
				System.out.println(PLAYER_ELIMINATED);
				iterationStep = numMoves;
			}
			else {
				game.addPlayerToMap(x, y);
				System.out.println("Won the fight.");
				System.out.printf(PLAYER_IN_POSITION, game.getPlayerTypeFromCurrentTem(x, y), x, y);
				iterationStep++;
			}
		}
		return iterationStep;
    }
    
    /**
     * Executes "game" command.
     * @param in Scanner object to read user input
     * @return Game object which is the game created from this command
     */
    private static Game executeGame(Scanner in) {
    	
        int mapWidth = in.nextInt();
        int mapHeight = in.nextInt();
        int teamsNumber = in.nextInt();
        int bunkersNumber = in.nextInt();
        
        Game game = new GameClass(teamsNumber, bunkersNumber, mapWidth, mapHeight);
        createBunkers(in, game, bunkersNumber);
        createTeams(in, game, teamsNumber);
        return game;   
    }
    
    /**
     * Executes "bunkers" command.
     * @param in Scanner object to read user input
     * @param game Game object of the active game
     * @param bunkersNumber Number of maximum bunkers to create
     */
    private static void createBunkers(Scanner in, Game game, int bunkersNumber) {
    	
        in.nextLine();
        System.out.printf(NUM_BUNKERS, bunkersNumber); 
        for(int i = 0; i < bunkersNumber; i++){  	
            int xCoord = in.nextInt();
            int yCoord = in.nextInt();
            int treasure = in.nextInt();
            String bunkerName = in.nextLine().trim();
           
            if (game.canCreateBunker(xCoord, yCoord, treasure, bunkerName))
            	game.createBunker(xCoord, yCoord, treasure, bunkerName);
            else
            	System.out.println(BUNKER_NOT_CREATED);
        }
    }
    
    /**
     * Executes "teams" command.
     * @param in Scanner object to read user input
     * @param game Game object of the active game
     * @param teamsNumber Number of maximum teams to create
     */
    private static void createTeams(Scanner in, Game game, int teamsNumber) {
    	
    	 System.out.printf(NUM_TEAMS, teamsNumber);
         for (int j = 0; j < teamsNumber; j++) {
         	String teamName = in.next();
         	String bunkerName = in.nextLine().trim();
         	if (game.canCreateTeam(teamName, bunkerName))
         		game.createTeam(teamName, bunkerName);
         	else
             	System.out.println(TEAM_NOT_CREATED);	
         }
        
         if (!game.hasEnoughTeams()) {       
         	game.endGame();					    
         	System.out.println(FATAL_ERROR);    
         }
    }
}
