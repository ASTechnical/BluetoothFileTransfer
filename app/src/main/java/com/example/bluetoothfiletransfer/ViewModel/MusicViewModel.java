package com.example.bluetoothfiletransfer.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bluetoothfiletransfer.modelclasses.AllItemModelClass;

import java.util.ArrayList;
import java.util.List;

public class MusicViewModel extends ViewModel {
    private MutableLiveData<ArrayList<AllItemModelClass>> musicLiveData;

    public LiveData<ArrayList<AllItemModelClass>> getMusicLiveData() {
        if (musicLiveData == null) {
            musicLiveData = new MutableLiveData<>();
        }
        return musicLiveData;
    }

    public void setMusicData(List<AllItemModelClass> musicList) {
        musicLiveData.setValue(new ArrayList<>(musicList));
    }
}
