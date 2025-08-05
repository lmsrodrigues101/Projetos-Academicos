package calendar;

import java.time.LocalDateTime;

/**
 * Interface that represents an account that can promote an event.
 * @authors Gustavo Sousa, Leandro Rodrigues
 */
public interface PromoterAccount extends Account {
	
	/**
	 * Checks if the event exists.
	 * @param eventName Name of the event
	 * @return true if the event already exists, false otherwise
	 */
	boolean doesEventExist(String eventName);
	
	/**
	 * Add an event to the collection of events promoted by the user.
	 * @param event Event object which is the new event promoted by the user
	 */
	void addOwnEvent(Event event);
	
	/**
	 * Checks if the user is busy with his own event
	 * @param date LocalDateTime object representing the date of the event 
	 * @return true if the user has his own event at that time, false otherwise
	 */
	boolean isBusyWithOwnEvent(LocalDateTime date);
	
	/**
	 * Removes his own event at the date specified.
	 * @param date LocalDateTime object representing the date of the event
	 * @return String representing the name of the event removed
	 */
	String removeOwnEvent(LocalDateTime date);
}
