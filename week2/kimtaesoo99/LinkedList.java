import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedList<E> {

    Node<E> first;
    Node<E> last;
    private int size;

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
        return size;
    }

    // 리스트가 비어있는 지 확인
    public boolean isEmpty() {
        return size == 0;
    }


    // 리스트에 특정 요소가 있는지 확인하는 메소드
    public boolean contains(Object obj) {
        Node<E> target = first;

        while (target != null) {
            if (Objects.equals(target.item, obj)) {
                return true;
            }
            target = target.next;
        }

        return false;
    }

    // 리스트를 배열로 반환하는 메소드
    public Object[] toArray() {
        Object[] arr = new Object[size];

        Node<E> target = first;

        for (int i = 0; i < size; i++){
            arr[i] = target.item;
            target = target.next;
        }

        return arr;
    }

    // index 위치의 노드를 반환하는 메소드
    private Node<E> search(int index) {
        Node<E> target;
        //앞에서 부터 탐색
        if (size / 2 > index) {
            target = first;
            for (int i = 0; i < index; i++) {
                target = target.next;
            }
        } else {
            target = last;
            for (int i = size - 1; i > index; i--) {
                target = target.prev;
            }
        }

        return target;
    }

    // ************* Add Func ***************

    // 리스트의 맨 앞에 노드를 추가하는 메소드
    public void addFirst(E data) {
        Node<E> firstNode = first;
        Node<E> newNode = new Node<>(data, firstNode, null);
        first = newNode;
        size++;

        if (last == null) {
            last = newNode;
        } else {
            firstNode.prev = newNode;
        }
    }

    // 리스트의 맨 뒤에 노드를 추가하는 메소드
    public void addLast(E data) {
        Node<E> lastNode = last;
        Node<E> newNode = new Node<>(data, null, lastNode);
        last = newNode;
        size++;

        if (first == null) {
            first = newNode;
        } else {
            lastNode.next = newNode;
        }
    }

    // 원하는 index 위치에 노드를 추가하는 메소드
    public void add(int index, E data) {
        if (!(0 <= index && index < size + 1)) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            addFirst(data);
            return;
        }

        if (index == size) {
            addLast(data);
            return;
        }

        Node<E> findNode = search(index);
        Node<E> prevNode = findNode.prev;
        Node<E> newNode = new Node<>(data, findNode, prevNode);
        size++;

        prevNode.next = newNode;
        findNode.prev = newNode;
    }

    // ********* Delete Func **************

    // 리스트 맨 앞 노드를 제거하는 메소드
    public void removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        Node<E> nextNode = first.next;

        // GC에 의해 관리되도록 null로 수정
        first.item = null;
        first.next = null;
        first = nextNode;
        size--;

        if (first == null) {
            last = null;
        } else {
            nextNode.prev = null;
        }
    }

    // 리스트 맨 뒤 노드를 제거하는 메소드
    public void removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        Node<E> prevNode = last.prev;

        last.prev = null;
        last.item = null;
        last = prevNode;
        size--;

        if (last == null) {
            first = null;
        } else {
            prevNode.next = null;
        }
    }

    // 리스트 index 위치의 노드를 제거하는 메소드
    public void remove(int index) {
        if (!(0 <= index && index < size)) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            removeFirst();
            return;
        }
        if (index == size - 1) {
            removeLast();
            return;
        }

        Node<E> node = search(index);

        Node<E> prevNode = node.prev;
        Node<E> nextNode = node.next;

        node.prev = null;
        node.next = null;
        node.item = null;
        size--;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }
}
