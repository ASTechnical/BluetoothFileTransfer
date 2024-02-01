package com.example.bluetoothfiletransfer.Fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.bluetoothfiletransfer.R;
import com.example.bluetoothfiletransfer.ViewModel.MusicViewModel;
import com.example.bluetoothfiletransfer.adapters.MusicAdapter;
import com.example.bluetoothfiletransfer.modelclasses.AllItemModelClass;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItems;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItemsArray;
import com.example.bluetoothfiletransfer.utils.Constants;
import com.example.bluetoothfiletransfer.utils.RecyclerTouchListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MusicFragment extends Fragment {
    public static MusicAdapter musicAdapter;
    private MusicViewModel musicViewModel;
    public static List<AllItemModelClass> musicList = new ArrayList();
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("message");
            int intExtra = intent.getIntExtra("position", -1);
            Log.d("fff", "onReceive: " + intExtra);
            if (stringExtra.equals(Constants.MUSIC)) {
                if (intExtra != -1) {
                    try {
                        AllItemModelClass allItemModelClass = (AllItemModelClass) MusicFragment.musicList.get(intExtra);
                        if (allItemModelClass.isSelected()) {
                            allItemModelClass.setSelected(false);
                        }
                        MusicFragment.musicAdapter.notifyItemChanged(intExtra);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    for (Integer integer : intent.getIntegerArrayListExtra("positionArray"))
                    {
                        int intValue = integer;
                        AllItemModelClass allItemModelClass2 = (AllItemModelClass) MusicFragment.musicList.get(intValue);
                        if (allItemModelClass2.isSelected())
                        {
                            allItemModelClass2.setSelected(false);
                            MusicFragment.musicAdapter.notifyItemChanged(intValue);
                        }
                    }
                }
            }
            Log.d("receiver", "Got position: " + intExtra);
        }
    };
    ProgressBar progressBar;
    RecyclerView recyclerView;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        return layoutInflater.inflate(R.layout.fragment_music, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);

        musicViewModel = new ViewModelProvider(this).get(MusicViewModel.class);

        // Observe LiveData changes
        musicViewModel.getMusicLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<AllItemModelClass>>() {
            @Override
            public void onChanged(ArrayList<AllItemModelClass> musicList) {
                updateView(musicList);
            }
        });
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(this.mMessageReceiver, new IntentFilter("custom-event-name"));
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        new AsyncTaskClass().execute(new String[]{"Audio"});
        this.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), this.recyclerView, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                AllItemModelClass allItemModelClass = (AllItemModelClass) MusicFragment.musicList.get(i);
                if (allItemModelClass.isSelected()) {
                    allItemModelClass.setSelected(false);
                    SelectedItemsArray.removeItem(new SelectedItems(allItemModelClass.getImgPath(), i, Constants.MUSIC, allItemModelClass.getItemSize()));
                    SelectedItemsArray.minusItemCount();
                   // FileShareFragment.main_bottom_id.setVisibility(View.GONE);
                    FileShareFragment.tv_itemCount.setText(String.valueOf(SelectedItemsArray.getItemCount()));
                    Log.d("fff", "onClick: position removed: " + i);
                    Log.d("fff", "onClick: " + SelectedItemsArray.getArraySize());
                } else {
                    allItemModelClass.setSelected(true);
                    SelectedItemsArray.addItem(new SelectedItems(allItemModelClass.getImgPath(), i, Constants.MUSIC, allItemModelClass.getItemSize()));
                    SelectedItemsArray.addItemCount();
                    FileShareFragment.main_bottom_id.setVisibility(View.VISIBLE);
                    FileShareFragment.tv_itemCount.setText(String.valueOf(SelectedItemsArray.getItemCount()));
                    Log.d("fff", "onClick: position added: " + i);
                    Log.d("fff", "onClick: " + SelectedItemsArray.getArraySize());
                }
                MusicFragment.musicAdapter.notifyItemChanged(i);
            }
        }));
    }

    public static ArrayList<AllItemModelClass> getAudioPath(Context context) {
        ArrayList<AllItemModelClass> arrayList = new ArrayList<>();
        Cursor query = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "title"}, (String) null, (String[]) null, "date_modified DESC");
        int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
        while (query.moveToNext()) {
            String string = query.getString(columnIndexOrThrow);
            Log.d("fff", "getAudioPath: Audios : " + query.getString(columnIndexOrThrow));
            arrayList.add(new AllItemModelClass(string, Constants.getFileSize(string), false, getFileName(string)));
        }
        return arrayList;
    }

    private static String getFileName(String str) {
        return new File(str).getName();
    }

    public class AsyncTaskClass extends AsyncTask<String, String, ArrayList<AllItemModelClass>> {
        public AsyncTaskClass() {
        }

        /* access modifiers changed from: protected */
        @SuppressLint("WrongConstant")
        public void onPreExecute() {
            super.onPreExecute();
            if (MusicFragment.this.progressBar.getVisibility() == 8 && MusicFragment.this.progressBar.isShown()) {
                MusicFragment.this.progressBar.setVisibility(0);
            }
        }

        /* access modifiers changed from: protected */
        public ArrayList<AllItemModelClass> doInBackground(String... strArr) {
            if (strArr[0].equals("Audio")) {
                return MusicFragment.getAudioPath(MusicFragment.this.getContext());
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(ArrayList<AllItemModelClass> arrayList) {
            super.onPostExecute(arrayList);
            if (arrayList != null) {
                musicViewModel.setMusicData(arrayList);
                MusicFragment.this.updateView(arrayList);
            }
        }
    }

    /* access modifiers changed from: private */
    @SuppressLint("WrongConstant")
    public void updateView(ArrayList<AllItemModelClass> arrayList) {
        musicList = arrayList;
        if (this.progressBar.getVisibility() == 0) {
            this.progressBar.setVisibility(8);
        }
        MusicAdapter musicAdapter2 = new MusicAdapter(getContext(), arrayList);
        musicAdapter = musicAdapter2;
        this.recyclerView.setAdapter(musicAdapter2);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, 1, false));
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setItemViewCacheSize(20);
        this.recyclerView.setDrawingCacheEnabled(true);
        this.recyclerView.setDrawingCacheQuality(1048576);
    }

    public void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.mMessageReceiver);
        } catch (Exception e) {
            Log.e("UnRegister Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }
}