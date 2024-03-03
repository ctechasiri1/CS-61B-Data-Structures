package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator comparator;

    public MaxArrayDeque(Comparator<T> c) {
        this.comparator = c;
    }

    public T max() {
        if (this.isEmpty()) {
            return null;
        }
        T maxValue = this.get(0);
        for (int i = 1; i < this.size(); i++) {
            T currentValue = this.get(i);
            if (comparator.compare(maxValue, currentValue) < 0) {
                maxValue = currentValue;
            }
        }
        return maxValue;
    }

    public T max(Comparator<T> c) {
        if (this.isEmpty()) {
            return null;
        }
        T maxValue = this.get(0);
        for (int i = 1; i < this.size(); i++) {
            T currentValue = this.get(i);
            if (c.compare(maxValue, currentValue) < 0) {
                maxValue = currentValue;
            }
        }
        return maxValue;
    }
}
