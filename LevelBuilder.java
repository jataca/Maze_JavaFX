package maze2;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView ;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class LevelBuilder{
    Image gold;
    ImageView goldView;
    float goldPosX;
    float goldPosY;
    float goldFudgeFactor = (float)40.0;
    int numRows;
    int numCols;
    Label staminaLabel;

    /*
    * level should decide background image
    */
    public LevelBuilder(int level, Group root, Player player, Monster [] monsters, ProgressBar progressbar){
        /*
        staminaLabel = new Label("Stamina");
        HBox hb = new HBox();
        hb.setSpacing(5);
        hb.getChildren().addAll(staminaLabel, progressbar);
        root.getChildren().add(hb); // add stamina bar
        */

        //switch (level){
        //    case 1:
                numRows = 7+level;
                numCols = 10 +level;

                player.setPlayerPositions(40, 40); // 40, 65
                gold = new Image("file:maze/able_square.png");
                goldView = new ImageView(gold);
                //goldPosX = numCols * goldFudgeFactor; // try gold position = numCols * 38.5
                //goldPosY = numRows * goldFudgeFactor; // try gold position = numRows * 38.5
                goldPosX = 40;
                goldPosY = 80;
                player.playerView.setX(player.playerX);
                player.playerView.setY(player.playerY);
                goldView.setX(goldPosX);
                goldView.setY(goldPosY);
                for (int i = 0; i < 3; i++){
                    Monster newMonster = new Monster();
                    monsters[i] = newMonster;
                }

                monsters[0].setMonsterPositions(125, 40);
                monsters[1].setMonsterPositions(125, 310);
                monsters[2].setMonsterPositions(125, 40);
                //monsters[0].drawMonsterImageView();

                monsters[0].monsterView.setX(125);
                monsters[0].monsterView.setY(40);
                monsters[1].monsterView.setX(125);
                monsters[1].monsterView.setY(310);
                monsters[2].monsterView.setX(450);
                monsters[2].monsterView.setY(40);

                root.getChildren().addAll(player.playerView, goldView, monsters[0].monsterView, monsters[1].monsterView, monsters[2].monsterView);
        //}
    }
}
