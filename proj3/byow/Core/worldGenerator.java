package byow.Core;


import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import org.junit.jupiter.api.MethodOrderer;


import javax.management.relation.RelationNotification;
import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class worldGenerator extends Hallway {
    private static int width = 60;
    private static int height = 30;
    private static long seed;
    private static final int minRoomNum = 15;
    private static final int maxRoomNum = 10;
    private static final int minRoomWidth = 3;
    private static final int maxRoomWidth = 10;
    private static final int minRoomHeight = 3;
    private static final int maxRoomHeight = 10;
    private static Random rand;

    public static void buildWorld(TETile[][] tiles, Long currSeed) {
        seed = currSeed;
        rand = new Random(seed);

        ArrayList<Room> rooms = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = Tileset.NOTHING;
            }
        }

        //Generate random rooms
        int numRooms = rand.nextInt(maxRoomNum) + minRoomNum;
        for (int i = 0; i < numRooms; i++) {
            //the plus part is the min number of rooms which is 2 and it selects a random number from 0-10 to add
            int roomWidth = rand.nextInt(maxRoomWidth) + minRoomWidth;
            int roomHeight = rand.nextInt(maxRoomHeight) + minRoomHeight;
            int roomX = rand.nextInt(width - roomWidth);
            int roomY = rand.nextInt(height - roomHeight);
            //Create and store the room object into the list
            Room room = new Room(roomX, roomY, roomHeight, roomWidth);
            if (!checkOccupied(tiles, room)) {
                makeSquareFloor(tiles, room);
                rooms.add(room);
            }

        }
        findAndConnectShortestDistance(tiles, rooms);
        generateWalls(tiles);
    }

    private static void makeSquareFloor(TETile[][] tiles, Room room) {
        for (int i = room.getX(); i < room.getX() + room.getWidth(); i++) {
            for (int j = room.getY(); j < room.getY() + room.getHeight(); j++) {
                if (i >= 0 && i < worldGenerator.width && j >= 0 && j < worldGenerator.height) {
                    //This fixes the problem with the rooms fusing together by checking if a floor is already taking up
                    // space
                    tiles[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    public static boolean checkOccupied(TETile[][] tiles, Room room1) {
        Room currRoom = room1;
        for (int x = currRoom.getX(); x < currRoom.getX() + currRoom.getWidth(); x++) {
            for (int y = currRoom.getY(); y < currRoom.getY() + currRoom.getHeight(); y++) {
                if (x > 0 && x < worldGenerator.width && y > 0 && y < worldGenerator.height - 1) {
                    if (tiles[x][y] == Tileset.FLOOR || tiles[x + 1][y] == Tileset.FLOOR || tiles[x - 1][y] == Tileset.FLOOR
                            || tiles[x][y + 1] == Tileset.FLOOR || tiles[x][y - 1] == Tileset.FLOOR) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void generateWalls(TETile[][] tiles) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tiles[x][y] == Tileset.FLOOR) {
                    if (x > 0 && x < worldGenerator.width - 1 && y > 0 && y < worldGenerator.height - 1) {
                        if (tiles[x - 1][y] == Tileset.NOTHING) {
                            tiles[x - 1][y] = Tileset.WALL;
                        }
                        if (tiles[x + 1][y] == Tileset.NOTHING) {
                            tiles[x + 1][y] = Tileset.WALL;
                        }
                        if (tiles[x][y - 1] == Tileset.NOTHING) {
                            tiles[x][y - 1] = Tileset.WALL;
                        }
                        if (tiles[x][y + 1] == Tileset.NOTHING) {
                            tiles[x][y + 1] = Tileset.WALL;
                        }
                    }
                    if ((tiles[x][y] == tiles[width-1][y] || tiles[x][y] == tiles[0][y]) && tiles[x][y] == Tileset.FLOOR){
                        tiles[x][y] = Tileset.WALL;
                    }
                    if ((tiles[x][y] == tiles[x][height-1] || tiles[x][y] == tiles[x][0]) && tiles[x][y] == Tileset.FLOOR){
                        tiles[x][y] = Tileset.WALL;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        TETile[][] tiles = new TETile[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = Tileset.NOTHING;
            }
        }
        long seed = 1824155662;
        buildWorld(tiles, seed);
        ter.renderFrame(tiles);
    }
}

