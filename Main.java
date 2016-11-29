package maze2;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.application.Platform;
import javafx.event.EventHandler;


public class Main extends Application{
    public Menu menu;
    /*
    * Main because we have to
    */
    public static void main(String[] args){
        
        launch(args);
    }

    /*
    * launch(args) from main(args) calls this function
    */
    public void start(Stage primaryStage){
        // create main menu
        menu = new Menu(primaryStage);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent e){
                Platform.exit();
                System.exit(0);
            }
        });
    }
}
