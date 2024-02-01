package com.example.bluetoothfiletransfer.interfaces;

import com.example.bluetoothfiletransfer.modelclasses.FileList;
import com.example.bluetoothfiletransfer.modelclasses.FilesDetail;

public interface Callback {
    void onDirectoryClick(FilesDetail filesDetail, String storageType);

    void onFileClick(FileList.FileWrapper fileWrapper);
}
