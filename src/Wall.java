//  Class of a wall

import java.awt.*;

public class Wall extends Rectangle {

    Wall(int x, int y, int wallWidth, int wallHeight){
        super(x,y,wallWidth,wallHeight);
    }

    public void draw(Graphics g, boolean isStatic) {
        if (isStatic)
            g.setColor(Color.white);    //white for static walls
        else
            g.setColor(Color.pink);     //pink for flickering walls
        g.fillRect(x, y, width, height);
    }
}
