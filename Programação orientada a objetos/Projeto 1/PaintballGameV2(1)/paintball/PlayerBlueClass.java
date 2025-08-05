package paintball;

/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * This class represents one of the subclasses of AbstractPlayer,
 * representing the blue player type.
 */
public class PlayerBlueClass extends AbstractPlayer {
	
	/**
	 * PlayerBlueClass Constructor
	 * @param x Position of player in the X axis of the map
	 * @param y Position of player in the Y axis of the map
	 * @param team Team object which is the player's team
	 */
	public PlayerBlueClass(int x, int y, Team team) {
		
		super(x, y, team, Game.BLUE);
	}
	
	/**
	 * Gets the direction of the movement, depending on the iteration step.
	 * @param iterationStep The number that corresponds to the step where
	 * 		  the iteration of the blue player's movement is at the moment
	 * @return The direction to which the player will move
	 */
	public int typeOfMovement(int iterationStep) {
		
		int movement = 0;
		switch (iterationStep) {
			case 1 -> movement = Game.MOVE_WEST;
			case 2 -> movement = Game.MOVE_EAST;
		}
		return movement;
	}
}
