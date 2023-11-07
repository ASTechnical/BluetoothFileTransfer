package com.example.bluetoothfiletransfer;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.bluetoothfiletransfer.adapters.VideoAdapter;
import com.example.bluetoothfiletransfer.modelclasses.AllItemModelClass;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItems;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItemsArray;
import com.example.bluetoothfiletransfer.utils.Constants;
import com.example.bluetoothfiletransfer.utils.RecyclerTouchListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class VideosFragment extends Fragment {
    public static VideoAdapter videoAdapter;
    /* access modifiers changed from: private */
    public static List<AllItemModelClass> videoList = new ArrayList();
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("message");
            int intExtra = intent.getIntExtra("position", -1);
            Log.d("fff", "onReceive: " + intExtra);
            if (stringExtra.equals(Constants.VIDEOS)) {
                if (intExtra != -1) {
                    try {
                        AllItemModelClass allItemModelClass = (AllItemModelClass) VideosFragment.videoList.get(intExtra);
                        if (allItemModelClass.isSelected()) {
                            allItemModelClass.setSelected(false);
                        }
                        VideosFragment.videoAdapter.notifyItemChanged(intExtra);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Iterator<Integer> it = intent.getIntegerArrayListExtra("positionArray").iterator();
                    while (it.hasNext()) {
                        int intValue = it.next().intValue();
                        AllItemModelClass allItemModelClass2 = (AllItemModelClass) VideosFragment.videoList.get(intValue);
                        if (allItemModelClass2.isSelected()) {
                            allItemModelClass2.setSelected(false);
                            VideosFragment.videoAdapter.notifyItemChanged(intValue);
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
        return layoutInflater.inflate(R.layout.fragment_videos, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(this.mMessageReceiver, new IntentFilter("custom-event-name"));
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        new AsyncTaskClass().execute(new String[]{"Videos"});
        this.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), this.recyclerView, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                AllItemModelClass allItemModelClass = (AllItemModelClass) VideosFragment.videoList.get(i);
                if (allItemModelClass.isSelected()) {
                    allItemModelClass.setSelected(false);
                    SelectedItemsArray.removeItem(new SelectedItems(allItemModelClass.getImgPath(), i, Constants.VIDEOS, allItemModelClass.getItemSize()));
                    SelectedItemsArray.minusItemCount();
                    FileShareFragment.tv_itemCount.setText(String.valueOf(SelectedItemsArray.getItemCount()));
                    Log.d("fff", "onClick: position removed: " + i);
                    Log.d("fff", "onClick: " + SelectedItemsArray.getArraySize());
                } else {
                    allItemModelClass.setSelected(true);
                    SelectedItemsArray.addItem(new SelectedItems(allItemModelClass.getImgPath(), i, Constants.VIDEOS, allItemModelClass.getItemSize()));
                    SelectedItemsArray.addItemCount();
                    FileShareFragment.tv_itemCount.setText(String.valueOf(SelectedItemsArray.getItemCount()));
                    Log.d("fff", "onClick: position added: " + i);
                    Log.d("fff", "onClick: " + SelectedItemsArray.getArraySize());
                }
                VideosFragment.videoAdapter.notifyItemChanged(i);
            }
        }));
    }

    public void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.mMessageReceiver);
        } catch (Exception e) {
            Log.e("UnRegister Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    public static ArrayList<AllItemModelClass> getVideoData(Context context) {
        ArrayList<AllItemModelClass> arrayList = new ArrayList<>();
        Cursor query = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "bucket_display_name"}, (String) null, (String[]) null, "datetaken DESC");
        int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
        query.getColumnIndexOrThrow("bucket_display_name");
        while (query.moveToNext()) {
            String string = query.getString(columnIndexOrThrow);
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
            if (VideosFragment.this.progressBar.getVisibility() == 8 && VideosFragment.this.progressBar.isShown()) {
                VideosFragment.this.progressBar.setVisibility(0);
            }
        }

        /* access modifiers changed from: protected */
        public ArrayList<AllItemModelClass> doInBackground(String... strArr) {
            if (strArr[0].equals("Videos")) {
                return VideosFragment.getVideoData(VideosFragment.this.getContext());
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(ArrayList<AllItemModelClass> arrayList) {
            super.onPostExecute(arrayList);
            if (arrayList != null) {
                VideosFragment.this.updateView(arrayList);
            }
        }
    }

    /* access modifiers changed from: private */
    @SuppressLint("WrongConstant")
    public void updateView(ArrayList<AllItemModelClass> arrayList) {
        videoList = arrayList;
        if (this.progressBar.getVisibility() == 0) {
            this.progressBar.setVisibility(8);
        }
        VideoAdapter videoAdapter2 = new VideoAdapter(getContext(), arrayList);
        videoAdapter = videoAdapter2;
        this.recyclerView.setAdapter(videoAdapter2);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, 1, false));
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setItemViewCacheSize(20);
        this.recyclerView.setDrawingCacheEnabled(true);
        this.recyclerView.setDrawingCacheQuality(1048576);
    }
}