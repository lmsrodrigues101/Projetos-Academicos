package TrainStation;


/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public interface Schedule extends ScheduleGets {


    /**
     * Checks if a schedule is the best time
     * @param startStation
     * @param endStation
     * @param date
     * @return minutes
     */
    int getTimeToStation(Station startStation, Station endStation, String date);

    /**
     * Adds stops(station and time) to a schedule
     */
    void addStops();

    /**
     * Removes the schedule of a certain station
     */
    void removeScheduleOfStation();

}
