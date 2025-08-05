package dataStructures;                                         

/**
 * Advanced BSTree Data Type implementation
 * @author AED team
 * @version 1.0
 * @param <K> Generic type Key, must extend comparable
 * @param <V> Generic type Value 
 */
public abstract class AdvancedBSTree<K extends Comparable<K>, V> extends BinarySearchTree<K,V>
{

    static final long serialVersionUID = 0L;

	/**
     * Performs a single left rotation rooted at Y node.
     * Node X was a  right  child  of Y before the  rotation,
     * then Y becomes the left child of X after the rotation.
     * @param Y - root of the rotation
     * @pre: Y has a right child
     */
    protected void rotateLeft( BSTNode<Entry<K,V>> Y)
    {
        // X é o filho direito de Y
    	BSTNode<Entry<K, V>> X = Y.getRight();

        // Y assume o lugar do filho esquerdo de X
    	Y.setRight(X.getLeft());
        if (X.getLeft() != null) {
            X.getLeft().setParent(Y);
        }

        X.setParent(Y.getParent());

        if (Y.getParent() == null) {
            root = X;
        } else if (Y == Y.getParent().getLeft()) {
            Y.getParent().setLeft(X);
        } else {
            Y.getParent().setRight(X);
        }

        X.setLeft(Y);
        Y.setParent(X);
    }


    /**
     * Performs a single right rotation rooted at Y node.
     * Node X was a  left  child  of Y before the  rotation,
     * then Y becomes the right child of X after the rotation.
     * @param Y - root of the rotation
     * @pre: Y has a left child
     */
    protected void rotateRight( BSTNode<Entry<K,V>> Y)
    {
        //X é filho esquerdo
    	BSTNode<Entry<K, V>> X = Y.getLeft();
        // Y seta o seu filho esquerdo como filho direito do X
        Y.setLeft(X.getRight());

        // Se X tiver filho direito, este torna-se filho do Y
        if (X.getRight() != null) {
            X.getRight().setParent(Y);
        }

        // X vai ficar com o pai do Y e substitui-lo
        X.setParent(Y.getParent());

        //Se Y era a raiz o X passa a ser a raiz
        if (Y.getParent() == null) {
            root = X;
        } else if (Y == Y.getParent().getLeft()) {
            // Se Y era o filho esquerdo do seu pai, X torna-se o filho esquerdo do pai do Y
            Y.getParent().setLeft(X);
        } else {
            // se era o direito torna se o direto
            Y.getParent().setRight(X);
        }
        // Y torna se o filho direito de X
        X.setRight(Y);
        //X é o pai de Y
        Y.setParent(X);
    }

    /**
     * Performs a tri-node restructuring (a single or double rotation rooted at X node).
     * Assumes the nodes are in one of following configurations:
     *
     * @param X - root of the rotation
     * <pre>
     *          z=c       z=c        z=a         z=a
     *         /  \      /  \       /  \        /  \
     *       y=b  t4   y=a  t4    t1  y=c     t1  y=b
     *      /  \      /  \           /  \         /  \
     *    x=a  t3    t1 x=b        x=b  t4       t2 x=c
     *   /  \          /  \       /  \             /  \
     *  t1  t2        t2  t3     t2  t3           t3  t4
     * </pre>
     * @return the new root of the restructured subtree
     */
    protected BSTNode<Entry<K, V>> restructure(BSTNode<Entry<K, V>> X) {
        BSTNode<Entry<K, V>> Y = X.getParent();
        BSTNode<Entry<K, V>> Z = Y.getParent(); 

        //Y é filho esquerdo do Z
        if (Y == Z.getLeft()) {
            //X filho esq de Y (left-left)
            if (X == Y.getLeft()) {
                rotateRight(Z);
                return Y; // Y nova raiz
            } else { // (left-right)
                rotateLeft(Y);
                rotateRight(Z);
                return X;
            }
        } else {
            if (X == Y.getRight()) { //(Right-Right)
                rotateLeft(Z); // Perform single rotation at Z
                return Y; // Y becomes the new root of this subtree
            } else { //(Right-Left)
                rotateRight(Y);
                rotateLeft(Z);
                return X;
            }
        }
    }


}

