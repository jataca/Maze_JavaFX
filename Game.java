package maze2;

import javafx.scene.input.KeyEvent;
import javafx.scene.image.ImageView ;
import javafx.scene.shape.Line;
import java.util.*;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.ProgressBar;
import javafx.concurrent.Task;
import javafx.scene.control.ScrollPane;
import javafx.scene.Node;
import javafx.scene.CacheHint;

//import javafx.Robot;
//import java.awt.event.KeyEvent;

public final class Game implements Runnable {

    Scene gameScene;
    Thread thread;
    Thread monsterT;
    boolean running;
    ImageView goldView;
    List <Line> lineArray;
    int moveSpeed = 5;
    int jumpDistance = 80;
    int monsterSpeed = 1;
    int gameLevel;
    Stage gameWindow;
    Player player;
    Monster [] monsters;
    private Timeline timeline;
    private AnimationTimer timer;
    private Integer index=0;
    boolean canJump = true;
    boolean jumpUp;
    boolean jumpDown;
    boolean jumpLeft;
    boolean jumpRight;
    boolean walkUp;
    boolean walkDown;
    boolean walkRight;
    boolean walkLeft;
    ProgressBar progressbar;
    float[] intervals;
    Task copyWorker;



    /*
    * We want to close the Menu Scene, but reuse the Stage
    */
    public Game(int level, Stage primaryStage){
        intervals = new float[]{0f, 0.5f, 1.0f};
        progressbar = new ProgressBar();
        progressbar.setProgress(intervals[2]);
        walkUp = true;
        walkDown = true;
        walkRight = true;
        walkLeft = true;
        gameLevel = level;
        gameWindow = primaryStage;
        player = new Player();
        monsters = new Monster[level * 3];
        Maze maze = new Maze(gameLevel, player, monsters, progressbar);
        gameScene = maze.mazeScene;  // gameScene and mazeScene are exactly the same... so, why have two if I can have one?
        goldView = maze.goldView;
        //System.out.println(lineArray.size());
        lineArray = maze.lineArray;

        primaryStage.setScene(gameScene);
        //primaryStage.setMaximized(true);
        System.out.println("new game");

        // trying to cache monsters.
        for (int i = 0; i < 3; i++ ){
            monsters[i].monsterView.setCache(true);
            monsters[i].monsterView.setCacheHint(CacheHint.QUALITY);
            monsters[i].monsterView.setCacheHint(CacheHint.SPEED);
        }
        //MonsterTimer timer = new MonsterTimer(player, monsters, lineArray, gameLevel);
        start();

    }

    public synchronized void start(){
        final StackPane stack = new StackPane();

        //create a timeline for moving the circle
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);

        //You can add a specific action when each frame is started.
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                //text.setText(index.toString());
                //System.out.println("handle() number 1");
                moveMonsters(); // takes up like 15 - 20% CPU
                index++;
            }
        };

        //create a keyValue with factory: scaling the circle 2times
        KeyValue keyValueX = new KeyValue(stack.scaleXProperty(), 2);
        KeyValue keyValueY = new KeyValue(stack.scaleYProperty(), 2);

        //create a keyFrame, the keyValue is reached at time 2s
        Duration duration = Duration.millis(4000);
        //one can add a specific action when the keyframe is reached
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                 //stack.setTranslateX(java.lang.Math.random()*200-100);
                 //System.out.println("handle() number 2");
                 //reset counter
                 index = 0;
            }
        };

        KeyFrame keyFrame = new KeyFrame(duration, onFinished , keyValueX, keyValueY);

        //add the keyframe to the timeline
        timeline.getKeyFrames().add(keyFrame);

        timeline.play();
        timer.start();

        running = true;
        thread = new Thread(this, "Display"); // Display is an optional parameter that gives a name to the thread
        thread.start(); // calls the run() method... this IS VERY CPU HEAVY!!!!
    }

    public synchronized void stop(){
        running = false;
        timer.stop();
        timeline.stop();
        try{
            thread.join();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*
    * EXTREMELY CPU HEAVY FUNCTION!!! Maybe just running a background thread is CPU HEAVY regardless of it's content?
    */
    @Override
    public void run(){
        while (running == true){
            try{
                thread.sleep(10); // cuts CPU % from 115 to 20. Cuts RAM usage from 700 to 170. May not be the best solution. Try a wait().
            }
            catch (Exception e){
                System.out.println("Caught sleeping");
            }

            /*
            * Allow player to move... NOT CPU HEAVY...
            */
            gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                boolean collision = false;
                switch (event.getCode()) {
                    case W:
                        jumpUp = true; jumpDown = false; jumpLeft = false; jumpRight = false;
                        player.playerView.setY(player.playerY-=moveSpeed);
                        for (int i = 0; i < lineArray.size(); i++){
                            if (player.playerView.intersects(lineArray.get(i).getBoundsInLocal())){
                                System.out.println("Up Collision!");
                                collision = true;
                            }
                        }
                        if (collision == true)
                            player.playerView.setY(player.playerY+=moveSpeed);
                        collision = false;
                        break;
                    case S:
                        jumpDown = true; jumpUp = false; jumpLeft = false; jumpRight = false;
                        player.playerView.setY(player.playerY+=moveSpeed);
                        for (int i = 0; i < lineArray.size(); i++){
                            if (player.playerView.intersects(lineArray.get(i).getBoundsInLocal())){
                                System.out.println("Down Collision!");
                                collision = true;
                            }
                        }
                        if (collision == true)
                            player.playerView.setY(player.playerY-=moveSpeed);
                        collision = false;
                        break;
                    case A:
                        jumpLeft = true; jumpUp = false; jumpDown = false; jumpRight = false;
                        player.playerView.setX(player.playerX-=moveSpeed);
                        for (int i = 0; i < lineArray.size(); i++){
                            if (player.playerView.intersects(lineArray.get(i).getBoundsInLocal())){
                                System.out.println("Left Collision!");
                                collision = true;
                            }
                        }
                        if (collision == true)
                            player.playerView.setX(player.playerX+=moveSpeed);
                        collision = false;
                    break;
                    case D:
                        jumpRight = true; jumpUp = false; jumpDown = false; jumpLeft = false;
                        player.playerView.setX(player.playerX+=moveSpeed);
                        for (int i = 0; i < lineArray.size(); i++){
                            if (player.playerView.intersects(lineArray.get(i).getBoundsInLocal())){
                                System.out.println("RIGHT Collision!");
                                collision = true;
                            }
                        }
                        if (collision == true)
                            player.playerView.setX(player.playerX-=moveSpeed);
                        collision = false;
                    break;
                    case SHIFT: System.out.println("SHIFT"); break;
                    case J:
                        if (canJump == true){
                            progressbar.setProgress(0); // set jump cooldown
                            copyWorker = createWorker();
                            new Thread(copyWorker).start();
                            progressbar.progressProperty().bind(copyWorker.progressProperty());

                            if (jumpUp == true){
                                System.out.println("JUMP UP!");
                                player.playerView.setY(player.playerY-=jumpDistance);
                                for (int i = 0; i < lineArray.size(); i++){
                                    if (player.playerView.intersects(lineArray.get(i).getBoundsInLocal())){
                                        System.out.println("UP Collision!");
                                        collision = true;
                                    }
                                }
                                if (collision == true)
                                    player.playerView.setY(player.playerY+=jumpDistance);
                                collision = false;
                            }
                            else if (jumpDown == true){
                                System.out.println("JUMP DOWN!");
                                player.playerView.setY(player.playerY+=jumpDistance);
                                for (int i = 0; i < lineArray.size(); i++){
                                    if (player.playerView.intersects(lineArray.get(i).getBoundsInLocal())){
                                        System.out.println("DOWN Collision!");
                                        collision = true;
                                    }
                                }
                                if (collision == true)
                                    player.playerView.setY(player.playerY-=jumpDistance);
                                collision = false;
                            }
                            else if (jumpLeft == true){
                                System.out.println("JUMP LEFT!");
                                player.playerView.setX(player.playerX-=jumpDistance);
                                for (int i = 0; i < lineArray.size(); i++){
                                    if (player.playerView.intersects(lineArray.get(i).getBoundsInLocal())){
                                        System.out.println("LEFT Collision!");
                                        collision = true;
                                    }
                                }
                                if (collision == true)
                                    player.playerView.setX(player.playerX += jumpDistance);
                                collision = false;
                            }
                            else if (jumpRight == true){
                                System.out.println("JUMP RIGHT!");
                                player.playerView.setX(player.playerX+=jumpDistance);
                                for (int i = 0; i < lineArray.size(); i++){
                                    if (player.playerView.intersects(lineArray.get(i).getBoundsInLocal())){
                                        System.out.println("RIGHT Collision!");
                                        collision = true;
                                    }
                                }
                                if (collision == true)
                                    player.playerView.setX(player.playerX-=jumpDistance);
                                collision = false;
                            }
                        }
                        else {}
                }

            }
            }); // player movement


            /* check if player won (NOT CPU HEAVY)*/
            if (player.playerView.intersects(goldView.getBoundsInLocal())){
                System.out.println("YOU WON!");
                // create alert box
                // endThread
                Platform.runLater(new Runnable() {
                        public void run() {
                            LevelVictory.display(gameLevel, gameWindow, true);
                        }
                    });
                running = false;
                stop();
            }



        } // while running == true
    }

    /*
    * This function is cpu intensive. Moving the monsters. Removing this function alleviated 10 - 15 % of CPU
    */
    public void moveMonsters(){
            //for (int i = 0; i < gameLevel * 3; i++){

            for (int i = 0; i < 3; i++ ){

                // handle player hitting monster
                if (player.playerView.intersects(monsters[i].monsterView.getBoundsInLocal())){
                    System.out.println("You Lost!");
                    Platform.runLater(new Runnable() {
                        public void run() {
                            LevelVictory.display(gameLevel, gameWindow, false);
                        }
                    });
                    running = false;
                    stop();
                }

                // handle monster movement
                if (monsters[i].moveUp == true){
                   // System.out.println("monster move up | lineArray size = " + lineArray.size());
                    monsters[i].monsterView.setY(monsters[i].monsterY-=monsterSpeed);
                    //System.out.println("I: " + i);
                    for (int j = 0; j < lineArray.size(); j++){
                        //System.out.println("go through line array");
                        if (monsters[i].monsterView.intersects(lineArray.get(j).getBoundsInLocal()) ){
                           // System.out.println("UP Collision!");
                            monsters[i].moveUp = false;
                        }
                    }
                    if (monsters[i].moveUp == false)
                        monsters[i].monsterView.setY(monsters[i].monsterY+=monsterSpeed);
                }
                else{
                    monsters[i].monsterView.setY(monsters[i].monsterY+=monsterSpeed);
                    for (int j = 0; j < lineArray.size(); j++){
                        if (monsters[i].monsterView.intersects(lineArray.get(j).getBoundsInLocal())){
                          //  System.out.println("DOWN Collision!");
                            monsters[i].moveUp = true;
                        }
                    }
                    if (monsters[i].moveUp == true)
                        monsters[i].monsterView.setY(monsters[i].monsterY-=monsterSpeed);
                }
            //   System.out.println("out of for loop: " +  running);
            }
           // System.out.println(running);
        }
//
    public Task createWorker(){
        System.out.println("createWorker");
        return new Task(){
            @Override
            protected Object call() throws Exception{
                System.out.println("object call");
                canJump = false;
                for (int i = -1; i < 10; i++){
                    updateProgress(i + 1, 10);
                    System.out.println(i);
                    Thread.sleep(500);
                }
                progressbar.progressProperty().unbind();
                canJump = true;
                return true;

            }
        };
    }

}
