//  Panel showing the credits and maybe other stuff

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class CreditsPanel extends JPanel implements ActionListener{




    JLabel bigText = new JLabel("<html>&#8239;&ensp;&ensp;A project by <br>&#8239;&ensp;&ensp;&ensp;<U>@broland29</U><br> &#8239;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;2021</html>");

    JButton backToWelcomeButton = new JButton("Back to start");

    JButton visitMeButton = new JButton("Visit me");

    CardFrame cardFrame;

    public CreditsPanel(CardFrame cardFrame){

        this.cardFrame = cardFrame;

        this.setFocusable(true);
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(420,420));
        this.setBounds(0,0,420,420);
        this.setLayout(null);

        bigText.setBounds(50,0,350,300);
        bigText.setFont(new Font(Font.MONOSPACED,Font.BOLD,30));
        bigText.setForeground(Color.white);

        bigText.setHorizontalTextPosition(JLabel.CENTER);
        bigText.setVerticalTextPosition(JLabel.BOTTOM);

        visitMeButton.setBounds(135,260,120,20);
        visitMeButton.setFocusable(false);
        visitMeButton.addActionListener(this);

        backToWelcomeButton.setBounds(135,300,120,20);
        backToWelcomeButton.setFocusable(false);
        backToWelcomeButton.addActionListener(this);

        this.add(bigText);
        this.add(visitMeButton);
        this.add(backToWelcomeButton);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == backToWelcomeButton){
            cardFrame.setCurrentLayout("welcomePanel");
        }
        else if (e.getSource() == visitMeButton){

            URI uri = null;
            try {
                uri = new URI("https://github.com/broland29");
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
            open(uri);
        }
    }

    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            }
            catch (IOException e) {
                System.out.println("link error");
            }
        }
        else {
            System.out.println("link error");
        }
    }
}

