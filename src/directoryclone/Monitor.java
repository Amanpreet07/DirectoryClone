
package directoryclone;

import java.io.Serializable;

public class Monitor implements Serializable{
    
    private String name;
    private String path;
    private String timeStamp;
    private String scanType;
    private String fList[];
    private String fList_added[];
    private String fList_removed[];
    private int count;
    private int count_change;
    private int count_added;
    private int count_removed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getfList_added() {
        return fList_added;
    }

    public void setfList_added(String[] fList_added) {
        this.fList_added = fList_added;
    }

    public String[] getfList_removed() {
        return fList_removed;
    }

    public void setfList_removed(String[] fList_removed) {
        this.fList_removed = fList_removed;
    }

    public int getCount_change() {
        return count_change;
    }

    public void setCount_change(int count_change) {
        this.count_change = count_change;
    }

    public int getCount_added() {
        return count_added;
    }

    public void setCount_added(int count_added) {
        this.count_added = count_added;
    }

    public int getCount_removed() {
        return count_removed;
    }

    public void setCount_removed(int count_removed) {
        this.count_removed = count_removed;
    }
    
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
