package dataStructures;


/**
 * Doubly linked list Implementation 
 * @author AED  Team
 * @version 1.0
 * @param <K, V> Generics - K extends Comparable
 * 
 */
class OrderedDoubleList<K extends Comparable<K>, V> 
	implements OrderedDictionary<K , V> {

    /**
	 * Serial Version UID of the Class
	 */
    static final long serialVersionUID = 0L;
    
	/**
     *  Node at the head of the list.
     */
	protected DoubleListNode<Entry<K,V>> head;

    /**
     * Node at the tail of the list.
     */
	protected DoubleListNode<Entry<K,V>> tail;

    /**
     * Number of elements in the list.
     */
	protected int currentSize;
	
    /**
     * Constructor of an empty ordered double linked list.
     * head and tail are initialized as null.
     * currentSize is initialized as 0.
     */
	public OrderedDoubleList() {
		head=null;
		tail=null;
		currentSize=0;
	}

    /**
     * Inserts the Entry element before node after.
     * Precondition: after is not the head of the ordered double list.
     * @param element - Entry to be inserted
     * @param after - Node to be next to the new node  
     */
	protected void addBeforeNode(Entry<K,V> element, DoubleListNode<Entry<K,V>> after){
        DoubleListNode<Entry<K, V>> prev = after.getPrevious();
        DoubleListNode<Entry<K, V>> node = new DoubleListNode<>(element, prev, after);
        prev.setNext(node);
        after.setPrevious(node);
        currentSize++;
    }
	
    /**
     * Inserts the Entry element at the first position in the list.
     * @param element - Entry to be inserted
     */
    protected void addFirst( Entry<K,V> element )
    {
        DoubleListNode<Entry<K,V>> newNode = new DoubleListNode<Entry<K,V>>(element, null, head);
        if ( this.isEmpty() )
            tail = newNode;
        else
            head.setPrevious(newNode);
        head = newNode;
        currentSize++;
    }


    /**
     * Inserts the Entry element at the last position in the list.
     * @param element - Entry to be inserted
     */
    protected void addLast( Entry<K,V> element )
    {
        DoubleListNode<Entry<K, V>> newNode = new DoubleListNode<Entry<K, V>>(element, tail, null);
        if(this.isEmpty()){
            head = newNode;
        } else {
            tail.setNext(newNode);
        }
        tail = newNode;
        currentSize++;
    }

	@Override
    public Entry<K, V> maxEntry() throws EmptyDictionaryException {
        if (isEmpty()) {
            throw new EmptyDictionaryException();
        }
        return tail.getElement();
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    @Override
	public Entry<K, V> minEntry() throws EmptyDictionaryException {
        if (isEmpty()) {
            throw new EmptyDictionaryException();
        }
        return head.getElement();
    }

    /**
     * Returns the node with the Entry with Key key
     * in the list, if the list contains this entry.
     * Otherwise, returns null.
     * @param key - Key of type K to be searched
     * @return DoubleListNode<E> where the Entry with key was found, or the one with the key immmediately after 
     */
	protected DoubleListNode<Entry<K,V>> findNode (K key){
        DoubleListNode<Entry<K, V>> currentNode = head;
        while (currentNode != null && !(currentNode.getElement().getKey().compareTo(key) == 0)) {
            currentNode = currentNode.getNext();
        }
        return currentNode;
    }
	
    @Override
	public V find(K key) {
		DoubleListNode<Entry<K,V>> node = findNode(key);
		if(node == null)
            return null;
        return node.getElement().getValue();
	}


	@Override
	public V insert(K key, V value) {
		DoubleListNode<Entry<K,V>> node = findNode(key);
		if ((node!=null) && (node.getElement().getKey().compareTo(key)==0)){
			V lastValue = node.getElement().getValue();
            node.setElement(new EntryClass<>(key,value));
            return lastValue;
		}
		else { 
		  Entry<K,V> newNode=new EntryClass<K,V> (key, value);
		  if(this.isEmpty()){
              addFirst(newNode);
          }else{
          DoubleListNode<Entry<K,V>> current = head;
          while(current != null){
              if(key.compareTo(current.getElement().getKey()) < 0){
                  if(current==head) {
                      addFirst(newNode);
                      return null;
                  }
                  addBeforeNode(newNode, current);
                  return null;
              }
              current = current.getNext();
          }
          addLast(newNode);
		}
          return null;
        }
	}
	
	@Override
    public boolean isEmpty() {
	
		return currentSize==0;
	}

    @Override
	public Iterator<Entry<K, V>> iterator() {
        return new DoubleListIterator<Entry<K,V>>(head, tail);
	}

    /**
     * Removes the first node in the list.
     * Pre-condition: the list is not empty.
     */
    protected void removeFirstNode( )
    {
        head = head.getNext();
        if ( head == null )
            tail = null;
        else
            head.setPrevious(null);
        currentSize--;
    }


    /**
     * Removes and returns the value at the first entry in the list.
     */
    protected V removeFirst( ) throws EmptyDictionaryException
    {
        if (isEmpty()) {
            throw new EmptyDictionaryException();
        }
        V value = head.getElement().getValue();
        this.removeFirstNode();
        return value;
    }


    /**
     * Removes the last node in the list.
     * Pre-condition: the list is not empty.
     */
    protected void removeLastNode( ) {
        tail = tail.getPrevious();
        if (head == null) {
            tail = null;
        } else {
            tail.setNext(null);
        }
        currentSize--;
    }


    /**
     * Removes and returns the value at the last entry in the list.
     */
    protected V removeLast( ) throws EmptyDictionaryException
    {
        if ( this.isEmpty() )
            throw new EmptyDictionaryException();

        V value = tail.getElement().getValue();
        this.removeLastNode();
        return value;
    }

    /**
     * Removes the specified node from the list.
     * Pre-condition: the node is neither the head nor the tail of the list.
     * @param node - middle node to be removed
     */
    protected void removeMiddleNode( DoubleListNode<Entry<K,V>> node ) {
        DoubleListNode<Entry<K, V>> prevNode = node.getPrevious();
        DoubleListNode<Entry<K, V>> nextNode = node.getNext();
        prevNode.setNext(nextNode);
        nextNode.setPrevious(prevNode);
        node.setPrevious(null);
        node.setNext(null);
        currentSize--;
    }

        @Override
    public V remove(K key) {
		DoubleListNode<Entry<K,V>> node = findNode(key);
		if ((node == null) || (node.getElement().getKey().compareTo(key)!=0))
			return null;
		else {
			  	if(node == head){
                      return removeFirst();
                } else if(node == tail){
                      return removeLast();
                } else {
                      V value = node.getElement().getValue();
                      removeMiddleNode(node);
                      return value;
                }
		}
	}

    @Override
	public int size() {
		return currentSize;
	}

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        s.writeInt(currentSize);
        for(DoubleListNode<Entry<K,V>> x = head; x != null; x = x.getNext()) {
            s.writeObject(x.getElement().getKey());
            s.writeObject(x.getElement().getValue());
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        int size = s.readInt();
        for(int i = 0; i < size; i++) {
            this.insert((K)s.readObject(), (V)s.readObject());
        }
    }


	
}
