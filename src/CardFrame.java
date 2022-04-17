//  Class containing a JPanel with card layout
//  It unites all panels except the game panel (game panel has different dimension)

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;

public class CardFrame extends JFrame{
    //instantiate container panel
    JPanel panelContainer = new JPanel();

    //instantiate other panels
    WelcomePanel welcomePanel = new WelcomePanel(this);
    StoryPanel storyPanel = new StoryPanel(this);
    HighScoresPanel highScoresPanel = new HighScoresPanel(this);
    InsertHighScorePanel insertHighScorePanel = new InsertHighScorePanel(this);
    CongratulationsPanel congratulationsPanel = new CongratulationsPanel(this);
    CreditsPanel creditsPanel = new CreditsPanel(this);

    //instantiate the card layout
    CardLayout cl = new CardLayout();

    //to solve the problem of multiple musics playing
    Music music = new Music();

    CardFrame() throws SQLException{
        //set container's layout to card layout
        panelContainer.setLayout(cl);

        //start intro music
        music.playMusicInLoop(Music.PATH_INTRO);

        //add other panels to container panel, giving them an identifier string as well
        panelContainer.add(welcomePanel,"welcomePanel");
        panelContainer.add(storyPanel,"storyPanel");
        panelContainer.add(highScoresPanel,"highScoresPanel");
        panelContainer.add(insertHighScorePanel,"insertHighScorePanel");
        panelContainer.add(congratulationsPanel,"congratulationsPanel");
        panelContainer.add(creditsPanel,"creditsPanel");

        //the welcome panel will show on startup
        cl.show(panelContainer,"welcomePanel");

        //add container panel to frame
        this.add(panelContainer);

        //finish setting up the frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    //other constructor, invoked right after finishing game
    CardFrame(int time) throws SQLException{
        //set the time for possible insertion of the new score
        insertHighScorePanel.setTime(time);

        panelContainer.setLayout(cl);

        panelContainer.add(welcomePanel,"welcomePanel");
        panelContainer.add(storyPanel,"storyPanel");
        panelContainer.add(highScoresPanel,"highScoresPanel");
        panelContainer.add(insertHighScorePanel,"insertHighScorePanel");
        panelContainer.add(congratulationsPanel,"congratulationsPanel");
        panelContainer.add(creditsPanel,"creditsPanel");

        //open the congratulations panel
        cl.show(panelContainer,"congratulationsPanel");

        this.add(panelContainer);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    //set the current panel from the panel container
    public void setCurrentLayout(String stringIdentifier){
        cl.show(panelContainer,stringIdentifier);
    }

    //launch the game itself
    public void launchGame(){
        this.dispose();
        new GameFrame();
    }

}
