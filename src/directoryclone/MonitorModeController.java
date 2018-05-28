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
        check = false;
        obj = new Monitor[list.getItems().size()];
        obj = DataManager.readObj();
    }

    private Monitor findObj(String val) {
        for (Monitor obj1 : obj) {
            if (obj1.getPath().equals(val)) {
                return obj1;
            }
        }
        return null;
    }

    private Monitor obj[];

    @FXML
    private ComboBox<String> list;

    @FXML
    private Label old_time;

    @FXML
    private JFXListView<String> added;

    @FXML
    private JFXListView<String> removed;

    @FXML
    private Label fileCount;
    
    private boolean check;
    
    private int findIndex(Monitor m){
        int index = 0;
        for (Monitor obj1 : obj) {
            if(obj1==m){
               
                return index;  
            }else{
                index++;
            }
        }
        return index;
    }
    
    @FXML
    void onUpdate(ActionEvent event) {
        if(check){
        Monitor obj1 = findObj(list.getValue());
        Monitor m = new Monitor();
            m.setCount(al.size());
            m.setPath(obj1.getPath());
            m.setScanType(obj1.getScanType());
            m.setTimeStamp(DateManip.getCurrentDT("all"));
            m.setfList(al.toArray(new String[al.size()]));
            // update object
           // System.out.println(findIndex(obj1));
            DataManager.updateObj(findIndex(obj1), m);
        }
    }

    @FXML
    void onScan(ActionEvent event) {
        // scan first
        al.clear();
        added.getItems().clear();
        removed.getItems().clear();
        String val = list.getValue();
        if (val == null) {
            return;
        }
        Monitor obj1 = findObj(list.getValue());
        if(obj1.getScanType().equals("shallow")){
            Shallow_Scan(new File(obj1.getPath()));
        }
        else{
            printDirectoryTree(new File(obj1.getPath()));
        }
       
        // output 
        old_time.setText(obj1.getTimeStamp());
        fileCount.setText(String.valueOf(Math.abs(obj1.getCount()-al.size())));
        fillLists(obj1);
        check = true;
    }

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
