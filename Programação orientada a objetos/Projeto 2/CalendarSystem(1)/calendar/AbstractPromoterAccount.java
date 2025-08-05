package calendar;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Abstract class that represents a user who can create events.
 * @authors Gustavo Sousa, Leandro Rodrigues
 */
public abstract class AbstractPromoterAccount extends AbstractAccount implements PromoterAccount {
	
	/**
	 * Events created by the user.
	 */
	private Set<String> myEvents;
	
	/**
	 * Events organized by dates.
	 */
	private Map<LocalDateTime, Event> mapMyEvents;
	
	/**
	 * Constructs an AbstractPromoterAccount object.
	 * @param accountName Name of the account
	 */
	protected AbstractPromoterAccount(String accountName) {
		super(accountName);
		myEvents = new HashSet<>();
		mapMyEvents = new HashMap<>();
	}
	
	@Override
	public boolean doesEventExist(String eventName) {
		return myEvents.contains(eventName);
	}
	
	@Override
	public void addOwnEvent(Event event) {
		myEvents.add(event.getName());
		mapMyEvents.put(event.getDate(), event);
		acceptEvent(event);
		addInvitationOrOwnEvent(event);
	}
	
	@Override
	public boolean isBusyWithOwnEvent(LocalDateTime date) {
		return mapMyEvents.containsKey(date);
	}
	
	@Override
	public String removeOwnEvent(LocalDateTime date) {
		String eventName = mapMyEvents.get(date).getName();
		mapMyEvents.remove(date);
		myEvents.remove(eventName);
		return eventName;
	}
}
