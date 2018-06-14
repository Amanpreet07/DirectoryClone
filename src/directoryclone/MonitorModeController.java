package directoryclone;

import com.jfoenix.controls.JFXListView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import setup.SetupManager;
import writelib.Writer;

public class MonitorModeController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        d_list.getItems().addAll(DataManager.readList());
    }

    // object to hold derseralised scans
    private Monitor obj[];

    // object to hold base object of current directory
    private Monitor base;

    // object to hold added files for update scan
    private String listAdded[];

    // object to hold removed files for update scan
    private String listRemoved[];

    // combobox for tracked directories
    @FXML
    private ComboBox<String> d_list;

    // combobox for scan sessions
    @FXML
    private ComboBox<String> sessions;

    // list view to show added files
    @FXML
    private JFXListView<String> added;

    // list view to show removed files
    @FXML
    private JFXListView<String> removed;

    // for info/warning/update
    @FXML
    private Label info;

    @FXML
    private Label label_removed;

    @FXML
    private Label label_timeStamp;

    @FXML
    private Label label_scanType;

    @FXML
    private Label label_count;

    @FXML
    private Label label_changed;

    @FXML
    private Label label_output;

    @FXML
    private Label label_added;

    @FXML
    private Label label_sessions;

    @FXML
    void onList(ActionEvent event) {
        if(d_list.getValue()==null){
            // to avoid null pointer exception.
            return;
        }
        sessions.getItems().clear();
        String dir = d_list.getValue();
        obj = DataManager.readObj(DataManager.findCounter(dir));

        String session[] = new String[obj.length];
        for (int i = 0; i < obj.length; i++) {
            session[i] = obj[i].getName();
            if (obj[i].getName().equals("Base Scan")) {
                base = obj[i];
            }
        }
        sessions.getItems().addAll(session);
        label_sessions.setText(String.valueOf(obj.length));
    }

    private void clearLabels() {
        label_timeStamp.setText("");
        label_scanType.setText("");
        label_count.setText("");
        label_changed.setText("");
        label_output.setText("Displaying output for ");
        label_added.setText("Added Files");
        label_removed.setText("Removed Files");
        added.getItems().clear();
        removed.getItems().clear();
    }

    @FXML
    void onSession(ActionEvent event) {
        clearLabels();
        // to supress null pointer exception
        if (sessions.getValue() == null) {
            return;
        }
        Monitor m = findSession(sessions.getValue());
        label_timeStamp.setText(m.getTimeStamp());
        label_scanType.setText(m.getScanType());
        if(m.getScanType().equals("shallow")){
        label_count.setText(String.valueOf(m.getCount()));
        }else{
        label_count.setText("N/A");    
        }    
        label_changed.setText(String.valueOf(m.getCount_change()));
        label_output.setText(label_output.getText() + " " + sessions.getValue());
        label_added.setText(label_added.getText() + " : "
                + String.valueOf(m.getCount_added()));
        label_removed.setText(label_removed.getText() + " : "
                + String.valueOf(m.getCount_removed()));
        if (m.getfList_added() == null) {
            added.getItems().add("Nothing added");
        } else {
            added.getItems().addAll(m.getfList_added());
        }
        if (m.getfList_removed() == null) {
            removed.getItems().add("Nothing removed");
        } else {
            removed.getItems().addAll(m.getfList_removed());
        }

    }

    @FXML
    void onDelete(ActionEvent event) {
        if(sessions.getValue()==null){
            info.setText("Removal cancelled. Reason: No session selected.");
            return;
        }
        if(sessions.getValue().equals("Base Scan")){
            info.setText("Cannot remove Base Scan.");
            return;
        }
        clearLabels();
        DataManager.removeObj(sessions.getValue(), 
                DataManager.findCounter(d_list.getValue()));
        info.setText("Session removed. Choose directory again to reflect changes");
        d_list.setValue(null);
        sessions.setValue(null);
        label_sessions.setText(null);
        
    }
    
    @FXML
    void onCurrent(ActionEvent event) {
        al.clear(); 
        added.getItems().clear();
        removed.getItems().clear();
        // stop if no input
        if (d_list.getValue() == null) {
            info.setText("No directory selected");
            return;
        }
        File f = new File(d_list.getValue());
        if (!f.exists()) {
            info.setText("Directory does not exist anymore.");
            return;
        }
        // info
        label_output.setText("Displaying output for");
        label_output.setText(label_output.getText()+" Current Scan");
        // scan
        if (base.getScanType().equals("shallow")) {
            Shallow_Scan(new File(base.getPath()));
        } else {
            printDirectoryTree(new File(base.getPath()));
        }
        // list output
        fillLists(base);
        added.getItems().addAll(listAdded);
        removed.getItems().addAll(listRemoved);
        // labels
        label_timeStamp.setText("CURRENT");
        label_scanType.setText(base.getScanType());
        if(base.getScanType().equals("shallow")){
        label_count.setText(String.valueOf(al.size()));
        }else{
        label_count.setText("N/A");    
        }    
        label_changed.setText(String.valueOf(listAdded.length+listRemoved.length));
        
    }
    
    public Monitor temp;
    
    @FXML
    void onView(ActionEvent event) throws IOException {
        if(sessions.getValue()==null){
            info.setText("No session selected.");
            return;
        }
        temp = findSession(sessions.getValue());
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FileList.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        // to transfer text to new controller
        FileListController fl = loader.getController();
        fl.setValue(temp.getfList());
        stage.setTitle("Complete Scan result");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    void onExport(ActionEvent event) {
        if(sessions.getValue()==null){
            info.setText("No session selected.");
            return;
        }
        temp = findSession(sessions.getValue());
        
        String path = SetupManager.getDir("desktop");
        String fname = temp.getName()+"_output.txt";
        File f = new File(path+"//"+fname);
        if(f.exists()){
            Writer.clearFile(fname, path);
        }
        SetupManager.createFile(fname, path);
        Writer.writeData_Block(fname, path, temp.getfList(),
                true, Writer.PLAIN_TEXT);
        info.setText("Exported to desktop//"+fname);
    }
    
    @FXML
    void onUpdate(ActionEvent event) {
        // Step 1: new Scan
        info.setText(null);
        al.clear(); // to empty the list
//        added.getItems().clear();
//        removed.getItems().clear();

        // stop if no input
        if (d_list.getValue() == null) {
            info.setText("No directory selected");
            return;
        }
        // stop is directory is outdated
        File f = new File(d_list.getValue());
        if (!f.exists()) {
            info.setText("Directory does not exist anymore.");
            return;
        }

        if (base.getScanType().equals("shallow")) {
            Shallow_Scan(new File(base.getPath()));
        } else {
            printDirectoryTree(new File(base.getPath()));
        }
        // output 
        fillLists(base);

        // break operation if changed count is zero
        if (listAdded.length + listRemoved.length == 0) {
            al.clear();
            listAdded = null;
            listRemoved = null;
            info.setText("Update Cancelled. Reason: No changes in the directory!!!");
            return;
        }

        // Step 2: New Object
        Monitor m = new Monitor();
        m.setName("Scan " + DateManip.getCurrentDT("dd-MMM-hh:mm"));
        m.setCount(al.size());
        m.setPath(base.getPath());
        m.setScanType(base.getScanType());
        m.setTimeStamp(DateManip.getCurrentDT("all"));
        m.setfList(al.toArray(new String[al.size()]));
        m.setfList_added(listAdded);
        m.setfList_removed(listRemoved);
        m.setCount_added(listAdded.length);
        m.setCount_removed(listRemoved.length);
        m.setCount_change(listAdded.length + listRemoved.length);
        // add object
        DataManager.writeObj(m);
        info.setText("New Session added successfully. "
                + "Choose directory again to reflect changes");
        d_list.setValue(null);
        sessions.setValue(null);
        label_sessions.setText(null);
        
    }

    // improve complexity
    // compares file lists to determine added and removed
    private void fillLists(Monitor m) {
        // c1 contains base scan's file list
        String temp[] = m.getfList();
        Collection c1 = new ArrayList();
        c1.addAll(Arrays.asList(temp));

        // c2 contains current scan's file list
        Collection c2 = new ArrayList();
        c2.addAll(al);

        // to determine added files
        c2.removeAll(c1);
        listAdded = (String[]) c2.toArray(new String[c2.size()]);

        // c3 contains current scan's file list
        // bcuz old collection has been modified
        Collection c3 = new ArrayList();
        c3.addAll(al);

        // to determine removed files
        c1.removeAll(c3);
        listRemoved = (String[]) c1.toArray(new String[c1.size()]);
    }

    // compares file lists to determine added and removed
    private void compareLists(Monitor m, Monitor m2) {

        String temp[] = m.getfList();
        Collection c1 = new ArrayList();
        c1.addAll(Arrays.asList(temp));

        // c2 contains current scan's file list
        Collection c2 = new ArrayList();
        c2.addAll((Arrays.asList(m2.getfList())));

        // to determine added files
        c2.removeAll(c1);
        listAdded = (String[]) c2.toArray(new String[c2.size()]);

        // c3 contains current scan's file list
        // bcuz old collection has been modified
        Collection c3 = new ArrayList();
        c3.addAll((Arrays.asList(m2.getfList())));

        // to determine removed files
        c1.removeAll(c3);
        listRemoved = (String[]) c1.toArray(new String[c1.size()]);
    }

    // to search and return object with given Name
    private Monitor findSession(String name) {
        Monitor m = null;
        for (Monitor obj1 : obj) {
            if (obj1.getName().equals(name)) {
                m = obj1;
            }
        }
        return m;
    }

    // all methods below are copied from HomeScreenController
    // Purpose: To Scan the directory 
    ArrayList<String> al = new ArrayList<>();

    private void Shallow_Scan(File file) {
        File flist[] = file.listFiles();
        String slist[] = new String[flist.length];
        int count = 0;
        for (String slist1 : slist) {
            slist1 = "|--" + flist[count++].getName() + "\n";
            al.add(slist1);
        }

    }

    private String printDirectoryTree(File folder) {
        int indent = 0;
        StringBuilder sb = new StringBuilder();
        printDirectoryTree(folder, indent, sb);
        al.add(sb.toString());
        return sb.toString();
    }

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

    private static void printFile(File file, int indent, StringBuilder sb) {
        sb.append(getIndentString(indent));
        sb.append("+--");
        sb.append(file.getName());
        sb.append("\n");
    }

    private static String getIndentString(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("|  ");
        }
        return sb.toString();
    }

}
