//  The first panel the player sees - also serves as home panel

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class WelcomePanel extends JPanel implements ActionListener {

    JLabel bigText = new JLabel("<html>&ensp;&ensp;Maze Game : <br> Save Christmas</html>");

    JButton startButton = new JButton("Start");
    JButton highScoresButton = new JButton("High scores");
    JButton creditsButton = new JButton("Credits");
    JButton exitButton = new JButton("Exit");

    CardFrame cardFrame;

    public WelcomePanel(CardFrame cardFrame){
        this.cardFrame = cardFrame;

        this.setFocusable(true);
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(420,420));
        this.setBounds(0,0,420,420);
        this.setLayout(null);

        bigText.setBounds(80,0,350,200);
        bigText.setFont(new Font(Font.MONOSPACED,Font.BOLD,30));
        bigText.setForeground(Color.white);

        startButton.setBounds(135,180,120,20);
        startButton.setFocusable(false);
        startButton.addActionListener(this);

        highScoresButton.setBounds(135,220,120,20);
        highScoresButton.setFocusable(false);
        highScoresButton.addActionListener(this);

        creditsButton.setBounds(135,260,120,20);
        creditsButton.setFocusable(false);
        creditsButton.addActionListener(this);

        exitButton.setBounds(135,300,120,20);
        exitButton.setFocusable(false);
        exitButton.addActionListener(this);

        this.add(bigText);
        this.add(startButton);
        this.add(highScoresButton);
        this.add(creditsButton);
        this.add(exitButton);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == creditsButton){
            cardFrame.setCurrentLayout("creditsPanel");
        }
        if (e.getSource() == exitButton){
            cardFrame.dispatchEvent(new WindowEvent(cardFrame,WindowEvent.WINDOW_CLOSING));
        }
        if (e.getSource() == highScoresButton){
            cardFrame.music.stopMusic();
            cardFrame.music.playMusicInLoop(Music.PATH_HIGH_SCORES);
            cardFrame.setCurrentLayout("highScoresPanel");
        }
        if (e.getSource() == startButton){
            cardFrame.music.stopMusic();
            cardFrame.music.playMusicInLoop(Music.PATH_STORY);
            cardFrame.setCurrentLayout("storyPanel");
        }
    }
}

