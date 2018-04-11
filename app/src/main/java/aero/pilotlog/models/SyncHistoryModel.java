package aero.pilotlog.models;

/**
 * Created by tuan.pv on 12/28/2015.
 */
public class SyncHistoryModel {
    private int mTypeHistoryFile;
    private String fileNameDisplay;
    private String fileNameUpload;
    private String realFile;
    private String timestamp;
    private boolean isSelected;

    public SyncHistoryModel(int pTypeHistoryFile, String fileNameDisplay, String fileNameUpload, String realFile, String timestamp) {
        this.mTypeHistoryFile = pTypeHistoryFile;
        this.fileNameDisplay = fileNameDisplay;
        this.fileNameUpload = fileNameUpload;
        this.realFile = realFile;
        this.timestamp = timestamp;
        this.isSelected = false;
    }

    public int getTypeHistoryFile() {
        return mTypeHistoryFile;
    }

    public void setTypeHistoryFile(int mTypeHistoryFile) {
        this.mTypeHistoryFile = mTypeHistoryFile;
    }

    public String getFileNameDisplay() {
        return fileNameDisplay;
    }

    public void setFileNameDisplay(String fileNameDisplay) {
        this.fileNameDisplay = fileNameDisplay;
    }

    public String getFileNameUpload() {
        return fileNameUpload;
    }

    public void setFileNameUpload(String fileNameUpload) {
        this.fileNameUpload = fileNameUpload;
    }

    public String getRealFile() {
        return realFile;
    }

    public void setRealFile(String realFile) {
        this.realFile = realFile;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
