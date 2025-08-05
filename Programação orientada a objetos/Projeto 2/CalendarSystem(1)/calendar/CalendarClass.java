package calendar;
import calendar.Event.InviteStatus;
import calendar.exceptions.*;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class that represents the Office Calendar application.
 * @authors Gustavo Sousa, Leandro Rodrigues
 */
public class CalendarClass implements Calendar {
	
	/**
	 * Sorted map of accounts registered in the system, organized by 
	 * account name -> account
	 */
	private SortedMap<String, Account> mapAccounts;
	
	/**
	 * Map of events registered in the system, organized by
	 * its promoters and matching to its own name in the form:
	 * promoter -> (event name -> event)
	 */
	private Map<Account, Map<String, Event>> mapEvents;
	
	/**
	 * Constructs a CalenderClass object, initializing data structures.
	 */
	public CalendarClass() {
		mapAccounts = new TreeMap<>();
		mapEvents = new HashMap<>();
	}
	
	@Override
	public void register(String accountName, String accountType) 
			throws AccountAlreadyExistsException, InvalidTypeException {
		Account account = createAccount(accountName, accountType);
		mapAccounts.put(accountName, account);
	}
	
	/**
	 * Creates an account with the name and type given.
	 * @param accountName String object representing the account e-mail
	 * @param accountType String object representing the account type
	 * @return Account object created with the input given
	 * @throws AccountAlreadyExistsException if the name of the account given
	 * 										 already exists
	 * @throws InvalidTypeException if the type of the account does not exist
	 */
	private Account createAccount(String accountName, String accountType)
			throws AccountAlreadyExistsException, InvalidTypeException {
		if (mapAccounts.containsKey(accountName))
			throw new AccountAlreadyExistsException();
		if (!validType(accountType))
			throw new InvalidTypeException();
		Account account = null;
		switch (accountType) {
			case MANAGER -> account = new ManagerAccountClass(accountName);
			case STAFF -> account = new StaffAccountClass(accountName);
			case GUEST -> account = new GuestAccountClass(accountName);
		}
		return account;
	}
	
	/**
	 * Checks if the type given is a valid type of user.
	 * @param type String object representing the type of user
	 * @return true if the type given is valid, false otherwise
	 */
	private static boolean validType(String type) {
		return type.equals(MANAGER) || type.equals(STAFF) || type.equals(GUEST);
	}
	
	@Override
	public Iterator<Entry<String, Account>> accounts() {
		return mapAccounts.entrySet().iterator();
	}	

	@Override
	public void createEvent(String accountName, String eventName, String priority, int year, 
			int month, int day, int hour, String topics) 
					throws AccountDoesNotExistException, InvalidPriorityException, 
					GuestCannotCreateEventException, StaffCannotCreateHighEventException,
					EventAlreadyExistsInAccountException, PromoterIsBusyException {
		if (!mapAccounts.containsKey(accountName))
			throw new AccountDoesNotExistException();
		if (!validPriority(priority))
			throw new InvalidPriorityException();
		
		Account account = mapAccounts.get(accountName);
		if (account.getType().equals(GUEST))
			throw new GuestCannotCreateEventException();
		if (account.getType().equals(STAFF) && priority.equals(HIGH))
			throw new StaffCannotCreateHighEventException();
		
		PromoterAccount promoter = (PromoterAccount)account;
		if (promoter.doesEventExist(eventName))
			throw new EventAlreadyExistsInAccountException();
		
		LocalDateTime date = LocalDateTime.of(year, month, day, hour, 0);
		if (promoter.isBusy(date))
			throw new PromoterIsBusyException();
		
		Event event = new EventClass(accountName, eventName, priority, date, topics);
		createEventAfterCheckingExceptions(event, promoter, date, eventName);
	}
	
	/**
	 * Creates an event after checking for exceptions.
	 * @param event Event object being created
	 * @param promoter PromoterAccount object which is the event's promoter
	 * @param date LocalDateTime object representing the date of the new event
	 * @param eventName Name of the event
	 */
	private void createEventAfterCheckingExceptions(Event event, PromoterAccount promoter,
													LocalDateTime date, String eventName) {
		promoter.addOwnEvent(event);
		List<Event> rejectedInvitations = promoter.rejectInvitations(date);
		if (rejectedInvitations != null) {
			for (Event e: rejectedInvitations)
				e.newRejectedInvitation(promoter.getName());
		}
		if (mapEvents.containsKey(promoter)) {
			Map<String, Event> promoterEvents = mapEvents.get(promoter);
			promoterEvents.put(eventName, event);
		}
		else {
			Map<String, Event> promoterEvents = new HashMap<>();
			promoterEvents.put(eventName, event);
			mapEvents.put(promoter, promoterEvents);
		}
	}
	
	/**
	 * Checks if the priority given is a valid priority.
	 * @param priority String object representing the event priority
	 * @return true if the priority given is valid, false otherwise
	 */
	private static boolean validPriority(String priority) {
		return priority.equals(MID) || priority.equals(HIGH);
	}

	@Override
	public Iterator<Event> events(String accountName) throws AccountDoesNotExistException {
		if (!mapAccounts.containsKey(accountName))
			throw new AccountDoesNotExistException();
		Account account = mapAccounts.get(accountName);
		return account.eventsIterator();
	}
	
	@Override
	public void handleInviteExceptions(String invitee, String promoter, String eventName)
			throws AccountDoesNotExistException, EventDoesNotExistInAccountException,
			AccountAlreadyInvitedToEventException, AccountAlreadyAcceptedOtherEventException {
		if (!mapAccounts.containsKey(promoter))
			throw new AccountDoesNotExistException(promoter);
		if (!mapAccounts.containsKey(invitee))
			throw new AccountDoesNotExistException(invitee);
		
		PromoterAccount promoterAcc = (PromoterAccount)mapAccounts.get(promoter);
		if (!promoterAcc.doesEventExist(eventName))
			throw new EventDoesNotExistInAccountException();
		
		Event event = mapEvents.get(promoterAcc).get(eventName);
		if (event.hasUserAlreadyBeenInvited(invitee))
			throw new AccountAlreadyInvitedToEventException();
		
		LocalDateTime date = event.getDate();
		Account inviteeAcc = mapAccounts.get(invitee);	
		if (promoterAcc.getType().equals(MANAGER) && inviteeAcc.getType().equals(STAFF)) {
			if (inviteeAcc.isBusyWithHighEvent(date))
				throw new AccountAlreadyAcceptedOtherEventException();
		}
		else {
			if (inviteeAcc.isBusy(date))
				throw new AccountAlreadyAcceptedOtherEventException();
		}
	}
	
	/**
	 * Makes the invitee accept the high priority event given as input
	 * @param event Event object which the invitee will be attending
	 * @param inviteeAcc Account object which is the user that will be attending the event
	 * @param invitee Name of the invitee
	 */
	private void staffAcceptHighInvitation(Event event, Account inviteeAcc, String invitee) {
		inviteeAcc.acceptEvent(event);
		event.newAcceptedInvitation(invitee);
	}
	
	@Override
	public String inviteCase1(String invitee, String promoter, String eventName) {
		PromoterAccount promoterAcc = (PromoterAccount)mapAccounts.get(promoter);
		Event event = mapEvents.get(promoterAcc).get(eventName);
		Account inviteeAcc = mapAccounts.get(invitee);
		LocalDateTime date = event.getDate();
		
		String nameEventRemoved = ((PromoterAccount)inviteeAcc).removeOwnEvent(date);
		Event eventRemoved = mapEvents.get(inviteeAcc).get(nameEventRemoved);
		Iterator<Entry<String, InviteStatus>> invitedAccounts = eventRemoved.invitedAccountsIterator();
		while (invitedAccounts.hasNext()) {
			String accountName = invitedAccounts.next().getKey();
			mapAccounts.get(accountName).removeInvitationOrOwnEvent(eventRemoved);
		}
		mapEvents.get(inviteeAcc).remove(nameEventRemoved);
		
		staffAcceptHighInvitation(event, inviteeAcc, invitee);
		return nameEventRemoved;
	}
	
	@Override
	public String[] inviteCase2(String invitee, String promoter, String eventName) {
		PromoterAccount promoterAcc = (PromoterAccount)mapAccounts.get(promoter);
		Event event = mapEvents.get(promoterAcc).get(eventName);
		Account inviteeAcc = mapAccounts.get(invitee);
		LocalDateTime date = event.getDate();

		Event rejectedEvent = inviteeAcc.rejectAcceptedEvent(date);
		rejectedEvent.decrementAcceptedInvitation();
		rejectedEvent.newRejectedInvitation(invitee);
		
		staffAcceptHighInvitation(event, inviteeAcc, invitee);
		return new String[] {rejectedEvent.getName(), rejectedEvent.getPromoter()};
	}

	@Override
	public Iterator<Event> inviteCase3(String invitee, String promoter, String eventName) {
		PromoterAccount promoterAcc = (PromoterAccount)mapAccounts.get(promoter);
		Event event = mapEvents.get(promoterAcc).get(eventName);
		Account inviteeAcc = mapAccounts.get(invitee);
		LocalDateTime date = event.getDate();
		
		staffAcceptHighInvitation(event, inviteeAcc, invitee);	
		List<Event> rejectedInvitations = inviteeAcc.rejectInvitations(date);
		if (rejectedInvitations == null) 
			return null;
		
		for (Event e: rejectedInvitations)
			e.newRejectedInvitation(invitee);
		
		return rejectedInvitations.iterator();
	}
	
	@Override
	public void inviteCase4(String invitee, String promoter, String eventName) {
		PromoterAccount promoterAcc = (PromoterAccount)mapAccounts.get(promoter);
		Event event = mapEvents.get(promoterAcc).get(eventName);
		Account inviteeAcc = mapAccounts.get(invitee);
		
		inviteeAcc.addInvitation(event);
		event.newInvitation(invitee);
	}
 
	@Override
	public boolean areAnyAccountsRegistered() {
		return !mapAccounts.isEmpty();
	}

	@Override
	public boolean isUserStaff(String accountName) {
		return mapAccounts.get(accountName).getType().equals(STAFF);
	}

	@Override
	public boolean isEventHighPriority(String promoterName, String eventName) {
		Account account = mapAccounts.get(promoterName);
		Event event = mapEvents.get(account).get(eventName);
		return event.getPriority().equals(HIGH);
	}
	
	@Override
	public boolean isUserBusyWithOwnEvent(String invitee, String promoter, String eventName) {
		Event event = mapEvents.get(mapAccounts.get(promoter)).get(eventName);
		LocalDateTime date = event.getDate();
		return ((PromoterAccount)mapAccounts.get(invitee)).isBusyWithOwnEvent(date);
	}

	@Override
	public boolean isUserBusy(String invitee, String promoter, String eventName) {
		Event event = mapEvents.get(mapAccounts.get(promoter)).get(eventName);
		LocalDateTime date = event.getDate();
		return mapAccounts.get(invitee).isBusy(date);
	}

	@Override
	public Iterator<Event> response(String invitee, String promoter, String eventName, String response)
			throws AccountDoesNotExistException, UnknownResponseException,
			EventDoesNotExistInAccountException, AccountNotInvitedToEventException,
			AccountAlreadyRespondedException, AccountAlreadyAcceptedOtherEventException {
		if (!mapAccounts.containsKey(promoter))
			throw new AccountDoesNotExistException(promoter);
		if (!mapAccounts.containsKey(invitee))
			throw new AccountDoesNotExistException(invitee);
		if (!response.equals(ACCEPT) && !response.equals(REJECT))
			throw new UnknownResponseException();
		PromoterAccount promoterAcc = (PromoterAccount)mapAccounts.get(promoter);
		if (!promoterAcc.doesEventExist(eventName))
			throw new EventDoesNotExistInAccountException();
		Event event = mapEvents.get(promoterAcc).get(eventName);
		if (!event.hasUserAlreadyBeenInvited(invitee))
			throw new AccountNotInvitedToEventException();
		if (event.hasAccountResponded(invitee))
			throw new AccountAlreadyRespondedException();
		Account inviteeAcc = mapAccounts.get(invitee);
		if (inviteeAcc.isBusy(event.getDate()))
			throw new AccountAlreadyAcceptedOtherEventException();
		
		return responseAfterCheckingExceptions(response, inviteeAcc, event, invitee);
	}
	
	/**
	 * User with the name "invitee" responds to the invitation for the event,
	 * after checking for exceptions.
	 * @param response Response of the invitee to the invitation
	 * @param inviteeAcc Account object of the invitee
	 * @param event Event object to which the invitee is responding to
	 * @param invitee Name of the invitee
	 * @return Iterator object of the events rejected by the invitee because he
	 * 		   accepted the event invitation that is at the same time as those
	 * 		   rejected events. Could be null if: the response was "reject" or
	 * 		   if there were no invitations at the time of the event
	 */
	private Iterator<Event> responseAfterCheckingExceptions(String response, Account inviteeAcc,
												 Event event, String invitee) {
		if (response.equals(ACCEPT)) {
			inviteeAcc.acceptEvent(event);
			event.newAcceptedInvitation(invitee);
			List<Event> rejectedInvitations = inviteeAcc.rejectInvitations(event.getDate());
			if (rejectedInvitations == null)
				return null;
			
			for (Event e: rejectedInvitations) {
				e.newRejectedInvitation(invitee);
			}
			return rejectedInvitations.iterator();
		}
		// response.equals(REJECT)
		event.newRejectedInvitation(invitee);
		inviteeAcc.removeEventInvitation(event);
		return null;
	}

	@Override
	public Iterator<Entry<String, InviteStatus>> event(String promoter, String eventName)
			throws AccountDoesNotExistException, EventDoesNotExistInAccountException {
		if (!mapAccounts.containsKey(promoter))
			throw new AccountDoesNotExistException();
		PromoterAccount promoterAcc = (PromoterAccount)mapAccounts.get(promoter);
		if (!promoterAcc.doesEventExist(eventName))
			throw new EventDoesNotExistInAccountException();
		Event event = mapEvents.get(promoterAcc).get(eventName);
		return event.invitedAccountsIterator();
	}

	@Override
	public String getEventFormattedDate(String promoter, String eventName) {
		PromoterAccount promoterAcc = (PromoterAccount)mapAccounts.get(promoter);
		LocalDateTime date = mapEvents.get(promoterAcc).get(eventName).getDate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH'h'");
		String formattedDate = date.format(formatter);
		return formattedDate;
	}
	
	@Override
	public Iterator<Event> topics(String[] topics) {
		List<Event> listEvents = new ArrayList<>();
		List<String> listTopics = new ArrayList<>();
		Collections.addAll(listTopics, topics);
      	for (Map<String, Event> map : mapEvents.values()) {
      		for (Event event : map.values())
      			if (hasTopicsInCommon(event, listTopics))
      				listEvents.add(event);
		}
      	Collections.sort(listEvents, new ComparatorByTopics(listTopics));
		return listEvents.iterator();
	}
	
	/**
	 * Checks if the event has topics in common with the list given.
	 * @param event Event object representing the event which topics being
	 * 		  compared
	 * @param listTopics List of topics
	 * @return true if the event has any of the topics in the list given,
	 * 		   false otherwise
	 */
	private boolean hasTopicsInCommon(Event event, List<String> listTopics) {
		List<String> listEventTopics = new ArrayList<>(event.getTopics());
		listEventTopics.retainAll(listTopics);
		return !listEventTopics.isEmpty();
	}
}
