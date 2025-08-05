package paintball;

/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * Abstract class that represents all types of players
 * in this game, given by the subclasses PlayerRedClass,
 * PlayerBlueClass and PlayerGreenClass. 
 * Players are responsible for the interaction between teams,
 * interacting with other players and bunkers of other teams.
 */
public abstract class AbstractPlayer extends AbstractMapElement implements Player {
	
	private String playerType; // Type of the player
	
	/**
	 * AbstractPlayer Constructor
	 * @param x Position of player in the X axis of the map
	 * @param y Position of player in the Y axis of the map
	 * @param team Team object which is the player's team
	 * @param playerType String which represents the type of the player created
	 */
	public AbstractPlayer(int x, int y, Team team, String playerType) {
		
		super(x, y, team);
		this.playerType = playerType;
	}
	
	@Override
	public String getPlayerType() {
		
		return playerType;
	}	

}
