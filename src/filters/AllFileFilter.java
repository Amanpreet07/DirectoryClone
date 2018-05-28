package filters;

import java.io.File;
import java.io.FileFilter;

public class AllFileFilter implements FileFilter{

    public boolean accept(File file) {   
        return true;
    }
}
