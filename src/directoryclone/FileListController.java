package directoryclone;

import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class FileListController implements Initializable {

    @FXML
    private JFXTextArea area;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        area.setText(null);
    }    
    
    public void setValue(String[] text){
        for (String text1 : text) {
            area.appendText(text1);
        }
    }
    
}
