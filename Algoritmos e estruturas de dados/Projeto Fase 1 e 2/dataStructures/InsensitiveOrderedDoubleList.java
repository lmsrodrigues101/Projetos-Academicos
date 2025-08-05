package dataStructures;


public class InsensitiveOrderedDoubleList<V> extends OrderedDoubleList<String,V> {

     /**
	 * 
	 */
	private static final long serialVersionUID = 0L;
	public InsensitiveOrderedDoubleList() {
         super();
     }
    protected DoubleListNode<Entry<String,V>> findNode (String key){
         DoubleListNode<Entry<String, V>> currentNode = head;
        while (currentNode != null && !(currentNode.getElement().getKey().toUpperCase().compareTo(key.toUpperCase()) == 0)) {
            currentNode = currentNode.getNext();
        }
        return currentNode;
    }
    public V insert(String key, V value) {
        DoubleListNode<Entry<String,V>> node = findNode(key);
        if ((node!=null) && (node.getElement().getKey().toUpperCase().compareTo(key.toUpperCase())==0)){
            V lastValue = node.getElement().getValue();
            node.setElement(new EntryClass<>(key,value));
            return lastValue;
        }
        else {
            Entry<String,V> newNode=new EntryClass<String,V> (key, value);
            if(this.isEmpty()){
                addFirst(newNode);
            }else{
                DoubleListNode<Entry<String,V>> current = head;
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
    public V remove(String key) {
        DoubleListNode<Entry<String,V>> node = findNode(key);
        if ((node == null) || (node.getElement().getKey().toUpperCase().compareTo(key.toUpperCase())!=0))
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
/*
    @Serial
    private void writeObject (java.io.ObjectOutputStream out) throws IOException {
        out.writeInt(currentSize);
        Iterator<Entry<String,V>> it = iterator();
        while (it.hasNext()) {
            Entry<String,V> entry = it.next();
            out.writeObject(entry.getKey());
            out.writeObject(entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    @Serial
    private void readObject (java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        int size = in.readInt();

        for (int i = 0; i < size; i++) {
            this.insert((String) in.readObject(), (V) in.readObject());
        }
    }

 */
}

