package linkedlist;

public class SingleLinkedList<E> {
    private Node<E> head; // 노드 첫부분
    private Node<E> tail; // 노드 끝부분

    private int size;

    public SingleLinkedList(){
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    private static class Node<E> {
        private E item; // 값
        private Node<E> next; // 주소

        public Node(E item, Node<E> next){
            this.item = item;
            this.next = next;
        }

    }

    public int size(){
        return this.size;
    }

    public boolean isEmpty(){
        if(size == 0) return true;
        else return false;
    }

    public Node<E> search(int index){

        Node<E> now = this.head;

        for (int i = 0; i < index; i++) {
            now = now.next;
        }

        return now;
    }

    public boolean contains(Object obj){
        Node<E> now = this.head;

        for (int i = 0; i < size; i++) {
            now = now.next;
            if(now == obj){
                return true;
            }
        }
        return false;
    }

    public Object[] toArray(){
        Object[] arr = new Node[size];
        Node<E> now = this.head;

        for (int i = 0; i < size; i++) {
            arr[i] = now.item;
        }

        return arr;
    }

    public void addFirst(E data){
        Node<E> first = this.head;

        Node<E> node = new Node<>(data, first);

        size ++;

        head = node;

        if(first == null){
            tail = node;
        }
    }

    public void addLast(E data){
        Node<E> last = this.tail;

        Node<E> node = new Node<>(data, null);

        size ++;

        tail = node;

        if(last == null){
            head = node;
        }else{
            last.next = node;
        }
    }

    public void add(int index, E data){
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }

        if(index == 0){
            addFirst(data);
            return;
        }else if(index == size -1){
            addLast(data);
            return;
        }

        Node<E> prev_node = search(index - 1);
        Node<E> next_node = prev_node.next;

        Node<E> node = new Node<>(data, next_node);
        prev_node.next = node;

        size ++;
    }

    public void remove(int index){
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }



        Node<E> prev_node = search(index-1);
        Node<E> now = prev_node.next;
        Node<E> next_node = now.next;

        now.next = null;
        now.item = null;


        prev_node.next = next_node;
        size--;

    }

    public void removeFirst(){
        if(head == null){
            throw new IndexOutOfBoundsException();
        }

        Node<E> first = head.next;

        head.next = null;
        head.item = null;

        head = first;

        size--;

        if(head == null){
            tail = null;
        }
    }

    public void removeLast(){
        remove(size - 1);
    }



}


