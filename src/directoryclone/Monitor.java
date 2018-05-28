
package directoryclone;

import java.io.Serializable;

public class Monitor implements Serializable{
    
    private String path;
    private String timeStamp;
    private String fList[];
    private String scanType;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Monitor() {
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Monitor(String path, String[] fList, String scanType) {
       
        this.path = path;
        this.fList = fList;
        this.scanType = scanType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getfList() {
        return fList;
    }

    public void setfList(String[] fList) {
        this.fList = fList;
    }

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }
    
}
