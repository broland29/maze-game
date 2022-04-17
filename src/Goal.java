//Class of goal - the green present santa goes for

import java.awt.*;

public class Goal extends Rectangle{

    Goal(int x, int y, int width, int height){
        super(x,y,width,height);
    }

    public void draw(Graphics g) {

        g.setColor(Color.green);
        g.fillRect(x, y, width, height);
    }
}