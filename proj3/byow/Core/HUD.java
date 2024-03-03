package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.awt.*;

public class HUD {
    TERenderer ter;
    public static String currDesc = "";
    public static int x;
    public static int y;
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HUD(TERenderer terrender, int width, int height) {
        ter = terrender;
        x = width;
        y = height;
        Font font = new Font("Arial", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(1, worldGenerator.height - 1, "Player 1");
        StdDraw.show();
    }

    public void updateHud(TETile[][] game, String desc, boolean win) {
        if (win != true) {
            Font font = new Font("Arial", Font.BOLD, 20);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);
            StdDraw.setFont(font);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.textLeft(1, worldGenerator.height - 1, "Player 1" + ":" + desc);
            StdDraw.textRight(worldGenerator.width - 2, worldGenerator.height - 1, formattedDateTime);
            StdDraw.show();
            currDesc = desc;
            ter.renderFrame(game);
        } else {
            Font font = new Font("Arial", Font.BOLD, 20);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);
            StdDraw.setFont(font);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.textLeft(1, worldGenerator.height - 1, "Player 1" + ":" + desc);
            StdDraw.textRight(worldGenerator.width - 2, worldGenerator.height - 1, formattedDateTime);
            StdDraw.text(x / 2, worldGenerator.height - 1, "YOU WIN!");
            StdDraw.show();
            currDesc = desc;
            ter.renderFrame(game);
        }
    }

}

