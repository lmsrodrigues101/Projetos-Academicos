package TrainStation;
import dataStructures.*;

import java.io.*;


/**
 * @author Leandro Rodrigues 68211 lms.rodrigues@campus.fct.unl.pt
 * @author Rodrigo Sinde 68614 r.sinde@campus.fct.unl.pt
 */
public class StationClass implements Station, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 0L;

	private  String name;

    private OrderedDictionary<Line,Line> lines;//  AVL

    private  OrderedDictionary<Integer,Time> trains;

    /**
     * Initializes the lines list and stations name
     */
    public StationClass(String name){
        lines = new AVLTree<>();
        trains = new AVLWithComparator<>(new StationComparator());
        this.name = name;
    }

    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof StationClass other)) return false;
        return this.name.equalsIgnoreCase(other.getName());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addLine(Line line) {
        lines.insert(line,line);
    }

    @Override
    public int getNumberOfLines() {
        return lines.size();
    }

    @Override
    public Iterator<Entry<Line,Line>> getLinesIterator() {
        return lines.iterator();
    }

    @Override
    public Iterator<Entry<Integer, Time>> getSchedules() {
        return trains.iterator();
    }

    @Override
    public void removeLine(Line line) {
       lines.remove(line);
    }

    @Override
    public void addSchedule(int trainId, Time value) {
        trains.insert(trainId, value);
    }

    @Override
    public void removeSchedule(int trainId, Time value) {
        trains.remove(trainId,value);
    }

    @Override
    public int compareTo(Station o) {
        return this.name.compareTo(o.getName());
    }

}
