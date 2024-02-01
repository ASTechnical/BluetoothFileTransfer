package com.example.bluetoothfiletransfer.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bluetoothfiletransfer.modelclasses.AllItemModelClass;

import java.util.ArrayList;
import java.util.List;

public class VideosViewModel extends ViewModel {
    private MutableLiveData<ArrayList<AllItemModelClass>> videosLiveData;

    public LiveData<ArrayList<AllItemModelClass>> getVideosLiveData() {
        if (videosLiveData == null) {
            videosLiveData = new MutableLiveData<>();
        }
        return videosLiveData;
    }

    public void setVideoData(List<AllItemModelClass> videosList) {
        videosLiveData.setValue(new ArrayList<>(videosList));
    }
}
