package calendar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Comparator class to compare and order events by topics.
 * @authors Gustavo Sousa, Leandro Rodrigues
 */
public class ComparatorByTopics implements Comparator<Event> {
	
	/**
	 * List of topics
	 */
	private final List<String> topics;
	
	/**
	 * Constructs ComparatorByTopics objects
	 * @param topics List of topics
	 */
	protected ComparatorByTopics(List<String> topics) {
		this.topics = topics;
	}

	@Override
	public int compare(Event o1, Event o2) {
		int numCommonTopicsO1 = countCommonTopics(o1);
		int numCommonTopicsO2 = countCommonTopics(o2);
		if (numCommonTopicsO1 != numCommonTopicsO2)
			return numCommonTopicsO2 - numCommonTopicsO1;
	    if(!o1.getName().equals(o2.getName()))
			return o1.getName().compareTo(o2.getName());
		return o1.getPromoter().compareTo(o2.getPromoter());
	}
	
	/**
	 * Counts the number of common topics.
	 * @param event Event object which we want to count the number
	 * 	      of common topics with the topics given
	 * @return Number of common topics
	 */
	private int countCommonTopics(Event event){
		List<String> list = new ArrayList<>(event.getTopics());
		list.retainAll(topics);
		return list.size();
	}
	

}
