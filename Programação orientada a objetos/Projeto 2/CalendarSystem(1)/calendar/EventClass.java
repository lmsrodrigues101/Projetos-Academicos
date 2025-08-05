package calendar;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class that represent an event.
 * @authors Gustavo Sousa, Leandro Rodrigues
 */
public class EventClass implements Event {
		
	/**
	 * Promoter of the event
	 */
	private String promoterName;
	
	/**
	 * Name of the event
	 */
	private String eventName;
	
	/**
	 * Date of the event
	 */
	private LocalDateTime date;
	
	/**
	 * Topics of the event in a list
	 */
	private List<String> listTopics;
	
	/**
	 * Topics of the event in a single String
	 */
	private String topics;
	
	/**
	 * Priority of the event
	 */
	private String priority;
	
	/**
	 * Map of accounts invited for the event, organized by
	 * account -> invite status
	 */
	private Map<String, InviteStatus> invitedAccounts;
	
	/**
	 * List of Map entries with accounts names and their corresponding
	 * invitation status, in order of invitation
	 */
	private List<Entry<String, InviteStatus>> invitedAccountsInOrder;
	
	/**
	 * Number of accepted invitations
	 */
	private int acceptedInvites;
	
	/**
	 * Number of rejected invitations
	 */
	private int rejectedInvites;
	
	/**
	 * Constructs an EventClass object.
	 * @param accountName Name of the event's promoter
	 * @param eventName Name of the event
	 * @param priority Priority of the event
	 * @param date LocalDateTime object representing the event date
	 * @param topics Array of string with the topics of the event
	 */
	protected EventClass(String accountName, String eventName, String priority,
					  LocalDateTime date, String topics) {
		promoterName = accountName;
		this.eventName = eventName;
		this.priority = priority;
		this.date = date;
		this.topics = topics;
		this.listTopics = new ArrayList<>();
		Collections.addAll(this.listTopics, topics.split(" "));
		invitedAccounts = new HashMap<>();
		invitedAccounts.put(accountName, InviteStatus.ACCEPTED);
		invitedAccountsInOrder = new ArrayList<>();
		invitedAccountsInOrder.add(Map.entry(accountName, InviteStatus.ACCEPTED));
		acceptedInvites = INITIAL_ACCEPTED_INVITATIONS;
		rejectedInvites = INITIAL_REJECTED_INVITATIONS;
	}

	@Override
	public LocalDateTime getDate() {
		return date;
	}
	
	@Override
	public String getName() {
		return eventName;
	}

	@Override
	public String getPromoter() {
		return promoterName;
	}

	@Override
	public String getPriority() {
		return priority;
	}

	@Override
	public Iterator<Entry<String, InviteStatus>> invitedAccountsIterator() {
		return invitedAccountsInOrder.iterator();
	}
	
	@Override
	public String getStringOfTopics() {
		return topics;
	}

	@Override
	public List<String> getTopics() {
		return listTopics;
	}

	@Override
	public int numberOfInvitations() {
		return invitedAccounts.size();
	}

	@Override
	public int numberOfAcceptedInvitations() {
		return acceptedInvites;
	}

	@Override
	public int numberOfRejectedInvitations() {
		return rejectedInvites;
	}

	@Override
	public int numberOfUnansweredInvitations() {
		return numberOfInvitations() - acceptedInvites - rejectedInvites;
	}

	@Override
	public boolean hasUserAlreadyBeenInvited(String accountName) {
		return invitedAccounts.containsKey(accountName);
	}

	@Override
	public void newInvitation(String accountName) {
		invitedAccounts.put(accountName, InviteStatus.UNANSWERED);	
		invitedAccountsInOrder.add(Map.entry(accountName, InviteStatus.UNANSWERED));
	}
	
	@Override
	public void newAcceptedInvitation(String accountName) {
		invitedAccounts.put(accountName, InviteStatus.ACCEPTED);
		int index = 0;
		boolean found = false;
		while (index < invitedAccountsInOrder.size() && !found) {
			if (!invitedAccountsInOrder.get(index).getKey().equals(accountName))
				index++;
			else {
				found = true;
				invitedAccountsInOrder.set(index, Map.entry(accountName, InviteStatus.ACCEPTED));
			}
		}
		if (found == false)
			invitedAccountsInOrder.add(Map.entry(accountName, InviteStatus.ACCEPTED));
		incrementAcceptedInvitation();
	}
	
	@Override
	public void newRejectedInvitation(String accountName) {
		invitedAccounts.put(accountName, InviteStatus.DECLINED);
		int index = 0;
		boolean found = false;
		while (index < invitedAccountsInOrder.size() && !found) {
			if (!invitedAccountsInOrder.get(index).getKey().equals(accountName))
				index++;
			else {
				found = true;
				invitedAccountsInOrder.set(index, Map.entry(accountName, InviteStatus.DECLINED));
			}
		}
		if (found == false) 
			invitedAccountsInOrder.add(Map.entry(accountName, InviteStatus.DECLINED));
		incrementRejectedInvitation();
	}
	
	/**
	 * Increments by 1 the number of accepted invitations.
	 */
	private void incrementAcceptedInvitation() {
		acceptedInvites++;
	}

	/**
	 * Increments by 1 the number of rejected invitations.
	 */
	private void incrementRejectedInvitation() {
		rejectedInvites++;
	}

	@Override
	public void decrementAcceptedInvitation() {
		acceptedInvites--;
	}
	
	@Override
	public boolean hasAccountResponded(String accountName) {
		return !invitedAccounts.get(accountName).equals(InviteStatus.UNANSWERED);
	}
}
