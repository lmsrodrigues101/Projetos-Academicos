package TrainStation;

import dataStructures.Entry;
import dataStructures.Iterator;

import java.io.Serializable;


/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public interface ScheduleGets extends Serializable {

    /**
     * Gets the schedule's trainId
     * @return trainId
     */
    int getTrainId();

    /**
     * Gets an iterator of all the entries in the schedule
     * @return iterator
     */
    Iterator<Entry<Station, Time>> getEntries();

    /**
     * Gets the first station and time of the schedule
     * @return entry
     */
    Entry<Station, Time> getFirstEntry();



}
