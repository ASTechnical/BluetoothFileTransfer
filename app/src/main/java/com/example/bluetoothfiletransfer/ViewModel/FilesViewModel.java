package com.example.bluetoothfiletransfer.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bluetoothfiletransfer.modelclasses.FileList;

public class FilesViewModel extends ViewModel {
    private MutableLiveData<FileList> fileListLiveData;

    public LiveData<FileList> getFileListLiveData() {
        if (fileListLiveData == null) {
            fileListLiveData = new MutableLiveData<>();
        }
        return fileListLiveData;
    }

    public void setFileList(FileList fileList) {
        fileListLiveData.setValue(fileList);
    }
}

