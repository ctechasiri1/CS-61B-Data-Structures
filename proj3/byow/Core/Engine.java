package byow.Core;


import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 60;
    public static final int HEIGHT = 30;

    private boolean win = false;
    private static final int NEXT_NUM = 10;
    private static final int PAUSE_NUM = 50;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        TETile[][] game;
        String desc;
        Menu mainMenu = new Menu(ter, WIDTH, HEIGHT);
        KeyboardInput input = new KeyboardInput();
        char c = 'p';
        HUD hud = new HUD(ter, WIDTH, HEIGHT);
        String playerInput = "";
        boolean exit = false;
        while (!exit) {
            c = input.getNextKey();
            if (c == 'J') {
                mainMenu.switchToJapanese();
                continue;
            }
            if (c == 'N') {
                playerInput += c;
                playerInput += mainMenu.newWorld();
                break;
            }
            if (c == 'L') {
                playerInput += c;
                break;
            }
            if (c == 'Q') {
                mainMenu.quit();
                System.exit(0); // Exit the game immediately
            }
            if (c == 'R') {
                playerInput += c;
                break;
            }
        }
        if (c == 'R') {
            String loadSeed = loadGame();
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(loadSeed);
            long seed = 0;

            while (matcher.find()) {
                seed = seed * NEXT_NUM + Long.parseLong(matcher.group());
            }
            int numberOfDigits = Long.toString(seed).length();
            for (int i = numberOfDigits + 2; i < loadSeed.length(); i++) {
                game = interactWithInputString(loadSeed.substring(0, i));
                ter.renderFrame(game);
                StdDraw.pause(PAUSE_NUM);
            }
        }
        game = interactWithInputString(playerInput);
        ter.renderFrame(game);
        while (!exit) {
            int mouseX = Math.min((int) Math.floor(StdDraw.mouseX()), game.length - 1);
            int mouseY = Math.min((int) Math.floor(StdDraw.mouseY()), game[0].length - 1);
            desc = game[mouseX][mouseY].description();
            hud.updateHud(game, desc, win);
            if (StdDraw.hasNextKeyTyped()) {
                c = input.getNextKey();
                playerInput += c;
                game = interactWithInputString(playerInput);
                hud.updateHud(game, desc, win);
            }
        }
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        String currInput = input;
        char[] charArr = input.toCharArray();
        long seed = 0;
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        int index = 0;
        boolean quit = false;
        while (index < currInput.length() && !quit) {
            char c = currInput.charAt(index);
            if (c == 'N' || c == 'n') {
                // New game
                index++;
                while (Character.isDigit(currInput.charAt(index))) {
                    seed = seed * NEXT_NUM + Long.parseLong(String.valueOf(currInput.charAt(index)));
                    index++;
                }
                worldGenerator.buildWorld(finalWorldFrame, seed);
            }
            if (c == 'l' || c == 'L' || c == 'R' || c == 'r') {
                String loadSeed = loadGame();
                currInput = loadSeed + input.substring(1);
                index++;
                while (Character.isDigit(currInput.charAt(index))) {
                    seed = seed * NEXT_NUM + Long.parseLong(String.valueOf(currInput.charAt(index)));
                    index++;
                }
                worldGenerator.buildWorld(finalWorldFrame, seed);
            } else if (c == 'W' || c == 'A' || c == 'S' || c == 'D'
                    || c == 'w' || c == 'a' || c == 's' || c == 'd') {
                // Move player
                movePlayer(finalWorldFrame, c);
                index++;
            } else if (c == ':') {
                if (currInput.length() > index + 1) {
                    char command = currInput.charAt(index + 1);
                    if (command == 'q' || command == 'Q') {
                        // Quit and save game
                        saveGame(currInput);
                    } else if (command == 's' || command == 'S') {
                        // Save game
                        saveGame(currInput.substring(0, currInput.length() - 2));
                    } else if (command == 'l' || command == 'L') {
                        // Load game
                        currInput = loadGame();
                        index++;
                    }
                }
                index += 2;
            }
        }


        return finalWorldFrame;
    }


    public void saveGame(String input) {
        try {
            FileWriter writer = new FileWriter("savefile.txt");
            writer.write(input);
            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }


    public String loadGame() {
        try {
            File file = new File("savefile.txt");
            if (!file.exists()) {
                System.out.println("No previous save found. Exiting...");
                System.exit(0);
            }
            Scanner scanner = new Scanner(file);
            String input = scanner.nextLine();
            String notations = input.substring(0, input.length() - 2);
            scanner.close();
            return notations;
        } catch (IOException e) {
            System.out.println("Error loading game: " + e.getMessage());
            return null;
        }
    }


    private void movePlayer(TETile[][] world, char c) {
        // Find player's current position
        int x = 0;
        int y = 0;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[i][j] == Tileset.AVATAR) {
                    x = i;
                    y = j;
                }
            }
        }
        // Move player based on input
        if (c == 'W' && y < HEIGHT - 1) {
            if (world[x][y + 1] != Tileset.WALL) {
                if (world[x][y + 1] == Tileset.FLOWER) {
                    win = true;
                    world[x][y] = Tileset.FLOOR;
                    world[x][y + 1] = Tileset.AVATAR;
                } else {
                    world[x][y] = Tileset.FLOOR;
                    world[x][y + 1] = Tileset.AVATAR;
                }
            }
        } else if (c == 'A' && x > 0) {
            if (world[x - 1][y] != Tileset.WALL) {
                if (world[x - 1][y] == Tileset.FLOWER) {
                    win = true;
                    world[x][y] = Tileset.FLOOR;
                    world[x - 1][y] = Tileset.AVATAR;
                } else {
                    world[x][y] = Tileset.FLOOR;
                    world[x - 1][y] = Tileset.AVATAR;
                }
            }
        } else if (c == 'S' && y > 0) {
            if (world[x][y - 1] != Tileset.WALL) {
                if (world[x][y - 1] == Tileset.FLOWER) {
                    win = true;
                    world[x][y] = Tileset.FLOOR;
                    world[x][y - 1] = Tileset.AVATAR;
                } else {
                    world[x][y] = Tileset.FLOOR;
                    world[x][y - 1] = Tileset.AVATAR;
                }
            }
        } else if (c == 'D' && x < WIDTH - 1) {
            if (world[x + 1][y] != Tileset.WALL) {
                if (world[x + 1][y] == Tileset.FLOWER) {
                    win = true;
                    world[x][y] = Tileset.FLOOR;
                    world[x + 1][y] = Tileset.AVATAR;
                } else {
                    world[x][y] = Tileset.FLOOR;
                    world[x + 1][y] = Tileset.AVATAR;
                }
            }
        }
    }


}
