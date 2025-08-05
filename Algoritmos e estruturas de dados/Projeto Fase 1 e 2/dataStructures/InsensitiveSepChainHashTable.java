package dataStructures;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

public class InsensitiveSepChainHashTable<V> extends SepChainHashTable <String,V>{

    /**
	 * 
	 */
	private static final long serialVersionUID = 0L;

	@SuppressWarnings("unchecked")
    public InsensitiveSepChainHashTable( int capacity )
    {
        int arraySize = HashTable.nextPrime((int) (1.1 * capacity));
        table = (Dictionary<String,V>[]) new Dictionary[arraySize];
        for ( int i = 0; i < arraySize; i++ )
            table[i] = new InsensitiveOrderedDoubleList<>();
        maxSize = capacity;
        currentSize = 0;
    }
    public InsensitiveSepChainHashTable(){
        this(DEFAULT_CAPACITY);
    }

    protected int hash( String key ) {
        return Math.abs( key.toUpperCase().hashCode() ) % table.length;
    }

    public V insert( String key, V value )
    {
        if(this.isFull())
            this.rehash();
        V oldValue = table[ this.hash(key) ].insert(key, value);
        if( oldValue == null)
            currentSize++;
        return oldValue;
    }
    public V find( String key )
    {
        return table[ this.hash(key) ].find(key);
    }

    public V remove( String key )
    {
        V value = table[this.hash(key)].remove(key);
        if(value != null)
            currentSize--;
        return value;
    }

    @SuppressWarnings("unchecked")
    private void rehash(){
        //igual ao construtor mas o arraySize aumenta para 2 vezes mais
        int arraySize = HashTable.nextPrime((int)(1.1 * currentSize * 2));
        Dictionary<String,V>[] newTable = (Dictionary<String, V>[])new Dictionary[arraySize];
        for( int i = 0; i < arraySize; i++ )
            newTable[i] = new InsensitiveOrderedDoubleList<>();

        // Iterar a table existente e meter as entrys na nova table nos seus respetivos/novos index
        for(int i = 0; i < table.length; i++ ){
            Iterator<Entry<String,V>> it = table[i].iterator();
            while(it.hasNext()){
                Entry<String,V> entry = it.next();
                int index = Math.abs(entry.getKey().toUpperCase().hashCode() ) % newTable.length;
                newTable[index].insert(entry.getKey(), entry.getValue());
            }
        }
        maxSize *= 2;
        table = newTable;
    }
    // Serialização: salva o estado da tabela
    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject(); // Serializa os campos não transitórios
        oos.writeInt(maxSize);
        oos.writeInt(currentSize);
        oos.writeInt(table.length);
        for (Dictionary<String, V> bucket : table) {
            oos.writeObject(bucket);
        }
    }

    // Desserialização: restaura o estado da tabela
    @SuppressWarnings("unchecked")
    @Serial
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject(); // Desserializa os campos não transitórios
        maxSize = ois.readInt();
        currentSize = ois.readInt();
        int arrayLength = ois.readInt();
        table = (Dictionary<String, V>[]) new Dictionary[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            table[i] = (Dictionary<String, V>) ois.readObject();
        }
    }

/*
    @Serial
    private void writeObject( ObjectOutputStream out ) throws IOException{
        out.writeInt(maxSize);
        out.writeInt(currentSize);
        Iterator<Entry<String,V>> it = this.iterator();
        while(it.hasNext()){
            Entry<String,V> entry = it.next();
            out.writeObject(entry.getKey());
            out.writeObject(entry.getValue());
        }

    }

    @SuppressWarnings("unchecked")
    @Serial
    private void readObject( ObjectInputStream in ) throws IOException, ClassNotFoundException{
        maxSize = in.readInt();
        int arraySize = HashTable.nextPrime((int)(1.1 * maxSize * 2));
        table = (Dictionary<String,V>[]) new Dictionary[arraySize];
        int size = in.readInt();
        for( int i = 0; i < arraySize; i++ )
            table[i] = new InsensitiveOrderedDoubleList<>();
        for(int j = 0; j < size; j++ ){
            String name = (String) in.readObject();
            this.insert(name, (V)in.readObject());
        }
    }

*/
}
