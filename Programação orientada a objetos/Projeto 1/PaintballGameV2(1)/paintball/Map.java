package paintball;

/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * Interface of MapClass
 */
public interface Map {
	
	/**
	 * Gets the map's maximum X position.
	 * @return maximum X position of the map
	 */
	int mapWidth();
	
	/**
	 * Gets the map's maximum Y position.
	 * @return maximum Y position of the map
	 */
	int mapHeight();
	
	/**
	 * Checks if the position given is occupied by any element (bunker or player).
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @return True, if the position is occupied; False, otherwise
	 * @pre: 0 < x && x <= mapWidth() && 0 < y && y <= mapHeight()
	 */
	boolean isPositionOccupied(int x, int y);
	
	/**
	 * Adds an element to the map, in the position given.
	 * @param elem MapElement object which is the element we want to add
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @pre: 0 < x && x <= mapWidth() && 0 < y && y <= mapHeight()
	 */
	void addElement(MapElement elem, int x, int y);
	
	/**
	 * Gets an array of MapElement objects in the map's row given.
	 * @param row Map's row we want to get
	 * @return Array of MapElement objects from the row asked
	 * @pre: 0 < row && row <= mapHeight()
	 */
	MapElement[] getMapRow(int row);
	
	/**
	 * Removes the element from the position in the map given.
	 * @param x Position in the X axis of the map
	 * @param y Position in the Y axis of the map
	 * @pre: 0 < x && x <= mapWidth() && 0 < y && y <= mapHeight()
	 */
	void removeElement(int x, int y);
}
