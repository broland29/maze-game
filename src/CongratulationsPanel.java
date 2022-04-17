//  Class congratulating the player that he/she succeeded (hopefully without going mad)

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CongratulationsPanel extends JPanel implements ActionListener {

    JLabel bigText = new JLabel("<html>Congratulations! <br> &ensp;&ensp;&ensp;You made it!</html>");

    JButton submitHighScoreButton = new JButton("Submit high score");
    JButton backToWelcomeButton = new JButton("Back to start");

    CardFrame cardFrame;    //needed to access setCurrentLayout method of the frame

    public CongratulationsPanel(CardFrame cardFrame){
        this.cardFrame = cardFrame;

        this.setFocusable(true);
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(420,420));
        this.setBounds(0,0,420,420);
        this.setLayout(null);

        bigText.setBounds(50,0,350,300);
        bigText.setFont(new Font(Font.MONOSPACED,Font.BOLD,30));
        bigText.setForeground(Color.white);

        ImageIcon santa = new ImageIcon("pictures\\santa.png");
        bigText.setIcon(santa);
        bigText.setHorizontalTextPosition(JLabel.CENTER);
        bigText.setVerticalTextPosition(JLabel.BOTTOM);


        backToWelcomeButton.setBounds(50,310,140,20);
        backToWelcomeButton.setFocusable(false);
        backToWelcomeButton.addActionListener(this);

        submitHighScoreButton.setBounds(200,310,140,20);
        submitHighScoreButton.setFocusable(false);
        submitHighScoreButton.addActionListener(this);

        this.add(bigText);
        this.add(backToWelcomeButton);
        this.add(submitHighScoreButton);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == backToWelcomeButton){
            cardFrame.music.playMusicInLoop(Music.PATH_INTRO);
            cardFrame.setCurrentLayout("welcomePanel");
        }
        else if (e.getSource() == submitHighScoreButton){
            cardFrame.setCurrentLayout("insertHighScorePanel");
        }
    }
}

