package TrainStation;
import Exceptions.LineDoesNotExistException;
import Exceptions.StartStationInexistentException;
import dataStructures.*;

import java.io.Serializable;


/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public interface LineGets extends Serializable {

    /**
     * Gets the line name
     * @return name
     */
    String getName();


    /**
     * Gets and iterator for the stations list
     * @return iterator
     */
    Iterator<Station> getStationsIterator();


    /**
     * Gets the schedule of the first station in the list
     * @param departureStation
     * @return iterator
     * @throws StartStationInexistentException
     */
    Iterator<Entry<Time, Schedule>> getIteratorOfStartStationNEW(Station departureStation)
            throws LineDoesNotExistException, StartStationInexistentException;
}
