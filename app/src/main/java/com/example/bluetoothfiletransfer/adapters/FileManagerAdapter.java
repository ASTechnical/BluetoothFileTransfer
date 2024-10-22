package com.example.bluetoothfiletransfer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothfiletransfer.R;
import com.example.bluetoothfiletransfer.modelclasses.AppsModelClass;
import com.example.bluetoothfiletransfer.modelclasses.FileList;
import com.example.bluetoothfiletransfer.modelclasses.FilesDetail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileManagerAdapter extends RecyclerView.Adapter<FileManagerAdapter.FileHolder> implements Filterable
{
    /* access modifiers changed from: private */
    public static String searchText = "";
    /* access modifiers changed from: private */
    public Callback callback;

    private final Filter fileFilterMethod = new Filter()
    {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence)
        {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0)
            {
                Log.d("ggg", "onQueryTextChange: null" + charSequence);
                arrayList.addAll(FileManagerAdapter.this.fileFullListArray);
                String unused = FileManagerAdapter.searchText = "";
            } else
            {
                String trim = charSequence.toString().toLowerCase().trim();
                for (FilesDetail filesDetail : FileManagerAdapter.this.fileFullListArray)
                {
                    if (filesDetail.getFileName().toLowerCase().contains(trim))
                    {
                        arrayList.add(filesDetail);
                        Log.d("ggg", "onQueryTextChange: not null " + charSequence);
                    }
                }
                String unused2 = FileManagerAdapter.searchText = charSequence.toString();
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults)
        {
            try
            {
                FileManagerAdapter.this.fileListArray.clear();
                FileManagerAdapter.this.fileListArray.addAll((List) filterResults.values);
                FileManagerAdapter.this.notifyDataSetChanged();
            } catch (NullPointerException e)
            {
                Log.d("rrr", "publishResults: " + e);
            }
        }
    };
    /* access modifiers changed from: private */
    public ArrayList<FilesDetail> fileFullListArray;
    /* access modifiers changed from: private */
    public FileList fileList;
    /* access modifiers changed from: private */
    public ArrayList<FilesDetail> fileListArray = new ArrayList<>();
    public ArrayList<FileList.FileWrapper> fileListwrapper = new ArrayList<>();
    Context mContext;

    public interface Callback
    {
        void onItemClick(FileList.FileWrapper fileWrapper);
    }

  /*  public int getItemViewType(int i)
    {
        return i;
    }*/

    public FileManagerAdapter(Context context)
    {
        this.mContext = context;
    }

    @NonNull
    public FileHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        Log.d("jjj", "FileManagerAdapter: Number 1");
        return new FileHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.file_manager_item, viewGroup, false));
    }

    public void onBindViewHolder(FileHolder fileHolder, int i)
    {

        Log.d("posi", "onBindViewHolder: pos: " + i);
        FilesDetail filesDetail = this.fileListArray.get(i);

    //    FileList.FileWrapper fileWrapper = fileListwrapper.get(i);

        fileHolder.title.setTextColor(ContextCompat.getColor(mContext, R.color.gray));

        if (filesDetail.isDirectory())
        {
            fileHolder.icon.setImageResource(R.drawable.baseline_folder_24);
            fileHolder.title.setText(filesDetail.getFileName());
            fileHolder.imgChecked.setVisibility(View.GONE);

        } else if (filesDetail.isFiles())
        {

            fileHolder.icon.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
            fileHolder.title.setText(filesDetail.getFileName());
        }
        // Check if the file is selected and update the check mark accordingly
        /*if (fileWrapper.isSelected()) {
            fileHolder.imgChecked.setImageResource(R.drawable.checked_circle);
        } else {
            fileHolder.imgChecked.setImageResource(R.drawable.uncheck_circle);
        }*/

    }

    public int getItemCount()
    {
        Log.d("jjj", "FileManagerAdapter: Number 3");
        return this.fileListArray.size();
    }

    public void setFileList(FileList fileList2)
    {
        this.fileList = fileList2;
        int size = fileList2 != null ? fileList2.directories.size() + fileList2.files.size() : 0;
        this.fileListArray.clear();
        Log.d("itemCount", "setFileList: " + getItemCount());
        for (int i = 0; i < size; i++)
        {

            if (i < fileList2.directories.size())
            {
                this.fileListArray.add(new FilesDetail(false, true,fileList2.directories.
                        get(i).toString()/*,false*/));
            } else
            {
                this.fileListArray.add(new FilesDetail(true, false, fileList2.files.
                        get(i - fileList2.directories.size()).toString()/*,true*/));
            }
        }
        ArrayList<FilesDetail> arrayList = new ArrayList<>();
        this.fileFullListArray = arrayList;
        this.fileFullListArray.addAll(this.fileListArray);
    }

    public FileList getFileList()
    {
        return this.fileList;
    }

    public Callback getCallback()
    {
        return this.callback;
    }

    public void setCallback(Callback callback2)
    {
        this.callback = callback2;
    }

    class FileHolder extends RecyclerView.ViewHolder
    {
        ImageView icon;
        TextView title;
        public ImageView imgChecked;
        public FileHolder(View view)
        {
            super(view);
            this.icon = view.findViewById(R.id.file_icon);
            this.title = view.findViewById(R.id.file_name);
            this.imgChecked = view.findViewById(R.id.imgChecked);
            view.setOnClickListener(view1 ->
            {
                if (FileManagerAdapter.this.callback != null)
                {
                    Log.d("direc",
                            "onClick: " + FileManagerAdapter.this.fileList.getDirectoriesString());
                    FilesDetail filesDetail = FileManagerAdapter.this.fileListArray.get(FileHolder.this.getAdapterPosition());
                    if (filesDetail.isDirectory())
                    {

                        for (FileList.FileWrapper next : FileManagerAdapter.this.fileList.directories)
                        {
                            if (filesDetail.getFileName().equals(next.getFileName()))
                            {
                                FileManagerAdapter.this.callback.onItemClick(next);
                                new LoadTask().execute(filesDetail.getFilePath());
                                return;
                            }
                        }
                    } else if (filesDetail.isFiles())
                    {
                        for (FileList.FileWrapper next2 : FileManagerAdapter.this.fileList.files)
                        {
                            if (filesDetail.getFileName().equals(next2.getFileName()))
                            {
                                FileManagerAdapter.this.callback.onItemClick(next2);
                                return;
                            }
                        }
                    }
                }
            });
        }
    }


    public Filter getFilter()
    {
        return this.fileFilterMethod;
    }

    private class LoadTask extends AsyncTask<String, Void, FileList>
    {
        private LoadTask()
        {
        }

        /* access modifiers changed from: protected */
        public FileList doInBackground(String... strArr)
        {
            try
            {
                Log.d("jjj", "onViewCreated: Number 5");
                return FileList.newInstance(strArr[0]);
            } catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(FileList fileList)
        {
            if (fileList != null)
            {
                Log.d("jjj", "onViewCreated: Number 6");
                FileManagerAdapter.this.setFileList(fileList);
                FileManagerAdapter.this.notifyDataSetChanged();
            }
        }
    }
}
