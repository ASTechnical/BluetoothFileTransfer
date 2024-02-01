package com.example.bluetoothfiletransfer.modelclasses;

public class FilesDetail {
    String fileName;
    String filePath = this.filePath;
    boolean isDirectory;
    boolean isFiles;


    public FilesDetail(boolean isFiles, boolean isDirectory, String fileName) {
        this.isFiles = isFiles;
        this.isDirectory = isDirectory;
        this.fileName = fileName;

    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public boolean isFiles() {
        return this.isFiles;
    }

    public void setFiles(boolean z) {
        this.isFiles = z;
    }

    public boolean isDirectory() {
        return this.isDirectory;
    }

    public void setDirectory(boolean z) {
        this.isDirectory = z;
    }


}
