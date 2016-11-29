package maze2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView ;

public class Player{

    Image playerImage;
    ImageView playerView;
    int playerX;
    int playerY;

    public Player(){
        //playerImage = new Image("file:maze/Bishop-W70.png");
        playerImage = new Image("file:maze/green_square.png");
        playerView = new ImageView(playerImage);
    }

    public void setPlayerPositions(int PPosX, int PPosY){
        playerX = PPosX;
        playerY = PPosY;
    }
}
