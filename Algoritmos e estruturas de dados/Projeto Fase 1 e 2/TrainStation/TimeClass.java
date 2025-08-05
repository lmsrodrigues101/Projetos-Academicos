package TrainStation;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public class TimeClass implements Time, Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 0L;
	private int hours;
    private int minutes;
    private String time;

    /**
     * Gets variable time as an argument
     */
    public TimeClass(String time) {
        this.time = time;
        convertTime();
    }

    @Override
    public int compareTo(Time o) {
        if(this.hours - o.getHours() != 0){
            return this.hours - o.getHours();
        }
          return this.minutes - o.getMinutes();
    }

    @Override
    public int getHours() {
        return hours;
    }

    @Override
    public int getMinutes() {
        return minutes;
    }

    /**
     * Converts the time string (format "HH:MM") into hours and minutes.
     */
    private void convertTime() {
        String[] tokens = time.split(":");
        hours = Integer.parseInt(tokens[0]);
        minutes = Integer.parseInt(tokens[1]);
    }

}






