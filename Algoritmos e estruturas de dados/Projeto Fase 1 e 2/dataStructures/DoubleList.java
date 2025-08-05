package dataStructures;

import java.io.IOException;
import java.io.Serial;

/**
 * Doubly linked list Implementation
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
public class DoubleList<E> implements List<E> {

    /**
     * Serial Version UID of the Class
     */
    static final long serialVersionUID = 0L;


    /**
     * Node at the head of the list.
     */
    protected DoubleListNode<E> head;

    /**
     * Node at the tail of the list.
     */
    protected DoubleListNode<E> tail;

    /**
     * Number of elements in the list.
     */
    protected int currentSize;

    /**
     * Constructor of an empty double linked list.
     * head and tail are initialized as null.
     * currentSize is initialized as 0.
     */
    public DoubleList() {
        head = null;
        tail = null;
        currentSize = 0;
    }


    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public TwoWayIterator<E> iterator() {
        return new DoubleListIterator<E>(head, tail);
    }

    @Override
    public E getFirst() throws EmptyListException {
        if (this.isEmpty())
            throw new EmptyListException();

        return head.getElement();
    }

    @Override
    public E getLast() throws EmptyListException {
        if (this.isEmpty())
            throw new EmptyListException();

        return tail.getElement();
    }

    /**
     * Retorna o nó na posição especificada na lista.
     * Pré-condição: a posição varia de 0 a currentSize-1.
     *
     * @param position - posição do elemento da lista a ser retornado
     * @return DoubleListNode<E> na posição
     */
    protected DoubleListNode<E> getNode(int position) {
        DoubleListNode<E> node;

        if (position <= (currentSize - 1) / 2) {
            node = head;
            for (int i = 0; i < position; i++)
                node = node.getNext();
        } else {
            node = tail;
            for (int i = currentSize - 1; i > position; i--)
                node = node.getPrevious();

        }
        return node;
    }

    @Override
    public E get(int position) throws InvalidPositionException {
        if (position < 0 || position >= currentSize)
            throw new InvalidPositionException();

        return getNode(position).getElement();
    }

    @Override
    public int find(E element) {
        DoubleListNode<E> node = head;
        int position = 0;
        while (node != null && !node.getElement().equals(element)) {
            node = node.getNext();
            position++;
        }
        if (node == null)
            return -1;
        else
            return position;
    }

    @Override
    public void addFirst(E element) {
        DoubleListNode<E> newNode = new DoubleListNode<E>(element, null, head);
        if (this.isEmpty())
            tail = newNode;
        else
            head.setPrevious(newNode);
        head = newNode;
        currentSize++;
    }

    @Override
    public void addLast(E element) {
        DoubleListNode<E> newNode = new DoubleListNode<E>(element, tail, null);
        if (this.isEmpty())
            head = newNode;
        else
            tail.setNext(newNode);
        tail = newNode;
        currentSize++;
    }

    /**
     * Insere o elemento especificado na posição especificada na lista.
     * Pré-condição: a posição varia de 1 a currentSize-1.
     *
     * @param position - posição intermediária para inserção do elemento
     * @param element  - elemento a ser inserido na posição intermediária
     */
    protected void addMiddle(int position, E element) {
        if (position < 1 || position > currentSize - 1)
            throw new InvalidPositionException();

        DoubleListNode<E> prevNode = this.getNode(position - 1); // Obtém o nó anterior da posição dada
        DoubleListNode<E> nextNode = prevNode.getNext(); // Obtém o próximo nó do prevNode
        DoubleListNode<E> newNode = new DoubleListNode<>(element, prevNode, nextNode); // Cria um novo nó

        prevNode.setNext(newNode); // Atualiza a referência next do nó anterior
        nextNode.setPrevious(newNode); // Atualiza a referência previous do próximo nó

        currentSize++;
    }

    @Override
    public void add(int position, E element) throws InvalidPositionException {
        if (position < 0 || position > currentSize)
            throw new InvalidPositionException();

        if (position == 0)
            this.addFirst(element);
        else if (position == currentSize)
            this.addLast(element);
        else
            this.addMiddle(position, element);
    }

    /**
     * Remove o primeiro nó da lista.
     * Pré-condição: a lista não está vazia.
     */
    protected void removeFirstNode() {
        head = head.getNext();
        if (head == null) {
            // Se a lista ficar vazia, define tail como null
            tail = null;
        } else {
            // Caso contrário, define a referência anterior do novo head como null
            head.setPrevious(null);
        }
        // Diminui o tamanho da lista
        currentSize--;
    }


    /**
     * Remove e retorna o primeiro elemento da lista.
     * Lança EmptyListException se a lista estiver vazia.
     */
    @Override
    public E removeFirst() throws EmptyListException {
        if (this.isEmpty()) {
            // Lança exceção se a lista estiver vazia
            throw new EmptyListException();
        }

        // Obtém o elemento do nó head
        E removed = head.getElement();
        // Remove o primeiro nó
        this.removeFirstNode();
        return removed;
    }

    /**
     * Remove o último nó da lista.
     * Pré-condição: a lista não está vazia.
     */
    protected void removeLastNode() {
        tail = tail.getPrevious();
        if (tail == null) {
            // Se a lista ficar vazia, define head como null
            head = null;
        } else {
            // Caso contrário, define a referência next do novo tail como null
            tail.setNext(null);
        }
        // Diminui o tamanho da lista
        currentSize--;
    }

    /**
     * Remove e retorna o último elemento da lista.
     * Lança EmptyListException se a lista estiver vazia.
     */
    @Override
    public E removeLast() throws EmptyListException {
        if (this.isEmpty()) {
            // Lança exceção se a lista estiver vazia
            throw new EmptyListException();
        }

        // Obtém o elemento do nó tail
        E removed = tail.getElement();
        // Remove o último nó
        this.removeLastNode();
        return removed;
    }

    /**
     * Remove o nó especificado da lista.
     * Pré-condição: o nó não é nem o head nem o tail da lista.
     *
     * @param node - nó do meio a ser removido
     */
    protected void removeMiddleNode(DoubleListNode<E> node) {
        DoubleListNode<E> prevNode = node.getPrevious();
        DoubleListNode<E> nextNode = node.getNext();

        // Atualiza as referências dos nós adjacentes
        prevNode.setNext(nextNode);
        nextNode.setPrevious(prevNode);

        // Limpa as referências do nó removido
        node.setPrevious(null);
        node.setNext(null);

        // Diminui o tamanho da lista
        currentSize--;
    }

    /**
     * Remove e retorna o elemento na posição especificada.
     * Lança InvalidPositionException se a posição for inválida.
     *
     * @param position - posição do elemento a ser removido
     * @return elemento removido
     * @throws InvalidPositionException se a posição for inválida
     */
    @Override
    public E remove(int position) throws InvalidPositionException {
        if (position < 0 || position >= currentSize) {
            // Lança exceção se a posição for inválida
            throw new InvalidPositionException();
        }

        if (position == 0) {
            return this.removeFirst();
        } else if (position == currentSize - 1) {
            return this.removeLast();
        } else {
            // Obtém o nó a ser removido
            DoubleListNode<E> toRemove = getNode(position);
            // Obtém o elemento do nó a ser removido
            E removed = toRemove.getElement();
            // Remove o nó do meio
            this.removeMiddleNode(toRemove);
            return removed;
        }
    }

    /**
     * Retorna o nó com a primeira ocorrência do elemento especificado
     * na lista, se a lista contiver o elemento.
     * Caso contrário, retorna null.
     *
     * @param element - elemento a ser procurado
     * @return DoubleListNode<E> onde o elemento foi encontrado, null se não encontrado
     */
    protected DoubleListNode<E> findNode(E element) {
        int pos = this.find(element);
        if (pos != -1) {
            return this.getNode(pos);
        } else {
            return null;
        }
    }

    /**
     * Remove o elemento especificado da lista.
     * Retorna true se o elemento foi removido, false caso contrário.
     *
     * @param element - elemento a ser removido
     * @return true se o elemento foi removido, false caso contrário
     */
    @Override
    public boolean remove(E element) {
        DoubleListNode<E> node = this.findNode(element);
        if (node == null) {
            return false;
        } else {
            if (node == head) {
                this.removeFirstNode();
            } else if (node == tail) {
                this.removeLastNode();
            } else {
                this.removeMiddleNode(node);
            }
            return true;
        }
    }

    /**
     * Remove todos os elementos da lista especificada e
     * insere-os no final da lista (na sequência correta).
     *
     * @param list - lista a ser anexada ao final desta
     */
    public void append(DoubleList<E> list) {
        if (isEmpty()) {
            this.head = list.head;
        } else {
            this.tail.setNext(list.head);
            list.head.setPrevious(tail);
        }
        this.tail = list.tail;
        currentSize += list.size();
        list.tail = null;
        list.head = null;
        list.currentSize = 0;
    }

    @Serial
    private void writeObject (java.io.ObjectOutputStream out) throws IOException {
        out.writeInt(currentSize);
        for(DoubleListNode<E> x = head; x != null; x = x.getNext()) {
            out.writeObject(x.getElement());
        }
    }

    @SuppressWarnings("unchecked")
    @Serial
    private void readObject (java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            this.addLast((E) in.readObject());
        }
    }




}


