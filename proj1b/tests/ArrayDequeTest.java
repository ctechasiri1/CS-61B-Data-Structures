import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDequeTest {

    @Test
    /** In this test, we have three different assert statements that verify that addFirst works correctly. */ public void addFirstTestBasic() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addFirst("back"); // after this call we expect: ["back"]
        assertThat(lld1.toList()).containsExactly("back").inOrder();

        lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
        assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

        lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
    }

    @Test
    public void addFirstFromEmptyAndNonEmpty() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addFirst("back"); // ["back"]
        lld1.addFirst("middle"); // ["middle", "back"]
        lld1.addFirst("front"); // ["front", "middle", "back"]

        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
    }

    @Test
    public void addLastFromEmptyAndNonEmpty() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addLast("front"); // ["back"]
        lld1.addLast("middle"); // ["middle", "back"]
        lld1.addLast("back"); // ["front", "middle", "back"]

        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
    }

    @Test
    public void addFirstAfterRemoveToEmpty() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addFirst(0); // after this call we expect: [0]
        lld1.removeFirst(); // []
        lld1.addFirst(0); // after this call we expect: [0]

        assertThat(lld1.toList()).containsExactly(0).inOrder();
    }

    @Test
    public void addLastAfterRemoveToEmpty() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0); // after this call we expect: [0]
        lld1.removeLast(); // []
        lld1.addLast(0); // after this call we expect: [0]

        assertThat(lld1.toList()).containsExactly(0).inOrder();
    }

    @Test
    public void removeFirst() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]
        lld1.removeFirst(); // [-1, 0, 1, 2]

        assertThat(lld1.toList()).containsExactly(-1, 0, 1, 2);
    }

    @Test
    public void removeLast() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]
        lld1.removeLast(); // [-2, -1, 0, 1]

        assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1);
    }

    @Test
    public void addFirstAfterRemove() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]
        lld1.removeFirst(); // [-2, -1, 0, 1]
        lld1.removeFirst(); // [-1, 0, 1]
        lld1.removeFirst(); // [0, 1]
        lld1.removeFirst(); // [1]
        lld1.removeFirst(); // []
        lld1.addFirst(0);   // [0]

        assertThat(lld1.toList()).containsExactly(0);
    }

    @Test
    public void addLastAfterRemove() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]
        lld1.removeLast(); // [-2, -1, 0, 1]
        lld1.removeLast(); // [-2, -1, 0]
        lld1.removeLast(); // [-2, -1]
        lld1.removeLast(); // [-2]
        lld1.removeLast(); // []
        lld1.addLast(0);   // [0]

        assertThat(lld1.toList()).containsExactly(0);
    }

    @Test
    public void sizeAfterRemoveFromEmpty() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]
        lld1.removeLast(); // [-2, -1, 0, 1]
        lld1.removeLast(); // [-2, -1, 0]
        lld1.removeLast(); // [-2, -1]
        lld1.removeLast(); // [-2]
        lld1.removeLast(); // []

        assertThat(lld1.size()).isEqualTo(0);
    }

    @Test
    public void sizeAfterRemoveToEmpty() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]
        lld1.removeLast(); // [-2, -1, 0, 1]
        lld1.removeLast(); // [-2, -1, 0]
        lld1.removeLast(); // [-2, -1]
        lld1.removeLast(); // [-2]
        lld1.removeLast(); // []
        lld1.addLast(0);   // [0]

        assertThat(lld1.size()).isEqualTo(1);
    }

    @Test
    public void getOobNeg() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(lld1.get(-1)).isEqualTo(null);
    }

    @Test
    public void getOobLarge() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(lld1.get(5)).isEqualTo(null);
    }

    @Test
    public void testSizeZero() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        assertThat(lld1.size() == 0);
    }

    @Test
    public void testSizeGreaterThanZero() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(lld1.size() > 0);
    }

    @Test
    public void testIsEmptyTrue() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        assertThat(lld1.isEmpty()).isTrue();
    }

    @Test
    public void testIsEmptyFalse() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(lld1.isEmpty()).isFalse();
    }

    @Test
    public void toListEmpty() {
        Deque<String> lld1 = new ArrayDeque<>();
        assertThat(lld1.toList().isEmpty());
    }

    @Test
    public void addFirstTriggerResizeUp() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addFirst(16);
        lld1.addFirst(15);
        lld1.addFirst(14);
        lld1.addFirst(13);
        lld1.addFirst(12);
        lld1.addFirst(11);
        lld1.addFirst(10);
        lld1.addFirst(9);
        lld1.addFirst(8);
        lld1.addFirst(7);
        lld1.addFirst(6);
        lld1.addFirst(5);
        lld1.addFirst(4);
        lld1.addFirst(3);
        lld1.addFirst(2);
        lld1.addFirst(1);

        assertThat(lld1.toList()).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16).inOrder();
    }

    @Test
    public void addLastTriggerResizeUp() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(16);
        lld1.addLast(15);
        lld1.addLast(14);
        lld1.addLast(13);
        lld1.addLast(12);
        lld1.addLast(11);
        lld1.addLast(10);
        lld1.addLast(9);
        lld1.addLast(8);
        lld1.addLast(7);
        lld1.addLast(6);
        lld1.addLast(5);
        lld1.addLast(4);
        lld1.addLast(3);
        lld1.addLast(2);
        lld1.addLast(1);

        assertThat(lld1.toList()).containsExactly(16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1).inOrder();
    }

    @Test
    public void removeFirstTriggerResizeDown() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addFirst(16);
        lld1.addFirst(15);
        lld1.addFirst(14);
        lld1.addFirst(13);
        lld1.addFirst(12);
        lld1.addFirst(11);
        lld1.addFirst(10);
        lld1.addFirst(9);
        lld1.addFirst(8);
        lld1.addFirst(7);
        lld1.addFirst(6);
        lld1.addFirst(5);
        lld1.addFirst(4);
        lld1.addFirst(3);
        lld1.addFirst(2);
        lld1.addFirst(1);
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();

        assertThat(lld1.toList()).containsExactly().inOrder();
    }

    @Test
    public void removeLastTriggerResizeDown() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(8);
        lld1.addLast(7);
        lld1.addLast(6);
        lld1.addLast(5);
        lld1.addLast(4);
        lld1.addLast(3);
        lld1.addLast(2);
        lld1.addLast(1);
        lld1.removeLast();
        lld1.removeLast();
        lld1.removeLast();
        lld1.removeLast();
        lld1.removeLast();
        lld1.removeLast();
        lld1.removeLast();
        lld1.removeLast();

        assertThat(lld1.toList()).containsExactly().inOrder();
    }

    @Test
    public void randomizedGetTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);
        lld1.addLast(1);
        lld1.addLast(2);
        lld1.removeLast();
        lld1.removeLast();
        lld1.removeLast();
        lld1.addLast(6);
        lld1.addFirst(7);
        lld1.removeFirst();
        lld1.removeLast();
        lld1.addLast(10);
        lld1.removeLast();
        lld1.addLast(12);
        lld1.removeFirst();
        lld1.addLast(14);
        lld1.addFirst(15);
        lld1.addLast(16);
        lld1.removeFirst();
        lld1.get(0);
        lld1.removeLast();
        lld1.get(0);
        lld1.addLast(21);
        lld1.removeFirst();
        lld1.get(0);

        assertThat(lld1.toList()).containsExactly(21);
    }
}

