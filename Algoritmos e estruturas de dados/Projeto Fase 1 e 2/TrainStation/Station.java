package TrainStation;



/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public interface Station extends StationGets,Comparable<Station> {

    /**
     * adds a new line to the station
     * @param line
     */
    void addLine(Line line);

    /**
     * Gets the number of lines of the station
     * @return number of lines
     */
    int getNumberOfLines();

    /**
     * Removes a line from the station
     */
    void removeLine(Line line);

    /**
     * Adds a schedule to the station
     */
    void addSchedule(int trainId, Time value);

    /**
     * Removes a schedule from the station
     */
    void removeSchedule(int trainID, Time value);
}
