package TrainStation;

import dataStructures.*;
import Exceptions.*;


/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public interface Line extends LineGets, Comparable<Line> {


    /**
     * Removes a schedule from the list
     * @param startStation
     * @param startTime
     * @throws ScheduleDoesNotExistException
     */
    void removeSchedule(Station startStation, String startTime) throws ScheduleDoesNotExistException;




    /**
     * Gets the best schedule between two schedules
     * @param departureStation
     * @param arrivalStation
     * @param date
     * @return schedule
     * @throws NonExistentDepartureStationException
     * @throws ImpossibleRouteException
     */
    Schedule getBestSchedule(Station departureStation,Station arrivalStation, String date)
            throws NonExistentDepartureStationException, ImpossibleRouteException;


    /**
     * Adds a station
     * @param station
     */
    void addStation(Station station);

    /**
     * Removes all schedules when a line is removed from the program
     */
    void removeScheduleOfStation();


    /**
     * Adds a schedule
     * @param trainNumber
     * @param queueSchedule
     */
    void addSchedule(int trainNumber, Queue<Entry<String, String>> queueSchedule) throws InvalidScheduleException;
}
