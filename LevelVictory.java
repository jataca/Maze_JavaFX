package maze2;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.stage.Modality;

public class LevelVictory{


    public static void display(int level, Stage primaryStage, boolean playerWon){



        // Stage
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); // block all other window interactions until this window is closed
        window.setMinWidth(250);



        // the layout
        VBox layout = new VBox(10);

        // Label
        Label label = new Label();


        if (playerWon == true){
            window.setTitle("Congratuations!");
            label.setText("Victory");
            Button nextLevel = new Button("Next Level");
            nextLevel.setOnAction(e -> {
                Game newGame = new Game(level + 1, primaryStage); // kick off the next level shiz
                window.close();
            });

            layout.getChildren().addAll(nextLevel);

        }

        else{
            window.setTitle("Sorry!");
            label.setText("Defeat");
        }

        // Button
        Button closeButton = new Button("Main menu");
        closeButton.setOnAction(e -> {
            Menu newMenu = new Menu(primaryStage);
            //window.setScene(Main.sceneMenu);
            window.close();
            //primaryStage.setScene();

        });

        // Layout

        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        // Scene
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait(); // program pauses here until the window closes
    }
}
