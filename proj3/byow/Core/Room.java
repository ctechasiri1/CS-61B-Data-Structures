package byow.Core;


public class Room {
    private int x;
    private int y;
    private int heightRoom;
    private int widthRoom;


    public Room(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.widthRoom = width;
        this.heightRoom = height;
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public int getHeight() {
        return heightRoom;
    }


    public int getWidth() {
        return widthRoom;
    }


    public int getCenterX() {
        return x + widthRoom / 2;
    }


    public int getCenterY() {
        return y + heightRoom / 2;
    }


}





