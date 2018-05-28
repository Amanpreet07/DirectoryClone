package filters;

import java.io.File;
import java.io.FileFilter;

public class AudioFileFilter implements FileFilter{

    private final String[] okFileExtensions = new String[]{".mp3", ".aac", ".ogg",".wma",".wav"};

    public boolean accept(File file) {
        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
