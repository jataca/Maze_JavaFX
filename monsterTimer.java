package maze2;
import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView ;
import javafx.scene.shape.Line;
import java.util.*;

public class monsterTimer extends AnimationTimer{
    Player player;
    Monster [] monsters;
    List <Line> lineArray;
    int moveSpeed = 5;
    int gameLevel;
    boolean running = true;

    public monsterTimer(Player passPlayer, Monster [] passMonster, List<Line> passLineArray, int passGameLevel){
        player = passPlayer;
        monsters = passMonster;
        lineArray = passLineArray;
        gameLevel = passGameLevel;
    }

    @Override
    public void handle(long now){
        moveMonsters();
    }

    private void moveMonsters(){
        while (running == true){
            for (int i = 0; i < gameLevel * 3; i++){

                // handle player hitting monster
                if (player.playerView.intersects(monsters[i].monsterView.getBoundsInLocal())){
                    System.out.println("You Lost!");
                }

                // handle monster movement
                if (monsters[i].moveUp == true){
                    monsters[i].monsterView.setY(monsters[i].monsterY-=moveSpeed);
                    for (int j = 0; j < lineArray.size(); j++){
                        if (monsters[i].monsterView.intersects(lineArray.get(j).getBoundsInLocal())){
                            System.out.println("UP Collision!");
                            monsters[i].moveUp = false;
                        }
                    }
                    if (monsters[i].moveUp == false)
                        monsters[i].monsterView.setY(monsters[i].monsterY+=moveSpeed);
                }
                else{
                    monsters[i].monsterView.setY(monsters[i].monsterY+=moveSpeed);
                    for (int j = 0; j < lineArray.size(); j++){
                        if (monsters[i].monsterView.intersects(lineArray.get(j).getBoundsInLocal())){
                            System.out.println("DOWN Collision!");
                            monsters[i].moveUp = true;
                        }
                    }
                    if (monsters[i].moveUp == true)
                        monsters[i].monsterView.setY(monsters[i].monsterY-=moveSpeed);
                }
            }
        }
    }
}
