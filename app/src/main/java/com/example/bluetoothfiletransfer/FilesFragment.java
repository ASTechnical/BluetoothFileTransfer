package com.example.bluetoothfiletransfer;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetoothfiletransfer.adapters.FileManagerAdapter;
import com.example.bluetoothfiletransfer.modelclasses.FileList;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItems;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItemsArray;
import com.example.bluetoothfiletransfer.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import moe.feng.common.view.breadcrumbs.BreadcrumbsView;
import moe.feng.common.view.breadcrumbs.DefaultBreadcrumbsCallback;
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem;

public class FilesFragment extends Fragment {

    public static FileManagerAdapter filesAdapter;
    /* access modifiers changed from: private */
    public String currentLocation;
    /* access modifiers changed from: private */
    public BreadcrumbsView mBreadcrumbsView;
    private RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);

        View view= layoutInflater.inflate(R.layout.fragment_files, viewGroup, false);

        return view;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.currentLocation = getCurrentPath();
        new LoadTask().execute(new String[]{this.currentLocation});
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mBreadcrumbsView = (BreadcrumbsView) view.findViewById(R.id.breadcrumbs_view);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        Log.d("jjj", "onViewCreated: Number 1");
        filesAdapter = new FileManagerAdapter(getContext());
        Log.d("jjj", "onViewCreated: Number 2");
        this.mRecyclerView.setAdapter(filesAdapter);
        Log.d("jjj", "onViewCreated: Number 3");
        filesAdapter.setCallback(new FileManagerAdapter.Callback() {
            @SuppressLint("WrongConstant")
            public void onItemClick(FileList.FileWrapper fileWrapper) {
                if (fileWrapper.isDirectory()) {
                    BreadcrumbItem breadcrumbItem = new BreadcrumbItem(FilesFragment.filesAdapter.getFileList().getDirectoriesString());
                    breadcrumbItem.setSelectedItem(fileWrapper.toString());
                    String unused = FilesFragment.this.currentLocation = FilesFragment.this.getCurrentPath() + "/" + fileWrapper.toString();
                    Log.d("currentPath", "onItemClick: path " + FilesFragment.this.getCurrentPath());
                    Log.d("currentPath", "onItemClick: location " + FilesFragment.this.currentLocation);
                    new LoadTask(breadcrumbItem).execute(new String[]{FilesFragment.this.currentLocation});
                } else if (fileWrapper.isFile()) {
                    if (!fileWrapper.isFileExist(fileWrapper.getFilePath())) {
                        SelectedItemsArray.addItem(new SelectedItems(fileWrapper.getFilePath(), 0, Constants.FILES, fileWrapper.getFileSize()));
                        SelectedItemsArray.addItemCount();
                        FileShareFragment.tv_itemCount.setText(String.valueOf(SelectedItemsArray.getItemCount()));
                        Toast.makeText(FilesFragment.this.getContext(), "File Selected", 0).show();
                    } else {
                        Toast.makeText(FilesFragment.this.getContext(), "File Already Selected!", 0).show();
                    }
                    Log.d("pjjj", "onItemClick: " + fileWrapper.getFilePath());
                }
            }
        });
        this.mBreadcrumbsView.setCallback(new DefaultBreadcrumbsCallback<BreadcrumbItem>() {
            public void onNavigateBack(BreadcrumbItem breadcrumbItem, int i) {
                FilesFragment filesFragment = FilesFragment.this;
                String unused = filesFragment.currentLocation = filesFragment.getPath(i);
                new LoadTask().execute(new String[]{FilesFragment.this.currentLocation});
            }

            public void onNavigateNewLocation(BreadcrumbItem breadcrumbItem, int i) {
                String unused = FilesFragment.this.currentLocation = FilesFragment.this.getPath(i - 1) + "/" + breadcrumbItem.getSelectedItem();
                new LoadTask().execute(new String[]{FilesFragment.this.currentLocation});
            }
        });
        if (bundle == null) {
            Log.d("jjj", "onViewCreated: savedInstanceState==null");
            this.mBreadcrumbsView.addItem(BreadcrumbItem.createSimpleItem("External Storage"));
            this.currentLocation = getCurrentPath();
            new LoadTask().execute(new String[]{this.currentLocation});
        }
    }

    private BreadcrumbItem createItem(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        return new BreadcrumbItem((List<String>) arrayList);
    }

    public String getCurrentPath() {
        return getPath(-1);
    }

    /* access modifiers changed from: private */
    public String getPath(int i) {
        Log.d("jjj", "onViewCreated: Number 4");
        if (i == -1) {
            i = this.mBreadcrumbsView.getItems().size() - 1;
        }
        StringBuffer stringBuffer = new StringBuffer(Environment.getExternalStorageDirectory().getAbsolutePath());
        for (int i2 = 1; i2 <= i; i2++) {
            stringBuffer.append("/").append(this.mBreadcrumbsView.getItems().get(i2).getSelectedItem());
        }
        return stringBuffer.toString();
    }

    private class LoadTask extends AsyncTask<String, Void, FileList> {
        private BreadcrumbItem nextItem;

        LoadTask() {
        }

        LoadTask(BreadcrumbItem breadcrumbItem) {
            this.nextItem = breadcrumbItem;
        }

        /* access modifiers changed from: protected */
        public FileList doInBackground(String... strArr) {
            try {
                Log.d("jjj", "onViewCreated: Number 5");
                return FileList.newInstance(strArr[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /* access modifiers changed from: protected */
        @SuppressLint("WrongConstant")
        public void onPostExecute(FileList fileList) {
            if (fileList != null) {
                if (FilesFragment.this.progressBar.getVisibility() == 0) {
                    FilesFragment.this.progressBar.setVisibility(8);
                }
                Log.d("jjj", "onViewCreated: Number 6");
                FilesFragment.filesAdapter.setFileList(fileList);
                FilesFragment.filesAdapter.notifyDataSetChanged();
                if (this.nextItem != null) {
                    FilesFragment.this.mBreadcrumbsView.addItem(this.nextItem);
                }
            } else if (this.nextItem != null) {
                Toast.makeText(FilesFragment.this.getContext(), "Something Wrong", 0).show();
            }
        }
    }

    public boolean onBackPressed() {
        if (this.mBreadcrumbsView.getItems().size() <= 1) {
            return false;
        }
        this.mBreadcrumbsView.removeLastItem();
        this.currentLocation = getCurrentPath();
        new LoadTask().execute(new String[]{this.currentLocation});
        return true;
    }
}