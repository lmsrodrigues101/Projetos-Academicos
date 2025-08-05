package dataStructures;                                         

import java.io.Serializable; 

/**
 * BST node implementation
 * 
 * @author AED team
 * @version 1.0
 *
 * @param <E> Generic Value
 */
public class BSTNode<E> implements Serializable
{                                                                   



    static final long serialVersionUID = 0L;



	/**
     * Element stored in the node.
     */
    protected E element;



    /**
     * (Pointer to) the left child.
     * 
     */
    protected BSTNode<E> left;

    /**
     * (Pointer to) the right child.
     * 
     */
    protected BSTNode<E> right;
    /**
     * (Pointer to) the parent node.
     *
     */
    protected BSTNode<E> parent;

    /**
     * Constructor for BST nodes
     * 
     */
    public BSTNode( E elem, BSTNode<E> parent, BSTNode<E> left, BSTNode<E> right )
    {                                                                
        this.element = elem;
        this.parent = parent;
        this.left = left;
        this.right = right;
    }
    public BSTNode(E elem) {
        this(elem,null,null,null);
    }

    /**
     * Returns the element of the current node.
     * 
     * @return
     */
    E getElement( )
    {
        return element;
    }



    /**
     * Returns the left child node of the current node.
     * 
     * @return
     */
    BSTNode<E> getLeft( )
    {    
        return left;
    }


    /**
     * Returns the right child node of the current node.
     * 
     * @return
     */
    BSTNode<E> getRight( )
    {
        return right;
    }
    /**
     * Returns the parent node of the current node.
     *
     * @return parent
     */
    BSTNode<E> getParent( )
    {
        return parent;
    }

    //true se o nó atual tem pelo menos um filho
    //Ou seja é interno o nó que nao é folha.
    //usa-se para determinar se vamos procurar aos filhos ou nao
    boolean isInternal() {
        return left != null || right != null;
    }

    //left dado é o novo filho esquerdo
    public void setLeft(BSTNode<E> left) {
        this.left = left;
    }

    //right dado é o novo filho direito
    public void setRight(BSTNode<E> right) {
        this.right = right;
    }

    //atualiza o nó atual pelo nó dado, mantendo os filhos e o pai
    public void setElement(E element) {
        this.element = element;
    }

    //atualiza o parent
    public void setParent(BSTNode<E> parent) {
        this.parent = parent;
    }
}
