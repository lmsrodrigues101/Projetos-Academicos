package TrainStation;

/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public interface Time extends Comparable<Time> {


    /**
     * Gets the hours of time
     * @return hours
     */
    int getHours();

    /**
     * Gets the minutes of time
     * @return minutes
     */
    int getMinutes();



}
