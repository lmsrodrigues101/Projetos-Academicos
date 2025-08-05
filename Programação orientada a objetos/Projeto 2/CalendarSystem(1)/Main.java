import java.util.Scanner;
import calendar.*;
import calendar.Event.InviteStatus;
import calendar.exceptions.*;
import java.util.Iterator;
import java.util.Map.Entry;


/**
 * Main program for the Office Calendar program.
 * @authors: Gustavo Sousa, Leandro Rodrigues
 */
public class Main {
	
	/**
	 * Feedback given by the program
	 */
	private static final String REGISTER = "register - registers a new account\n"; 
	private static final String ACCOUNTS = "accounts - lists all registered accounts\n";
	private static final String CREATE = "create - creates a new event\n";
	private static final String EVENTS = "events - lists all events of an account\n";
	private static final String INVITE = "invite - invites an user to an event\n";
	private static final String RESPONSE = "response - response to an invitation\n";
	private static final String EVENT = "event - shows detailed information of an event\n";
 	private static final String TOPICS = "topics - shows all events that cover a list of topics\n";
	private static final String HELP = "help - shows the available commands\n";
	private static final String EXIT = "exit - terminates the execution of the program\n";
	private static final String UNKNOWN = "Unknown command %s. Type help to see available commands.\n";
	private static final String ACCOUNT_ALREADY_EXISTS = "Account %s already exists.\n";
	private static final String UNKNOWN_ACCOUNT_TYPE = "Unknown account type.";
	private static final String ACCOUNT_REGISTERED = "%s was registered.\n";
	private static final String NO_ACCOUNTS = "No account registered.";
	private static final String EVENT_SCHEDULED = "%s is scheduled.\n";
	private static final String ACCOUNT_DOES_NOT_EXIST = "Account %s does not exist.\n";
	private static final String ACCOUNT_IS_BUSY = "Account %s already attending another event.\n";
	private static final String EVENT_NON_EXISTENT = "%s does not exist in account %s.\n";
	private static final String EVENT_REJECTED = "%s promoted by %s was rejected.\n";
	
	/**
	 * Enum that defines user's commands.
	 */
	private enum Command {
		REGISTER, ACCOUNTS, CREATE, EVENTS, INVITE,
		RESPONSE, EVENT, TOPICS, HELP, EXIT, UNKNOWN
	}
	
	/**
	 * Main method. Invokes the command interpreter.
	 * @param args command-line arguments (not used in this program)
	 */
	public static void main(String[] args) {
		commandInterpreter();
	}
	
	/**
	 * Command interpreter.
	 */
	private static void commandInterpreter() {
		Scanner in = new Scanner(System.in);
		Calendar calendar = new CalendarClass();
		String stringCmd = in.next().toUpperCase().trim();
		Command cmd = getCommand(stringCmd);
		while(!cmd.equals(Command.EXIT)){
			switch(cmd) {
				case HELP -> System.out.printf("Available commands:\n" + REGISTER + ACCOUNTS + CREATE +
										   	   EVENTS + INVITE + RESPONSE + EVENT + TOPICS + HELP + EXIT);
				case REGISTER -> executeRegister(in, calendar);
				case ACCOUNTS -> executeAccounts(in, calendar);
				case CREATE -> executeCreate(in, calendar);
				case EVENTS -> executeEvents(in, calendar);
				case INVITE -> executeInvite(in, calendar);
				case RESPONSE -> executeResponse(in, calendar);
				case EVENT -> executeEvent(in, calendar);
				case TOPICS -> executeTopics(in, calendar);
				case UNKNOWN -> System.out.printf(UNKNOWN, stringCmd);
				default -> {}
			}
			stringCmd = in.next().toUpperCase().trim();
			cmd = getCommand(stringCmd);
		}
		System.out.println("Bye!");	
		in.close();
	}
	
	/**
	 * If it is an expected command, returns the corresponding value 
	 * from the enum Command, otherwise, returns the enum Command UNKNOWN
	 * @param stringCmd String object, which is the command given
	 * @return Command Enum value that represents the command given
	 */
	private static Command getCommand(String stringCmd)  {
		try {
			return Command.valueOf(stringCmd);
		} 
		catch (IllegalArgumentException e) {
			return Command.UNKNOWN;
		}
	}
	
	/**
	 * Registers a new account in the system.
	 * @param in Scanner object that reads user input
	 * @param calendar Calendar object, which is the calendar system
	 */
	private static void executeRegister(Scanner in, Calendar calendar) {
		String accountName = in.next().trim();
		String accountType = in.nextLine().trim();
		try {
			calendar.register(accountName, accountType);
			System.out.printf(ACCOUNT_REGISTERED, accountName);
		}
		catch (AccountAlreadyExistsException e) {
			System.out.printf(ACCOUNT_ALREADY_EXISTS, accountName);
		}
		catch (InvalidTypeException e) {
			System.out.println(UNKNOWN_ACCOUNT_TYPE);
		}
	}
	
	/**
	 * Lists all registered accounts.
	 * @param in Scanner object that reads user input
	 * @param calendar Calendar object, which is the calendar system
	 */
	private static void executeAccounts(Scanner in, Calendar calendar) {
		if (calendar.areAnyAccountsRegistered()) {
			Iterator<Entry<String, Account>> it = calendar.accounts();
			System.out.println("All accounts:");
			while (it.hasNext()) {
				Account account = it.next().getValue();
				System.out.println(account.getName() + " [" + account.getType() + "]");
			}
		}
		else
			System.out.println(NO_ACCOUNTS);
	}

	/**
	 * Creates an event.
	 * @param in Scanner object that reads user input
	 * @param calendar Calendar object, which is the calendar system
	 */
	private static void executeCreate(Scanner in, Calendar calendar) {
		String accountName = in.nextLine().trim();
		String eventName = in.nextLine().trim();
		String priority = in.next().trim();
		int year = in.nextInt(); 
		int month = in.nextInt();
		int day = in.nextInt();
		int hour = in.nextInt();
		in.nextLine();
		String topics = in.nextLine().trim();
		try{
			calendar.createEvent(accountName, eventName, priority, year, month, day, hour, topics);
			System.out.printf(EVENT_SCHEDULED, eventName);
		}
		catch(AccountDoesNotExistException e){
			System.out.printf(ACCOUNT_DOES_NOT_EXIST, accountName);
		}
		catch(InvalidPriorityException e){
			System.out.printf("Unknown priority type.\n");
		}
		catch (GuestCannotCreateEventException e){
			System.out.printf("Guest account %s cannot create events.\n", accountName);
		}
		catch (StaffCannotCreateHighEventException e){
			System.out.printf("Account %s cannot create high priority events.\n", accountName);
		}
		catch (EventAlreadyExistsInAccountException e){
			System.out.printf("%s already exists in account %s.\n", eventName, accountName);
		}
		catch (PromoterIsBusyException e){
			System.out.printf("Account %s is busy.\n", accountName);
		}
	}
	
	/**
	 * Lists all events of an account.
	 * @param in Scanner object that reads user input
	 * @param calendar Calendar object, which is the calendar system
	 */
	private static void executeEvents(Scanner in, Calendar calendar) {
		String accountName = in.nextLine().trim();
		try {
			Iterator<Event> it = calendar.events(accountName);
			if (!it.hasNext())
				System.out.printf("Account %s has no events.\n", accountName);
			else {
				System.out.printf("Account %s events:\n", accountName);
				while(it.hasNext()) {
					Event event = it.next();
					System.out.println(event.getName() + " status " +
						 		   "[invited " + event.numberOfInvitations() + "] " +
								   "[accepted " + event.numberOfAcceptedInvitations() + "] " +
								   "[rejected " + event.numberOfRejectedInvitations() + "] " +
								   "[unanswered " + event.numberOfUnansweredInvitations() + "]");
				}
			}
		}
		catch (AccountDoesNotExistException e) {
			System.out.printf(ACCOUNT_DOES_NOT_EXIST, accountName);
		}
	}
	
	/**
	 * Invites an user to an event.
	 * @param in Scanner object that reads user input
	 * @param calendar Calendar object, which is the calendar system
	 */
	private static void executeInvite(Scanner in, Calendar calendar) {
		String invitee = in.next().trim();
		String promoter = in.next().trim();
		String eventName = in.nextLine().trim();
		try {
			calendar.handleInviteExceptions(invitee, promoter, eventName);
			// Staff user AND High event
			if (calendar.isUserStaff(invitee) && calendar.isEventHighPriority(promoter, eventName)) 
				executeInviteStaffandHighEventCase(invitee, promoter, eventName, calendar);
			// If user Not Staff OR event Not High
			else {
				calendar.inviteCase4(invitee, promoter, eventName);
				System.out.println(invitee + " was invited.");
			}
		}
		catch (AccountDoesNotExistException e) {
			String invalidAccountName = e.getMessage();
			System.out.printf(ACCOUNT_DOES_NOT_EXIST, invalidAccountName);
		}
		catch(EventDoesNotExistInAccountException e) {
			System.out.printf(EVENT_NON_EXISTENT, eventName, promoter);
		}
		catch(AccountAlreadyInvitedToEventException e) {
			System.out.printf("Account %s was already invited.\n", invitee);
		}
		catch (AccountAlreadyAcceptedOtherEventException e) {
			System.out.printf(ACCOUNT_IS_BUSY, invitee);
		}
	}
	
	/**
	 * Invites a staff user to a high event.
	 * @param invitee Name of the user invited to the event
	 * @param promoter Name of the evebt's promoter
	 * @param eventName Name of the event
	 * @param calendar Calendar object, which is the calendar system
	 */
	private static void executeInviteStaffandHighEventCase(String invitee, String promoter, 
														   String eventName, Calendar calendar) {
		System.out.printf(invitee + " accepted the invitation.\n");
		// User is busy with own event
		if (calendar.isUserBusyWithOwnEvent(invitee, promoter, eventName)) {
			String eventRemoved = calendar.inviteCase1(invitee, promoter, eventName);
			System.out.printf("%s promoted by %s was removed.\n", eventRemoved, invitee);
		}
		// User is busy (with other event)
		else if (calendar.isUserBusy(invitee, promoter, eventName)) {
			String[] eventRejected = calendar.inviteCase2(invitee, promoter, eventName);
			System.out.printf(EVENT_REJECTED, eventRejected[0], eventRejected[1]);
		}
		// User is not busy
		else {
			Iterator<Event> eventsRejected = calendar.inviteCase3(invitee, promoter, eventName);
			while (eventsRejected.hasNext()) {
				Event event = eventsRejected.next();
				System.out.printf(EVENT_REJECTED, event.getName(), event.getPromoter());
			}
		}
	}
	
	/**
	 * An user responds to an event invitation.
	 * @param in Scanner object that reads user input
	 * @param calendar Calendar object, which is the calendar system
	 */
	private static void executeResponse(Scanner in, Calendar calendar) {
		String invitee = in.nextLine().trim();
		String promoter = in.next().trim();
		String eventName = in.nextLine().trim();
		String response = in.nextLine().trim();
		try {
			Iterator<Event> rejectedEvents = calendar.response(invitee, promoter, eventName, response);
			System.out.printf("Account %s has replied %s to the invitation.\n", invitee, response);
			if (response.equals(Calendar.ACCEPT)) {
				while (rejectedEvents.hasNext()) {
					Event event = rejectedEvents.next();
					System.out.printf(EVENT_REJECTED, event.getName(), event.getPromoter());
				}
			}
		}
		catch (AccountDoesNotExistException e) {
			String invalidAccountName = e.getMessage();
			System.out.printf(ACCOUNT_DOES_NOT_EXIST, invalidAccountName);
		} 
		catch (UnknownResponseException e) {
			System.out.println("Unknown event response.");
		}
		catch (EventDoesNotExistInAccountException e) {
			System.out.printf(EVENT_NON_EXISTENT, eventName, promoter);
		}
		catch (AccountNotInvitedToEventException e) {
			System.out.printf("Account %s is not on the invitation list.\n", invitee);
		}
		catch (AccountAlreadyRespondedException e) {
			System.out.printf("Account %s has already responded.\n", invitee);
		}
		catch (AccountAlreadyAcceptedOtherEventException e) {
			System.out.printf(ACCOUNT_IS_BUSY, invitee);
		}
	}
	
	/**
	 * Shows detailed information of an event.
	 * @param in Scanner object that reads user input
	 * @param calendar Calendar object, which is the calendar system
	 */
	private static void executeEvent(Scanner in, Calendar calendar) {
		String accountName = in.next().trim();
		String eventName = in.nextLine().trim();
		try {
			Iterator<Entry<String, InviteStatus>> it = calendar.event(accountName, eventName);
			String formattedDate = calendar.getEventFormattedDate(accountName, eventName);
			System.out.printf("%s occurs on %s:\n", eventName, formattedDate);
			while (it.hasNext()) {
				Entry<String, InviteStatus> entry = it.next();
				System.out.printf("%s [%s]\n", entry.getKey(), entry.getValue().getDescription());
			}
		}
		catch (AccountDoesNotExistException e) {
			System.out.printf(ACCOUNT_DOES_NOT_EXIST, accountName);
		}
		catch (EventDoesNotExistInAccountException e) {
			System.out.printf(EVENT_NON_EXISTENT, eventName, accountName);
		}
	}
	
	/**
	 * Shows all events that cover a list of topics.
	 * @param in Scanner object that reads user input
	 * @param calendar Calendar object, which is the calendar system
	 */
	private static void executeTopics(Scanner in, Calendar calendar) {
		String topics = in.nextLine().trim();
		String[] listTopics = topics.split(" ");
		Iterator<Event> eventsIterator = calendar.topics(listTopics);
		if (!eventsIterator.hasNext())
			System.out.println("No events on those topics.");
		else {
			System.out.printf("Events on topics %s:\n", topics);
			while(eventsIterator.hasNext()) {
				Event event = eventsIterator.next();
				System.out.printf("%s promoted by %s on %s\n", event.getName(), event.getPromoter(), event.getStringOfTopics());
			}
		}
	}
	
}