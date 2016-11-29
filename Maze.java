package maze2;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView ;
import java.util.*;
import javafx.scene.control.ProgressBar;
import java.util.Random;
import javafx.scene.control.ScrollPane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



// we may have to add an additional layer to our structure. some sort of view that can go in a scrollpane so we can put the scrollpane in the group

public class Maze {
    Scene mazeScene;
    int [][] mazeArray;
    int numRows; // 8
    int numCols; // 11
    Line mazeLine;
    List <Line> lineArray = new ArrayList<Line>(); // I think we should give the Game class its own copy of this
    Group root;
    ImageView goldView;
    Image flowers [];
    ImageView flowerViews [];
    ScrollPane sp;
    Label staminaLabel;
    ProgressBar progressbar;
    Rectangle labelBackGround;


    public Maze(int level, Player player, Monster [] monsters, ProgressBar pb){
        root = new Group();
        progressbar = pb;
        LevelBuilder levelBuilder = new LevelBuilder(level, root, player, monsters, pb);
        sp = new ScrollPane();
        goldView = levelBuilder.goldView;
        numRows = levelBuilder.numRows;
        numCols = levelBuilder.numCols;
        flowers = new Image[numRows * numCols];
        flowerViews = new ImageView[numRows * numCols];
        createMazeArray(level); // I think I should move this to LevelBuilder
        mazeScene = drawMaze();
        System.out.println("rows: "  + numRows);

    }

    /*
    *   Draws the Maze using JavaFX
    */
    private Scene drawMaze(){
        int numFlower = 0;
        int xPos = 50; // starting X pos
        int yPos = 50; // starting Y pos
        int strokeWidth = 40;
        int doubleStroke = strokeWidth * 2;
        int halfStrokeWidth = strokeWidth / 2;
        int edgeDrawer = strokeWidth + halfStrokeWidth;

        System.out.println("rows: " + numRows + "\ncolumns: " + numCols + "\n");

        // actually draw the maze
        for (int i = 0; i < numRows; i++){
            //System.out.println(numRows);
            for (int j = 0; j < numCols; j++){
                if (mazeArray[i][j] == 1){
                    //mazeLine = new Line(xPos, yPos, xPos, yPos);
                    mazeLine = new Line(xPos-1, yPos, xPos, yPos);
                    //mazeLine = new Line(xPos-1, yPos+halfStrokeWidth, xPos+halfStrokeWidth, yPos+halfStrokeWidth); // no invisable buffer

                    mazeLine.setStrokeWidth(strokeWidth);
                    //mazeLine.setStrokeWidth(1);
                    root.getChildren().add(mazeLine);

                    lineArray.add(mazeLine);
                    flowers[numFlower] = new Image("file:maze/flower.png");
                    flowerViews[numFlower] = new ImageView(flowers[numFlower]);
                    flowerViews[numFlower].setX(xPos  - halfStrokeWidth);
                    flowerViews[numFlower].setY(yPos - halfStrokeWidth);
                    //root.getChildren().add(flowerViews[numFlower]);
                }
                if (i == 0){
                    mazeLine = new Line(xPos-halfStrokeWidth, yPos-halfStrokeWidth, xPos+halfStrokeWidth, yPos-halfStrokeWidth);
                    mazeLine.setStrokeWidth(1);
                    root.getChildren().add(mazeLine);
                    lineArray.add(mazeLine);
                }
                if (i == numRows-1 && mazeArray[i][j] != 9){
                     mazeLine = new Line(xPos-halfStrokeWidth, yPos+halfStrokeWidth, xPos+halfStrokeWidth, yPos+halfStrokeWidth);
                    mazeLine.setStrokeWidth(10);
                    root.getChildren().add(mazeLine);
                    lineArray.add(mazeLine);
                }
                if (j == 0){
                     mazeLine = new Line(xPos-halfStrokeWidth, yPos-halfStrokeWidth, xPos-halfStrokeWidth, yPos+halfStrokeWidth);
                    mazeLine.setStrokeWidth(1);
                    root.getChildren().add(mazeLine);
                    lineArray.add(mazeLine);
                }
                if (j == numCols-1){
                     mazeLine = new Line(xPos+halfStrokeWidth, yPos-halfStrokeWidth, xPos+halfStrokeWidth, yPos+halfStrokeWidth);
                    mazeLine.setStrokeWidth(1);
                    root.getChildren().add(mazeLine);
                    lineArray.add(mazeLine);
                }

                /*
                // if border
                if (i == 0){
                    mazeLine = new Line(xPos-edgeDrawer, yPos-edgeDrawer, xPos+edgeDrawer, yPos-edgeDrawer);
                    mazeLine.setStrokeWidth(doubleStroke);
                    root.getChildren().add(mazeLine);
                    lineArray.add(mazeLine);
                }
                if (i == 7 && mazeArray[i][j] != 9){
                     mazeLine = new Line(xPos-edgeDrawer, yPos+edgeDrawer, xPos+edgeDrawer, yPos+edgeDrawer);
                    mazeLine.setStrokeWidth(doubleStroke);
                    root.getChildren().add(mazeLine);
                    lineArray.add(mazeLine);
                }
                if (j == 0){
                     mazeLine = new Line(xPos-edgeDrawer, yPos-edgeDrawer, xPos-edgeDrawer, yPos+edgeDrawer);
                    mazeLine.setStrokeWidth(doubleStroke);
                    root.getChildren().add(mazeLine);
                    lineArray.add(mazeLine);
                }
                if (j == 10){
                     mazeLine = new Line(xPos+edgeDrawer, yPos-edgeDrawer, xPos+edgeDrawer, yPos+edgeDrawer);
                    mazeLine.setStrokeWidth(doubleStroke);
                    root.getChildren().add(mazeLine);
                    lineArray.add(mazeLine);
                }
                */
                numFlower ++;
                xPos+=strokeWidth;
            } // columns
            yPos += strokeWidth;
            xPos = 50; // start position
        } // rows

         // this is where important screen sizing happens and allows scrolling in game. It should be moved LevelBuilder

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        sp.setContent(root);
        sp.setPrefSize(visualBounds.getWidth() - 50, visualBounds.getHeight() - 50);
        Group newRoot = new Group();
        staminaLabel = new Label("Stamina");
        labelBackGround = new Rectangle();
        //labelBackGround.setWidth(280.0f);
        //labelBackGround.setHeight(80.0f);
        staminaLabel.setTextFill(Color.web("#0076a3"));
        //labelBackGround.setFill(Color.RED);
        //staminaLabel.setBackground(Color.web("#0076a3"));
        HBox hb = new HBox();
        hb.setSpacing(5);
        hb.getChildren().addAll(staminaLabel, progressbar);
        //newRoot.getChildren().add(hb); // add stamina bar
        newRoot.getChildren().addAll(sp, hb);

        mazeScene = new Scene(newRoot); // wut.
        //Scale scale = new Scale(scaleFactor, scaleFactor);
        //scale.setPivotX(0);
        //scale.setPivotY(0);
        //mazeScene.getRoot().getTransforms.setAll(scale);
        //System.out.println(lineArray.size());
        return mazeScene;
    }

    /*
    * Because I don't want to read from a file we keep the mazes here
    */
    private void createMazeArray(int level){
        /*
        switch (level){
            case 1:
                //System.out.println("createMazeArray"); // we get here
                mazeArray = new int[][] {
                    {0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0},
                    {0, 3, 0, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0},
                    {0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0},
                    {0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0},
                    {0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}
                } ;
            break;
        }
        */

        Random rand = new Random();
        //int numRows = 25;
        //int numCols = 25;
        mazeArray = new int[numRows][numCols];

        for (int i = 0; i < numRows; i++){
            for (int j = 0; j < numCols; j++){
                // if not side
                if (i != 0 && i != numRows - 1 && j != 0 && j != numCols - 1){
                    // if there's a diagnol wall
                    if (mazeArray[i-1][j-1] == 1 || mazeArray[i-1][j+1] == 1){
                        mazeArray[i][j] = 0;
                    }
                    else{
                        int randomInt = rand.nextInt(2);
                        mazeArray[i][j] = randomInt;
                    }
                }
                else{
                    int randomInt = rand.nextInt(2);
                    mazeArray[i][j] = randomInt;
                }
            }
        }

        for (int i = 0; i < numRows; i++){
            for (int j = 0; j < numCols; j++){
                // if not side
                if (i != 0 && i != numRows - 1 && j != 0 && j != numCols - 1){
                    // if we have a square
                    if (mazeArray[i-1][j-1] == 0 && mazeArray[i-1][j] == 0 && mazeArray[i][j-1] == 0 && mazeArray[i][j] == 0){
                        int randomInt = rand.nextInt(4);
                        switch (randomInt + 1){
                        case 1:
                            mazeArray[i-1][j-1] = 1;
                        case 2:
                            mazeArray[i][j-1] = 1;
                        case 3:
                            mazeArray[i-1][j] = 1;
                        case 4:
                            mazeArray[i][j] = 1;
                        }
                    }

                }
            }
        }

        // go through the mazeArray and get rid of squares
        for (int i = 0; i < numRows; i++){
            for (int j = 0; j < numCols; j++){
                // if not side
                //if (i != 0 && i != numRows - 1 && j != 0 && j != numCols - 1){ // looks like black squares can occur on the left and bot side with this
                if (i != 0 && j != 0){
                    // if we have a square
                    if (mazeArray[i-1][j-1] == 1 && mazeArray[i-1][j] == 1 && mazeArray[i][j-1] == 1 && mazeArray[i][j] == 1){
                        int randomInt = rand.nextInt(4);
                        switch (randomInt + 1){
                        case 1:
                            mazeArray[i-1][j-1] = 0;
                        case 2:
                            mazeArray[i][j-1] = 0;
                        case 3:
                            mazeArray[i-1][j] = 0;
                        case 4:
                            mazeArray[i][j] = 0;
                        }
                    }

                }
            }
        }

        mazeArray[numRows-1][numCols-1] = 0;
        mazeArray[0][0] = 0;
        mazeArray[1][0] = 0;
        mazeArray[numRows-2][numCols-1] = 0;
        mazeArray[numRows-1][numCols-2] = 0;

    }
}
