package filters;

import java.io.File;
import java.io.FileFilter;

public class SafeFileFilter implements FileFilter{

    private final String[] okFileExtensions = new String[]{".","~$"};

    public boolean accept(File file) {
        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().startsWith(extension)) {
                return true;
            }
        }
        return false;

    }
}
