package directoryclone;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import setup.SetupManager;
import writelib.Writer;

public class HomeScreenController implements Initializable {

    @FXML
    private VBox Base;

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
    private FontAwesomeIconView Error_img;

    @FXML
    private TextField OutputPath;

    @FXML
    private Label track_status;

    @FXML
    private CheckBox tracking;

//    @FXML
//    private RadioButton filter_all;
//
//    @FXML
//    private RadioButton filter_image;
//
//    @FXML
//    private RadioButton filter_audio;
//
//    @FXML
//    private RadioButton filter_video;
//
//    @FXML
//    private RadioButton filter_doc;
    /**
     * Triggers upon clicking choose button for input path.
     *
     * @param event
     */
    @FXML
    void Action_Choose1(ActionEvent event) {
        Stage stage = (Stage) Base.getScene().getWindow();
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose root folder to scan");
        File dir = dc.showDialog(stage);
        
        // disables tracking by default
        tracking.setSelected(false);
        // warning message is hidden if proper folder is choosen.
        if (dir != null) {
            InputPath.setText(dir.getAbsolutePath());
            Error_img.setVisible(false);
            Error.setText(null);

            String alreadyHas[] = DataManager.readList();
            boolean flag = false;
            for (String alreadyHa : alreadyHas) {
                if (alreadyHa.equals(InputPath.getText())) {
                    flag = true;     
                }    
            }
            if(flag){
                track_status.setVisible(true);
                track_status.setText("This directory is already being tracked.");
                tracking.setDisable(true);
//                track_mode.setVisible(true);
            }else{
                track_status.setVisible(false);
                tracking.setDisable(false);
//                track_mode.setVisible(false);
            }

    } // display warning message if no folder is choosen.

        else {
            if (InputPath.getText().isEmpty()) {
            Error_img.setVisible(true);
            Error.setText("CHOOSE PROPER FOLDER...");
        }
    }
}

/**
 * To clear the text area
 *
 * @param event
 */
@FXML
    void Action_Clear(ActionEvent event) {
        Check_append.setDisable(true);
        Check_append.setSelected(false);
        TextArea.clear();
        al.clear();
    }

//    /**
//     * To minimize the parent screen with custom button because parent
//     * is set to UNDECORATED.
//     * @param event 
//     */
//    @FXML
//    void Action_Minimize(ActionEvent event) {
//       Stage stage = (Stage) Base.getScene().getWindow(); 
//       stage.setIconified(true);
//    }
//    
//    /**
//     * Stops the process and ends the execution.
//     * @param event 
//     */
//    @FXML
//    void Action_Quit(ActionEvent event) {
//        Platform.exit();
//        System.exit(0);
//    }
    /**
     * To write the scan results to a file created and written by my TextLibrary
     * Scans for previous event sequence and report error message to complete it
     * before continuing further. Otherwise, creates a new file and writes the
     * scan results.. shows message after successful completion
     *
     * @param event
     */
    @FXML
    void Action_Save(ActionEvent event) {

        if (TextArea.getText().isEmpty()) {
            Error_img.setVisible(true);
            Error.setText("NO SCAN RESULTS TO WRITE...");
        } else if (OutputPath.getText().isEmpty()) {
            Error_img.setVisible(true);
            Error.setText("NO OUTPUT PATH PROVIDED...");
        } else if (OutputFile.getText().isEmpty()) {
            Error_img.setVisible(true);
            Error.setText("PROVIDE FILENAME(without extension) TO WRITE TO...");
        } else if (!OutputFile.getText().matches("[a-zA-Z0-9]+")) {
            Error_img.setVisible(true);
            Error.setText("INAPPROPRIATE FILENAME...");
        } else {
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
     *
     * @param event
     */
    @FXML
    void Action_Scan(ActionEvent event) {
        // checks for input path ...shows error message if not found.
        if (InputPath.getText().isEmpty()) {
            Error_img.setVisible(true);
            Error.setText("CHOOSE INPUT PATH FIRST...");
            return;
        } else {
            Error_img.setVisible(false);
            Error.setText(null);
        }
        // if append is not selected, clears the textArea and the array list.
        if (!Check_append.isSelected()) {
            TextArea.clear();
            al.clear();
        } else {
            TextArea.clear();
        }
        // calls the deep scan method to execute if it is selected 
        // adds a line notifying of the same.
        if (Option_DeepScan.isSelected()) {
            if (Check_append.isSelected()) {
                TextArea.appendText("### Appending DeepScan results for " + InputPath.getText()
                        + " ###\n");
            } else {
                TextArea.appendText("### DeepScan results for " + InputPath.getText()
                        + " ###\n");
            }
            printDirectoryTree(new File(InputPath.getText()));
            fill();
        } // calls the shallow scan method and notifies for the same by adding a 
        // line to the result area.
        else if (Option_ShallowScan.isSelected()) {
            if (Check_append.isSelected()) {
                TextArea.appendText("### Apending ShallowScan results for " + InputPath.getText()
                        + " ###\n");
            } else {
                TextArea.appendText("### ShallowScan results for " + InputPath.getText()
                        + " ###\n");
            }

            Shallow_Scan(new File(InputPath.getText()));
            fill_Shallow();
        }
        Check_append.setDisable(false);
        if (tracking.isSelected()) {
            // reads list of tracked directories
            String alreadyHas[] = DataManager.readList();
            int check = 0;
            // to check if current input dir already tracked or not
            for (String alreadyHa : alreadyHas) {
                if (alreadyHa.equals(InputPath.getText())) {
                    check = 1;
                }
            }
            // if already tracked, do nothing.
            if (check == 1) {
                // {O_o}
            } else {
                // if new, initiate tracking using first scan.
                DataManager.addToList(InputPath.getText());
                Monitor m = new Monitor();
                m.setName("Base Scan");
                m.setCount(al.size());
                m.setCount_added(0);
                m.setCount_removed(0);
                m.setCount_change(0);
                m.setPath(InputPath.getText());
                String scan = "shallow";
                if (Option_DeepScan.isSelected()) {
                    scan = "deep";
                }
                m.setScanType(scan);
                m.setTimeStamp(DateManip.getCurrentDT("all"));
                m.setfList(al.toArray(new String[al.size()]));
                m.setfList_added(null);
                m.setfList_removed(null);
                DataManager.writeObj(m);
                Error.setText("Tracking added!!!");
                tracking.setSelected(false);
                tracking.setDisable(true);
                track_status.setVisible(false);
            }
        }
    }

    @FXML
    void onMode(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MonitorMode.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.centerOnScreen();
        stage.setTitle("github.com/Amanpreet07");
//        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();

    }

     @FXML
    void action_track(ActionEvent event) {
        if(tracking.isSelected()){
            track_status.setVisible(true);
            track_status.setText("Scan once to initiate tracking.");
        }else{
            track_status.setVisible(false);
        }
    }
    
    /**
     * reads the list of all files and folders within provided path. NOTE : It
     * does not scan the sub folders. To be used for directories with lot of sub
     * folders like Games, Softwares, OS directories etc.
     *
     * @param file
     */
    private void Shallow_Scan(File file) {
        File flist[] = file.listFiles();
        String slist[] = new String[flist.length];
        int count = 0;
        for (String slist1 : slist) {
            slist1 = "|--" + flist[count++].getName() + "\n";
            al.add(slist1);
        }

    }

//    private FileFilter getFilter(){
//        
//        if(filter_all.isSelected()){
//            return new AllFileFilter();
//        }else if(filter_audio.isSelected()){
//            return new AudioFileFilter();
//        }else if(filter_doc.isSelected()){
//            return new DocFileFilter();
//        }else if(filter_image.isSelected()){
//            return new ImageFileFilter();
//        }else if(filter_video.isSelected()){
//            return new VideoFileFilter();
//        }
//        return new AllFileFilter();
//    }
    /**
     * writes the result of shallow scan to result area.
     */
    private void fill_Shallow() {
        al.stream().forEach((al1) -> {
            TextArea.appendText(al1);
        });
    }

    @Override
        public void initialize(URL url, ResourceBundle rb) {

        Check_append.setDisable(true);
        // event for click on textfield for ouput path
        // more efficient way to use this instead of a method.
        OutputPath.setOnMouseClicked(e -> {
            Stage stage = (Stage) Base.getScene().getWindow();
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Choose root folder");
            File dir = dc.showDialog(stage);
            if (dir != null) {
                OutputPath.setText(dir.getAbsolutePath());
                Error_img.setVisible(false);
                Error.setText(null);
            } else {
                if (OutputPath.getText().isEmpty()) {
                    Error_img.setVisible(true);
                    Error.setText("CHOOSE PROPER OUTPUT FOLDER...");
                }
            }
        });

         // initialize the css for textFields.
//        InputPath.setStyle("-fx-text-inner-color: white;-fx-background-color: #3d5afe");
//        OutputPath.setStyle("-fx-text-inner-color: white;-fx-background-color: #3d5afe");
//        OutputFile.setStyle("-fx-text-inner-color: white;-fx-background-color: #3d5afe");
    }

    // Array list to hold scan results.
    private static ArrayList<String> al = new ArrayList<>();

    /**
     * Method to scan directory and sub folders. Taken from StackOverflow.
     *
     * @param folder
     * @return
     */
    public String printDirectoryTree(File folder) {
        int indent = 0;
        StringBuilder sb = new StringBuilder();
        printDirectoryTree(folder, indent, sb);
        al.add(sb.toString());
        return sb.toString();
    }

    /**
     * method for deep scan. Taken from StackOverflow.
     *
     * @param folder
     * @param indent
     * @param sb
     */
    private void printDirectoryTree(File folder, int indent,
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
     * methods to add symbol for directory representation in result area. Taken
     * from StackOverflow
     *
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
     *
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
