import java.util.NoSuchElementException;

public class LinkedList<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    public LinkedList() {}

    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        public Node(E data) {
            this.data = data;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private Node<E> search(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        if (index < size / 2) {
            Node<E> x = head;

            for (int i = 0; i < index; i++) {
                x = x.next;
            }

            return x;
        } else {
            Node<E> x = tail;

            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }

            return x;
        }
    }

    public E get(int index) {
        return search(index).data;
    }

    public int indexOf(Object value) {
        int index = 0;

        for (Node<E> x = head; x != null; x = x.next) {
            if (value.equals(x.data)) {
                return index;
            }

            index++;
        }

        return -1;
    }

    public int lastIndexOf(Object value) {
        int index = size - 1;

        for (Node<E> x = tail; x != null; x = x.prev) {
            if (value.equals(x.data)) {
                return index;
            }

            index--;
        }

        return -1;
    }

    public boolean contains(Object value) {
        return indexOf(value) >= 0;
    }

    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;

        for (Node<E> x = head; x != null; x = x.next) {
            array[index++] = x.data;
        }

        return array;
    }

    public void addFirst(E data) {
        Node<E> newNode = new Node<E>(data);

        if (head != null) {
            newNode.next = head;
            head.prev = newNode;
        }

        head = newNode;
        size++;

        if (tail == null) {
            tail = head;
        }
    }

    public void addLast(E data) {
        Node<E> newNode = new Node<E>(data);
        
        if (tail != null) {
            newNode.prev = tail;
            tail.next = newNode;
        }

        tail = newNode;
        size++;

        if (head == null) {
            head = tail;
        }
    }

    public void add(int index, E data) {
        if (index == 0) {
            addFirst(data);
            return;
        }

        if (index == size) {
            addLast(data);
            return;
        }

        Node<E> nextNode = search(index);
        Node<E> prevNode = nextNode.prev;
        Node<E> newNode = new Node<E>(data);

        prevNode.next = newNode;
        newNode.next = nextNode;
        nextNode.prev = newNode;
        newNode.prev = prevNode;
        size++;
    }

    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        Node<E> newHead = head.next;
        E returnData = head.data;
        head.data = null;
        head.next = null;
        head = null;
        head = newHead;
        size--;

        if (head == null) {
            tail = null;
        } else {
            head.prev = null;
        }

        return returnData;
    }

    public E removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        Node<E> newTail = tail.prev;
        E returnData = tail.data;
        tail.data = null;
        tail.prev = null;
        tail = null;
        tail = newTail;
        size--;

        if (tail == null) {
            head = null;
        } else {
            tail.next = null;
        }

        return returnData;
    }

    public E remove(int index) {
        if (index == 0) {
            return removeFirst();
        }

        if (index == size - 1) {
            return removeLast();
        }

        Node<E> target = search(index);
        Node<E> prevNode = target.prev;
        Node<E> nextNode = target.next;

        E returnData = target.data;
        target.data = null;
        target.next = null;
        target.prev = null;
        prevNode.next = null;
        nextNode.prev = null;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        size--;

        return returnData;
    }

}