package directoryclone;

import java.io.File;
import java.util.ArrayList;
import readlib.Reader;
import setup.SetupManager;
import writelib.Writer;

public class DataManager {
    private static final String path = setup.SetupManager.getDir("documents")+"\\dclone";

    public static boolean verify(){
        File f = new File(path);
        File f2 = new File(path+"\\list.dcl");
        File f3 = new File(path+"\\counter.dcl");
        if(f.exists()){
            return f2.exists() && f3.exists();
        }else{
            return false;
        }
    }
    
    public static void setupDir(){
        SetupManager.createFolder("dclone", SetupManager.getDir("documents"));
        SetupManager.createFile("list.dcl", path);
        SetupManager.createFile("counter.dcl", path);
        Writer.writeData_Single("counter.dcl", path, "100", false, Writer.ENCRYPT);
    }
    
    public static int getCounter(){
        String temp[] = Reader.readAll("counter.dcl", path, Reader.ENCRYPTED);
        return Integer.parseInt(temp[0]);
    }
    
    // provide old value as a param to avoid exra reading of file for value.
    public static void updateCounter(int old){
        Writer.overwrite_Line("counter.dcl", path, String.valueOf(old),
                String.valueOf(old+1), Writer.ENCRYPT, Writer.UPDATE);
    }
    
    public static String getPath() {
        return path;
    }

    public static String[] readList(){
        String val[] = Reader.readAll("list.dcl", getPath(), Reader.ENCRYPTED);
        String list[] = new String[val.length];
        String temp[] = null;
        // seperating values and counters
        for(int i = 0; i < val.length; i++){
            temp = val[i].split("##@##");
            list[i] = temp[1];
        }
        return list;
    }
    
    public static int findCounter(String val){
        int counter = 0;
        String temp[] = null;
        String list[] = Reader.readAll("list.dcl", getPath(), Reader.ENCRYPTED);
        for (String list1 : list) {
            if(list1.contains(val)){
                temp = list1.split("##@##");
                counter = Integer.parseInt(temp[0]);
            }
        }
        return counter;
    }
    
    public static void addToList(String val){
        // add counter value before actual value : seperator ##@##
        String sep = "##@##";
        String counter = String.valueOf(getCounter());
        updateCounter(Integer.parseInt(counter));
        Writer.writeData_Single("list.dcl", path, counter+sep+val,
                true, Reader.ENCRYPTED);
    }
    
    public static void writeObj(Monitor m){
        String fname = String.valueOf(findCounter(m.getPath())) + ".dcl";
        SetupManager.createFile(fname, path);
        File f = new File(getPath()+"\\"+fname);
        new ObjRW<Monitor>().write(f, m);
    }
    
    public static Monitor[] readObj(int counter){
       File f = new File(getPath()+"\\"+String.valueOf(counter)+".dcl");
       ArrayList<Monitor>  ar = new ObjRW<Monitor>().read(f);
       return ar.toArray(new Monitor[ar.size()]);
    }
    
    public static void updateObj(int index, Monitor m2){
        File f = new File(getPath()+"\\data.dcl");
        // find index
        new ObjRW<Monitor>().remove(f, index);
        new ObjRW<Monitor>().write(f, m2);
    }
    
}
