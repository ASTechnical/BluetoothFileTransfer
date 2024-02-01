package com.example.bluetoothfiletransfer.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bluetoothfiletransfer.modelclasses.AllItemModelClass;

import java.util.ArrayList;

public class PicturesViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<AllItemModelClass>> imagesLiveData = new MutableLiveData<>();

    public PicturesViewModel(Application application) {
        super(application);
    }

    public LiveData<ArrayList<AllItemModelClass>> getImagesLiveData() {
        return imagesLiveData;
    }

    public void setImagesData(ArrayList<AllItemModelClass> imagesData) {
        imagesLiveData.setValue(imagesData);
    }
}
