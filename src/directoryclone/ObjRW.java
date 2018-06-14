package directoryclone;
// Purpose: To manage object serialisation and deserialisation

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ObjRW<T> {
    
    public boolean write(File f, T t) {
        boolean ok = true;
        if (read(f) == null) {
          ok = writeFirst(f, t);
        } else {
          ok = writeAnother(f, t);
        }
        return ok;
    }

    private boolean writeFirst(File f, T t) {
        boolean ok = true;
        ArrayList<T> arr = new ArrayList<>();
        arr.add(t);
        try (FileOutputStream fs = new FileOutputStream(f);) {
            ObjectOutputStream ob = new ObjectOutputStream(fs);
            ob.writeObject(arr);
            ob.close();
        } catch (FileNotFoundException ex) {
            ok = true;
        } catch (IOException ex) {
            ok = true;
        }
        return ok;
    }

    private boolean writeAnother(File f, T t) {
        boolean ok = true;
        ArrayList<T> arr = new ArrayList<>();
        // read previous data
        arr = read(f);
        // add new data   
        arr.add(t);
        // write again
        try (FileOutputStream fs = new FileOutputStream(f);) {
            ObjectOutputStream ob = new ObjectOutputStream(fs);
            ob.writeObject(arr);
            ob.close();
        } catch (FileNotFoundException ex) {
            ok = false;
        } catch (IOException ex) {
            ok = false;
        }
        return ok;
    }

    public ArrayList<T> read(File f) {
        ArrayList<T> temp = null;
        try (FileInputStream fs = new FileInputStream(f);) {
            ObjectInputStream ob = new ObjectInputStream(fs);
            temp = (ArrayList<T>) ob.readObject();
            ob.close();
        } catch (FileNotFoundException ex) {
            return null;
        } catch (EOFException ex) {
            return null;
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }
        return temp;
    }
                  
}
