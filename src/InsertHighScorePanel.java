//  Panel where player writes his/her username and submits the score

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class InsertHighScorePanel extends JPanel implements ActionListener {

    private int time = 9999;    //will be overwritten from value from game panel

    JLabel bigText = new JLabel("<html>What's your name,<br> &emsp;&ensp; Champion?</html>");
    JButton submitButton = new JButton("Submit");
    JButton backButton = new JButton("Back");
    JTextField nameField = new JTextField();
    JLabel nameFieldLabel = new JLabel(" Username:");
    JLabel messageLabel = new JLabel(); //to write message if username is inappropriate

    DatabaseHandler databaseHandler = new DatabaseHandler();

    CardFrame cardFrame;

    public InsertHighScorePanel(CardFrame cardFrame){
        this.cardFrame = cardFrame;

        this.setFocusable(true);
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(420,420));
        this.setBounds(0,0,420,420);
        this.setLayout(null);

        nameFieldLabel.setBounds(50,200,95,25);
        messageLabel.setBounds(125,300,200,35);

        nameFieldLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,15));
        nameFieldLabel.setBackground(Color.white);
        nameFieldLabel.setOpaque(true);

        messageLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,15));

        nameField.setBounds(150,200,200,25);

        submitButton.setBounds(75,250,100,25);
        submitButton.addActionListener(this);
        submitButton.setFont(new Font(Font.MONOSPACED,Font.BOLD,15));
        submitButton.setFocusable(false); //no little box around

        backButton.setBounds(225,250,100,25);
        backButton.addActionListener(this);
        backButton.setFont(new Font(Font.MONOSPACED,Font.BOLD,15));
        backButton.setFocusable(false);

        bigText.setBounds(50,0,350,200);
        bigText.setFont(new Font(Font.MONOSPACED,Font.BOLD,30));

        this.add(nameFieldLabel);
        this.add(messageLabel);
        this.add(nameField);
        this.add(submitButton);
        this.add(backButton);
        this.add(bigText);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == submitButton){

            String name = nameField.getText();

            if (name.length() < 3){
                messageLabel.setText("Name too short!");
            }
            else if (name.matches("[0-9A-z]+")){
                messageLabel.setText("Valid name!");

                try{
                    databaseHandler.insertRecord(name,time);
                    cardFrame.music.playMusicInLoop(Music.PATH_HIGH_SCORES);
                    cardFrame.setCurrentLayout("highScoresPanel");
                }
                catch (SQLException ex){
                    ex.printStackTrace();
                }
            }
            else{
                messageLabel.setText("<html>Use only characters <br> and digits!</html>");
            }
        }
        else if (e.getSource() == backButton){
            cardFrame.setCurrentLayout("welcomePanel");
        }
    }

    public void setTime(int time) {
        this.time = time;
    }
}
