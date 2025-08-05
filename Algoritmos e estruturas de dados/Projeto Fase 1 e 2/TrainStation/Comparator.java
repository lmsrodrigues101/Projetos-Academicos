package TrainStation;
import java.io.Serializable;

/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public interface Comparator<E> extends Serializable {
 int compare(E o1, E o2);
}
