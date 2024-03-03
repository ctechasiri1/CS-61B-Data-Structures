package byow.Core;


import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


import java.util.ArrayList;
import java.util.HashSet;


public class Hallway {

    private static double distanceBetweenRooms(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private static void connectRooms(TETile[][] tiles, Room room1, Room room2) {
        //Get the center of each room
        int room1CenterX = room1.getCenterX();
        int room1CenterY = room1.getCenterY();
        int room2CenterX = room2.getCenterX();
        int room2CenterY = room2.getCenterY();

        //Connects room 1 and room 2 together
        int currentX = room1CenterX;
        int currentY = room1CenterY;

        while (currentX != room2CenterX) {
            tiles[currentX][currentY] = Tileset.FLOOR;
            int increment = Integer.compare(room2CenterX, currentX);
            currentX += increment;
        }
        while (currentY != room2CenterY) {
            tiles[currentX][currentY] = Tileset.FLOOR;
            int increment = Integer.compare(room2CenterY, currentY);
            currentY += increment;
        }
    }

    public static void findAndConnectShortestDistance(TETile[][] tiles, ArrayList<Room> rooms) {
        // Keep track of connected rooms
        HashSet<Room> connectedRooms = new HashSet<>();
        // Start with the first room
        Room startRoom = rooms.get(0);
        connectedRooms.add(startRoom);

        while (connectedRooms.size() < rooms.size()) {
            Room closestRoom1 = null;
            Room closestRoom2 = null;
            double shortestDistance = Double.MAX_VALUE;

            for (Room room1 : connectedRooms) {
                for (Room room2 : rooms) {
                    if (connectedRooms.contains(room2)) {
                        // Skip already connected room
                        continue;
                    }
                    double distance = distanceBetweenRooms(room1.getCenterX(), room1.getCenterY(),
                            room2.getCenterX(), room2.getCenterY());

                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        closestRoom1 = room1;
                        closestRoom2 = room2;
                    }
                }
            }

            if (closestRoom1 != null && closestRoom2 != null) {
                connectRooms(tiles, closestRoom1, closestRoom2);
                connectedRooms.add(closestRoom2);
            }
        }
    }
}

