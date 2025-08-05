package calendar;

/**
 * Class that represents an account of type Manager.
 * @authors Gustavo Sousa, Leandro Rodrigues
 */
public class ManagerAccountClass extends AbstractPromoterAccount {
	
	/**
	 * Constructs a ManagerAccountClass object, with its name. 
	 * @param accountName E-mail of the account
	 */
	protected ManagerAccountClass(String accountName) {
		super(accountName);
	}

	@Override
	public String getType() {
		return Calendar.MANAGER;
	}

}
