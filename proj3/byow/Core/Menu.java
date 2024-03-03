package byow.Core;


import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;


import java.awt.*;


public class Menu {
    public static int x;
    public static int y;
    public String language = "English";


    public Menu(TERenderer ter, int width, int height) {
        ter.initialize(60, 30);
        x = width / 2;
        y = height / 2;
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(x, y, "Welcome to My Game!");
        Font fontOptions = new Font("Arial", Font.BOLD, 20);
        StdDraw.setFont(fontOptions);
        StdDraw.text(x, y - 2, "(N) New Game");
        StdDraw.text(x, y - 4, "(L) Load Game");
        StdDraw.text(x, y - 6, "(Q) Quit");
        StdDraw.text(x, y - 8, "(J) Switch to Japanese");
        StdDraw.text(x, y - 10, "(R) Replay");




        StdDraw.show();
    }


    public static String load() {
        In in = new In("savefile.txt");
        String input = in.readAll();
        return input;
    }


    public String newWorld() {
        KeyboardInput nextKey = new KeyboardInput();
        String inputs = "";
        Character currInput = 'N';
        while (currInput != 'S') {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setPenColor(StdDraw.WHITE);
            if (language.equals("English")) {
                StdDraw.text(x, y, "Enter a seed: ");
                StdDraw.text(x, y - 2, inputs);
                StdDraw.text(x, y - 4, "Type (S) to submit your seed");
            } else if (language.equals("Japanese")) {
                StdDraw.text(x, y, "シードを入力してください: ");
                StdDraw.text(x, y - 2, inputs);
                StdDraw.text(x, y - 4, "(S) を入力してシードを送信");
            }
            StdDraw.show();
            currInput = nextKey.getNextKey();
            inputs += currInput;
        }
        return inputs;
    }


    public void quit() {
        System.exit(0);
    }


    public void switchToJapanese() {
        language = "Japanese";
        KeyboardInput keyboardInput = new KeyboardInput();
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(x, y, "私のゲームへようこそ!");
        Font fontOptions = new Font("Arial", Font.BOLD, 20);
        StdDraw.setFont(fontOptions);
        StdDraw.text(x, y - 2, "(N) 新しいゲームを始める");
        StdDraw.text(x, y - 4, "(L) ゲームをロードする");
        StdDraw.text(x, y - 6, "(Q) 終了する");
        StdDraw.text(x, y - 8, "(E) 英語に戻す");
        StdDraw.text(x, y - 8, "(R) リプレイ");
        StdDraw.show();
        Character currInput = ' ';
        while (currInput != 'E') {
            currInput = keyboardInput.getNextKey();
            if (currInput == 'N' || currInput == 'L' || currInput == 'Q') {
                // Exit loop when N, L, or Q is pressed
                break;
            }
        }
        if (currInput == 'E') {
            language = "English";
            // Handle switching to English separately
            new Menu(new TERenderer(), 60, 30);
        }
    }
}







