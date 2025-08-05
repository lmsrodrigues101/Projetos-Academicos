package paintball;

/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * This class represents one of the subclasses of AbstractPlayer,
 * representing the red player type.
 */
public class PlayerRedClass extends AbstractPlayer {
	
	/**
	 * PlayerRedClass Constructor
	 * @param x Position of player in the X axis of the map
	 * @param y Position of player in the Y axis of the map
	 * @param team Team object which is the player's team
	 */
	public PlayerRedClass(int x, int y, Team team) {
		
		super(x, y, team, Game.RED);
	}

}
