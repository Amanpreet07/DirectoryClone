package directoryclone;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DirectoryClone extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
       Parent root = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        Scene scene = new Scene(root);
        // to make curved edges transparent
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        // for curved edges of parent screen
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
