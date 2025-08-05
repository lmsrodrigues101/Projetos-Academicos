package dataStructures;

import java.io.IOException;
import java.io.Serial;

/**
 * BinarySearchTree implementation
 * @author AED team
 * @version 1.0
 * @param <K> Generic type Key, must extend comparable
 * @param <V> Generic type Value 
 */
public class BinarySearchTree<K extends Comparable<K>, V> 
    implements OrderedDictionary<K,V>
{                                                                   
    static final long serialVersionUID = 0L;

	/**
     * The root of the tree.                                            
     * 
     */
    BSTNode<Entry<K,V>> root;

    /**
     * Number of entries in the tree.                                  
     * 
     */
    protected int currentSize;




    /**
     * Tree Constructor - creates an empty tree.
     */
    public BinarySearchTree( )                                    
    {    
        root = null;
        currentSize = 0;
    }


    @Override
    public boolean isEmpty( )                               
    {    
        return root == null;
    }


    @Override
    public int size( )                                      
    {    
        return currentSize;
    }


    @Override
    public V find( K key )                             
    {    
        BSTNode<Entry<K,V>> node = this.findNode(key);
        if ( node == null || node.getElement().getKey().compareTo(key) != 0 )
            return null;                                    
        else                                                     
            return node.getElement().getValue();
    }


    /*
    **
     * Returns the node whose key is the specified key;
     * or null if no such node exists.        
     *                         
     * @param node where the search starts 
     * @param key to be found
     * @return the found node, when the search is successful
     */
    BSTNode<Entry<K,V>> findNode( BSTNode<Entry<K,V>> node, K key )
    {                                                                   
        if ( node == null )
            return null;
        else
        {
            int compResult = key.compareTo( node.getElement().getKey() );
            if ( compResult == 0 )
                return node;                                         
            else if ( compResult < 0 )
                return this.findNode(node.getLeft(), key);
            else                                                     
                return this.findNode(node.getRight(), key); 
        }                 
    }


    @Override
    public Entry<K,V> minEntry( ) throws EmptyDictionaryException
    {                                                                   
        if ( this.isEmpty() )                              
            throw new EmptyDictionaryException();           

        return this.minNode(root).getElement();
    }


    /**
     * Returns the node with the smallest key 
     * in the tree rooted at the specified node.
     * Requires: node != null.
     * @param node - node that roots the tree
     * @return node with the smallest key in the tree
     */
    BSTNode<Entry<K,V>> minNode( BSTNode<Entry<K,V>> node )
    {

        while ( node.getLeft() != null )
        {
            node = node.getLeft();
        }
        return node;

    }                               


    @Override
    public Entry<K,V> maxEntry( ) throws EmptyDictionaryException
    {                                                                   
        if ( this.isEmpty() )                              
            throw new EmptyDictionaryException();           

        return this.maxNode(root).getElement();
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }


    /**
     * Returns the node with the largest key 
     * in the tree rooted at the specified node.
     * Requires: node != null.
     * @param node that roots the tree
     * @return node with the largest key in the tree
     */
    BSTNode<Entry<K,V>> maxNode( BSTNode<Entry<K,V>> node )
    {                                                                   
        if ( node.getRight() == null )                            
            return node;                                             
        else                                                     
            return this.maxNode( node.getRight() );                       
    }                               


    /**
     * Returns the node whose key is the specified key;
     * or the parent of the node where the key should exist if no such node exists.
     * @param key to be searched
     * @return see above
     
     */
    BSTNode<Entry<K,V>> findNode( K key)
    {      
        BSTNode<Entry<K,V>> node = root;
        BSTNode<Entry<K,V>> current = null;
        while ( node != null )
        {
            int compResult = key.compareTo( node.getElement().getKey() );
            if ( compResult == 0 )// Se as chaves forem iguais, encontramos o nó.
                return node;
            else if ( compResult < 0 ) {// Se a chave for menor, vamos para a subárvore esquerda.
                current = node;
                node = node.getLeft();
            }
            else { // Se a chave for maior, vamos para a subárvore direita.
                current = node;
                node = node.getRight();
            }
        }
        return current; // Retorna o nó pai, onde a chave deveria ser.
    }                               


    @Override
    public V insert( K key, V value )
    {                                                                   

        BSTNode<Entry<K,V>> node = this.findNode(key);

        if ( node == null || node.getElement().getKey().compareTo(key) != 0 )
        { //Chave nao existe, node do findNode é o parente
            BSTNode<Entry<K,V>> newLeaf = new BSTNode<>(new EntryClass<>(key, value));
            this.linkSubtreeInsert(newLeaf, node);
            currentSize++;
            return null;   
        }                                 
        else 
        {
            V oldValue = node.getElement().getValue();
            node.setElement(new EntryClass<>(key, value));
            return oldValue;
        }
    }


    /**
     * Links a new subtree, rooted at the specified node, to the tree.
     *
     * @param node - root of the subtree
     * @param parent - parent node for the new subtree
     */
    void linkSubtreeInsert(BSTNode<Entry<K,V>> node, BSTNode<Entry<K,V>> parent) {
        if ( parent == null )
            // Se o nó pai for nulo, o nó inserido se torna a raiz.
            root = node;
        else {
            if (node != null) {
                node.setParent(parent);// Define o nó pai do novo nó.
                // Verifica se o nó deve ser inserido à esquerda ou direita.
                if (parent.getElement().getKey().compareTo(node.getElement().getKey()) <= 0)
                    parent.setRight(node);
                else
                    parent.setLeft(node);
            }
        }
    }

    /**
     *
     * @param grandchild child of middle, to be made child of parent.
     * @param parent to be linked to grandchild, if not null.
     * @param middle node that is to be removed, child of parent, parent of grandchild
     */
    void linkSubtreeRemove( BSTNode<Entry<K,V>> grandchild, BSTNode<Entry<K,V>> parent, BSTNode<Entry<K,V>> middle)
    {
        if ( parent == null )
            // Se o nó pai for nulo, significa que o nó removido é a raiz da árvore.
            // Nesse caso, o "neto" se torna a nova raiz.
            root = grandchild;
        else {
	        if (grandchild != null)
                grandchild.setParent(parent);  // Ajusta o ponteiro do "neto" para apontar para o "pai" atual.

            // Substitui o "meio" (nó removido) pelo "neto" no nó pai.
            // Se o nó removido for o filho esquerdo do pai, o "neto" se torna o novo filho esquerdo.
            if (middle == parent.left)
                parent.setLeft(grandchild);
            else
                parent.setRight(grandchild);

        }
    }





    @Override
    public V remove( K key )
    {

        BSTNode<Entry<K,V>> node = this.findNode(key);
        if ( node == null || node.getElement().getKey().compareTo(key) != 0 )
            return null; // Se o nó não existir, retorna null.
        else
        {
            V oldValue = node.getElement().getValue();// valor antigo.

	        if ( node.getLeft() == null )
                // The left subtree is empty.
                this.linkSubtreeRemove(node.getRight(), node.getParent(),node);
            else if ( node.getRight() == null )
                // The right subtree is empty.
                this.linkSubtreeRemove(node.getLeft(), node.getParent(),node);
            else
            {
                // Node has 2 children. Replace the node's entry with
                // the 'minEntry' of the right subtree.
                BSTNode<Entry<K,V>> minNode = this.minNode(node.getRight());
                node.setElement( minNode.getElement() );
                // Remove the 'minEntry' of the right subtree.
                this.linkSubtreeRemove(minNode.getRight(), minNode.getParent(),minNode);
            }
            currentSize--;
            return oldValue;
        }                                 
    }                                


    /**
     * Returns an iterator of the entries in the dictionary 
     * which preserves the key order relation.
     * @return  key-order iterator of the entries in the dictionary
     */
    public Iterator<Entry<K,V>> iterator( ) 
    {
        return new BSTKeyOrderIterator<>(root);
    }


    @Serial
    protected void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeInt(currentSize);
        if (root == null) {
            return;
        }
        Iterator<Entry<K, V>> it = new BSTBreadthFirstIterator<>(root);
        out.writeInt(currentSize);
        while (it.hasNext()) {
            Entry<K, V> entry = it.next();
            out.writeObject(entry.getKey());
            out.writeObject(entry.getValue());
        }
    }
    @SuppressWarnings("unchecked")
    @Serial
    protected void readObject (java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            this.insert((K) in.readObject(), (V) in.readObject());
        }
    }


}

