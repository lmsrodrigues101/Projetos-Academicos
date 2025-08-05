package dataStructures;

import java.io.IOException;
import java.io.Serial;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * AVL tree implementation
 * 
 * @author AED team
 * @version 1.0
 *
 * @param <K> Generic type Key, must extend comparable
 * @param <V> Generic type Value 
 */
public class AVLTree<K extends Comparable<K>, V> 
extends AdvancedBSTree<K,V> implements OrderedDictionary<K,V> {

	static final long serialVersionUID = 0L;

	AVLTree(AVLNode<Entry<K, V>> node) {
		root = node;
	}

	public AVLTree() {
		this(null);
	}

	/**
	 * Rebalance method called by insert and remove.  Traverses the path from
	 * zPos to the root. For each node encountered, we recompute its height
	 * and perform a trinode restructuring if it's unbalanced.
	 * the rebalance is completed with O(log n) running time
	 */
	void rebalance(AVLNode<Entry<K, V>> zPos) {
		// Atualiza a altura inicial de zPos caso tenha filhos.
		if (zPos.isInternal()) {
			zPos.setHeight();
		}
		// Percorre do nó atual até a raiz da árvore.
		while (zPos != null) {
			// Atualiza a altura de zPos após alterações em seus filhos.
			zPos.setHeight();
			// Verifica se zPos está desbalanceado.
			if (!zPos.isBalanced()) {

				AVLNode<Entry<K, V>> yPos = zPos.tallerChild();
				if(yPos == null){
					break;
				}
				// Identifica o neto mais alto de zPos (e.g., neto para rotação).
				AVLNode<Entry<K, V>> xPos = yPos.tallerChild();
				if(xPos == null){
					break;
				}
				// Realiza a reestruturação (rotação simples ou dupla).
				zPos = (AVLNode<Entry<K, V>>) restructure(xPos);
				// Após a reestruturação, atualiza as alturas dos nós afetados.
				((AVLNode<Entry<K, V>>) zPos.getLeft()).setHeight();
				((AVLNode<Entry<K, V>>) zPos.getRight()).setHeight();
				zPos.setHeight();
			}
			zPos = (AVLNode<Entry<K, V>>) zPos.getParent();
		}
	}


	@Override
	public V insert(K key, V value) {
		V valueToReturn = null;
		AVLNode<Entry<K, V>> newNode = null;
		AVLNode<Entry<K, V>> node = (AVLNode<Entry<K, V>>) findNode(key);

		if (node == null || node.getElement().getKey().compareTo(key) != 0) {
			newNode = new AVLNode<>(new EntryClass<>(key, value), node, null, null);
			this.linkSubtreeInsert(newNode, node);
			currentSize++;
		} else {
			valueToReturn = node.getElement().getValue();
			node.setElement(new EntryClass<>(key, value));
		}

		if (newNode != null)
			rebalance(newNode);
		return valueToReturn;
	}

	@Override
	public V remove(K key) {
		V valueToReturn = null;
		AVLNode<Entry<K,V>> node = null;
		AVLNode<Entry<K,V>> oldNode = (AVLNode<Entry<K,V>>) super.findNode(key);
		if ( oldNode == null || oldNode.getElement().getKey().compareTo(key) != 0 )
			return null;
		else
		{
			valueToReturn = oldNode.getElement().getValue();

			if ( oldNode.getLeft() == null )
				// The left subtree is empty.
				this.linkSubtreeRemove(oldNode.getRight(), oldNode.getParent(),oldNode);
			else if ( oldNode.getRight() == null )
				// The right subtree is empty.
				this.linkSubtreeRemove(oldNode.getLeft(), oldNode.getParent(),oldNode);
			else
			{
				// Node has 2 children. Replace the node's entry with
				// the 'minEntry' of the right subtree.
				BSTNode<Entry<K,V>> minNode = this.minNode(oldNode.getRight());
				oldNode.setElement( minNode.getElement() );
				// Remove the 'minEntry' of the right subtree.
				this.linkSubtreeRemove(minNode.getRight(), minNode.getParent(),minNode);
			}

			if (oldNode.getParent() != null) {
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
/*
	@Serial
	protected void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeInt(currentSize);
		if (root == null) {
			return;
		}
		Iterator<Entry<K, V>> it = new BSTBreadthFirstIterator<>(root);
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

*/


}
