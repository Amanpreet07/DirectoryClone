package directoryclone;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import setup.SetupManager;
import writelib.Writer;

public class HomeScreenController implements Initializable {
    
    @FXML
    private AnchorPane Base;
    
    @FXML
    private TextField InputPath;

    @FXML
    private RadioButton Option_ShallowScan;
    
    @FXML
    private CheckBox Check_append;

    @FXML
    private RadioButton Option_DeepScan;
    
    @FXML
    private JFXTextArea TextArea;
    
    @FXML
    private JFXTextField OutputFile;
    
    @FXML
    private Label Error;

    @FXML
    private ImageView Error_img;

    @FXML
    private TextField OutputPath;

    /**
     * Triggers upon clicking choose button for input path.
     * @param event 
     */
    @FXML
    void Action_Choose1(ActionEvent event) {
       Stage stage = (Stage) Base.getScene().getWindow();
       DirectoryChooser dc = new DirectoryChooser();
       dc.setTitle("Choose root folder to scan");
       File dir = dc.showDialog(stage);
       // warning message is hidden if proper folder is choosen.
       if(dir != null){
           InputPath.setText(dir.getAbsolutePath());
           Error_img.setVisible(false);
           Error.setText(null);
       }
       // display warning message if no folder is choosen.
       else{  
           if(InputPath.getText().isEmpty()){     
           Error_img.setVisible(true);
           Error.setText("CHOOSE PROPER FOLDER...");
       }
       }
          
    }

    /**
     * To minimize the parent screen with custom button because parent
     * is set to UNDECORATED.
     * @param event 
     */
    @FXML
    void Action_Minimize(ActionEvent event) {
       Stage stage = (Stage) Base.getScene().getWindow(); 
       stage.setIconified(true);
    }
    
    /**
     * Stops the process and ends the execution.
     * @param event 
     */
    @FXML
    void Action_Quit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    /**
     * To write the scan results to a file created and written by my TextLibrary
     * Scans for previous event sequence and report error message to complete
     * it before continuing further.
     * Otherwise, creates a new file and writes the scan results..
     * shows message after successful completion
     * @param event 
     */
    @FXML
    void Action_Save(ActionEvent event) {
       
        if(TextArea.getText().isEmpty()){
            Error_img.setVisible(true);
            Error.setText("NO SCAN RESULTS TO WRITE...");
        }
        else if(OutputPath.getText().isEmpty()){
            Error_img.setVisible(true);
            Error.setText("NO OUTPUT PATH PROVIDED...");
        }
        else if(OutputFile.getText().isEmpty()){
            Error_img.setVisible(true);
            Error.setText("PROVIDE FILENAME(without extension) TO WRITE TO...");
        }
        else if(!OutputFile.getText().matches("[a-zA-Z0-9]+")){
        Error_img.setVisible(true);
        Error.setText("INAPPROPRIATE FILENAME...");    
        }
        else{
            // custom library function to create a new file.
            SetupManager.createFile(OutputFile.getText() + ".txt", 
                    OutputPath.getText());
            // custom library function to write block of data in append mode 
            // without any encryption.
            Writer.writeData_Block(OutputFile.getText() + ".txt", OutputPath.getText(),
                    TextArea.getText().split("\\r?\\n"),
                    true, Writer.PLAIN_TEXT);
         
            Error.setText("FILE SAVED SUCCESSFULLY...");
         
        }   
    }
    
    /**
     * Checks the conditions for scan (like deep, shallow and append mode.) then
     * continues if inputs are correct.
     * @param event 
     */
    @FXML
    void Action_Scan(ActionEvent event) {
        // checks for input path ...shows error message if not found.
        if(InputPath.getText().isEmpty()){     
           Error_img.setVisible(true);
           Error.setText("CHOOSE INPUT PATH FIRST...");
           return;
       }
        else{
           Error_img.setVisible(false);
           Error.setText(null);
        }
        // if append is not selected, clears the textArea and the array list.
        if(!Check_append.isSelected()){
        TextArea.clear();
        al.clear();    
        }
        else{
        TextArea.clear();
        }
        // calls the deep scan method to execute if it is selected 
        // adds a line notifying of the same.
        if(Option_DeepScan.isSelected()){
            if(Check_append.isSelected()){
            al.add("### Appending DeepScan results for " + InputPath.getText() 
                    + " ###\n");    
            }
            else{
            al.add("### DeepScan results for " + InputPath.getText() 
                    + " ###\n");    
            }
            printDirectoryTree(new File(InputPath.getText()));  
            fill();
        }
        // calls the shallow scan method and notifies for the same by adding a 
        // line to the result area.
        else if(Option_ShallowScan.isSelected()){
            if(Check_append.isSelected()){
            al.add("### Apending ShallowScan results for " + InputPath.getText() 
                    + " ###\n");    
            }
            else{
            al.add("### ShallowScan results for " + InputPath.getText() 
                    + " ###\n");    
            }
            
            Shallow_Scan(new File(InputPath.getText()));
            fill_Shallow();
        }
        
    }
    
    /**
     * reads the list of all files and folders within provided path.
     * NOTE : It does not scan the sub folders.
     * To be used for directories with lot of sub folders like Games, Softwares,
     * OS directories etc.
     * @param file 
     */
    private void Shallow_Scan(File file){
        File flist[] = file.listFiles();
        String slist[] = new String[flist.length];
        int count = 0;
        for (String slist1 : slist) {
            slist1 = "|--" + flist[count++].getName() + "\n";
            al.add(slist1);
        }
        
    }
    
    /**
     * writes the result of shallow scan to result area. 
     */
    private void fill_Shallow(){
        al.stream().forEach((al1) -> {
            TextArea.appendText(al1);
        });
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         
        // event for click on textfield for ouput path
        // more efficient way to use this instead of a method.
         OutputPath.setOnMouseClicked(e -> {
         Stage stage = (Stage) Base.getScene().getWindow();
         DirectoryChooser dc = new DirectoryChooser();
         dc.setTitle("Choose root folder");
         File dir = dc.showDialog(stage);
         if(dir != null){
           OutputPath.setText(dir.getAbsolutePath());
           Error_img.setVisible(false);
           Error.setText(null);
         }
         else{  
           if(OutputPath.getText().isEmpty()){     
           Error_img.setVisible(true);
           Error.setText("CHOOSE PROPER OUTPUT FOLDER...");
        }
        }
       });
        
         // initialize the css for textFields.
        InputPath.setStyle("-fx-text-inner-color: white;-fx-background-color: #3d5afe");
        OutputPath.setStyle("-fx-text-inner-color: white;-fx-background-color: #3d5afe");
        OutputFile.setStyle("-fx-text-inner-color: white;-fx-background-color: #3d5afe");
        
    } 
    
    // Array list to hold scan results.
    private static ArrayList<String> al=new ArrayList<>();
    
    /**
     * Method to scan directory and sub folders.
     * Taken from StackOverflow.
     * @param folder
     * @return 
     */
    public static String printDirectoryTree(File folder) {
       int indent = 0;
       StringBuilder sb = new StringBuilder();
       printDirectoryTree(folder, indent, sb);
       al.add(sb.toString());
       return sb.toString();
    }

    /**
     * method for deep scan.
     * Taken from StackOverflow.
     * @param folder
     * @param indent
     * @param sb 
     */
    private static void printDirectoryTree(File folder, int indent,
        StringBuilder sb) {
      sb.append(getIndentString(indent));
      sb.append("+--");
      sb.append(folder.getName());
      sb.append("/");
      sb.append("\n");
      for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
            printDirectoryTree(file, indent + 1, sb);
           } else {
            printFile(file, indent + 1, sb);
            }
       }

    }

    /**
     * methods to add symbol for directory representation in result area.
     * Taken from StackOverflow
     * @param file
     * @param indent
     * @param sb 
     */
    private static void printFile(File file, int indent, StringBuilder sb) {
     sb.append(getIndentString(indent));
      sb.append("+--");
      sb.append(file.getName());
      sb.append("\n");
    }

    /**
     * To add symbols before file names to represent layout in result area.
     * Taken from StackOverflow
     * @param indent
     * @return 
     */
    private static String getIndentString(int indent) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < indent; i++) {
         sb.append("|  ");
     }
     return sb.toString();
    }

    /**
     * Writes the deep scan results to the result area.
     */
    private void fill() {
       
        al.stream().forEach((al1) -> {
            TextArea.appendText(al1);
        });
        
    }
    
}
