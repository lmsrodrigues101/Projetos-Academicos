package paintball;

/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * This class creates objects of type Map,
 * whose instance variable mapMatrix is responsible
 * for organizing the game and its main elements (bunkers
 * and players), facilitating the interaction between those.
 */
public class MapClass implements Map {
	
	private MapElement mapMatrix[][];	
	
	/**
	 * Constructor of the MapClass.
	 * @param width Map's maximum X position
	 * @param height Map's maximum Y position
	 * @pre: width >= 0 && height >= 0
	 */
	public MapClass(int width, int height) {
		
		mapMatrix = new AbstractMapElement[height][width];
	}
	
	@Override
	public int mapWidth() {
		
		return mapMatrix[0].length;
	}
	
	@Override
	public int mapHeight() {
		
		return mapMatrix.length;
	}

	@Override
	public boolean isPositionOccupied(int x, int y) {
		
		return mapMatrix[y-1][x-1] != null;
	}

	@Override
	public void addElement(MapElement elem, int x, int y) {
		
		mapMatrix[y-1][x-1] = elem;
	}

	@Override
	public MapElement[] getMapRow(int row) {
		
		return mapMatrix[row];
	}

	@Override
	public void removeElement(int x, int y) {
		
		mapMatrix[y-1][x-1] = null;
	}
}
