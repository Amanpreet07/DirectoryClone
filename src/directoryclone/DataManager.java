package directoryclone;

import java.io.File;
import java.util.ArrayList;
import readlib.Reader;
import setup.SetupManager;
import writelib.Writer;

public class DataManager {
    private static final String path = setup.SetupManager.getDir("documents")+"\\dclone";

    // to verify the existance of core files
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
    
    // to create core files 
    public static void setupDir(){
        SetupManager.createFolder("dclone", SetupManager.getDir("documents"));
        SetupManager.createFile("list.dcl", path);
        SetupManager.createFile("counter.dcl", path);
        Writer.writeData_Single("counter.dcl", path, "100", false, Writer.ENCRYPT);
    }
    
    // read counter value from the file
    public static int getCounter(){
        String temp[] = Reader.readAll("counter.dcl", path, Reader.ENCRYPTED);
        return Integer.parseInt(temp[0]);
    }
    
    // provide old value as a param to avoid exra reading of file for value.
    public static void updateCounter(int old){
        Writer.overwrite_Line("counter.dcl", path, String.valueOf(old),
                String.valueOf(old+1), Writer.ENCRYPT, Writer.UPDATE);
    }
    
    // returns path of core files
    public static String getPath() {
        return path;
    }

    // to read the file for tracked directories
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
    
    // to identify counter embeded at the begining of tracked list
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
    
    // add counter value before actual value : seperator ##@##
    public static void addToList(String val){
        String sep = "##@##";
        String counter = String.valueOf(getCounter());
        updateCounter(Integer.parseInt(counter));
        Writer.writeData_Single("list.dcl", path, counter+sep+val,
                true, Reader.ENCRYPTED);
    }
    
    // to write ser object into target file
    public static void writeObj(Monitor m){
        String fname = String.valueOf(findCounter(m.getPath())) + ".dcl";
        SetupManager.createFile(fname, path);
        File f = new File(getPath()+"\\"+fname);
        new ObjRW<Monitor>().write(f, m);
    }
    
    // read data from target file. deseralised objects
    public static Monitor[] readObj(int counter){
       File f = new File(getPath()+"\\"+String.valueOf(counter)+".dcl");
       ArrayList<Monitor>  ar = new ObjRW<Monitor>().read(f);
       return ar.toArray(new Monitor[ar.size()]);
    }
    
    // add another session into target file
    public static void updateObj(Monitor m2, int counter){
        File f = new File(getPath()+"\\"+String.valueOf(counter)+".dcl");
        new ObjRW<Monitor>().write(f, m2);
    }
    
    // remove session from the target file
    public static void removeObj(String s_name, int counter){
        ObjRW<Monitor> ob = new ObjRW<>();    
        File f = new File(getPath()+"\\"+String.valueOf(counter)+".dcl");
        // read file
        ArrayList<Monitor>  ar = new ObjRW<Monitor>().read(f);
        // clear old file
        Writer.clearFile(String.valueOf(counter)+".dcl", getPath());
        // remove target object
        for (Monitor ar1 : ar) {
            if(ar1.getName().equals(s_name)){
                // do nothing.
                continue;
            }else{
                // write back
                ob.write(f, ar1);
            }
        }
        
    }
}
