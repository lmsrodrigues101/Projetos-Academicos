package dataStructures;


class BSTBreadthFirstIterator<K,V> implements Iterator<Entry<K,V>> {

	static final long serialVersionUID = 1L;
	
	protected BSTNode<Entry<K,V>> root;
	// espacial 1 n n
	protected Queue<BSTNode<Entry<K,V>>> q;
	
	// 1 1 1
	public  BSTBreadthFirstIterator(BSTNode<Entry<K,V>> root){
		this.root=root;
		rewind();
		
	}
	
	// 1 1 1
	public boolean hasNext( ){
		return !q.isEmpty();
	}
	
	// 1 1 1
	private void enqueueNode(BSTNode<Entry<K,V>> node){
		if (node!=null) q.enqueue(node);
	}
	
	// 1 1 1
    public Entry<K,V> next( ) throws NoSuchElementException{
    	if (!hasNext()) throw new NoSuchElementException();
    	BSTNode<Entry<K,V>> node=q.dequeue();
    	enqueueNode(node.getLeft());
    	enqueueNode(node.getRight());
    	return node.getElement();
    }

    // 1 1 1
    public void rewind( ){
    	q=new QueueInList<BSTNode<Entry<K,V>>>();
    	enqueueNode(root);
    	
    }

}
