//  Panel displaying previous high scores

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class HighScoresPanel extends JPanel implements ActionListener {

    JLabel bigText = new JLabel("High Scores:");
    JLabel highScores = new JLabel();

    //button to go to the previous page
    JButton prevArrow = new JButton("<-");

    //button to go back to start
    JButton backButton = new JButton("Back to start");

    //button to go to the next page
    JButton nextArrow = new JButton("->");

    //need an instance of DatabaseHandler, since we will use its select query
    DatabaseHandler databaseHandler = new DatabaseHandler();

    //initial offset is 0
    int databaseOffset = 0;

    //can set a maximum offset so player doesn't wander to infinity by repeatedly clicking nextArrow
    private static final int MAX_OFFSET = 5;

    CardFrame cardFrame;

    public HighScoresPanel(CardFrame cardFrame) throws SQLException {
        this.cardFrame = cardFrame;

        this.setFocusable(true);
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(420,420));
        this.setBounds(0,0,420,420);
        this.setLayout(null);

        bigText.setBounds(100,-50,350,200);
        bigText.setFont(new Font(Font.MONOSPACED,Font.BOLD,30));
        bigText.setForeground(Color.white);

        highScores.setBounds(20,100,360,200);
        highScores.setBackground(Color.white);
        highScores.setOpaque(true);
        highScores.setFont(new Font(Font.MONOSPACED,Font.BOLD,13));
        highScores.setText(databaseHandler.showTable(0));

        prevArrow.setBounds(30,310,50,20);
        prevArrow.setFocusable(false);
        prevArrow.addActionListener(this);

        backButton.setBounds(125,310,150,20);
        backButton.setFocusable(false);
        backButton.addActionListener(this);

        nextArrow.setBounds(320,310,50,20);
        nextArrow.setFocusable(false);
        nextArrow.addActionListener(this);

        this.add(bigText);
        this.add(highScores);
        this.add(prevArrow);
        this.add(backButton);
        this.add(nextArrow);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == prevArrow){
            if (databaseOffset != 0) databaseOffset--;  //go to previous page
            try {
                highScores.setText(databaseHandler.showTable(databaseOffset));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getSource() == nextArrow){
            if (databaseOffset != MAX_OFFSET) databaseOffset++; //go to next page
            try {
                highScores.setText(databaseHandler.showTable(databaseOffset));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getSource() == backButton){
            cardFrame.music.stopMusic();
            cardFrame.music.playMusicInLoop(Music.PATH_INTRO);
            cardFrame.setCurrentLayout("welcomePanel");
        }
    }
}