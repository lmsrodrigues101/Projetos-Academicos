package TrainStation;
import dataStructures.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public class StationComparator implements Comparator<Entry<Integer,Time>>, Serializable {

    private static final long serialVersionUID = 0L;

    @Override
    public int compare(Entry<Integer, Time> o1, Entry<Integer, Time> o2) {
        int comparison = o1.getValue().compareTo(o2.getValue());
        if(comparison == 0) {
            return o1.getKey().compareTo(o2.getKey());
        }
        return comparison;
    }
}
