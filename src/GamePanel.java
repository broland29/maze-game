//  Actual implementation of the game

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{

    //dimensions of panel
    static final int GAME_WIDTH = 1250; //should be multiple of UNIT_SIZE
    static final int GAME_HEIGHT = 650; //should be multiple of UNIT_SIZE
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);

    //dimensions referring to one unit (a square of the panel)
    static final int UNIT_SIZE = 50;
    static final int UNITS_HORIZONTALLY = GAME_WIDTH / UNIT_SIZE;
    static final int UNITS_VERTICALLY = GAME_HEIGHT / UNIT_SIZE;

    //dimensions referring to player
    static final int PLAYER_DIAMETER = UNIT_SIZE / 2;
    static final int PLAYER_OFFSET = (UNIT_SIZE - PLAYER_DIAMETER) / 2;

    //dimensions referring to goal
    static final int GOAL_DIAMETER = PLAYER_DIAMETER;
    static final int GOAL_OFFSET = (UNIT_SIZE - GOAL_DIAMETER) / 2;

    //dimensions referring to enemy
    static final int ENEMY_DIAMETER = PLAYER_DIAMETER;
    static final int ENEMY_OFFSET = PLAYER_OFFSET;

    //game thread, to run game loop
    Thread gameThread;

    //stuff for paint function
    Image image;
    Graphics graphics;

    //instance of player
    Player player;

    //arrays containing walls
    Wall[] staticWalls;
    Wall[] flickeringWalls;

    //instance of goal
    Goal goal;

    //counter thread, to count time from start
    MyCounter counterThread;

    //array of enemies
    Enemy[] enemies;

    //two music instances
    Music backgroundMusic = new Music();
    Music otherMusic = new Music();//sfx

    //necessary to pass score
    GameFrame gameFrame;

    //wall counters
    private int currentStaticWallNumber = 0;
    private int currentFlickeringWallNumber = 0;

    //current level, at start 1
    private int level = 1;

    //did player finish the game?
    private boolean finished = false;

    //enemy count
    private int currentEnemyNumber = 0;


    //constructor
    GamePanel(GameFrame gameFrame){
        this.gameFrame = gameFrame;

        //choose starting level
        chooseLevel(level);

        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        //start game loop
        gameThread = new Thread(this);
        gameThread.start();

        //start counting
        counterThread = new MyCounter();
        counterThread.start();

        //start background music
        backgroundMusic.playMusicInLoop(Music.PATH_BACKGROUND);
    }

    //function to choose level
    public void chooseLevel(int level){
        switch (level){
            case 1:
                newPlayer();
                newWalls1();
                newGoal();
                break;
            case 2:
                newPlayer();
                newWalls2();
                newGoal();
                break;
            case 3:
                newPlayer();
                newWalls3();
                newGoal();
                break;
            case 4:
                newPlayer();
                newWalls4();
                newGoal();
                break;
            case 5:
                newPlayer();
                newWalls5();
                newGoal();
                break;
            case 6:
                newPlayer();
                newWalls6();
                newGoal();
                break;
            case 7:
                newPlayer();
                newWalls2();
                newEnemies7();
                newGoal();
                break;
            case 8:
                newPlayer();
                newWalls8();
                newEnemies8();
                newGoal();
                break;
            case 9:
                newPlayer();
                newWalls9();
                newEnemies9();
                newGoal();
                break;
            case 10:
                newPlayer();
                newWalls2();
                newEnemies10();
                newGoal();
                break;
        }
    }

    //make a new player - always on same spot
    public void newPlayer() {
        player = new Player(UNIT_SIZE + PLAYER_OFFSET,(UNITS_VERTICALLY / 2)*UNIT_SIZE + PLAYER_OFFSET,PLAYER_DIAMETER,PLAYER_DIAMETER);
    }

    //make a new goal - always on same spot
    public void newGoal(){
        goal = new Goal(((UNITS_HORIZONTALLY-1) * UNIT_SIZE) + GOAL_OFFSET,(UNITS_VERTICALLY / 2)*UNIT_SIZE + GOAL_OFFSET,GOAL_DIAMETER,GOAL_DIAMETER);
    }

    //paint method
    public void paint(Graphics g) {
        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
    }

    //draw method
    public void draw(Graphics g) {

//        //draw grid - mainly used while making levels
//        g.setColor(Color.GRAY);
//        for(int i=0;i<UNITS_VERTICALLY;i++) {
//            g.drawLine(0, i*UNIT_SIZE, GAME_WIDTH, i*UNIT_SIZE);  //horizontal grid lines
//        }
//        for (int i=0; i<UNITS_HORIZONTALLY; i++){
//            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, GAME_HEIGHT); //vertical grid lines
//        }

        //draw player
        player.draw(g);

        //draw static walls
        for (int i=0; i<currentStaticWallNumber; i++)
            staticWalls[i].draw(g,true);

        //draw goal
        goal.draw(g);

        //draw counter
        counterThread.draw(g);

        //draw flickering walls - they appear each even second
        for (int i = 0; i< currentFlickeringWallNumber; i++)
            if (counterThread.getSeconds() % 2 == 0)
                flickeringWalls[i].draw(g,false);

        //draw enemies
        for (int i=0; i<currentEnemyNumber; i++)
            enemies[i].draw(g,level);

        Toolkit.getDefaultToolkit().sync(); //helps with the animation
    }

    //method calling each individual move method
    public void move() {
        //move player
        player.move();

        //move enemies
        for (int i=0; i<currentEnemyNumber; i++)
            enemies[i].move();
    }

    //method checking any kind of collision
    public void checkCollision() {

        //stop player at window edges
        if(player.y<=0)
            player.y=0;
        if(player.y >= (GAME_HEIGHT-PLAYER_DIAMETER))
            player.y = GAME_HEIGHT-PLAYER_DIAMETER;
        if(player.x<=0)
            player.x=0;
        if(player.x >= (GAME_WIDTH-PLAYER_DIAMETER))
            player.x = GAME_WIDTH-PLAYER_DIAMETER;

        //collision with static walls
        for (int i=0; i<currentStaticWallNumber; i++) {
            if (player.intersects(staticWalls[i]) && !finished) {
                chooseLevel(level);                     //restart level
                otherMusic.playMusic(Music.PATH_WALL);  //hit wall sound effect
            }
        }

        //collision with goal
        if (player.intersects(goal)){
            if (level < 10){
                chooseLevel(++level);                   //advance to next level
                otherMusic.playMusic(Music.PATH_GOAL);  //hit goal sound effect
            }
            else{
                if (!finished){//first collision
                    backgroundMusic.stopMusic();
                    otherMusic.playMusic(Music.PATH_WIN);
                    gameFrame.launchCardFrame(counterThread.getSeconds());  //send the score
                }
                finished = true;    ///so that we execute instructions above only once, even if player stands on goal
            }
        }

        //collision with flickering walls - only count as collision when odd second (when they are visible)
        for (int i = 0; i< currentFlickeringWallNumber; i++)
            if (counterThread.getSeconds() % 2 == 0)
                if (player.intersects(flickeringWalls[i])){
                    chooseLevel(level);                     //restart level
                    otherMusic.playMusic(Music.PATH_WALL);  //hit wall sound effect
                }

        //collision with enemies
        for (int i=0; i<currentEnemyNumber; i++) {
            if (player.intersects(enemies[i]) && !finished) {   //kept hearing sounds after finishing - added !finished to condition
                chooseLevel(level);                         //restart level
                otherMusic.playMusic(Music.PATH_ENEMY);     //hit enemy sound effect
            }
        }

        //enemies bounce off edges, walls and other enemies - no case for flickering walls in my levels
        boolean bounce; //do they bounce at the moment?
        for (int i=0; i<currentEnemyNumber; i++){

            bounce = false;     //suppose not

            //check for static walls
            for (int j=0; j<currentStaticWallNumber; j++)
                if (enemies[i].intersects(staticWalls[j]))
                    bounce = true;

            //check for borders
            if (enemies[i].x < 0 || enemies[i].y < 0 || (enemies[i].x + ENEMY_DIAMETER) > GAME_WIDTH || (enemies[i].y + ENEMY_DIAMETER) > GAME_HEIGHT)
                bounce = true;

            //check for other enemies
            for (int j=0; j<currentEnemyNumber; j++)
                if (i != j && enemies[i].intersects(enemies[j]))
                    bounce = true;

            //if they need to bounce, just invert their velocity - one velocity is always 0
            if (bounce){
                enemies[i].setXVelocity(-enemies[i].getXVelocity());
                enemies[i].setYVelocity(-enemies[i].getYVelocity());
            }
        }
    }

    //the game loop
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks =60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while(true) {
            if (finished) counterThread.stop(); //stop counter
            long now = System.nanoTime();
            delta += (now -lastTime)/ns;
            lastTime = now;
            if(delta >=1) {
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    //only player is controlled
    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }
    }

    //counter class, to implement counter thread, to count seconds
    public class MyCounter extends Thread{

        private int seconds = 0;

        @Override
        public void run(){
            while (gameThread.isAlive()) {
                try {
                    this.sleep(1000);   //sleep for one second
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
                seconds++;  //increment seconds
            }
        }

        //draw the seconds
        public void draw(Graphics g){
            g.setColor(Color.BLACK);
            g.setFont(new Font(Font.MONOSPACED,Font.BOLD,40));

            FontMetrics metrics = getFontMetrics(g.getFont());

            //                   minutes msb            minutes lsb             seconds msb       seconds lsb
            String timeFormat =  (seconds/60)/10 + "" + (seconds/60)%10 + ":" + (seconds%60)/10 + (seconds%60)%10;

            g.drawString(timeFormat,(GAME_WIDTH - metrics.stringWidth(timeFormat))/2,g.getFont().getSize());
        }

        //getter for the seconds, used when player finished
        public int getSeconds(){
            return this.seconds;
        }
    }

    //  In the next part method newWallsX implements the walls for level X
    //  In each method, the first static wall is the one covering the clock

    public void newWalls1(){
        currentStaticWallNumber = 1;
        currentFlickeringWallNumber = 0;

        staticWalls = new Wall[currentStaticWallNumber];

        staticWalls[0] = new Wall((UNITS_HORIZONTALLY-2)/2*UNIT_SIZE,0,UNIT_SIZE*3,UNIT_SIZE);
    }

    public void newWalls2(){
        currentStaticWallNumber = 3;
        currentFlickeringWallNumber = 0;

        staticWalls = new Wall[3];

        staticWalls[0] = new Wall((UNITS_HORIZONTALLY-2)/2*UNIT_SIZE,0,UNIT_SIZE*3,UNIT_SIZE);
        staticWalls[1] = new Wall(UNITS_HORIZONTALLY/2*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE,(UNITS_VERTICALLY/2-1)*UNIT_SIZE);
        staticWalls[2] = new Wall(UNITS_HORIZONTALLY/2*UNIT_SIZE,(UNITS_VERTICALLY/2+1)*UNIT_SIZE,UNIT_SIZE,(UNITS_VERTICALLY/2)*UNIT_SIZE);
    }

    public void newWalls3(){
        currentStaticWallNumber = 7;
        currentFlickeringWallNumber = 0;

        staticWalls = new Wall[currentStaticWallNumber];

        //walls from newWalls2
        staticWalls[0] = new Wall((UNITS_HORIZONTALLY-2)/2*UNIT_SIZE,0,UNIT_SIZE*3,UNIT_SIZE);
        staticWalls[1] = new Wall(UNITS_HORIZONTALLY/2*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE,(UNITS_VERTICALLY/2-1)*UNIT_SIZE);
        staticWalls[2] = new Wall(UNITS_HORIZONTALLY/2*UNIT_SIZE,(UNITS_VERTICALLY/2+1)*UNIT_SIZE,UNIT_SIZE,(UNITS_VERTICALLY/2)*UNIT_SIZE);

        //newer walls
        staticWalls[3] = new Wall((UNITS_HORIZONTALLY/2-3)*UNIT_SIZE,(UNITS_VERTICALLY/2-1)*UNIT_SIZE,UNIT_SIZE*7,UNIT_SIZE);
        staticWalls[4] = new Wall((UNITS_HORIZONTALLY/2-3)*UNIT_SIZE,(UNITS_VERTICALLY/2+1)*UNIT_SIZE,UNIT_SIZE*7,UNIT_SIZE);
        staticWalls[5] = new Wall((UNITS_HORIZONTALLY/2-5)*UNIT_SIZE,(UNITS_VERTICALLY/2-1)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE*3);
        staticWalls[6] = new Wall((UNITS_HORIZONTALLY/2+5)*UNIT_SIZE,(UNITS_VERTICALLY/2-1)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE*3);
    }

    public void newWalls4(){
        currentStaticWallNumber = 15;
        currentFlickeringWallNumber = 0;

        staticWalls = new Wall[currentStaticWallNumber];

        //walls from newWalls3
        staticWalls[0] = new Wall((UNITS_HORIZONTALLY-2)/2*UNIT_SIZE,0,UNIT_SIZE*3,UNIT_SIZE);
        staticWalls[1] = new Wall(UNITS_HORIZONTALLY/2*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE,(UNITS_VERTICALLY/2-1)*UNIT_SIZE);
        staticWalls[2] = new Wall(UNITS_HORIZONTALLY/2*UNIT_SIZE,(UNITS_VERTICALLY/2+1)*UNIT_SIZE,UNIT_SIZE,(UNITS_VERTICALLY/2)*UNIT_SIZE);
        staticWalls[3] = new Wall((UNITS_HORIZONTALLY/2-3)*UNIT_SIZE,(UNITS_VERTICALLY/2-1)*UNIT_SIZE,UNIT_SIZE*8,UNIT_SIZE); //width increased
        staticWalls[4] = new Wall((UNITS_HORIZONTALLY/2-4)*UNIT_SIZE,(UNITS_VERTICALLY/2+1)*UNIT_SIZE,UNIT_SIZE*8,UNIT_SIZE); //width increased and x changed
        staticWalls[5] = new Wall((UNITS_HORIZONTALLY/2-5)*UNIT_SIZE,(UNITS_VERTICALLY/2-1)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE*3);
        staticWalls[6] = new Wall((UNITS_HORIZONTALLY/2+5)*UNIT_SIZE,(UNITS_VERTICALLY/2-1)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE*3);

        //newer walls
        //left part
        staticWalls[7] = new Wall((UNITS_HORIZONTALLY/2-3)*UNIT_SIZE,(UNITS_VERTICALLY/2-2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
        staticWalls[8] = new Wall((UNITS_HORIZONTALLY/2-7)*UNIT_SIZE,(UNITS_VERTICALLY/2-3)*UNIT_SIZE,UNIT_SIZE*5,UNIT_SIZE);
        staticWalls[9] = new Wall((UNITS_HORIZONTALLY/2-7)*UNIT_SIZE,(UNITS_VERTICALLY/2-2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE*6);
        staticWalls[10] = new Wall((UNITS_HORIZONTALLY/2-6)*UNIT_SIZE,(UNITS_VERTICALLY/2+3)*UNIT_SIZE,UNIT_SIZE*5,UNIT_SIZE);
        //right part
        staticWalls[11] = new Wall((UNITS_HORIZONTALLY/2+3)*UNIT_SIZE,(UNITS_VERTICALLY/2+2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
        staticWalls[12] = new Wall((UNITS_HORIZONTALLY/2+3)*UNIT_SIZE,(UNITS_VERTICALLY/2+3)*UNIT_SIZE,UNIT_SIZE*5,UNIT_SIZE);
        staticWalls[13] = new Wall((UNITS_HORIZONTALLY/2+7)*UNIT_SIZE,(UNITS_VERTICALLY/2-3)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE*6);
        staticWalls[14] = new Wall((UNITS_HORIZONTALLY/2+2)*UNIT_SIZE,(UNITS_VERTICALLY/2-3)*UNIT_SIZE,UNIT_SIZE*5,UNIT_SIZE);
    }

    public void newWalls5(){
        newWalls3();    //wall setup of level 3

        currentFlickeringWallNumber = 3;    //overwrite the number of flickering walls

        flickeringWalls = new Wall[currentFlickeringWallNumber];

        flickeringWalls[0] = new Wall((UNITS_HORIZONTALLY/2-3)*UNIT_SIZE,(UNITS_VERTICALLY/2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
        flickeringWalls[1] = new Wall((UNITS_HORIZONTALLY/2)*UNIT_SIZE,(UNITS_VERTICALLY/2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
        flickeringWalls[2] = new Wall((UNITS_HORIZONTALLY/2+3)*UNIT_SIZE,(UNITS_VERTICALLY/2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
    }

    public void newWalls6(){
        newWalls4();    //wall setup of level 4

        currentFlickeringWallNumber = 9;    //overwrite the number of flickering walls

        flickeringWalls = new Wall[9];

        //flickering walls of level 5
        flickeringWalls[0] = new Wall((UNITS_HORIZONTALLY/2-3)*UNIT_SIZE,(UNITS_VERTICALLY/2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
        flickeringWalls[1] = new Wall((UNITS_HORIZONTALLY/2)*UNIT_SIZE,(UNITS_VERTICALLY/2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
        flickeringWalls[2] = new Wall((UNITS_HORIZONTALLY/2+3)*UNIT_SIZE,(UNITS_VERTICALLY/2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);

        //new flickering walls
        flickeringWalls[3] = new Wall((UNITS_HORIZONTALLY/2-5)*UNIT_SIZE,(UNITS_VERTICALLY/2-2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
        flickeringWalls[4] = new Wall((UNITS_HORIZONTALLY/2-5)*UNIT_SIZE,(UNITS_VERTICALLY/2+2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
        flickeringWalls[5] = new Wall((UNITS_HORIZONTALLY/2-2)*UNIT_SIZE,(UNITS_VERTICALLY/2+2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
        flickeringWalls[6] = new Wall((UNITS_HORIZONTALLY/2+5)*UNIT_SIZE,(UNITS_VERTICALLY/2+2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
        flickeringWalls[7] = new Wall((UNITS_HORIZONTALLY/2+5)*UNIT_SIZE,(UNITS_VERTICALLY/2-2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
        flickeringWalls[8] = new Wall((UNITS_HORIZONTALLY/2+2)*UNIT_SIZE,(UNITS_VERTICALLY/2-2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
    }

    public void newEnemies7(){
        currentEnemyNumber = UNITS_HORIZONTALLY-3;  //2 empty space on left, one on right

        enemies = new Enemy[currentEnemyNumber];

        int i;
        //enemies going right
        for (i=0; i<currentEnemyNumber/2; i++)
            enemies[i] = new Enemy((2+i)*UNIT_SIZE+ENEMY_OFFSET,(UNITS_VERTICALLY/2)*UNIT_SIZE+ENEMY_OFFSET,ENEMY_DIAMETER,ENEMY_DIAMETER, Enemy.Directions.RIGHT);
        //enemies going left
        for (int j=0; i<currentEnemyNumber; i++,j++)
            enemies[i] = new Enemy((UNITS_HORIZONTALLY-2-j)*UNIT_SIZE+ENEMY_OFFSET,(UNITS_VERTICALLY/2)*UNIT_SIZE+ENEMY_OFFSET,ENEMY_DIAMETER,ENEMY_DIAMETER, Enemy.Directions.LEFT);
    }

    public void newWalls8(){
        currentStaticWallNumber = 5;
        currentFlickeringWallNumber = 0;

        staticWalls = new Wall[currentStaticWallNumber];

        staticWalls[0] = new Wall((UNITS_HORIZONTALLY-2)/2*UNIT_SIZE,0,UNIT_SIZE*3,UNIT_SIZE);
        staticWalls[1] = new Wall(UNITS_HORIZONTALLY/2*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE,(UNITS_VERTICALLY/2-2)*UNIT_SIZE);
        staticWalls[2] = new Wall(UNITS_HORIZONTALLY/2*UNIT_SIZE,(UNITS_VERTICALLY/2+2)*UNIT_SIZE,UNIT_SIZE,(UNITS_VERTICALLY/2)*UNIT_SIZE);
        staticWalls[3] = new Wall((UNITS_HORIZONTALLY/2-4)*UNIT_SIZE,(UNITS_VERTICALLY/2-3)*UNIT_SIZE,UNIT_SIZE*9,UNIT_SIZE);
        staticWalls[4] = new Wall((UNITS_HORIZONTALLY/2-4)*UNIT_SIZE,(UNITS_VERTICALLY/2+3)*UNIT_SIZE,UNIT_SIZE*9,UNIT_SIZE);
    }

    public void newEnemies8(){
        currentEnemyNumber = 8;

        enemies = new Enemy[currentEnemyNumber];

        //enemies on left half
        enemies[0] = new Enemy((UNITS_HORIZONTALLY/2-4)*UNIT_SIZE+ENEMY_OFFSET,(UNITS_VERTICALLY/2+2)*UNIT_SIZE+ENEMY_OFFSET,ENEMY_DIAMETER,ENEMY_DIAMETER, Enemy.Directions.UP);
        enemies[1] = new Enemy((UNITS_HORIZONTALLY/2-3)*UNIT_SIZE+ENEMY_OFFSET,(UNITS_VERTICALLY/2+2)*UNIT_SIZE+ENEMY_OFFSET,ENEMY_DIAMETER,ENEMY_DIAMETER, Enemy.Directions.UP);
        enemies[2] = new Enemy((UNITS_HORIZONTALLY/2-2)*UNIT_SIZE+ENEMY_OFFSET,(UNITS_VERTICALLY/2+2)*UNIT_SIZE+ENEMY_OFFSET,ENEMY_DIAMETER,ENEMY_DIAMETER, Enemy.Directions.UP);
        enemies[3] = new Enemy((UNITS_HORIZONTALLY/2-1)*UNIT_SIZE+ENEMY_OFFSET,(UNITS_VERTICALLY/2+2)*UNIT_SIZE+ENEMY_OFFSET,ENEMY_DIAMETER,ENEMY_DIAMETER, Enemy.Directions.UP);

        //enemies on right half
        enemies[4] = new Enemy((UNITS_HORIZONTALLY/2+1)*UNIT_SIZE+ENEMY_OFFSET,(UNITS_VERTICALLY/2-2)*UNIT_SIZE+ENEMY_OFFSET,ENEMY_DIAMETER,ENEMY_DIAMETER, Enemy.Directions.DOWN);
        enemies[5] = new Enemy((UNITS_HORIZONTALLY/2+2)*UNIT_SIZE+ENEMY_OFFSET,(UNITS_VERTICALLY/2-2)*UNIT_SIZE+ENEMY_OFFSET,ENEMY_DIAMETER,ENEMY_DIAMETER, Enemy.Directions.DOWN);
        enemies[6] = new Enemy((UNITS_HORIZONTALLY/2+3)*UNIT_SIZE+ENEMY_OFFSET,(UNITS_VERTICALLY/2-2)*UNIT_SIZE+ENEMY_OFFSET,ENEMY_DIAMETER,ENEMY_DIAMETER, Enemy.Directions.DOWN);
        enemies[7] = new Enemy((UNITS_HORIZONTALLY/2+4)*UNIT_SIZE+ENEMY_OFFSET,(UNITS_VERTICALLY/2-2)*UNIT_SIZE+ENEMY_OFFSET,ENEMY_DIAMETER,ENEMY_DIAMETER, Enemy.Directions.DOWN);
    }

    public void newWalls9(){
        currentStaticWallNumber = 2;
        currentFlickeringWallNumber = 0;

        staticWalls = new Wall[currentStaticWallNumber];

        staticWalls[0] = new Wall(0,0,GAME_WIDTH,UNIT_SIZE);
        staticWalls[1] = new Wall((UNITS_HORIZONTALLY/2-3)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
    }

    public void newEnemies9(){
        int leak = 3;   //the space in one line where no enemies spawn
        int enemiesOnOneRow = UNITS_VERTICALLY - leak - 1; //minus first row minus leak

        currentEnemyNumber = (enemiesOnOneRow)*2;

        enemies = new Enemy[currentEnemyNumber];

        //first wave of enemies
        for (int i=0; i<enemiesOnOneRow; i++)
            enemies[i] = new Enemy((UNITS_HORIZONTALLY/2-6)*UNIT_SIZE+ENEMY_OFFSET,(i+1)*UNIT_SIZE+ENEMY_OFFSET,ENEMY_DIAMETER,ENEMY_DIAMETER, Enemy.Directions.LEFT);

        //second wave of enemies
        for (int i=0; i<enemiesOnOneRow; i++)
            enemies[i+enemiesOnOneRow] = new Enemy((UNITS_HORIZONTALLY/2+11)*UNIT_SIZE+ENEMY_OFFSET,(1+leak+i)*UNIT_SIZE+ENEMY_OFFSET,ENEMY_DIAMETER,ENEMY_DIAMETER, Enemy.Directions.LEFT);
    }

    public void newEnemies10(){
        currentEnemyNumber = 10;

        enemies = new Enemy[currentEnemyNumber];

        //enemies on left half
        enemies[0] = new Enemy((UNITS_HORIZONTALLY/2-3)*UNIT_SIZE,(UNITS_VERTICALLY/2-2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE, Enemy.Directions.LEFT);
        enemies[1] = new Enemy((UNITS_HORIZONTALLY/2-2)*UNIT_SIZE,(UNITS_VERTICALLY/2-1)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE, Enemy.Directions.LEFT);
        enemies[2] = new Enemy((UNITS_HORIZONTALLY/2-1)*UNIT_SIZE,(UNITS_VERTICALLY/2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE, Enemy.Directions.LEFT);
        enemies[3] = new Enemy((UNITS_HORIZONTALLY/2-2)*UNIT_SIZE,(UNITS_VERTICALLY/2+1)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE, Enemy.Directions.LEFT);
        enemies[4] = new Enemy((UNITS_HORIZONTALLY/2-3)*UNIT_SIZE,(UNITS_VERTICALLY/2+2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE, Enemy.Directions.LEFT);

        //enemies on right half
        enemies[5] = new Enemy((UNITS_HORIZONTALLY/2+3)*UNIT_SIZE,(UNITS_VERTICALLY/2-2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE, Enemy.Directions.RIGHT);
        enemies[6] = new Enemy((UNITS_HORIZONTALLY/2+2)*UNIT_SIZE,(UNITS_VERTICALLY/2-1)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE, Enemy.Directions.RIGHT);
        enemies[7] = new Enemy((UNITS_HORIZONTALLY/2+1)*UNIT_SIZE,(UNITS_VERTICALLY/2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE, Enemy.Directions.RIGHT);
        enemies[8] = new Enemy((UNITS_HORIZONTALLY/2+2)*UNIT_SIZE,(UNITS_VERTICALLY/2+1)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE, Enemy.Directions.RIGHT);
        enemies[9] = new Enemy((UNITS_HORIZONTALLY/2+3)*UNIT_SIZE,(UNITS_VERTICALLY/2+2)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE, Enemy.Directions.RIGHT);
    }
}
