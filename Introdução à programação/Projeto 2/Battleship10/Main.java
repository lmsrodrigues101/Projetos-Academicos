import java.util.Scanner;
import java.io.*;

/** 
 * @authors Gustavo Sousa, Leandro Rodrigues
 * This Main class' goal is to interact directly with the class 
 * Game (which is the interface of the game). It will take
 * some inputs from the users to create and initialize the game, getting
 * also some commands from the user to check / change the state of 
 * the game. Also, certain commands and situations will trigger some 
 * outputs to the screen with information of the current game. 
 */
public class Main {
	
	// Constants
	private static final String QUIT = "quit";
	private static final String PLAYER = "player";
	private static final String SCORE = "score";
	private static final String FLEET = "fleet";
	private static final String CLASSIFICATION = "scores";
	private static final String IN_GAME = "players";
	private static final String SHOOT = "shoot";
	private static final String INVALID_COMMAND = "Invalid command";
	private static final String FILE = "fleets.txt";
	private static final String INVALID_PLAYER = "Nonexistent player";
	private static final String SCORE_PLAYER = "%s has %d points\n";
	private static final String GAME_IS_OVER = "The game is over";
	
	// Methods
	public static void main(String[] args) throws FileNotFoundException {

		Scanner in = new Scanner(System.in);
		Game battleship = createGame(in);
		String command;
		do {
            command = in.next();
            executeCommand(in, battleship, command);
        } while (!command.equals(QUIT));
		in.close();
	}
	
	/**
	 * Creates the game and adds to it players with its corresponding 
	 * names and fleets.
	 * 
	 * @param in, which is the Scanner created to read the inputs from the keyboard
	 * @throws FileNotFoundException
	 */
	private static Game createGame(Scanner in) throws FileNotFoundException {
		
		Scanner inReader = new Scanner(new FileReader(FILE));
		
		int numPlayers = in.nextInt();
		Game battleship = new Game(numPlayers);
		
		in.nextLine();
		String playerName = in.nextLine().trim();
		int fleetNum = in.nextInt();
		char[][] lastMatrix = createFirstPlayer(inReader, battleship, 
				fleetNum, playerName);
		int lastFleetNum = fleetNum;
		
		for (int i = 1; i < numPlayers; i++) {
			in.nextLine();
			playerName = in.nextLine().trim();
			fleetNum = in.nextInt();
			lastMatrix = createOtherPlayers(inReader, battleship, fleetNum, 
					playerName, lastFleetNum, lastMatrix);
			lastFleetNum = fleetNum;
		}
		inReader.close();
		return battleship;
	}
	
	/**
	 * Creates the first player given in the standard input.
	 * 
	 * @param inReader, the stream created using the FileReader class
	 * @param battleship, represents the instance of the Game class
	 * @param fleetNum, is the number of the fleet we want to associate
	 *        to the player
	 * @param playerName, the name of the player
	 * @return the player's fleet
	 * @throws FileNotFoundException
	 */
	private static char[][] createFirstPlayer(Scanner inReader, Game battleship,
			int fleetNum, String playerName) throws FileNotFoundException {
		
		int skipMatrixes = fleetNum - 1;
		skip(skipMatrixes, inReader); 
		char[][] fleet = getFleet(inReader);
		battleship.addPlayer(playerName, fleet);	
		
		return fleet;
	}
	
	/**
	 * Creates all the other players after the first player.
	 * 
	 * @param inReader, the stream created using the FileReader class
	 * @param battleship, represents the instance of the Game class
	 * @param fleetNum, is the number of the fleet we want to associate
	 * 		  to the player
	 * @param playerName, the name of the player
	 * @param lastFleetNum, the fleet number of the player that precedes the player
	 *        being created
	 * @param lastFleet, the fleet of the player that precedes the player being
	 *        created
	 * @return the fleet of the player created, whether it is the same
	 *         fleet given as input or a new one
	 * @throws FileNotFoundException
	 */
	private static char[][] createOtherPlayers(Scanner inReader, Game battleship, 
			int fleetNum, String playerName, int lastFleetNum, 
			char[][] lastFleet) throws FileNotFoundException {
		
		int skipFleets = (fleetNum - lastFleetNum) - 1;
		skip(skipFleets, inReader); 
		if (skipFleets != -1)
			lastFleet = getFleet(inReader);
	
		battleship.addPlayer(playerName, lastFleet);
		
		return lastFleet;
	}
	
	/**
	 * It reads, without storing, the fleets that will not be used
	 * by any of the players.
	 * 
	 * @param skipFleets, number of fleets to read
	 * @param inReader, the stream created using the FileReader class
	 * @throws FileNotFoundException
	 */
	private static void skip(int skipFleets, Scanner inReader) 
			throws FileNotFoundException {
	
		for (int i = 0; i < skipFleets; i++) {
			int skipRows = inReader.nextInt();
			inReader.nextLine();
			
			for (int j = 0; j < skipRows; j++) 
				inReader.nextLine();
		}
	}
	
	/**
	 * Turns a fleet, with each line being a string, into a char[][] matrix.
	 * 
	 * @param inReader, the stream created using the FileReader class
	 * @return the fleet in a matrix of chars
	 * @throws FileNotFoundException
	 */
	private static char[][] getFleet(Scanner inReader)
			throws FileNotFoundException {
		
		int rows = inReader.nextInt();
		int cols = inReader.nextInt();
		char[][] fleet = new char[rows][cols];
		inReader.nextLine();
		for (int i = 0; i < fleet.length; i++) {
			char[] charsInRow = inReader.nextLine().trim().toCharArray();
			
			for (int j = 0; j < fleet[0].length; j++) 
				fleet[i][j] = charsInRow[j];	
		}
		return fleet;
	}
	
	/**
	 * Executes the method related to the command given as input.
	 * 
	 * @param in, stream created from standard input
	 * @param battleship, represents the instance of the Game class
	 * @param option
	 */
	private static void executeCommand(Scanner in, Game battleship, String command) {

		switch(command) {

		case PLAYER:
			executePlayer(battleship);
			break;
		case SCORE:
			executeScore(in, battleship);
			break;
		case FLEET: 
			executeFleet(in, battleship);
			break;
		case CLASSIFICATION:
			executeClassification(battleship);
			break;
		case IN_GAME:
			executeInGame(in, battleship);
			break;
		case SHOOT:
			executeShoot(in, battleship);
			break;
		case QUIT:
			executeQuit(battleship);
			break;  

		default:
			in.nextLine();
			System.out.println(INVALID_COMMAND);
		}
	}
	
	/**
	 * Prints out the player that has the turn to play or 
	 * it prints out that the game is over, depending on the
	 * state of the game.
	 * 
	 * @param battleship, represents the instance of the Game class
	 */
	private static void executePlayer(Game battleship) {
		
		if (battleship.isGameOver()) 
			System.out.println(GAME_IS_OVER);
		else 
			System.out.println("Next player: " + battleship.getNextPlayerName());
	}
	
	/**
	 * Prints out the score of the player with the name given.
	 * If the name does not correspond to any player, it warns us about it.
	 * 
	 * @param in, stream created from standard input
	 * @param battleship, represents the instance of the Game class
	 */
	private static void executeScore(Scanner in, Game battleship) {
		
		String name = in.nextLine().trim();
		if (battleship.doesPlayerExist(name)) 
			System.out.printf(SCORE_PLAYER, name, battleship.getPlayersPoints(name));
		else
			System.out.println(INVALID_PLAYER);
	} 
	
	/**
	 * Prints out the fleet of the player with the name given.
	 * If the name does not correspond to any player, it warns us about it.
	 * 
	 * @param in, stream created from standard input
	 * @param battleship, represents the instance of the Game class
	 */
	private static void executeFleet(Scanner in, Game battleship) {
		
		String name = in.nextLine().trim();
		if (battleship.doesPlayerExist(name)) {
			char[][] fleet = battleship.getPlayersFleet(name);
			for (int i = 0; i < fleet.length; i++) {
				System.out.print(fleet[i]);
				System.out.println();	
			}
		}
		else
			System.out.println(INVALID_PLAYER);
	}
	
	/**
	 * Prints out the scores and names of all players in the current
	 * state of the game, in descending order of points or
	 * by lexicographical order of the player's names that have the same score.
	 * 
	 * @param battleship, represents the instance of the Game class
	 */
	private static void executeClassification(Game battleship) {
		
		PlayersIterator it = battleship.iterator();
		while (it.hasNext()) {
			Player player = it.next();
			System.out.printf(SCORE_PLAYER, player.getName(), player.getPoints());
		}
	}
	
	/**
	 * Prints out the names of the players that have not been eliminated in
	 * the current state of the game, in the order by which they were added to
	 * the game.
	 * 
	 * @param in, stream created from standard input
	 * @param battleship, represents the instance of the Game class
	 */
	private static void executeInGame(Scanner in, Game battleship) {
		
		PlayersInGameIterator it = battleship.filtIterator();
		while(it.hasNext()) {
			Player player = it.next();
			System.out.println(player.getName());
		}
	}
	
	/**
	 * The player that has the next play will shoot the fleet of the player that
	 * has the name given.
	 * However, the shot will not be made if:
	 * the game is already over;
	 * the name of the player given is the one that has the turn;
	 * the name of the player given does not correspond to any of the players;
	 * the name of the player given corresponds to a player that has been eliminated
	 * at that current state of the game;
	 * the shot was made to a position that does not belong to the fleet disposition.
	 * 
	 * In all of those cases, this method will warn us specifically about those.
	 * 
	 * @param in, stream created from standard input
	 * @param battleship, represents the instance of the Game class
	 */
	private static void executeShoot(Scanner in, Game battleship) {
		
		int row = in.nextInt();
		int col = in.nextInt();
		String name = in.nextLine().trim();
		
		if (battleship.isGameOver()) 
			System.out.println(GAME_IS_OVER);
		
		else if (battleship.selfInflicted(name)) 
			System.out.println("Self-inflicted shot");
		
		else if (!battleship.doesPlayerExist(name))
			System.out.println(INVALID_PLAYER);
		
		else if (!battleship.isPlayerStillPlaying(name))
			System.out.println("Eliminated player");
		
		else if (!battleship.isShotValid(row, col, name))
			System.out.println("Invalid shot");
		
		else
			battleship.shoot(row, col, name);
	}
	
	/**
	 * Prints out the winner if the game is over; in the other hand,
	 * it prints out that the game was not over.
	 * 
	 * @param battleship, represents the instance of the Game class
	 */
	private static void executeQuit(Game battleship) {
		
		if (battleship.isGameOver())
			System.out.println(battleship.winner() + " won the game!"); 
		else 
			System.out.println("The game was not over yet...");	
	}
}
