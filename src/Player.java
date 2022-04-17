//  Class of a player - rectangle moving in both directions based on input

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Rectangle{

    int xVelocity;
    int yVelocity;
    int speed = 4;  //found from testing various values

    Player(int x, int y, int width, int height){
        super(x,y,width,height);
    }

    //movement implemented by this action listener method
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP) {
            setYDirection(-speed);
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN) {
            setYDirection(speed);
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT) {
            setXDirection(-speed);
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
            setXDirection(speed);
        }
    }

    //important to stop when key is released
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP) {
            setYDirection(0);
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN) {
            setYDirection(0);
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT) {
            setXDirection(0);
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
            setXDirection(0);
        }
    }

    public void setYDirection(int yDirection) {
        yVelocity = yDirection;
    }

    public void setXDirection(int xDirection) {
        xVelocity = xDirection;
    }

    public void move() {
        y = y + yVelocity;
        x = x + xVelocity;
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(x, y, width, height);
    }
}