package TrainStation;

import dataStructures.Entry;
import dataStructures.Iterator;

import java.io.Serializable;

/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public interface StationGets  extends Serializable {

    /**
     * Gets back the station name
     * @return name
     */
    String getName();

    /**
     * Gets back the lines of the station
     * @return iterator
     */
    Iterator<Entry<Line, Line>> getLinesIterator();

    /**
     * Gets back the schedules of the station
     * @return iterator
     */
    Iterator<Entry<Integer, Time>> getSchedules();
}
