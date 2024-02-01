package com.example.bluetoothfiletransfer.modelclasses;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class FileList {
    public List<FileWrapper> directories;
    public List<FileWrapper> files;

    public FileList() {
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
        Log.i("FileList", "Directory path: " + str);

        File[] directories = new File(str).listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        File[] files = new File(str).listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });

        if (directories == null) {
            directories = new File[0];
        }

        if (files == null) {
            files = new File[0];
        }


        Log.i("FileList", "Number of directories: " + directories.length);
        Log.i("FileList", "Number of files: " + files.length);

        fileList.directories = Arrays.asList(FileWrapper.wraps(directories));
        fileList.files = Arrays.asList(FileWrapper.wraps(files));

        return fileList;
    }


    public static class FileWrapper {
        private final File mFile;
        private List<FileWrapper> files;

        public List<FileWrapper> getFiles() {
            if (isDirectory()) {
                // Logic to retrieve files within this directory
                File[] filesArray = mFile.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        return file.isFile();
                    }
                });

                if (filesArray != null) {
                    return Arrays.asList(wraps(filesArray));
                }
            }
            return null; // Return null if not a directory or no files
        }
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
            for (SelectedItems selectedItems : SelectedItemsArray.getAllSelectedItems())
            {
                if (selectedItems.getImgPath().trim().equals(str.trim()))
                {
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
        boolean isSelected;
        public boolean isSelected() {
            return this.isSelected;
        }

        public void setSelected(boolean z) {
            this.isSelected = z;
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
            return decimalFormat2.format(f / 1024.0f) + " Kb";
        }
        if (f < 1.07374182E9f) {
            return decimalFormat.format(f / 1048576.0f) + " Mb";
        }
        return f < 1.09951163E12f ? decimalFormat.format(f / 1.07374182E9f) + " Gb" : "";
    }

}
