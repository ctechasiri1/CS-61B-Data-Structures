import java.util.ArrayList;
import java.util.List;

public class ArrayDeque<T> implements Deque<T> {

    private T[] item;
    private int front;
    private int rear;
    private int size;

    private static final int STARTINGSIZE = 8;

    public ArrayDeque() {
        front = -1;
        rear = -1;
        size = 0;
        item = (T[]) new Object[STARTINGSIZE];
    }

    //check for resize when the array is full
    @Override
    public void addFirst(T x) {
        if (isEmpty()) {
            front = 0;
            rear = 0;
        } else if (front == 0) {
            front = item.length - 1;
        } else {
            front = (front - 1 + item.length) % item.length;
        }
        item[front] = x;
        size++;
        if (size == item.length) {
            resizeUp();
        }
    }

    //check for resize when the array is full
    @Override
    public void addLast(T x) {
        if (isEmpty()) {
            front = 0;
            rear = 0;
        } else if (rear == item.length - 1) {
            rear = 0;
        } else {
            rear = (rear + 1) % item.length;
        }
        item[rear] = x;
        size++;
        if (size == item.length) {
            resizeUp();
        }
    }


    //temp copy front too so the copy of the array has its front and rear pointers too
    private void resizeUp() {
        T[] newQueue = (T[]) new Object[item.length * 2];
        List<T> temp = toList();
        item = newQueue;
        for (int i = 0; i < temp.size(); i++) {
            item[i] = temp.get(i);
        }
        front = 0;
        rear = size - 1;
    }

    //toList fix with the temp
    @Override
    public List<T> toList() {
        List<T> element = new ArrayList<>();
        if (size == 0) {
            return element;
        }
        int temp = front;
        for (int i = 0; i < size; i++) {
            element.add(item[temp]);
            temp = (temp + 1) % item.length;
        }
        return element;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    //resize down for when its empty
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T temp = item[front];
        item[front] = null;
        front = (front + 1) % item.length;
        size--;
        if (size > 0 && size == item.length / 4 && item.length > STARTINGSIZE) {
            resizeDown();
        }
        return temp;
    }

    //resize down for when its empty
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T temp = item[rear];
        item[rear] = null;
        if (rear == 0) {
            rear = item.length - 1;
        } else {
            rear = (rear - 1 + item.length) % item.length;
        }
        size--;
        if (size > 0 && size - 1 <= item.length / 4 && item.length > STARTINGSIZE) {
            resizeDown();
        }
        return temp;
    }

    //implement toList into resize


    private void resizeDown() {
        T[] newQueue = (T[]) new Object[item.length / 2];
        List<T> temp = toList();
        item = newQueue;
        for (int i = 0; i < temp.size(); i++) {
            item[i] = temp.get(i);
        }
        front = 0;
        rear = size - 1;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int theIndex = (front + index) % item.length;
        return item[theIndex];
    }

    public static void main(String[] args) {
        Deque<Integer> ad = new ArrayDeque<>();
    }

}
