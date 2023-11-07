package com.example.bluetoothfiletransfer.adapters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.bluetoothfiletransfer.AppsFragment;
import com.example.bluetoothfiletransfer.FilesFragment;
import com.example.bluetoothfiletransfer.MusicFragment;
import com.example.bluetoothfiletransfer.PicturesFragment;
import com.example.bluetoothfiletransfer.VideosFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public int getCount() {
        return 5;
    }

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public Fragment getItem(int i) {
        if (i == 0) {

            return new AppsFragment();
        }
        if (i == 1) {
            return new PicturesFragment();
        }
        if (i == 2) {
            return new VideosFragment();
        }
        if (i == 3) {
            return new MusicFragment();
        }
        if (i != 4) {
            return null;
        }
        return new FilesFragment();
    }
}
