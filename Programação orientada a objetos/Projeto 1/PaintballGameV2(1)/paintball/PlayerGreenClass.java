package paintball;

/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * This class represents one of the subclasses of AbstractPlayer,
 * representing the green player type.
 */
public class PlayerGreenClass extends AbstractPlayer {
	
	/**
	 * PlayerGreenClass Constructor
	 * @param x Position of player in the X axis of the map
	 * @param y Position of player in the Y axis of the map
	 * @param team Team object which is the player's team
	 */
	public PlayerGreenClass(int x, int y, Team team) {
		
		super(x, y, team, Game.GREEN);
	}
	
	/**
	 * Gets the direction of the movement, depending on the iteration step.
	 * @param iterationStep The number that corresponds to the step where
	 * 		  the iteration of the green player's movement is at the moment
	 * @return The directions to which the player will move
	 */
	public int[] typeOfMovement(int iterationStep) {
		
		int[] movement = null;
		switch (iterationStep) {
			case 1 -> movement = new int[] {Game.MOVE_WEST, Game.MOVE_NORTH};
			case 2 -> movement = new int[] {Game.MOVE_EAST, Game.MOVE_NORTH};
			case 3 -> movement = new int[] {Game.MOVE_WEST, Game.MOVE_SOUTH};
			case 4 -> movement = new int[] {Game.MOVE_EAST, Game.MOVE_SOUTH};
		}
		return movement;
	}

}
