package dataStructures;

class BSTKeyOrderIterator<K,V> implements Iterator<Entry<K,V>> {

	

	static final long serialVersionUID = 0L;

	protected BSTNode<Entry<K,V>> root;

	protected Stack<BSTNode<Entry<K,V>>> p;


	BSTKeyOrderIterator(BSTNode<Entry<K,V>> root){
		this.root=root;
		rewind();
	}
	
	private void pushPathToMinimum(BSTNode<Entry<K,V>> node) {
		while(node != null) {
			p.push(node);
			node = node.getLeft();
		}
	}

	public boolean hasNext(){
		 return !p.isEmpty();
	 }


    public Entry<K,V> next( ) throws NoSuchElementException {
    	if (!hasNext()) throw new NoSuchElementException();
    	else {
    		BSTNode<Entry<K,V>> node = p.pop();
    		if(node.getRight() != null) {
    			this.pushPathToMinimum(node.getRight());
    		}
    		return node.getElement();
    	}
    }

    public void rewind( ){
		p = new StackInList<>();
    	pushPathToMinimum(root);
    }
}
