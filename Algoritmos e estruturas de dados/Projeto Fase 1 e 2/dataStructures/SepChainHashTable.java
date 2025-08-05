package dataStructures;



/**
 * Separate Chaining Hash table implementation
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key, must extend comparable
 * @param <V> Generic Value 
 */

public class SepChainHashTable<K extends Comparable<K>, V> 
    extends HashTable<K,V> {
    /**
     * Serial Version UID of the Class.
     */
    static final long serialVersionUID = 0L;

    /**
     * The array of dictionaries.
     */
    protected Dictionary<K, V>[] table;


    /**
     * Constructor of an empty separate chaining hash table,
     * with the specified initial capacity.
     * Each position of the array is initialized to a new ordered list
     * maxSize is initialized to the capacity.
     *
     * @param capacity defines the table capacity.
     */
    @SuppressWarnings("unchecked")
    public SepChainHashTable(int capacity) {
        int arraySize = HashTable.nextPrime((int) (1.1 * capacity));
        table = (Dictionary<K, V>[]) new Dictionary[arraySize];
        for (int i = 0; i < arraySize; i++)
            table[i] = new OrderedDoubleList<K, V>();
        maxSize = capacity;
        currentSize = 0;
    }


    public SepChainHashTable() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Returns the hash value of the specified key.
     *
     * @param key to be encoded
     * @return hash value of the specified key
     */
    protected int hash(K key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    @Override
    public V find(K key) {
        return table[this.hash(key)].find(key);
    }

    @Override
    public V insert(K key, V value) {
        if (this.isFull())
            this.rehash();
        V oldValue = table[this.hash(key)].insert(key, value);
        if (oldValue == null)
            currentSize++;
        return oldValue;
    }

    @Override
    public V remove(K key) {
        V value = table[this.hash(key)].remove(key);
        if (value != null)
            currentSize--;
        return value;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new SepChainHashTableIterator<>(table);
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        //igual ao construtor mas o arraySize aumenta para 2 vezes mais
        int arraySize = HashTable.nextPrime((int) (1.1 * currentSize * 2));
        Dictionary<K, V>[] newTable = (Dictionary<K, V>[]) new Dictionary[arraySize];
        for (int i = 0; i < arraySize; i++)
            newTable[i] = new OrderedDoubleList<K, V>();
        // Iterar a table existente e meter as entrys na nova table nos seus respetivos/novos index
        for (int i = 0; i < table.length; i++) {
            Iterator<Entry<K, V>> it = table[i].iterator();
            while (it.hasNext()) {
                Entry<K, V> entry = it.next();
                int index = Math.abs(entry.getKey().hashCode()) % newTable.length;
                newTable[index].insert(entry.getKey(), entry.getValue());
            }
        }
        maxSize *= 2;
        table = newTable;
    }


    /*
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(maxSize);
        out.writeInt(currentSize);
        Iterator<Entry<K,V>> it = this.iterator();
        while (it.hasNext()) {
            Entry<K, V> entry = it.next();
            out.writeObject(entry.getKey());
            out.writeObject(entry.getValue());
        }

    }

    @SuppressWarnings("unchecked")
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        maxSize = in.readInt();
        int arraySize = HashTable.nextPrime((int) (1.1 * maxSize * 2));
        table = (Dictionary<K, V>[]) new Dictionary[arraySize];
        int size = in.readInt();
        for (int i = 0; i < arraySize; i++)
            table[i] = new OrderedDoubleList<>();
        for (int j = 0; j < size; j++) {
            this.insert((K) in.readObject(), (V) in.readObject());
        }
    }

*/



}
































