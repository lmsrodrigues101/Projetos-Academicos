package TrainStation;
import dataStructures.*;

import java.io.*;

/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public class ScheduleClass implements Schedule, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 0L;

	private int trainId;

    private  List<Entry <Station, Time>> route;

    /**
     * Initializes the schedule with its respective trainId and route list
     */
    public ScheduleClass(int trainId, List<Entry <Station, Time>> route) {
        this.trainId = trainId;
        this.route = route;
    }

    @Override
    public int getTrainId() {
        return trainId;
    }


    @Override
    public Iterator<Entry<Station, Time>> getEntries() {
        return route.iterator();
    }

    @Override
    public Entry<Station, Time> getFirstEntry() {
        return route.getFirst();
    }

    @Override
    public int getTimeToStation(Station departureStation, Station arrivalStation, String date) {
        Iterator<Entry<Station, Time>> entryIterator = route.iterator();
        Time time = new TimeClass(date);
        int timeDifference = -1;
        while (entryIterator.hasNext()) {
            Entry<Station, Time> entry = entryIterator.next();
            Station currentStation = entry.getKey();
            if (currentStation.getName().equalsIgnoreCase(departureStation.getName())) {
                while (entryIterator.hasNext()) {
                    Entry<Station, Time> entry2 = entryIterator.next();
                    Station currentStation2 = entry2.getKey();
                    if (currentStation2.getName().equalsIgnoreCase(arrivalStation.getName())) {
                        if (entry2.getValue().compareTo(time) > 0) {
                            return -1;
                        } else {
                            int hourDifference = time.getHours() - entry2.getValue().getHours();
                            int minuteDifference = time.getMinutes() - entry2.getValue().getMinutes();
                            return (hourDifference * 60) + minuteDifference;
                        }
                    }
                    if (!entryIterator.hasNext() && !currentStation2.getName().equalsIgnoreCase(arrivalStation.getName())) {
                        return -1;
                    }
                }
            }
        }
        return timeDifference; // Retorna -1 se não encontrar a rota válida.
    }

    @Override
    public void addStops() {
        Iterator<Entry<Station,Time>> routeIt = route.iterator();
        while (routeIt.hasNext()) {
            Entry<Station, Time> entry = routeIt.next();
            Station currentStation = entry.getKey();
            currentStation.addSchedule(this.getTrainId(), entry.getValue());
        }
    }

    @Override
    public void removeScheduleOfStation() {
        Iterator<Entry<Station,Time>> entryIt = route.iterator();
        while (entryIt.hasNext()) {
            Entry<Station, Time> entry = entryIt.next();
            Station currentStation = entry.getKey();
            currentStation.removeSchedule(this.getTrainId(), entry.getValue());
        }
    }
}
