package com.example.bluetoothfiletransfer.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.example.bluetoothfiletransfer.Fragments.AppsFragment;
import com.example.bluetoothfiletransfer.Fragments.FilesFragment;
import com.example.bluetoothfiletransfer.Fragments.MusicFragment;
import com.example.bluetoothfiletransfer.Fragments.PicturesFragment;
import com.example.bluetoothfiletransfer.Fragments.VideosFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter
{
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
