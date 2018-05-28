package directoryclone;

import java.io.File;
import java.util.ArrayList;
import readlib.Reader;
import writelib.Writer;

public class DataManager {
    private static final String path = setup.SetupManager.getDir("documents")+"\\dclone";

    public static String getPath() {
        return path;
    }

    public static String[] readList(){
        String val[] = Reader.readAll("list.dcl", getPath(), Reader.ENCRYPTED);
        return val;
    }
    
    public static void addToList(String val){
        Writer.writeData_Single("list.dcl", path, val, true, Reader.ENCRYPTED);
    }
    
    public static void writeObj(Monitor m){
        File f = new File(getPath()+"\\data.dcl");
        new ObjRW<Monitor>().write(f, m);
    }
    
    public static Monitor[] readObj(){
        File f = new File(getPath()+"\\data.dcl");
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
