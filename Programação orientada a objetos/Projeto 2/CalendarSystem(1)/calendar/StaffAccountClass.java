package calendar;

/**
 * Class that represents an account of type Staff.
 * @authors Gustavo Sousa, Leandro Rodrigues
 */
public class StaffAccountClass extends AbstractPromoterAccount {

	/**
	 * Constructs a StaffAccountClass object, with its name. 
	 * @param accountName E-mail of the account
	 */
    public StaffAccountClass(String accountName){
        super(accountName);
    }
    
    @Override
	public String getType() {
		return Calendar.STAFF;
	}
}
