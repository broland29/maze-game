//  Frame for the game

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;

public class GameFrame extends JFrame {

    GamePanel panel;

    GameFrame(){
        panel = new GamePanel(this);

        this.add(panel);
        this.setTitle("Maze Game");
        this.setResizable(false);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    //used when we finished
    void launchCardFrame(int time){
        this.dispose();
        try {
            new CardFrame(time);    //call the constructor with time parameter
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}