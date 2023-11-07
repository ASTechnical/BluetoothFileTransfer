package com.example.bluetoothfiletransfer.modelclasses;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FileList {
    public List<FileWrapper> directories;
    public List<FileWrapper> files;

    private FileList() {
    }

    public List<String> getDirectoriesString() {
        ArrayList arrayList = new ArrayList();
        for (FileWrapper fileWrapper : this.directories) {
            arrayList.add(fileWrapper.toString());
        }
        return arrayList;
    }

    public static FileList newInstance(String str) {
        FileList fileList = new FileList();
        Log.i("FileList", str);
        fileList.directories = Arrays.asList(FileWrapper.wraps(new File(str).listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        })));
        fileList.files = Arrays.asList(FileWrapper.wraps(new File(str).listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        })));
        return fileList;
    }

    public static class FileWrapper {
        private File mFile;

        public String getFilePath() {
            return this.mFile.getAbsolutePath();
        }

        public String getFileSize() {
            return FileList.getStringSizeLengthFile(this.mFile.length());
        }

        public String getFileName() {
            return this.mFile.getName();
        }

        public boolean isFileExist(String str) {
            Iterator<SelectedItems> it = SelectedItemsArray.getAllSelectedItems().iterator();
            while (it.hasNext()) {
                if (it.next().getImgPath().trim().equals(str.trim())) {
                    return true;
                }
            }
            return false;
        }

        public FileWrapper(File file) {
            this.mFile = file;
        }

        public boolean isDirectory() {
            return this.mFile.isDirectory();
        }

        public boolean isFile() {
            return this.mFile.isFile();
        }

        public String toString() {
            return this.mFile.getName();
        }

        public static FileWrapper[] wraps(File[] fileArr) {
            FileWrapper[] fileWrapperArr = new FileWrapper[fileArr.length];
            for (int i = 0; i < fileArr.length; i++) {
                fileWrapperArr[i] = new FileWrapper(fileArr[i]);
            }
            return fileWrapperArr;
        }
    }

    public static String getStringSizeLengthFile(long j) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        DecimalFormat decimalFormat2 = new DecimalFormat("0");
        float f = (float) j;
        if (f < 1048576.0f) {
            return decimalFormat2.format((double) (f / 1024.0f)) + " Kb";
        }
        if (f < 1.07374182E9f) {
            return decimalFormat.format((double) (f / 1048576.0f)) + " Mb";
        }
        return f < 1.09951163E12f ? decimalFormat.format((double) (f / 1.07374182E9f)) + " Gb" : "";
    }
}
