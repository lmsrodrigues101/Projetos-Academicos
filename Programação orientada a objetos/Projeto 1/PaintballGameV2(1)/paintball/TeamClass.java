package paintball;
import dataStructures.*;

/**
 * @authors Gustavo Sousa, Leandro Rodrigues
 * This class creates objects of Team type, which
 * is responsible for grouping players and bunkers
 * in a way they can "work together" to eliminate
 * other teams
 */
public class TeamClass implements Team {
	
	private String name;
	private Array<Bunker> bunkers;
	private Array<Player> players; 
	
	public TeamClass(String name, Bunker bunker) {
		
		this.name = name;
		bunkers = new ArrayClass<Bunker>();
		players = new ArrayClass<Player>();
		bunkers.insertLast(bunker);
	}

	@Override
	public String getName() {
		
		return name;
	}
	
	@Override
	public Iterator<Bunker> getBunkers() {
		
		return bunkers.iterator();
	}
	
	@Override
	public Iterator<Player> getPlayers() {
		
		return players.iterator();
	}

	@Override
	public int getNumBunkers() {
		
		return bunkers.size();
	}
	
	@Override
	public int getNumPlayers() {
		
		return players.size();
	}
	
	@Override
	public boolean doesBunkerExist(String name) {
		
		return searchBunkerIdx(name) != NOT_FOUND;
	}
	
	@Override
	public void createPlayer(Player player, Bunker bunker) {
		
		players.insertLast(player);
		bunker.occupyBunker(player);
	}
	
	@Override
	public boolean isPlayerInPosition(int x, int y) {
		
		return searchPlayerIdxByCoords(x, y) != NOT_FOUND;
	}
	
	@Override
	public Player playerInPosition(int x, int y) {
		
		return players.get(searchPlayerIdxByCoords(x, y));
	}
	
	private int searchPlayerIdxByCoords(int x, int y) {
		
		boolean found = false;
		int i = 0;
		while (i < players.size() && !found) {
			Player player = players.get(i);
			found = (player.getX() == x && player.getY() == y);
			if (!found)
				i++;
		}
		if (!found)
			return NOT_FOUND;
		return i;
	}	
	
	private int searchBunkerIdx(String name) {

		int i = 0;
		while (i < bunkers.size() && !name.equals(bunkers.get(i).getName()))
			i++;
		if (i < bunkers.size())
			return i;
		return NOT_FOUND;
	}
	
	private int searchBunkerByCoords(int x, int y) {
		
		boolean found = false;
		int i = 0;
		while (i < bunkers.size() && !found) {
			Bunker bunker = bunkers.get(i);
			found = (bunker.getX() == x && bunker.getY() == y);
			if (!found)
				i++;
		}
		if (!found)
			return NOT_FOUND;
	
		return i;
	}

	@Override
	public boolean isBunkerInPosition(int x, int y) {
		
		return searchBunkerByCoords(x,y) != NOT_FOUND;
	}

	@Override
	public Bunker bunkerInPosition(int x, int y) {
		
		return bunkers.get(searchBunkerByCoords(x,y));
	}

	@Override
	public void removeBunker(Bunker bunker) {
		
		int i = bunkers.searchIndexOf(bunker);
		bunkers.removeAt(i);
	}

	@Override
	public void addBunker(Bunker bunker) {
		
		bunkers.insertLast(bunker);
	}

	@Override
	public void removePlayer(Player player) {
		
		int i = players.searchIndexOf(player);
		players.removeAt(i);
	}

	@Override
	public boolean doesPlayerExist(Player player) {
		
		return players.searchIndexOf(player) != NOT_FOUND;
	}
	
	@Override
	public Player getPlayerByIdx(int index) {
		
		return players.get(index);
	}
}
