package com.example.bluetoothfiletransfer.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bluetoothfiletransfer.modelclasses.AppsModelClass;

import java.util.ArrayList;

public class AppsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<AppsModelClass>> appsLiveData = new MutableLiveData<>();

    public LiveData<ArrayList<AppsModelClass>> getAppsLiveData() {
        return appsLiveData;
    }

    public void setAppsData(ArrayList<AppsModelClass> appsList) {
        appsLiveData.setValue(appsList);
    }
}
