public class Main {
    private static LinkedList list;
    private static java.util.LinkedList list2;

    public static void main(String[] args) {
        list = new LinkedList();
        list2 = new java.util.LinkedList();

        //add
        measureExecutionTime(Main::addCusLinkedList);
        measureExecutionTime(Main::addLinkedList);

        //remove
        measureExecutionTime(Main::removeCusLinkedList);
        measureExecutionTime(Main::removeLinkedList);

    }

    private static void addCusLinkedList() {
        for (int i = 0; i < 1000000; i++) {
            list.addFirst(i);
        }
    }

    private static void addLinkedList() {
        for (int i = 0; i < 1000000; i++) {
            list2.addFirst(i);
        }
    }

    private static void removeCusLinkedList() {
        for (int i = 0; i < 100000; i++) {
            list.remove(5000);
        }
    }

    private static void removeLinkedList() {
        for (int i = 0; i < 100000; i++) {
            list2.remove(5000);
        }
    }

    private static void measureExecutionTime(Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();

        System.out.println(endTime - startTime + " milliseconds");
    }
}
