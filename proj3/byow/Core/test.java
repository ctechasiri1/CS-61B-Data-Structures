package byow.Core;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class test {
        @Test
        public void testBasic() {
            Engine engine = new Engine();
            System.out.println(engine.interactWithInputString("n1824155662461692906s"));

            System.out.println(engine.interactWithInputString("n1824155662461692906s"));

            assertThat(engine.interactWithInputString("n1824155662461692906s")).isEqualTo(engine.interactWithInputString("n1824155662461692906s"));
        }
    }


