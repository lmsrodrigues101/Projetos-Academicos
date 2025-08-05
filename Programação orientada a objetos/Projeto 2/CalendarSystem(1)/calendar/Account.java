package calendar;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

/**
 * Interface that represents a user of the application.
 * @authors Gustavo Sousa, Leandro Rodrigues
 */
public interface Account extends Comparable<Account> {
	
	/**
	 * Gets the name of the account.
	 * @return String object representing the name of the account
	 */
	String getName();
	
	/**
	 * Gets the type of an account.
	 * @return String object representing the type of the account
	 */
	String getType();
	
	/**
	 * Adds event to accepted events, but removes it from the
	 * collection of invitations.
	 * @param event Event object being added to the account
	 */
	void acceptEvent(Event event);
	
	/**
	 * Removes event from the collections of invitations.
	 * @param event Event object being removed from the account
	 */
	void removeEventInvitation(Event event);
	
	/**
	 * Checks if the user is busy at the date given as input.
	 * @param date LocalDateTime object representing the date of an event
	 * @return true if the user is busy (has another event)
	 * 		   at that time, false otherwise
	 */
	boolean isBusy(LocalDateTime date);
	
	/**
	 * Gets an iterator of the set composed by all the
	 * events the user was invited or created.
	 * @return Iterator object of Event objects
	 */
	Iterator<Event> eventsIterator();
	
	/**
	 * Adds event to the set of invitations and own events
	 * @param event Event object being added to the set
	 */
	void addInvitationOrOwnEvent(Event event);
	
	/**
	 * Removes event from the set of invitations and own events.
	 * @param event Event object being removed from the set
	 */
	void removeInvitationOrOwnEvent(Event event);
	
	/**
	 * Adds event to the map with only invitations, storing
	 * the invitations with the same date in the same list
	 * @param event Event object the user was invited to
	 */
	void addInvitation(Event event);
	
	/**
	 * Checks if the account has promoted any events and
	 * been invited to other events
	 * @param accountName Name of the account
	 * @return true if the account has events, false otherwise
	 */
	boolean hasEvents();
	
	/**
	 * Checks if the user is busy with high priority event at that date
	 * @return true if the user is occupied with high event, false otherwise
	 */
	boolean isBusyWithHighEvent(LocalDateTime date);
	
	/**
	 * Removes an already accepted event with the date given.
	 * @param date LocalDateTime object representing the date of 
	 * 			   the event we want to remove
	 * @return Event object representing the event being rejected
	 */
	Event rejectAcceptedEvent(LocalDateTime date);
	
	/**
	 * Rejects invitations at the date given.
	 * @param date LocalDateTime object representing the date of 
	 * 			   the event(s) we want to reject
	 * @return Iterator object of the events whose invitations were rejected
	 */
	List<Event> rejectInvitations(LocalDateTime date);
}
