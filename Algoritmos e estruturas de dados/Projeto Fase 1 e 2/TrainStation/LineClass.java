package TrainStation;
import Exceptions.*;
import dataStructures.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;


/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public class LineClass implements Line {

    /**
	 * 
	 */
    private static final long serialVersionUID = 0L;

	private transient String name;

    private transient List<Station> stations;

    private transient OrderedDictionary<Time, Schedule> departureSchedules;

    private transient OrderedDictionary<Time, Schedule> arrivalSchedules;

    /**
     * Initializes a line with its name, a list of stations and a sorted list of schedules.
     *
     * @param name the name of the line
     */
    public LineClass(String name) {
        this.name = name;
        this.stations = new DoubleList<>();
        this.departureSchedules = new AVLTree<>();
        this.arrivalSchedules = new AVLTree<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Iterator<Station> getStationsIterator() {
        return stations.iterator();
    }


    @Override
    public Iterator<Entry<Time, Schedule>> getIteratorOfStartStationNEW(Station departureStation) {
        if (departureStation == null || isStationTerminal(departureStation.getName())) {
            throw new StartStationInexistentException();
        }
        if (departureStation.equals(stations.getFirst()))
            return departureSchedules.iterator();

        return arrivalSchedules.iterator();
    }

    @Override
    public Schedule getBestSchedule(Station departureStation, Station arrivalStation, String date)
            throws NonExistentDepartureStationException, ImpossibleRouteException {

        if (departureStation == null || stations.find(departureStation) == -1)
            throw new NonExistentDepartureStationException();
        if (arrivalStation == null || stations.find(arrivalStation) == -1)
            throw new ImpossibleRouteException();

        Iterator<Entry<Time,Schedule>> schedulesIterator = departureSchedules.iterator();
            Schedule bestSchedule = null;
            int bestTime = -1;
            while (schedulesIterator.hasNext()) {
                Entry<Time,Schedule> entry = schedulesIterator.next();
                Schedule schedule = entry.getValue();
                int time = schedule.getTimeToStation(departureStation, arrivalStation, date);
                if (time == 0)
                    return schedule;
                if (time > 0) {
                    if (bestTime == -1 || time < bestTime) {
                        bestTime = time;
                        bestSchedule = schedule;
                    }
                }
            }
            if (bestSchedule != null) {
                return bestSchedule;
            }
            schedulesIterator = arrivalSchedules.iterator();
            while (schedulesIterator.hasNext()) {
                Entry<Time,Schedule> entry = schedulesIterator.next();
                Schedule schedule = entry.getValue();
                int time = schedule.getTimeToStation(departureStation, arrivalStation, date);
                if (time == 0)
                    return schedule;
                if (time > 0) {
                    if (bestTime == -1 || time < bestTime) {
                        bestTime = time;
                        bestSchedule = schedule;
                    }
                }
            }
            if (bestSchedule == null) {
                throw new ImpossibleRouteException();
            } else {
                return bestSchedule;
            }
    }

    @Override
    public void addStation(Station station) {
        stations.addLast(station);
    }

    @Override
    public void removeSchedule(Station departureStation, String startTime) throws ScheduleDoesNotExistException {

        if(departureStation.equals(stations.getFirst())){
            Schedule schedule = departureSchedules.remove(new TimeClass(startTime));
            if(schedule == null)
                throw new ScheduleDoesNotExistException();
            schedule.removeScheduleOfStation();
        }else{
            Schedule schedule = arrivalSchedules.remove(new TimeClass(startTime));
            if(schedule == null)
                throw new ScheduleDoesNotExistException();
            schedule.removeScheduleOfStation();
        }
    }

    @Override
    public void addSchedule(int trainNumber, Queue<Entry<String, String>> queueSchedule) throws InvalidScheduleException{
        String firstStationNameOnQueue = queueSchedule.peek().getKey();
        if (isStationTerminal(firstStationNameOnQueue)) {
            throw new InvalidScheduleException();
        }
        boolean isDepartureSchedule = firstStationNameOnQueue.equalsIgnoreCase(stations.getFirst().getName());
        List<Entry<Station, Time>> scheduleList = new DoubleList<>();
        Time departureTime = new TimeClass(queueSchedule.peek().getValue());
        if (isDepartureSchedule) {
            TwoWayIterator<Station> stationIt = stations.iterator();
            Time previous = null;
            while (stationIt.hasNext()) {
                Station station = stationIt.next();
                previous = getTime(queueSchedule, scheduleList, previous, station);
            }
        } else {
            TwoWayIterator<Station> stationIt = stations.iterator();
            Time next = null;
            stationIt.fullForward();
            while (stationIt.hasPrevious()) {
                Station station = stationIt.previous();
                next = getTime(queueSchedule, scheduleList, next, station);
            }
        }
        if (queueSchedule.isEmpty()) {
            Schedule schedule = new ScheduleClass(trainNumber, scheduleList);
            if (isDepartureSchedule) {
                if (isOvertakingDeparture(schedule, departureTime))
                    throw new InvalidScheduleException();
                departureSchedules.insert(departureTime, schedule);
            } else {
                if (isOvertakingArrival(schedule, departureTime))
                    throw new InvalidScheduleException();
                arrivalSchedules.insert(departureTime, schedule);
            }
            schedule.addStops();
        } else {
            throw new InvalidScheduleException();
        }
    }

    private Time getTime(Queue<Entry<String, String>> queueSchedule, List<Entry<Station, Time>> scheduleList, Time previous, Station station) {
        if (!queueSchedule.isEmpty() && station.getName().equalsIgnoreCase(queueSchedule.peek().getKey())) {
            Entry<String, String> currentEntry = queueSchedule.dequeue();
            Time currentTime = new TimeClass(currentEntry.getValue());
            if (previous != null && previous.compareTo(currentTime) >= 0) {
                throw new InvalidScheduleException();
            } else {
                previous = currentTime;
            }
            Entry<Station, Time> entryStationTime = new EntryClass<>(station, currentTime);
            scheduleList.addLast(entryStationTime);
        }
        return previous;
    }
    

    /**
     * Checks if a schedule overtakes another schedule on arrivalSchedules
     * @param schedule      the schedule to check
     * @param departureTime the departure time of the schedule
     * @return true if overtaking occurs, false otherwise
     */
    private boolean isOvertakingArrival(Schedule schedule, Time departureTime) {
        return overtake(schedule, departureTime, arrivalSchedules);
    }


    private boolean overtake(Schedule schedule, Time departureTime, OrderedDictionary<Time, Schedule> arrivalSchedules) {
        Iterator<Entry<Time,Schedule>> scheduleIt = arrivalSchedules.iterator();
        while (scheduleIt.hasNext()) {
            Entry<Time,Schedule> entry = scheduleIt.next();
            Schedule currentSchedule = entry.getValue();
            Entry<Station, Time> entryCurrent = currentSchedule.getFirstEntry();
            if (overtake(schedule, currentSchedule, departureTime, entryCurrent.getValue()))
                return true;
        }
        return false;
    }


    /**
     * Checks if a schedule overtakes another schedule on departureSchedules
     * @param schedule      the schedule to check
     * @param departureTime the departure time of the schedule
     * @return true if overtaking occurs, false otherwise
     */
    private boolean isOvertakingDeparture(Schedule schedule, Time departureTime) {
        return overtake(schedule, departureTime, departureSchedules);
    }
    @Override
    public void removeScheduleOfStation() {
        Iterator<Entry<Time,Schedule>> departureSchedulesIt = departureSchedules.iterator();
        while (departureSchedulesIt.hasNext()) {
            Entry<Time,Schedule> entry = departureSchedulesIt.next();
            Schedule currentSchedule = entry.getValue();
            currentSchedule.removeScheduleOfStation();
        }

        Iterator<Entry<Time,Schedule>> arrivalSchedulesIt = arrivalSchedules.iterator();
        while (arrivalSchedulesIt.hasNext()) {
            Entry<Time,Schedule> entry = arrivalSchedulesIt.next();
            Schedule currentSchedule = entry.getValue();
            currentSchedule.removeScheduleOfStation();
        }
    }

    @Override
    public int compareTo(Line o) {
        return this.name.toUpperCase().compareTo(o.getName().toUpperCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineClass other)) return false;
        return this.name.equalsIgnoreCase(other.getName());
    }


    // Métodos privados

    /**
     * Checks if a given station is a terminal.
     * @param stationName the name of the station to check
     * @return true if the station is a terminal, false otherwise
     */
    private boolean isStationTerminal(String stationName) {
        return !stationName.equalsIgnoreCase(stations.getFirst().getName()) && !stationName.equalsIgnoreCase(stations.getLast().getName());
    }

    /**
     * Determines whether one schedule overtakes another based on timings.
     * @param schedule1 the first schedule
     * @param schedule2 the second schedule
     * @param date1 the departure time of the first schedule
     * @param date2 the departure time of the second schedule
     * @return true if overtaking occurs, false otherwise
     */
    private boolean overtake(Schedule schedule1, Schedule schedule2, Time date1, Time date2) {
        Entry<Station, Time> schedule1FirstEntry = schedule1.getFirstEntry();
        Entry<Station, Time> schedule2FirstEntry = schedule2.getFirstEntry();
        if (schedule1FirstEntry.getKey() != schedule2FirstEntry.getKey()) {
            return false;
        }
        boolean overtaking = false;
        Iterator<Entry<Station, Time>> it1 = schedule1.getEntries();
        it1.next();
        Iterator<Entry<Station, Time>> it2 = schedule2.getEntries();
        it2.next();

        if (date1.compareTo(date2) > 0) {
            Entry<Station, Time> stop1 = it1.next();
            Entry<Station, Time> stop2 = it2.next();
            while (!overtaking) {
                int index1 = stations.find(stop1.getKey());
                int index2 = stations.find(stop2.getKey());
                if (index1 == index2 && stop1.getValue().compareTo(stop2.getValue()) <= 0) {
                    overtaking = true;
                } else if (index1 > index2) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    stop2 = it2.next();
                } else {
                    if (!it1.hasNext()) {
                        break;
                    }
                    stop1 = it1.next();
                }
            }
        } else if (date1.compareTo(date2) < 0) {
            Entry<Station, Time> stop1 = it1.next();
            Entry<Station, Time> stop2 = it2.next();
            while (!overtaking) {
                int index1 = stations.find(stop1.getKey());
                int index2 = stations.find(stop2.getKey());
                if (index1 == index2 && stop1.getValue().compareTo(stop2.getValue()) >= 0) {
                    overtaking = true;
                } else if (index1 > index2) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    stop2 = it2.next();
                } else {
                    if (!it1.hasNext()) {
                        break;
                    }
                    stop1 = it1.next();
                }
            }
        } else {
            return true;
        }

        return overtaking;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeUTF(name);
        out.writeObject(stations);
        out.writeObject(departureSchedules);
        out.writeObject(arrivalSchedules);
    }
    @SuppressWarnings("unchecked")
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        name = in.readUTF();
        stations = (DoubleList<Station>) in.readObject();
        departureSchedules = (AVLTree<Time,Schedule>) in.readObject();
        arrivalSchedules = (AVLTree<Time,Schedule>) in.readObject();
    }


}

