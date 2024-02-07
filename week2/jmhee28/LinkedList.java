import java.util.Arrays;

public class LinkedList<E> {
    // Doubly Linked List

    Node<E> head;
    Node<E> tail;
    private int size = 0;

    public LinkedList() {
        this.head = new Node<>(null, null, null);
        this.tail = new Node<>(null, null, null);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        public Node(E item, Node<E> next, Node<E> prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    // *********** Basic Func ******************

    // 리스트 요소 개수를 반환
    public int size() {
        return this.size;
    }

    // 리스트가 비어있는 지 확인
    public boolean isEmpty() {
        return this.size == 0;
    }

    // index 위치의 노드를 반환하는 메소드
    public E get(int index) {
        Node cur = this.head.next;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return (E) cur.item;
    }

    // 리스트에 특정 요소가 있는지 확인하는 메소드
    public boolean contains(Object obj) {
        return false;
    }

    // 리스트를 배열로 반환하는 메소드
    public Object[] toArray() {
        Object[] arr = new Object[this.size];
        Node cur = this.head.next;
        for (int i = 0; i < this.size; i++) {
            arr[i] = cur.item;
            cur = cur.next;
        }
        return arr;
    }

    // ************* Add Func ***************

    // 리스트의 맨 앞에 노드를 추가하는 메소드
    public void addHead(E data){
        Node newNode = new Node(data, this.head.next, this.head);
        this.head.next.prev = newNode;
        this.head.next = newNode;
        this.size++;
    }

    // 리스트의 맨 뒤에 노드를 추가하는 메소드
    public void addTail(E data) {
        Node newNode = new Node(data, this.tail, this.tail.prev);
        this.tail.prev.next = newNode;
        this.tail.prev = newNode;
        this.size++;
    }

    // 원하는 index 위치에 노드를 추가하는 메소드
    public void add(int index, E data) {
        Node cur = this.head.next;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        Node newNode = new Node(data, cur, cur.prev);
        cur.prev.next = newNode;
        cur.prev = newNode;
        this.size++;
    }

    // ********* Delete Func **************

    // 리스트 맨 앞 노드를 제거하는 메소드
    public void removeHead() {
        if(this.isEmpty()) return;

        this.head.next = this.head.next.next;
        this.head.next.prev = this.head;
        this.size--;
    }

    // 리스트 맨 뒤 노드를 제거하는 메소드
    public void removeTail() {
        if(this.isEmpty()) return;
        this.tail.prev = this.tail.prev.prev;
        this.tail.prev.next = this.tail;
        this.size--;
    }

    // 리스트 index 위치의 노드를 제거하는 메소드
    public void remove(int index) {
        if(this.isEmpty()) return;

        Node cur = this.head.next;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        cur.prev.next = cur.next;
        cur.next.prev = cur.prev;
        this.size--;
    }

    public void printDLL() {
        System.out.printf("size: %d\n", this.size());
        System.out.println(Arrays.toString(this.toArray()));
    }
    // Test DLL
    public static void main(String[] args) {
        LinkedList<Integer> dll = new LinkedList<>();
        dll.addTail(1);
        dll.addTail(2);
        dll.addTail(3);
        dll.addTail(4);
        dll.addTail(5);
        dll.addTail(6);
        dll.addTail(7);
        dll.printDLL();

        dll.remove(3);
        dll.printDLL();

        dll.removeTail();
        dll.printDLL();

        System.out.println(dll.get(3));

        dll.removeHead();
        dll.printDLL();

        dll.addHead(0);
        dll.printDLL();

        dll.addTail(0);
        dll.printDLL();
    }
}