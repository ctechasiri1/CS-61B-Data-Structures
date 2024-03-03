package byow.Core;




import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;




import java.util.*;




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


        // Create a mapping of rooms to their distance from the start room
        HashMap<Room, Double> distanceMap = new HashMap<>();
        for (Room room : rooms) {
            if (room != startRoom) {
                distanceMap.put(room, Double.MAX_VALUE);
            }
        }


        // Create a mapping of rooms to their previous room in the shortest path
        HashMap<Room, Room> previousMap = new HashMap<>();


        // Initialize the distance of the start room to 0
        distanceMap.put(startRoom, 0.0);


        // Use a priority queue to store rooms based on their distance from the start room
        PriorityQueue<Room> pq = new PriorityQueue<>(Comparator.comparingDouble(distanceMap::get));
        pq.add(startRoom);


        while (!pq.isEmpty()) {
            Room currentRoom = pq.poll();
            connectedRooms.add(currentRoom);


            // If all rooms are connected, exit early
            if (connectedRooms.size() == rooms.size()) {
                break;
            }


            for (Room room : rooms) {
                if (!connectedRooms.contains(room)) {
                    double distance = distanceBetweenRooms(currentRoom.getCenterX(),
                            currentRoom.getCenterY(), room.getCenterX(), room.getCenterY());
                    double totalDistance = distanceMap.get(currentRoom) + distance;
                    if (totalDistance < distanceMap.get(room)) {
                        distanceMap.put(room, totalDistance);
                        previousMap.put(room, currentRoom);
                        pq.add(room);
                    }
                }
            }
        }


        // Connect the rooms based on the shortest path
        for (Room room : rooms) {
            if (room != startRoom) {
                Room previousRoom = previousMap.get(room);
                connectRooms(tiles, previousRoom, room);
            }
        }
    }
}





