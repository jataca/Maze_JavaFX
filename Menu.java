package maze2;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;

/*
* Constructer for a new menu
*/
public class Menu{

    private final int VBoxSpacing = 20;
    private final int sceneWidth = 200;
    private final int sceneHeight = 220;
    public Scene sceneMenu;
    Game newGame;

    /*
    * REMEMBER CONSTRUCTORS DON'T HAVE RETURN TYPES
    */
    public Menu(Stage primaryStage){

        // Window title
        primaryStage.setTitle("Stage Title");
        
        // Label
        Label label = new Label();
        label.setText("Main Menu");

        // Buttons
        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> {
             newGame = new Game(1, primaryStage);
        });
        //Button loadGameButton = new Button("Load Game");
        //Button optionsButton = new Button("Options");
        Button exitGameButton = new Button("Exit Game");
        exitGameButton.setOnAction(e -> {
            primaryStage.close();
            Platform.exit();
            System.exit(0);
        }
            );

        // VBox puts objects in a vertical order with a 20 pixel seperation
        VBox layoutMenu = new VBox(VBoxSpacing);

        layoutMenu.getChildren().addAll(label, newGameButton, exitGameButton);
        layoutMenu.setAlignment(Pos.CENTER);



        // Scene initilization
        sceneMenu = new Scene(layoutMenu, sceneWidth, sceneHeight);

        primaryStage.setScene(sceneMenu);
        primaryStage.show();

    }
}
