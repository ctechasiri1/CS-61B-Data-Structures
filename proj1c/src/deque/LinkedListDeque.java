package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkedListDeque<T> implements Deque<T> {


    public class Node {
        private T item;
        private Node prev;
        private Node next;

        public Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node sentinal;
    private int size;

    public LinkedListDeque() {
        sentinal = new Node(null, null, null);
        sentinal.next = sentinal;
        sentinal.prev = sentinal;
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        Node newNode = new Node(x, sentinal, sentinal.next);
        sentinal.next = newNode;
        sentinal.next.next.prev = sentinal.next;
        size += 1;
    }

    @Override
    public void addLast(T x) {
        Node newNode = new Node(x, sentinal.prev, sentinal);
        sentinal.prev = newNode;
        sentinal.prev.prev.next = sentinal.prev;
        size += 1;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        Node iterator = sentinal.next;
        while (iterator != sentinal) {
            returnList.add(iterator.item);
            iterator = iterator.next;
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T item = sentinal.next.item;
        sentinal.next.next.prev = sentinal;
        sentinal.next = sentinal.next.next;
        size -= 1;
        return item;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T item = sentinal.prev.item;
        sentinal.prev.prev.next = sentinal;
        sentinal.prev = sentinal.prev.prev;
        size -= 1;
        return item;
    }

    @Override
    public T get(int index) {
        if (index < 0) {
            return null;
        } else if (index > size) {
            return null;
        } else {
            Node startingNode = sentinal.next;
            for (int i = 0; i < index; i++) {
                startingNode = startingNode.next;
            }
            return startingNode.item;
        }
    }

    /**
     * This getRecursiveNode is the helper function to getRecursive where
     */
    private T getRecursiveHelper(Node node, int index) {
        if (index == 0) {
            return node.item;
        } else if (node == null) {
            return node.item;
        }
        return getRecursiveHelper(node.next, index - 1);
    }

    @Override
    public T getRecursive(int index) {
        Node newNode = sentinal.next;
        if (index == 0) {
            return newNode.item;
        } else if (newNode == null) {
            return null;
        } else if (index < 0) {
            return null;
        } else if (index > size) {
            return null;
        }
        return getRecursiveHelper(newNode.next, index - 1);
    }

    public static void main(String[] agrs) {
        Deque<Integer> lld = new LinkedListDeque<>();
    }

    @Override
    public Iterator<T> iterator() {
        return new LLIterator();

    }

    public class LLIterator implements Iterator<T> {
        private int position;

        public LLIterator() {
            position = 0;
        }

        @Override
        public boolean hasNext() {
            return position < size;
        }

        @Override
        public T next() {
            T returnItem = get(position);
            position += 1;
            return returnItem;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Deque otherSet) {
            if (this.size != otherSet.size()) {
                return false;
            }
            for (T x : this) {
                if (!otherSet.toList().contains(x)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder returnSB = new StringBuilder("[");
        for (int i = 0; i < size - 1; i += 1) {
            returnSB.append(get(i).toString());
            returnSB.append(", ");
        }
        returnSB.append(get(size - 1));
        returnSB.append("]");
        return returnSB.toString();
    }
}
