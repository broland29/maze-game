//  Class handling audio files

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Music {

    static final String PATH_ENEMY = "music\\enemy_hit.wav";
    static final String PATH_GOAL = "music\\goal_hit.wav";
    static final String PATH_WALL = "music\\wall_hit.wav";
    static final String PATH_WIN = "music\\win.wav";
    static final String PATH_BACKGROUND = "music\\background.wav";
    static final String PATH_INTRO = "music\\intro.wav";
    static final String PATH_STORY = "music\\story.wav";
    static final String PATH_HIGH_SCORES = "music\\high_scores.wav";

    Clip clip;

    void playMusic(String musicLocation){
        try{
            File musicPath = new File(musicLocation);

            if (!musicPath.exists()){
                System.out.println("Can't find file!");
            }
            else{
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    void playMusicInLoop(String musicLocation){
        try{
            File musicPath = new File(musicLocation);

            if (!musicPath.exists()){
                System.out.println("Can't find file!");
            }
            else{
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                //JOptionPane.showMessageDialog(null,"test");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    void stopMusic(){
        if (clip == null)
            System.out.println("Nothing to stop");
        else
            clip.stop();
    }
}