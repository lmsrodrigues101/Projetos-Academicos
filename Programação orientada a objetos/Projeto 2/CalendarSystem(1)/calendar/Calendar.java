package calendar;

import calendar.Event.InviteStatus;
import calendar.exceptions.*;
import java.util.Iterator;
import java.util.Map;

/**
 * Interface that represents the Office Calendar application.
 * @authors Gustavo Sousa, Leandro Rodrigues
 */
public interface Calendar {
	
	/**
	 * Constants representing each account type
	 */
    static final String MANAGER = "manager";
	static final String STAFF = "staff";
	static final String GUEST = "guest";
	
	/**
	 * Constants representing each event priority type
	 */
	static final String MID = "mid";
	static final String HIGH = "high";
	
	/**
	 * Constants representing possible "response" to an invitation
	 */
	static final String ACCEPT = "accept";
	static final String REJECT = "reject";

	/**
	 * Registers a new account in the system.
	 * @param accountName String object representing the user's 
	 * @param accountType String object representing the account's type
	 * @throws AccountAlreadyExistsException if the name of the account given
	 * 										 already exists
	 * @throws InvalidTypeException if the type of the account does not exist
	 */
	void register(String accountName, String accountType) 
			throws AccountAlreadyExistsException, InvalidTypeException;
	
	/**
	 * Gets an iterator of the set composed by all the
	 * entries of the SortedMap of accounts.
	 * @return Iterator object of Map.Entry<String, Account>> objects
	 */
	Iterator<Map.Entry<String, Account>> accounts();
	
	/**
	 * Creates an event with the information given.
	 * @param accountName Event promoter's name
	 * @param eventName Event name
	 * @param priority Event level of priority
	 * @param year Year that the event was scheduled to
	 * @param month Month that the event was scheduled to
	 * @param day Day that the event was scheduled to
	 * @param hour Hour that the event was scheduled to
	 * @param topics String with topics
	 * @throws AccountDoesNotExistException if the name of the account given
	 * 										does not exist
	 * @throws InvalidPriorityException if the priority given is not valid
	 * @throws GuestCannotCreateEventException if the account given is of "guest" type
	 * @throws StaffCannotCreateHighEventException if the account given is of "staff"
	 * 											   type and the priority of the event
	 * 											   being created is "high"
	 * @throws EventAlreadyExistsInAccountException if the event with "eventName " already
	 * 												exists in the promoter "accountName" account
	 * @throws PromoterIsBusyException if the promoter is busy at the time of the event he is
	 * 								   trying to create
	 */
	void createEvent(String accountName, String eventName, String priority,
			int year, int month, int day, int hour, String topics)
					throws AccountDoesNotExistException, InvalidPriorityException, 
					GuestCannotCreateEventException, StaffCannotCreateHighEventException,
					EventAlreadyExistsInAccountException, PromoterIsBusyException ;
	
	/**
	 * Gets an iterator of the set composed by all the
	 * events the user with the name given was invited or created.
	 * @param accountName Name of the account
	 * @return Iterator object of Event objects
	 * @throws AccountDoesNotExistException if the account is not registered in the system
	 */
	Iterator<Event> events(String accountName) throws AccountDoesNotExistException;
		
	/**
	 * Checks if there are accounts in the system.
	 * @return true if there are accounts registered, false otherwise
	 */
	boolean areAnyAccountsRegistered();
	
	/**
	 * Checks if the user with the name given is staff.
	 * @param accountName Name of the account
	 * @return true if the account is staff, false otherwise
	 */
	boolean isUserStaff(String accountName);
	
	/**
	 * Checks if the event has high priority.
	 * @param promoterName Name of the event's promoter
	 * @param eventName Name of the event
	 * @return true if the event is high priority, false otherwise
	 */
	boolean isEventHighPriority(String promoterName, String eventName);
	
	/**
	 * Checks if the user with the name given is busy at the time of
	 * the event with the name given.
	 * @param invitee Name of the user
	 * @param promoter Name of the event's promoter
	 * @param eventName Name of the event which date we want to access
	 * @return true if the user is occupied, false otherwise
	 */
	boolean isUserBusyWithOwnEvent(String invitee, String promoter, String eventName);
	
	/**
	 * Handles only the exceptions from the invite method.
	 * @param invitee Name of the user invited to the event
	 * @param promoter Name of the promoter to the 
	 * @param eventName Name of the event
	 * @throws AccountDoesNotExistException if the name of the account given
	 * 										does not exist
	 * @throws EventDoesNotExistInAccountException if the event does not exist in 
	 * 											   promoter's account
	 * @throws AccountAlreadyInvitedToEventException if the invitee was already invited
	 * 												 to that event
	 * @throws AccountAlreadyAcceptedOtherEventException if the invitee has already accepted
	 * 													 another event at that time
	 */
	void handleInviteExceptions(String invitee, String promoter, String eventName)
			throws AccountDoesNotExistException, EventDoesNotExistInAccountException,
			AccountAlreadyInvitedToEventException, AccountAlreadyAcceptedOtherEventException;
	
	/**
	 * Case of the invite command where:
	 * - the invitee is staff;
	 * - the event with eventName and promoter given is high priority;
	 * - the invitee is busy at the time of the event with its own event.
	 * @param invitee Name of the invitee
	 * @param promoter Name of the event's promoter
	 * @param eventName Name of the event
	 * @return String object representing the name of the invitee's event
	 * 		   that he had to cancel (remove)
	 */
	String inviteCase1(String invitee, String promoter, String eventName);
	
	/**
	 * Case of the invite command where:
	 * - the invitee is staff;
	 * - the event with eventName and promoter given is high priority;
	 * - the invitee is busy at the time of the event with another event.
	 * 	 (that is not his own)
	 * @param invitee Name of the invitee
	 * @param promoter Name of the event's promoter
	 * @param eventName Name of the event
	 * @return String array with the name of the event that the invitee was 
	 * 		   previously attending and the name of its promoter (in this order)
	 */
	String[] inviteCase2(String invitee, String promoter, String eventName);
	
	/**
	 * Case of the invite command where:
	 * - the invitee is staff;
	 * - the event with eventName and promoter given is high priority;
	 * - the invitee is not busy at the time of the event.
	 * @param invitee Name of the invitee
	 * @param promoter Name of the event's promoter
	 * @param eventName Name of the event
	 * @return Iterator object of events that the invitee was invited to
	 * 		   at the time of the event (which could be null, in case there
	 * 		   were no invitations at that time)
	 */
	Iterator<Event> inviteCase3(String invitee, String promoter, String eventName);
	
	/**
	 * Case of the invite command where one of the two options happens:
	 * - the invitee is not staff;
	 * - the event with eventName and promoter given is not high priority.
	 * @param invitee Name of the invitee
	 * @param promoter Name of the event's promoter
	 * @param eventName Name of the event
	 */
	void inviteCase4(String invitee, String promoter, String eventName);
	
	/**
	 * Checks if the user is busy at the time of the event with the name given.
	 * @param invitee Name of the user invited
	 * @param promoter Name of the event's promoter
	 * @param eventName Name of the event
	 * @return true if the user was invited, false otherwise
	 */
	boolean isUserBusy(String invitee, String promoter, String eventName);
	
	/**
	 * User with the name "invitee" responds to the invitation for the event
	 * with the name and promoter name given.
	 * @param invitee Name of the user invited, which is responding to the invitation
	 * @param promoter Name of the event's promoter
	 * @param eventName Name of the event
	 * @param response Response of the invitee to the invitation
	 * @return Iterator object of the events rejected by the invitee because he
	 * 		   accepted the event invitation that is at the same time as those
	 * 		   rejected events. Could be null if: the response was "reject" or
	 * 		   if there were no invitations at the time of the event with "eventName"
	 * 	 	   and promoter named "promoter"
	 * @throws AccountDoesNotExistException if the name of any of the accounts given
	 * 										does not exist
	 * @throws UnknownResponseException if the response does not correspond to the ones expected
	 * @throws EventDoesNotExistInAccountException if the event does not exist in 
	 * 											   promoter's account
	 * @throws AccountNotInvitedToEventException if the invitee was not invited to the event
	 * @throws AccountAlreadyRespondedException if the invitee has already responded to the
	 * 											event's invitation
	 * @throws AccountAlreadyAcceptedOtherEventException if the invitee has already accepted the
	 * 													 invitation of another event
	 */
	Iterator<Event> response(String invitee, String promoter, String eventName, String response)
			throws AccountDoesNotExistException, UnknownResponseException,
			EventDoesNotExistInAccountException, AccountNotInvitedToEventException,
			AccountAlreadyRespondedException, AccountAlreadyAcceptedOtherEventException;
	
	/**
	 * Gets detailed information of an event with the promoter and name given.
	 * @param promoter Name of the event's promoter
	 * @param eventName Name of the event
	 * @return Iterator of map entries, consisting of accounts invited to the
	 * 	       specified event with their corresponding status of invitation
	 * @throws AccountDoesNotExistException if the name of the account given
	 * 										does not exist
	 * @throws EventDoesNotExistInAccountException if the event does not exist in 
	 * 											   promoter's account
	 */
	Iterator<Map.Entry<String, InviteStatus>> event(String promoter, String eventName)
			throws AccountDoesNotExistException, EventDoesNotExistInAccountException;
	
	/**
	 * Gets the date of the event with the promoter and name given.
	 * @param promoter Name of the event's promoter
	 * @param eventName Name of the event
	 * @return String representing a formatted LocalDateTime object,
	 * 		   which is the event's date
	 */
	String getEventFormattedDate(String promoter, String eventName);
	
	/**
	 * Gets an iterator of all events that cover at least one
	 * of the topics in the array given as input
	 * @param topics Array of String objects
	 * @return Iterator of Event objects that have topics in common
	 * 		   with the list given, ordered by:
	 * 		   - the number of common topics, from higher to lower.
	 * 		   In case of draw:
	 * 		   - the name of the events, in alphabetical order
	 */
	Iterator<Event> topics(String[] topics);
}
