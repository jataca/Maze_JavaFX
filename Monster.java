package maze2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView ;

public class Monster {
    Image monsterImage;
    ImageView monsterView;
    int monsterX;
    int monsterY;
    boolean moveUp;

    public Monster(){
        monsterImage = new Image("file:maze/monster.png");
        monsterView = new ImageView(monsterImage);
        moveUp = true;
    }

    // what's the purpose of a setter function if the variables are public? there is none...
    public void setMonsterPositions(int PosX, int PosY){
        monsterX = PosX;
        monsterY = PosY;
    }
}
