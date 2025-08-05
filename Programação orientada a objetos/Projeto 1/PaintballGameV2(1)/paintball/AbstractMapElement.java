package paintball;

/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * Abstract class that represents those elements which will be part
 * of the Map matrix: Bunkers and Players
 */
public abstract class AbstractMapElement implements MapElement {
	
	private int x; // X coordinate of the element
	private int y; // Y coordinate of the element
	private Team team; // Team of the element
	
	/**
	 * Constructor of AbstractMapElement for Bunker elements.
	 * @param x Position of the Bunker in the X axis of the map
	 * @param y Position of the Bunker in the Y axis of the map
	 */
	public AbstractMapElement(int x, int y) {
		
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Constructor of AbstractMapElement for Player elements.
	 * @param x Position of the Player in the X axis of the map
	 * @param y Position of the Player in the Y axis of the map
	 * @param team Team object which is the player's team
	 */
	public AbstractMapElement(int x, int y, Team team) {
		
		this.x = x;
		this.y = y;
		this.team = team;
	}
	
	@Override
	public int getX() {
		
		return x;
	}
    
	@Override
    public int getY() {
    	
    	return y;
    }
	
	@Override
	public Team getTeam() {
		
		return team;
	}
	
	@Override
	public String getTeamName() {
		
		return team.getName();
	}
	
	@Override
	public void assignTeam(Team team) {
		
		this.team = team;
	}
	
	@Override
	public void changeX(int x) {
		
		this.x = x;
	}
	
	@Override
	public void changeY(int y) {
		
		 this.y = y;
	}	
}
