package calendar;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * Interface that represent an event.
 * @authors Gustavo Sousa, Leandro Rodrigues
 */
public interface Event {
	
	/*
	 * Constants for describing each InviteStatus enum member
	 */
	static final String ACCEPTED_DESCRIPTION = "accept";
	static final String DECLINED_DESCRIPTION = "reject";
	static final String UNANSWERED_DESCRIPTION = "no_answer";
	
	/**
	 * Constants representing the initial values of accepted and
	 * rejected invitations (when the event is created)
	 */
	static final int INITIAL_ACCEPTED_INVITATIONS = 1;
	static final int INITIAL_REJECTED_INVITATIONS = 0;
	
	/**
	 * Enum of all the possible status for an invite
	 */
	enum InviteStatus {
		
		ACCEPTED(ACCEPTED_DESCRIPTION), 
		DECLINED(DECLINED_DESCRIPTION), 
		UNANSWERED(UNANSWERED_DESCRIPTION);
		
		/**
		 * String that describes a member of InviteStatus enum
		 */
		private final String description;
		
		/**
		 * Constructor of InviteStatus enum members.
		 * @param description Description associated with the enum member
		 */
		private InviteStatus(String description) {
			this.description = description;
		}
		
		/**
		 * Gets the description of the enum member
		 * @return Description for the enum member
		 */
		public String getDescription() {
			return description;
		}
	}
	
	/**
	 * Gets the name of the event's promoter.
	 * @return Name of the promoter
	 */
	String getPromoter();
	
	/**
	 * Gets the name of the event.
	 * @return Name of the event
	 */
	String getName();
	
	/**
	 * Gets the date of the event.
	 * @return LocalDateTime object that represents the date of the event
	 */
	LocalDateTime getDate();
	
	/**
	 * Get the priority type of the event.
	 * @return Priority of the event
	 */
	String getPriority();
	
	/**
	 * Gets an iterator of the set composed by all the
	 * entries of the HashMap of invited accounts, including the promoter.
	 * @return Iterator object of Entry<Account, String>> objects
	 */
	Iterator<Entry<String, InviteStatus>> invitedAccountsIterator();
	
	/**
	 * Gets a single String object with the topics of the event.
	 * @return String with all the topics of the event
	 */
	String getStringOfTopics();
	
	/**
	 * Gets an array of the event's topics.
	 * @return List of Strings with the event's topics
	 */
	List<String> getTopics();
	
	/**
	 * Gets the number of people invited to the event.
	 * @return Number of people invited
	 */
	int numberOfInvitations();
	
	/**
	 * Gets the number of people that accepted the invite to the event.
	 * @return Number of people who accepted the invitation
	 */
	int numberOfAcceptedInvitations();
	
	/**
	 * Gets the number of people that rejected the invite to the event.
	 * @return Number of people who rejected the invitation
	 */
	int numberOfRejectedInvitations();
	
	/**
	 * Gets the number of people that did not answer the invite 
	 * to the event yet.
	 * @return Number of people who is yet to respond to the invitation
	 */
	int numberOfUnansweredInvitations();
	
	/**
	 * Checks if the user with the name given has already been invited
	 * to this event.
	 * @param accountName Name of the user
	 * @return true if the user has already been invited, false otherwise
	 */
	boolean hasUserAlreadyBeenInvited(String accountName);
	
	/**
	 * Adds a new user to the collection of invited users.
	 * @param accountName Name of the account invited
	 */
	void newInvitation(String accountName);
	
	/**
	 * Decrements by 1 the number of accepted invitations.
	 */
	void decrementAcceptedInvitation();
	
	/**
	 * The user with the name given accepts the invitation to the event.
	 * @param accountName Name of the user who accepted the invitation
	 */
	void newAcceptedInvitation(String accountName);
	
	/**
	 * The user with the name given rejects the invitation to the event.
	 * @param accountName Name of the user who rejected the invitation
	 */
	void newRejectedInvitation(String accountName);
	
	/**
	 * Checks if an account with the name given has already
	 * responded to the invitation for the event
	 * @param accountName Name of the account
	 * @return true if the account has already responded, false otherwise
	 */
	boolean hasAccountResponded(String accountName);
}
