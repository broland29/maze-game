//  Class of one enemy - it moves, and it is bad!

import java.awt.*;

public class Enemy extends Rectangle{

    private int xVelocity;
    private int yVelocity;

    static final int SPEED = 5;  //found from testing various values

    Enemy(int x, int y, int width, int height, Directions initialDirection) {
        //constructor of Rectangle class
        super(x,y,width,height);

        //analyze given initial direction - enemies move just on one axis
        switch (initialDirection){
            case UP:
                xVelocity = 0;
                yVelocity = -SPEED;
                break;
            case RIGHT:
                xVelocity = SPEED;
                yVelocity = 0;
                break;
            case DOWN:
                xVelocity = 0;
                yVelocity = SPEED;
                break;
            case LEFT:
                xVelocity = -SPEED;
                yVelocity = 0;
                break;
        }
    }

    //this is how enemies move
    public void move() {
        y = y + yVelocity;
        x = x + xVelocity;
    }

    public void draw(Graphics g, int level) {
        if (level == 10)
            g.setColor(Color.black);    //in the last level we have invisible enemies
        else
            g.setColor(Color.cyan);     //otherwise, they are cyan

        g.fillRect(x, y, width, height);
    }

    public enum Directions {UP,RIGHT,DOWN,LEFT}

    public void setXVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setYVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }

    public int getXVelocity() {
        return xVelocity;
    }

    public int getYVelocity() {
        return yVelocity;
    }
}