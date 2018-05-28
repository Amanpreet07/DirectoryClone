package filters;

import java.io.File;
import java.io.FileFilter;

public class VideoFileFilter implements FileFilter{

    private final String[] okFileExtensions = new String[]{".mp4", ".mkv", ".avi",".3gp",".wmv"};

    public boolean accept(File file) {
        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

}
