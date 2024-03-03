package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

public class KeyboardInput {
    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                return c;
            }
        }
    }
}
