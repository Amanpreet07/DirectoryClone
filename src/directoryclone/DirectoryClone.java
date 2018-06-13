package directoryclone;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DirectoryClone extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        // verify core folders
        if(!DataManager.verify()){
        DataManager.setupDir();
        }
       Parent root = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
//        stage.setMaximized(true);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
