package Source;

public class LinkedListTest {

	public static void main(String[] args) {
		LinkedList<Integer> slist = new LinkedList();
		
//		slist.addLast(33);
//		slist.addLast(55);
//		slist.addLast(196);
		//slist.addFirst(12);
		slist.add(0, 7);
		
		slist.print();
//		System.out.println(slist.contains(-1));
		System.out.println(slist.size());
	}

}
