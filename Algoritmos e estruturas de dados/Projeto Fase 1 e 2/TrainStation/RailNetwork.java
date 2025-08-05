package TrainStation;

import dataStructures.*;
import Exceptions.*;

import java.io.Serializable;


/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public interface RailNetwork extends Serializable {

    /**
     * Adds a line to the list
     * @param lineName
     * @param queue
     * @throws AlreadyExistLineException
     */
    void addLine(String lineName, Queue<String> queue) throws AlreadyExistLineException;

    /**
     * Removes a line from the list
     * @param lineName
     * @throws LineDoesNotExistException
     */
    void removeLine(String lineName) throws LineDoesNotExistException;

    /**
     * Calls the method getStationsIterator from line class
     * @param nameLine
     * @return iterator
     * @throws LineDoesNotExistException
     */
    Iterator<Station> getStationsIterator(String nameLine) throws LineDoesNotExistException;

    /**
     * Calls the method getLinesIterator from station class
     * @param stationName
     * @return iterator
     */
    Iterator<Entry<Line, Line>> getLinesIterator(String stationName);

    /**
     * Adds a new schedule to a line
     * @param lineName
     * @param trainNumber
     * @param schedule
     * @throws LineDoesNotExistException
     * @throws InvalidScheduleException
     */
    void insertTime(String lineName, int trainNumber, Queue<Entry<String,String>> schedule)
            throws LineDoesNotExistException, InvalidScheduleException;


    /**
     * Calls the method removeSchedule from line class
     * @param lineName
     * @param startStation
     * @param startTime
     * @throws LineDoesNotExistException
     * @throws InvalidScheduleException
     */
    void removeTime(String lineName, String startStation, String startTime)
            throws LineDoesNotExistException, InvalidScheduleException;

    /**
     * Calls the method getIteratorOfStartStation from line class
     *
     * @param lineName
     * @param startStation
     * @return iterator
     * @throws LineDoesNotExistException
     * @throws StartStationInexistentException
     */
    Iterator<Entry<Time,Schedule>> getSchedulesOfStartStation(String lineName, String startStation)
            throws LineDoesNotExistException, StartStationInexistentException;

    /**
     * Calls the method getBestSchedule from line class
     * @param lineName
     * @param startStation
     * @param endStation
     * @param expArrivalTime
     * @return best schedule
     * @throws LineDoesNotExistException
     * @throws StartStationInexistentException
     * @throws ImpossibleRouteException
     */
    Schedule getBestTime(String lineName, String startStation, String endStation, String expArrivalTime)
            throws LineDoesNotExistException, StartStationInexistentException, ImpossibleRouteException;

    /**
     * Calls the method getSchedules from the station class
     * @param stationName
     * @return iterator
     * @throws StationDoesNotExistException
     */
    Iterator<Entry<Integer,Time>> getSchedulesByStation(String stationName) throws StationDoesNotExistException;




}
