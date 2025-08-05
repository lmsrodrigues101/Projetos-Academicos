package dataStructures;                                         

/**
 * AVL node version of the BSTNode
 * 
 * @author AED team
 * @version 1.0
 *
 * @param <E> Generic element type
 */
class AVLNode<E> extends BSTNode<E>
{


    static final long serialVersionUID = 0L;
    
	/**
     * Height of the node
     * A altura de um nó é a distância até a folha mais distante abaixo dele.
     */
    protected int height;


    /**
     * Constructor for AVL nodes
     *
     * @param element element stored in the node

     * @param left    sub-tree of this node
     * @param right   sub-tree of this node
     */
    public AVLNode( E element, AVLNode<E> parent,AVLNode<E> left, AVLNode<E> right )
    {                                                                
        super(element, parent, left, right);
        // Calcula a altura como 1 + o maior valor entre as alturas dos filhos esquerdo e direito.
        height= 1 + Math.max(getHeight(left),getHeight(right));
    }
    /**
     * Méto do para obter a altura de um nó AVL.
     *
     * @param node Nó a ser verificado.
     * @return A altura do nó. Retorna 0 se o nó for nulo.
     */
    protected int getHeight(AVLNode<E> node) {
        if (node==null)
            return 0;
        return node.getHeight();
    }

    /**
     * Returns the height of the tree rooted at the given node.
     * @return height of the tree.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Tests whether the tree rooted at the given node is balanced.
     *  Verifica se a subárvore enraizada neste nó está balanceada.
     * A árvore está balanceada se a diferença de altura entre
     * os filhos esquerdo e direito for -1, 0 ou 1.
     *
     *
     * @return true iff tree is balanced.
     */
    public boolean isBalanced() {
        int dif= getHeight((AVLNode<E>)left)-getHeight((AVLNode<E>)right);
        return dif==0 ||dif==-1 ||dif ==1;
    }

    /**
     * Constructor for AVL nodes
     * 
     * @param elem to be stored in this AVL tree node
     */
    public AVLNode( E elem  )
    {    
        super(elem);
        height = 1;
    }


    /**
     * Recomputes and returns the height of the tree rooted at this node by querying
     * (but not recursively recomputing) the heights of the left and right subtrees.
     * @return the recomputed height.
     */
    public int setHeight() {
        height= 1 + Math.max(getHeight((AVLNode<E>)left),getHeight((AVLNode<E>)right));
        return height;
    }

    /**
     * Return the child of this node with greater height
     */
    AVLNode<E> tallerChild() {
        return getHeight((AVLNode<E>) left) > getHeight((AVLNode<E>) right) ? (AVLNode<E>) left : (AVLNode<E>) right;
    }
}






