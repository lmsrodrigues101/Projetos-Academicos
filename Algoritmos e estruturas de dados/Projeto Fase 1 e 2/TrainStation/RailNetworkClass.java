package TrainStation;

import Exceptions.*;
import dataStructures.*;

import java.io.Serial;


/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public class RailNetworkClass implements RailNetwork{

    /**
	 * 
	 */
    private static final long serialVersionUID = 0L;

	private Dictionary<String,Line> lines;

    private Dictionary<String,Station> stations;

    /**
     * Initializes the lines and stations lists
     */
    public RailNetworkClass() {
        this.lines = new InsensitiveSepChainHashTable<>();
        this.stations = new InsensitiveSepChainHashTable<>();
    }

    @Override
    public void addLine(String lineName, Queue<String> queue) throws AlreadyExistLineException {
        if (lines.find(lineName) != null)
            throw new AlreadyExistLineException();

        Line line = new LineClass(lineName);
        while (!queue.isEmpty()) {
            String stationName = queue.dequeue();
            Station station = stations.find(stationName);
            if (station == null) {
                station = new StationClass(stationName);
                stations.insert(stationName, station);
            }
            station.addLine(line);
            line.addStation(station);
        }
        lines.insert(lineName, line);
    }
    @Override
    public void removeLine(String lineName) throws LineDoesNotExistException {
       Line line = lines.remove(lineName);
        if (line == null)
            throw new LineDoesNotExistException();
        Iterator<Station> iterator = line.getStationsIterator();
        line.removeScheduleOfStation();
        while (iterator.hasNext()) {
            Station station = iterator.next();
            if(station.getNumberOfLines() == 1){
                stations.remove(station.getName());
            }else {
                station.removeLine(line);
            }

        }
    }
    @Override
    public Iterator<Station> getStationsIterator(String lineName) throws LineDoesNotExistException {
        Line line = lines.find(lineName);
        if (line == null)
            throw new LineDoesNotExistException();
        return line.getStationsIterator();
    }

    @Override
    public Iterator<Entry<Line,Line>> getLinesIterator(String stationName) {
        Station station = stations.find(stationName);
        if(station == null)
            throw new StationDoesNotExistException();
        return station.getLinesIterator();
    }


    @Override
    public void insertTime(String lineName, int trainNumber, Queue<Entry<String,String>> queueSchedule)
            throws LineDoesNotExistException, InvalidScheduleException {

        Line line = lines.find(lineName);
        if (line == null)
            throw new LineDoesNotExistException();

        line.addSchedule(trainNumber, queueSchedule);
    }


    @Override
    public void removeTime(String lineName, String startStation, String startTime)
            throws LineDoesNotExistException, ScheduleDoesNotExistException{

        Line line = lines.find(lineName);
        if (line == null)
            throw new LineDoesNotExistException();
        Station departureStation = stations.find(startStation);
        line.removeSchedule(departureStation, startTime);
    }
    @Override
    public Iterator<Entry<Time,Schedule>> getSchedulesOfStartStation(String lineName, String startStation)
            throws LineDoesNotExistException, StartStationInexistentException {
        Line line = lines.find(lineName);
        if (line == null)
            throw new LineDoesNotExistException();
        Station departureStation = stations.find(startStation);
        return line.getIteratorOfStartStationNEW(departureStation);
    }
    @Override
    public Iterator<Entry<Integer, Time>> getSchedulesByStation(String stationName) throws StationDoesNotExistException{
        Station station = stations.find(stationName);
        if(station == null){
            throw new StationDoesNotExistException();
        }
        return station.getSchedules();
    }
    @Override
    public Schedule getBestTime(String lineName, String startStation, String endStation, String expArrivalTime)
            throws LineDoesNotExistException, StartStationInexistentException, ImpossibleRouteException {

        Line line = lines.find(lineName);
        if (line == null)
            throw new LineDoesNotExistException();
        Station departureStation = stations.find(startStation);
        Station arrivalStation = stations.find(endStation);
        return line.getBestSchedule(departureStation,arrivalStation,expArrivalTime);
    }

}


