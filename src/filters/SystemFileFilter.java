package filters;

import java.io.File;
import java.io.FileFilter;

public class SystemFileFilter implements FileFilter{

    private final String[] okFileExtensions = new String[]{".ini", ".tmp"};

    public boolean accept(File file) {
        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;

    }
}
