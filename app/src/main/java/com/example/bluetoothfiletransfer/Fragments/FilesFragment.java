package com.example.bluetoothfiletransfer.Fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bluetoothfiletransfer.R;
import com.example.bluetoothfiletransfer.ViewModel.FilesViewModel;
import com.example.bluetoothfiletransfer.adapters.FileManagerAdapter;
import com.example.bluetoothfiletransfer.modelclasses.FileList;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItems;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItemsArray;
import com.example.bluetoothfiletransfer.utils.Constants;

import java.io.File;
import java.util.Collections;

import moe.feng.common.view.breadcrumbs.BreadcrumbsView;
import moe.feng.common.view.breadcrumbs.DefaultBreadcrumbsCallback;
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem;

public class FilesFragment extends Fragment {
    public static FileManagerAdapter filesAdapter;
    private FilesViewModel filesViewModel;
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
        this.mBreadcrumbsView = view.findViewById(R.id.breadcrumbs_view);

        return view;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.currentLocation = getCurrentPath();
        new LoadTask().execute(this.currentLocation);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);

        filesViewModel = new ViewModelProvider(this).get(FilesViewModel.class);

        // Observe LiveData changes
        filesViewModel.getFileListLiveData().observe(getViewLifecycleOwner(), new Observer<FileList>() {
            @Override
            public void onChanged(FileList fileList) {
              //  updateView(fileList);
            }
        });
        this.mBreadcrumbsView = view.findViewById(R.id.breadcrumbs_view);
        this.mRecyclerView = view.findViewById(R.id.recyclerView);
        this.progressBar = view.findViewById(R.id.progressBar);
        Log.d("jjj", "onViewCreated: Number 1");
        filesAdapter = new FileManagerAdapter(getContext());

        Log.d("jjj", "onViewCreated: Number 2");
        this.mRecyclerView.setAdapter(filesAdapter);
        Log.d("jjj", "onViewCreated: Number 3");
        filesAdapter.setCallback(new FileManagerAdapter.Callback() {
            @SuppressLint("WrongConstant")
            public void onItemClick(FileList.FileWrapper fileWrapper) {

                if (fileWrapper.isDirectory()) {
                    FileShareFragment.main_bottom_id.setVisibility(View.GONE);
                    BreadcrumbItem breadcrumbItem = new BreadcrumbItem(FilesFragment.filesAdapter.getFileList().getDirectoriesString());
                    breadcrumbItem.setSelectedItem(fileWrapper.toString());
                    new LoadTask().execute(fileWrapper.getFilePath());
                    String unused = FilesFragment.this.currentLocation = FilesFragment.this.getCurrentPath() + "/" + fileWrapper;
                    Log.d("currentPath", "onItemClick: path " + FilesFragment.this.getCurrentPath());
                    Log.d("currentPath", "onItemClick: location " + FilesFragment.this.currentLocation);
                    new LoadTask(breadcrumbItem).execute(FilesFragment.this.currentLocation);

                } else if (fileWrapper.isFile()) {

                    if (!fileWrapper.isFileExist(fileWrapper.getFilePath())) {
                        SelectedItemsArray.addItem(new SelectedItems(fileWrapper.getFilePath(), 0, Constants.FILES, fileWrapper.getFileSize()));
                        SelectedItemsArray.addItemCount();
                        fileWrapper.setSelected(true);
                        FileShareFragment.main_bottom_id.setVisibility(View.VISIBLE);
                        FileShareFragment.tv_itemCount.setText(String.valueOf(SelectedItemsArray.getItemCount()));
                        Toast.makeText(FilesFragment.this.getContext(), "File Selected", 0).show();
                    } else {
                        SelectedItemsArray.removeItem(new SelectedItems(fileWrapper.getFilePath(), 0, Constants.FILES, fileWrapper.getFileSize()));
                        SelectedItemsArray.minusItemCount();
                        fileWrapper.setSelected(false);
                        FileShareFragment.main_bottom_id.setVisibility(View.GONE);
                        FileShareFragment.tv_itemCount.setText(String.valueOf(SelectedItemsArray.getItemCount()));
                        Toast.makeText(FilesFragment.this.getContext(), "File Unselected now!", 0).show();
                    }
                    Log.d("pjjj", "onItemClick: " + fileWrapper.getFilePath());
                }
            }
        });
        this.mBreadcrumbsView.setCallback(new DefaultBreadcrumbsCallback<BreadcrumbItem>() {
            public void onNavigateBack(BreadcrumbItem breadcrumbItem, int i) {
                FilesFragment filesFragment = FilesFragment.this;
                FilesFragment.this.mBreadcrumbsView.removeAllViews();
                String unused = filesFragment.currentLocation = filesFragment.getPath(i);
                new LoadTask().execute(FilesFragment.this.currentLocation);
            }

            public void onNavigateNewLocation(BreadcrumbItem breadcrumbItem, int i) {
                String unused = FilesFragment.this.currentLocation = FilesFragment.this.getPath(i - 1) + "/" + breadcrumbItem.getSelectedItem();
                new LoadTask().execute(FilesFragment.this.currentLocation);
            }
        });
        if (bundle == null) {
            Log.d("jjj", "onViewCreated: savedInstanceState==null");
           // this.mBreadcrumbsView.addItem(BreadcrumbItem.createSimpleItem("External Storage"));
            this.currentLocation = getCurrentPath();
            new LoadTask().execute(this.currentLocation);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle back press here
                onBackPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    public String getCurrentPath() {

        return getPath(-1);
    }

    /* access modifiers changed from: private */
    private String getPath(int i) {
        Log.d("jjj", "onViewCreated: Number 4");
        if (i == -1) {
            i = this.mBreadcrumbsView.getItems().size() - 1;
        }
        StringBuffer stringBuffer = new StringBuffer(Environment.getExternalStorageDirectory().getPath());
        for (int i2 = 1; i2 <= i; i2++) {
            stringBuffer.append(File.separator).append(this.mBreadcrumbsView.getItems().get(i2).getSelectedItem());
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
        protected FileList doInBackground(String... strArr) {
            try {
                Log.d("jjj", "onViewCreated: Number 5");
                if (strArr != null && strArr.length > 0 && strArr[0] != null && !strArr[0].isEmpty()) {
                    String encodedPath = encodePath(strArr[0]);
                    File directory = new File(encodedPath);
                    if (directory.exists() && directory.isDirectory()) {
                        return FileList.newInstance(encodedPath);
                    } else {
                        Log.e("LoadTask", "Invalid directory path: " + encodedPath);
                    }
                } else {
                    Log.e("LoadTask", "Invalid or empty path provided");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private String encodePath(String path) {
            return path.replaceAll(" ", "%20");
        }




        /* access modifiers changed from: protected */
            public void onPostExecute(FileList fileList) {
            if (fileList != null) {
                Log.d("LoadTask", "onPostExecute: Loaded files successfully");
                if (FilesFragment.this.progressBar.getVisibility() == View.VISIBLE) {
                    FilesFragment.this.progressBar.setVisibility(View.GONE);
                }

                FilesFragment.filesAdapter.setFileList(fileList);
                FilesFragment.filesAdapter.notifyDataSetChanged();
                if (this.nextItem != null) {
                    // Check if the last item in breadcrumbs is a folder
                    boolean isLastItemFolder = FilesFragment.this.mBreadcrumbsView.getItems().isEmpty() ||
                                               (!fileList.getDirectoriesString().isEmpty() &&
                                                FilesFragment.this.mBreadcrumbsView.getItems().get(FilesFragment.this.mBreadcrumbsView.getItems().size() - 1).getSelectedItem().equals(fileList.getDirectoriesString().get(0)));

                    if (isLastItemFolder) {
                        FilesFragment.this.mBreadcrumbsView.addItem(this.nextItem);
                    }
                }
                filesViewModel.setFileList(fileList);
              //  updateView(fileList);
            } else if (this.nextItem != null) {
               // Toast.makeText(requireContext(), "Some thing wrong file null", Toast.LENGTH_SHORT).show();
                Log.d("LoadTask", "onPostExecute: Some thing wrong fileList null");
            }
        }
    }
    //list view data in file fragment in top of fragment is loading in this updateView
  /*  @SuppressLint("NotifyDataSetChanged")
    private void updateView(FileList fileList) {
        if (fileList != null) {
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }

            // Assuming filesAdapter is an instance of FileManagerAdapter
            filesAdapter.setFileList(fileList);
            filesAdapter.notifyDataSetChanged();

            // If you want to reset the breadcrumbs, you can do something like this:
            //mBreadcrumbsView.clearAllItems();
            mBreadcrumbsView.addItem(BreadcrumbItem.createSimpleItem("External Storage"));

            // Add other breadcrumbs based on the fileList or any other logic
            for (String directory : fileList.getDirectoriesString()) {
                BreadcrumbItem breadcrumbItem = new BreadcrumbItem(Collections.singletonList(directory));
                mBreadcrumbsView.addItem(breadcrumbItem);
            }
        } else {
            Toast.makeText(getContext(), "fileList is null", Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();

    }
    private void onBackPressed() {
        if (currentLocation != null) {
            String parentDirectory = new File(currentLocation).getParent();
            Log.d("onBackPressed", "Current location: " + currentLocation);
            Log.d("onBackPressed", "Parent directory: " + parentDirectory);

            if (parentDirectory != null) {
                // Update the current location before executing LoadTask
                currentLocation = parentDirectory;
                new LoadTask().execute(parentDirectory);
            } else {
                // Handle the back press when the parent directory is null
                FilesFragment.this.mBreadcrumbsView.removeAllViews();

                // Remove the fragment from the back stack
                getParentFragmentManager().popBackStack();
            }
        }
    }




}