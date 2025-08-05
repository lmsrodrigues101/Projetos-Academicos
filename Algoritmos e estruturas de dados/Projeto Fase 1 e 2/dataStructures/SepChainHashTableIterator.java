package dataStructures;

public class SepChainHashTableIterator<K extends Comparable<K>,V> implements Iterator<Entry<K,V>> {
    static final long serialVersionUID = 0L;

    private Dictionary<K,V>[] table;
    private int currentIndex = 0;
    private Iterator<Entry<K,V>> currentIT;

    public SepChainHashTableIterator(Dictionary<K,V>[] table){
        rewind();
        this.table=table;
        nonEmptyDictionary();
    }
    private void nonEmptyDictionary() {
        while (currentIndex < table.length && (table[currentIndex] == null || table[currentIndex].isEmpty())) {
            currentIndex++;
        }
        if(currentIndex< table.length) {
            currentIT = table[currentIndex].iterator();
        }
    }


    @Override
    public boolean hasNext() {
        if(currentIT == null) {
            return false;
        }
        if(currentIT.hasNext()) {
            return true;
        }
        currentIndex++;
        nonEmptyDictionary();
        return currentIndex < table.length;
    }

    @Override
    public Entry<K, V> next() throws NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return currentIT.next();
    }

    @Override
    public void rewind() {
        currentIndex = 0;
        currentIT = null;
    }
}
