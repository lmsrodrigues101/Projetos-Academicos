package calendar;

/**
 * Class that represents an account of type Guest.
 * @authors Gustavo Sousa, Leandro Rodrigues
 */
public class GuestAccountClass extends AbstractAccount {

    /**
	 * Constructs a GuestAccountClass object, with its name. 
	 * @param accountName E-mail of the account
	 */
    protected GuestAccountClass (String accountName){
    	super(accountName);
    }
    
    @Override
	public String getType() {
		return Calendar.GUEST;
	}
}
