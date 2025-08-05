package calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Abstract class that represents a user of the application.
 * @authors Gustavo Sousa, Leandro Rodrigues
 */
public abstract class AbstractAccount implements Account {
	
	/**
	 * E-mail of the account
	 */
	private String accountName;
	
	/**
	 * Events already accepted by the user.
	 */
	private Map<LocalDateTime, Event> acceptedEvents; 
	
	/**
	 * Events the user was invited to, as well as his own events.
	 */
	private List<Event> invitationsAndOwnEvents;
	
	/**
	 * Events the user was invited to, organized by their date.
	 */
	private Map<LocalDateTime, List<Event>> invitations;
		
	/**
	 * Constructs an abstract user Account.
	 */
	protected AbstractAccount(String accountName) {
		this.accountName = accountName;
		acceptedEvents = new HashMap<>();
		invitationsAndOwnEvents = new ArrayList<>();
		invitations = new HashMap<>();
	}
	
	@Override
	public int compareTo(Account other) {
		return accountName.compareTo(other.getName());
	}
	
	@Override
	public String getName() {
		return accountName;
	}
	
	@Override
	public void acceptEvent(Event event) {
		acceptedEvents.put(event.getDate(), event);
		removeEventInvitation(event); 
	}
	
	@Override
	public void removeEventInvitation(Event event) {
		List<Event> list = invitations.get(event.getDate());
		if (list != null)
			list.remove(event);
	}
	
	@Override
	public boolean isBusy(LocalDateTime date) {
		return acceptedEvents.containsKey(date);
	}
	
	@Override
	public Iterator<Event> eventsIterator() {
		return invitationsAndOwnEvents.iterator();
	}
	
	@Override
	public void addInvitationOrOwnEvent(Event event) {
		invitationsAndOwnEvents.add(event);
	}
	
	@Override
	public void removeInvitationOrOwnEvent(Event event) {
		invitationsAndOwnEvents.remove(event);
	}
	
	@Override
	public void addInvitation(Event event) {
		LocalDateTime date = event.getDate();
		if (invitations.containsKey(date)) {
			List<Event> events = invitations.get(date);
			events.add(event);
		}
		else {
			List<Event> events = new ArrayList<>();
			events.add(event);
			invitations.put(date, events);
		}
		addInvitationOrOwnEvent(event);
	}
	
	@Override
	public boolean hasEvents() {
		return !invitationsAndOwnEvents.isEmpty();
	}
	
	@Override
	public boolean isBusyWithHighEvent(LocalDateTime date) {
		Event event = acceptedEvents.get(date);
		return event != null && event.getPriority().equals(Calendar.HIGH);
	}
	
	@Override
	public Event rejectAcceptedEvent(LocalDateTime date) {
		Event event = acceptedEvents.get(date);
		acceptedEvents.remove(date);
		return event;
	}
	
	@Override
	public List<Event> rejectInvitations(LocalDateTime date) {
		List<Event> list = invitations.get(date);
		invitations.remove(date);
		return list;
	}
	
}
