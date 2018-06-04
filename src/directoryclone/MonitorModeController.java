package directoryclone;

import com.jfoenix.controls.JFXListView;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class MonitorModeController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list.getItems().addAll(DataManager.readList());
    }
    
    // object to hold derseralised scans
    private Monitor obj[];

    // object to hold base object of current directory
    private Monitor base;
    
    // combobox for tracked directories
    @FXML
    private ComboBox<String> list;

    // combobox for scan sessions
    @FXML
    private ComboBox<String> sessions;
    
    @FXML
    private JFXListView<String> added;

    @FXML
    private JFXListView<String> removed;
    
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
        sessions.getItems().clear();
        String dir = list.getValue();
        obj = DataManager.readObj(DataManager.findCounter(dir));
        
        String session[] = new String[obj.length];
        for (int i = 0; i < obj.length; i++) {
            session[i] = obj[i].getName();
            if(obj[i].getName().equals("Base Scan")){
                base = obj[i];
            }
        }
        sessions.getItems().addAll(session);
        label_sessions.setText(String.valueOf(obj.length));
    }
    
    private void clearLabels(){
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
        if(sessions.getValue()==null){
            return;
        }
        Monitor m = findSession(sessions.getValue());
        label_timeStamp.setText(m.getTimeStamp());
        label_scanType.setText(m.getScanType());
        label_count.setText(String.valueOf(m.getCount()));
        label_changed.setText(String.valueOf(m.getCount_change()));
        label_output.setText(label_output.getText()+" "+sessions.getValue());
        label_added.setText(label_added.getText()+" : "
                +String.valueOf(m.getCount_added()));
        label_removed.setText(label_removed.getText()+" : "
                +String.valueOf(m.getCount_removed()));
        if(m.getfList_added()==null){
        added.getItems().add("Nothing added");
        }else{
        added.getItems().addAll(m.getfList_added());    
        }
        if(m.getfList_removed()==null){
        removed.getItems().add("Nothing removed");    
        }else{
        removed.getItems().addAll(m.getfList_removed());    
        }
           
    }
    
    @FXML
    void onUpdate(ActionEvent event) {
        // Step 1: new Scan
        // clear fields
        al.clear();
        added.getItems().clear();
        removed.getItems().clear();
        // stop if no input
        if (list.getValue() == null) {
            return;
        }
        if(base.getScanType().equals("shallow")){
            Shallow_Scan(new File(base.getPath()));
        }
        else{
            printDirectoryTree(new File(base.getPath()));
        }
        // output 
        fillLists(base);
        // Step 2: New Object
        Monitor m = new Monitor();
            m.setCount(al.size());
            m.setPath(base.getPath());
            m.setScanType(base.getScanType());
            m.setTimeStamp(DateManip.getCurrentDT("all"));
            m.setfList(al.toArray(new String[al.size()]));
            // update object
           // System.out.println(findIndex(obj1));
           
        
    }

    // improve complexity
    private void fillLists(Monitor m){
        String temp[] = m.getfList();
        Collection c1 = new ArrayList();
        c1.addAll(Arrays.asList(temp));
        
        Collection c2 = new ArrayList();
        c2.addAll(al);
        
        c2.removeAll(c1);
        added.getItems().addAll(c2);
        
        Collection c3 = new ArrayList();
        c3.addAll(al);
        
        c1.removeAll(c3);
        removed.getItems().addAll(c1);
    }
    
    // to search and return object with given Name
    private Monitor findSession(String name){
        Monitor m = null;
        for (Monitor obj1 : obj) {
            if(obj1.getName().equals(name)){
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
