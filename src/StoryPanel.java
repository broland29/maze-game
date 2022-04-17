//  Panel showing a short story before starting the game

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StoryPanel extends JPanel implements ActionListener {

    JLabel bigText = new JLabel("<html>&ensp;&ensp;&ensp;&ensp;Santa is in <br> &ensp;a big big trouble! <br> He needs your help!</html>");

    JButton startButton = new JButton("I'm ready!");

    CardFrame cardFrame;

    Music music;

    public StoryPanel(CardFrame cardFrame){
        this.cardFrame = cardFrame;

        this.setFocusable(true);
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(420,420));
        this.setBounds(0,0,420,420);
        this.setLayout(null);

        bigText.setBounds(30,0,350,300);
        bigText.setFont(new Font(Font.MONOSPACED,Font.BOLD,30));
        bigText.setForeground(Color.white);

        bigText.setHorizontalTextPosition(JLabel.CENTER);
        bigText.setVerticalTextPosition(JLabel.BOTTOM);

        ImageIcon grinch = new ImageIcon("pictures\\grinch.png");
        bigText.setIcon(grinch);
        bigText.setHorizontalTextPosition(JLabel.CENTER);
        bigText.setVerticalTextPosition(JLabel.BOTTOM);

        startButton.setBounds(135,300,120,20);
        startButton.setFocusable(false);
        startButton.addActionListener(this);

        this.add(bigText);
        this.add(startButton);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        cardFrame.music.stopMusic();
        cardFrame.launchGame();
    }
}

