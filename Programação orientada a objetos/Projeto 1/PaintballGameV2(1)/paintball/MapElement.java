package paintball;

/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * Interface of AbstractMapElement
 */
public interface MapElement {
	
	/**
	 * Gets the X coordinate of the element on the map.
	 * @return X coordinate of the element
	 */
	int getX();
	
	/**
	 * Gets the Y coordinate of the element on the map.
	 * @return Y coordinate of the element
	 */
	int getY();
	
	/**
	 * Gets the team of the element.
	 * @return Team object that represents the element's team
	 */
	Team getTeam();
	
	/**
	 * Gets the team's name of the element.
	 * @return String which is the team's name of the element.
	 */
	String getTeamName();
	
	/**
	 * Assign the team given to the element.
	 * @param team Team object which is the team we want to assign to the element.
	 */
	void assignTeam(Team team);
	
	/**
	 * Change the map's X coordinate of the element.
	 * @param x Position in the X axis of the map
	 */
	void changeX(int x);
	
	/**
	 * Change the map's Y coordinate of the element.
	 * @param y Position in the Y axis of the map
	 */
	void changeY(int y);
}
