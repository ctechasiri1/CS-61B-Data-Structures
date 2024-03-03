package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Movement {
    private int x;
    private int y;
    private TETile[][] tiles;

    public Movement(TETile[][] world) {
        tiles = world;
        x = worldGenerator.width / 2;
        y = worldGenerator.height / 2;
        tiles[x][y] = Tileset.AVATAR;
    }

    public void move(char input) {
        int newX = x;
        int newY = y;
        switch (input) {
            case 'w':
                newY += 1;
                break;
            case 'a':
                newX -= 1;
                break;
            case 's':
                newY -= 1;
                break;
            case 'd':
                newX += 1;
                break;
            default:
                break;
        }

        if (newX >= 0 && newX < worldGenerator.width && newY >= 0 && newY < worldGenerator.height
                && tiles[newX][newY] != Tileset.WALL) {
            tiles[x][y] = Tileset.FLOOR;
            x = newX;
            y = newY;
            tiles[x][y] = Tileset.AVATAR;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}


