
/** 
 * @authors Gustavo Sousa, Leandro Rodrigues
 * This class describes the actions that each player can perform and
 * give information of the player in question to the Game class,
 * which is the only class that can get direct information from here.
 * There will be created at least two instances of this class, interacting
 * with each other, in ways described in the Game's methods.
 */
public class Player {
	
	// Instance variables
	private String name;
	private char[][] fleet;
	private char[][] originalFleet;
	private int numBoats;
	private int points;
	
	// Constructor
	/**
	 * @param name, name of the player
	 * @param matrix, the fleet of the player
	 * @pre: name != null && matrix != null
	 */
	public Player (String name, char[][] matrix) {
		
		this.name = name;
		fleet = copyMatrix(matrix);  
		points = 0;
		originalFleet = copyMatrix(matrix);
		calcInitialNumBoats(matrix);
	}
	
	// Methods
	/**
	 * @return player's name
	 */
	public String getName() {
		
		return name;
	}
	
	/**
	 * @return player's fleet
	 */
	public char[][] getFleet(){
		
		return fleet;
	}
	
	/**
	 * @return true, if the player is still playing;
	 * false, if it has been eliminated
	 */
	public boolean stillPlaying() {
		
		return numBoats > 0;
	}
	
	/**
	 * @return player's points
	 */
	public int getPoints() {
		
		return points;
	}
	
	/**
	 * Calculates the number of initial boats in the player's fleet
	 * and stores it in an instance variable called "numBoats".
	 * 
	 * @param matrix, the initial (original) fleet of the player
	 */
	private void calcInitialNumBoats(char[][] matrix) {
		
		char[][] aux = copyMatrix(matrix);
		for (int i = 0; i < aux.length; i++) 
			for (int j = 0; j < aux[0].length; j++) 
				if (aux[i][j] != Game.WATER && aux[i][j] != Game.DOWN) {
					searchBoat(aux, i, j);
					numBoats++;
				}
	}
	
	/**
	 * @param matrix, the matrix we want a copy of
	 * @return a copy of the matrix given as input
	 */
	private char[][] copyMatrix(char[][] matrix){
				
		char[][] aux = new char[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) 
			for (int j = 0; j < matrix[0].length; j++) 
				aux[i][j] = matrix[i][j];
			
		return aux;
	}
	
	/**
	 * It searches for a boat starting from the coordinates given as input,
	 * where the boat can be arranged horizontally, vertically or it
	 * has only one pipe.
	 * If the count from the horizontal search comes as 1, it will search if
	 * the boat is displayed vertically or if it has only one pipe.
	 * 
	 * @param auxFleet, the fleet used to count the number of initial boats
	 * @param i, row index of the character that belongs to the boat found
	 * @param j, column index of the character that belongs to the boat found
	 */
	private void searchBoat(char[][] auxFleet, int i, int j) {
		
		char c = auxFleet[i][j];
		int count = searchBoatHorizontal(auxFleet, i, j);
        
        if (count == 1) {
        	auxFleet[i][j] = c;
        	searchBoatVertical(auxFleet, i, j);
        }
    }
	
	/**
	 * It searches horizontally for a boat, starting from the coordinates
	 * given as input.
	 * 
	 * @param auxFleet, the fleet used to count the number of initial boats
	 * @param i, row index of the character that belongs to the boat found
	 * @param j, column index of the character that belongs to the boat found
	 * @return the number of pipes of the boat found
	 */
	private int searchBoatHorizontal(char[][] auxFleet, int i, int j) {
		
		int position = j;
        char c = auxFleet[i][j]; 
        int count = 0;
        
        while (position < auxFleet[i].length && auxFleet[i][position] == c) {
        	auxFleet[i][position++] = Game.DOWN;
            count++;
        }
        return count;
	}
	
	/**
	 * It searches vertically for a boat, starting from the coordinates
	 * given as input.
	 * 
	 * @param auxFleet, the fleet used to count the number of initial boats
	 * @param i, row index of the character that belongs to the boat found
	 * @param j, column index of the character that belongs to the boat found
	 */
	private void searchBoatVertical(char[][] auxFleet, int i, int j) {
		
		int position = i;
        char c = auxFleet[i][j]; 

        while (position < auxFleet.length && auxFleet[position][j] == c)
        	auxFleet[position++][j] = Game.DOWN;
	}
	
	/**
	 * Compares this player with another player given as input.
	 * A player is bigger than the other if it has more points
	 * or, in case of a tie, by lexicographical order.
	 * 
	 * @param other, the other player we want to compare with
	 * @return a positive number, if this player is "bigger" than the other;
	 * a negative number, if this player is "smaller" than the other
	 * @pre: !other.getName().equals(this.getName())
	 */
	public int compareTo(Player other) {
		
		 int diff = this.getPoints() - other.getPoints();
		 if (diff == 0)
		 	return other.getName().compareTo(this.getName());
		 
		 return diff; 
	}
	
	/**
	 * It sinks a boat present on the row and the column of the
	 * fleet given as input.
	 * 
	 * @param row, the row where the boat found was shot at
	 * @param col, the column where the boat found was shot at
	 * @return the number of pipes of the boat shot down
	 * @pre: 1 <= row && row <= this.getFleet().length &&  
	 * 1 <= col && col <= this.getFleet()[0].length
	 */
	public int sinkBoat(int row, int col) {
		
		numBoats--;
		char c = fleet[row - 1][col - 1];
		int counter = 0;
		
		counter += shotHorizontalBoat(row, col, c);
		if (counter == 0)
			counter += shotVerticalBoat(row, col, c);
		
		fleet[row - 1][col -1] = Game.DOWN;
		return ++counter;
	}

	/**
	 * @param row, the row where the boat sunk was shot at
	 * @param col, the column where the boat sunk was shot at
	 * @return the number of pipes of the boat that
	 * was previously in the location shot
	 * @pre: 1 <= row && row <= this.getFleet().length &&
	 * 1 <= col && col <= this.getFleet()[0].length
	 */
	public int sunkenBoat(int row, int col) {
		
		char c = originalFleet[row - 1][col - 1];
		int counter = 0;
		
		counter += checkHorizontalBoat(row, col, c);
		if (counter == 0)
			counter += checkVerticalBoat(row, col, c);
		
		return ++counter;
	}
	
	/**
	 * Checks if the boat sunk was horizontally displayed.
	 * 
	 * @param row, the row where the boat sunk was shot at
	 * @param col, the column where the boat sunk was shot at
	 * @param c, the character that represents the boat sunk that was shot at
	 * @return the number of pipes of the boat that
	 * was previously in the location shot
	 */
	private int checkHorizontalBoat(int row, int col, char c) {
		
		int counter = 0;
		int posRight = col; 
        int posLeft = col - 2; 
         
        while (posRight < originalFleet[0].length && originalFleet[row - 1][posRight] == c) {
            posRight++;
            counter++;
        }
        while (posLeft >= 0 && originalFleet[row - 1][posLeft] == c) {
            posLeft--;
            counter++;
        }
        return counter;
	}
	
	/**
	 * Checks if the boat sunk was vertically displayed
	 * 
	 * @param row, the row where the boat sunk was shot at
	 * @param col, the column where the boat sunk was shot at
	 * @param c, the character that represents the boat sunk that was shot at
	 * @return the number of pipes of the boat that
	 * was previously in the location shot
	 */
	private int checkVerticalBoat(int row, int col, char c) {
		
		int counter = 0;
		int posUp = row - 2; 
        int posDown = row; 
         
        while (posUp >= 0 && originalFleet[posUp][col - 1] == c) {
            posUp--;
            counter++;
        }
        while (posDown < originalFleet.length && originalFleet[posDown][col - 1] == c) {
            posDown++;
            counter++;
        }
        return counter;
	}
	
	/**
	 * Shoots down a boat, checking if it is horizontally displayed.
	 * 
	 * @param row, the row where the boat found was shot at
	 * @param col, the column where the boat found was shot at
	 * @param c, the character that represents the boat shot down
	 * @return the number of pipes of the boat shot down
	 */
	private int shotHorizontalBoat(int row, int col, char c) {
		
		int counter = 0;
		int posRight = col; 
        int posLeft = col - 2; 
         
        while (posRight < fleet[0].length && fleet[row - 1][posRight] == c) {
        	fleet[row - 1][posRight] = Game.DOWN; 
            posRight++;
            counter++;
        }
        while (posLeft >= 0 && fleet[row - 1][posLeft] == c) {
        	fleet[row - 1][posLeft] = Game.DOWN; 
            posLeft--;
            counter++;
        } 
        return counter;
	}
	
	/**
	 * Shoots down a boat, checking if it is vertically displayed.
	 * 
	 * @param row, the row where the boat found was shot at
	 * @param col, the column where the boat found was shot at
	 * @param c, the character that represents the boat shot down
	 * @return the number of pipes of the boat shot down
	 */
	private int shotVerticalBoat(int row, int col, char c) {
		
		int counter = 0;
		int posUp = row - 2; 
        int posDown = row; 
         
        while (posUp >= 0 && fleet[posUp][col - 1] == c) {
        	fleet[posUp][col - 1] = Game.DOWN; 
            posUp--;
            counter++;
        }
        while (posDown < fleet.length && fleet[posDown][col - 1] == c) {
        	fleet[posDown][col - 1] = Game.DOWN; 
            posDown++;
            counter++;
        }
        return counter;
	}
	
	/**
	 * Update player's points to "newPoints".
	 * 
	 * @param newPoints, the updated score of the player
	 */
	public void updatePoints(int newPoints) {
		
		points = newPoints;
	}
}
