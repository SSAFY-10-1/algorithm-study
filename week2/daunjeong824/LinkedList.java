package Source;

import java.util.NoSuchElementException;

public class LinkedList<E> {

    Node<E> head;
    Node<E> tail;
    
    private static final int MAX_NODE = 1000;
    private static int node_cnt = 0;
    private static Node[] node_pool = new Node[MAX_NODE];
    

    public LinkedList() {
    	this.head = null;
    	this.tail = null;
    	node_cnt = 0;
    	
    	for(int i = 0; i < MAX_NODE; i++)
    		node_pool[i] = new Node<E>();
    }
    
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;
    }
    
    public Node<E> newNode(E item, Node<E> next, Node<E> prev) {
    	
    	node_pool[node_cnt].item = item;
        node_pool[node_cnt].next = next;
        node_pool[node_cnt].prev = prev;

        return node_pool[node_cnt++];
    }

    // *********** Basic Func ******************

    // 리스트 요소 개수를 반환
    public int size() {
        return node_cnt;
    }

    // 리스트가 비어있는 지 확인
    public boolean isEmpty() {
        return node_cnt == 0;
    }

    // index 위치의 노드를 반환하는 메소드
    public E get(int index) {
    	Node<E> ptr = head;
    	int cnt = 0;
    	
    	while(ptr != null && cnt != index) {
    		ptr = ptr.next;
    		cnt++;
    	}
        return ptr.item;
    }

    // 리스트에 특정 요소가 있는지 확인하는 메소드
    public boolean contains(Object obj) {
        Node<E> ptr = head;
        
        while(ptr != null && ptr.item != obj)
        	ptr = ptr.next;
        
    	return ptr != null;
    }
    
    // 리스트를 배열로 반환하는 메소드
    public Object[] toArray() {
        return null;
    }
    
    // 출력하는 메소드
    public void print() {
    	Node<E> ptr = head;
    	
    	while(ptr != null) {
    		System.out.print(ptr.item + " : ");
    		ptr = ptr.next;
    	}
    	System.out.println();
    }

    // ************* Add Func ***************

    // 리스트의 맨 앞에 노드를 추가하는 메소드
    public void addFirst(E data){
    	Node<E> h = head;
    	Node<E> node = newNode(data, h, null);
    	head = node;
    	
    	if(h != null)  // 첫 노드가 존재한다면
    		h.prev = node;		
    	 else  // 첫 시작이라면
    		tail = node;
    }

    // 리스트의 맨 뒤에 노드를 추가하는 메소드
    public void addLast(E data) {
    	Node<E> t = tail;
    	Node<E> node = newNode(data, null, t);
    	tail = node;
    	
    	if(t != null) t.next = node;
    	else head = node;
    }

    // 원하는 index 위치에 노드를 추가하는 메소드
    public void add(int index, E data) {
    	
    	if(index > node_cnt || index < 0)
    		throw new IndexOutOfBoundsException();
    	
    	if(index == node_cnt) {
    		addLast(data);
    		return;
    	}
    	
    	if(index == 0) {
    		addFirst(data);
    		return;
    	}
    	
    	Node<E> tmp = head;
    	for(int i = 0; i < index; i++)
    		tmp = tmp.next;
    	
    	Node<E> prevNode = tmp.prev;
    	Node<E> node = newNode( data, tmp, prevNode );
    	tmp.prev = node;
    	prevNode.next = node;
    }

    // ********* Delete Func **************

    // 리스트 맨 앞 노드를 제거하는 메소드
    public E removeFirst() {
    	if(head == null)
    		throw new NoSuchElementException();
    	
    	E item = head.item;
    	Node<E> headNext = head.next;
    	
    	head = headNext;
    	if(head != null) head.prev = null; // head 앞 링크 끊기
    	else tail = null; // 리스트가 1개인데 삭제 -> 전부 비워짐
    	
    	return item;
    }

    // 리스트 맨 뒤 노드를 제거하는 메소드
    public E removeLast() {
    	if(tail == null)
    		throw new NoSuchElementException();
    	
    	E item = tail.item;
    	Node<E> tailPrev = tail.prev;
    	
    	tail = tailPrev;
    	if(tail != null) tail.next = null; // tail 다음 링크 끊기
    	else head = null; // 리스트 1개인데 삭제 -> 전부 비우기
    	
    	return item;
    }

    // 리스트 index 위치의 노드를 제거하는 메소드
    public E remove(int index) {
    	if(index > node_cnt || index < 0)
    		throw new IndexOutOfBoundsException();
    	
    	if(index == node_cnt) 
    		return removeLast();
    	
    	if(index == 0) 
    		return removeFirst();	
    	
    	Node<E> tmp = head;
    	for(int i = 0; i < index; i++)
    		tmp = tmp.next; // index에 해당하는 노드
    	
    	Node<E> prevNode = tmp.prev; // 해당 노드의 앞
    	Node<E> nextNode = tmp.next; // 해당 노드의 뒤
    	
    	prevNode.next = nextNode; // 앞노드 - next -> 뒷노드
    	tmp.prev = null; 
    	nextNode.prev = prevNode; // 뒷노드 - prev -> 앞노드
    	tmp.next = null;
    	
    	E item = tmp.item;
    	tmp.item = null;
    	return item;
    }
}