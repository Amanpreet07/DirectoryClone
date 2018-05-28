package filters;

import java.io.File;
import java.io.FileFilter;

public class DocFileFilter implements FileFilter{

    private final String[] okFileExtensions = new String[]{".pdf", ".xlsx", ".txt",".docx",
        ".ppt",".xls"};

    public boolean accept(File file) {
        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;

    }
}
