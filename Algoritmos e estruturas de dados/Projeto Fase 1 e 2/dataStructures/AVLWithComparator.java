package dataStructures;
import TrainStation.Comparator;

import java.io.IOException;
import java.io.Serial;

/**
 * The AVL tree with comparator Class

 */
public class AVLWithComparator<K extends Comparable<K>, V extends Comparable<V>> extends AVLTree<K, V> {
    /**
     * Serial Version UID of the Class.
     */
    static final long serialVersionUID = 0L;
    /**
     * The comparator
     */
    protected Comparator<Entry<K, V>> comparator;
    /**
     * Constructor for AVL tree with comparator
     */
    public AVLWithComparator(Comparator<Entry<K,V>> comparator) {
        super();
        this.comparator = comparator;
    }
    BSTNode<Entry<K,V>> findNode( Entry<K,V> entry)
    {
        BSTNode<Entry<K,V>> node = root;
        BSTNode<Entry<K,V>> current = null;
        while ( node != null )
        {
            int compResult = comparator.compare( entry, node.getElement());
            if ( compResult == 0 )
                return node;
            else if ( compResult < 0 ) {
                current = node;
                node = node.getLeft();
            }
            else {
                current = node;
                node = node.getRight();
            }
        }
        return current;
    }
    public V insert(K key, V value) {
        V valueToReturn = null;
        AVLNode<Entry<K,V>> newNode = null;
        AVLNode<Entry<K,V>> node = (AVLNode<Entry<K,V>>) findNode(new EntryClass<>(key, value));

        if ( node == null || comparator.compare( new EntryClass<>(key, value) , node.getElement()) != 0 )
        { // Key does not exist, node is "parent"
            newNode = new AVLNode<>(new EntryClass<>(key, value), node, null, null);
            this.linkSubtreeInsert(newNode, node);
            currentSize++;
        }
        else
        {
            valueToReturn = node.getElement().getValue();
            node.setElement(new EntryClass<>(key, value));
        }

        if(newNode != null)
            rebalance(newNode);
        return valueToReturn;
    }
    /**
     * Links a new subtree, rooted at the specified node, to the tree.
     *
     * @param node - root of the subtree
     * @param parent - parent node for the new subtree
     */
    void linkSubtreeInsert(BSTNode<Entry<K,V>> node, BSTNode<Entry<K,V>> parent) {
        if ( parent == null )
            root = node;
        else {
            if (node != null) {
                node.setParent(parent);
                if (comparator.compare(parent.getElement(),node.getElement()) <= 0)
                    parent.setRight(node);
                else
                    parent.setLeft(node);
            }
        }
    }
    public V remove(K key,V value) {
        V valueToReturn = null; // Valor associado à chave a ser retornado.
        AVLNode<Entry<K, V>> node = null; // Nodo a partir do qual a árvore será reequilibrada.
        AVLNode<Entry<K, V>> oldNode = (AVLNode<Entry<K, V>>) findNode(new EntryClass<>(key, value)); // Busca o nó associado à chave.


        if (oldNode == null || comparator.compare( new EntryClass<>(key, value) , oldNode.getElement()) != 0) {
            return null;
        } else {
            valueToReturn = oldNode.getElement().getValue();

            if (oldNode.getLeft() == null) {
                this.linkSubtreeRemove(oldNode.getRight(), oldNode.getParent(), oldNode); // Remove o nó e conecta a subárvore direita ao pai.
            }

            else if (oldNode.getRight() == null) {
                this.linkSubtreeRemove(oldNode.getLeft(), oldNode.getParent(), oldNode); // Remove o nó e conecta a subárvore esquerda ao pai.
            }
            else {
                BSTNode<Entry<K, V>> minNode = this.minNode(oldNode.getRight());
                oldNode.setElement(minNode.getElement());
                this.linkSubtreeRemove(minNode.getRight(), minNode.getParent(), minNode);
            }
            if (oldNode.getParent() == null) {
                node = (AVLNode<Entry<K, V>>) oldNode.getParent();
            } else {
                node = (AVLNode<Entry<K, V>>) root;
            }
            currentSize--;
        }
        if (node != null) {
            rebalance(node);
        }
        return valueToReturn;
    }


    @Serial
    protected void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(comparator);
        super.writeObject(out);
    }
    @SuppressWarnings("unchecked")
    @Serial
    protected void readObject (java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        comparator = (Comparator<Entry<K,V>>) in.readObject();
        super.readObject(in);
    }


}