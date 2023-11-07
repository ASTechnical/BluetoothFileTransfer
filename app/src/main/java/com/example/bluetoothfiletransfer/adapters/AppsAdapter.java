package com.example.bluetoothfiletransfer.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.bluetoothfiletransfer.R;
import com.example.bluetoothfiletransfer.modelclasses.AppsModelClass;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItems;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItemsArray;
import com.example.bluetoothfiletransfer.utils.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> implements Filterable {
    public static String searchText = "";
    private Filter appsFilter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0) {
                Log.d("ggg", "onQueryTextChange: null " + charSequence);
                arrayList.addAll(AppsAdapter.this.appsFullList);
                String unused = AppsAdapter.searchText = "";
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                Iterator<AppsModelClass> it = AppsAdapter.this.appsFullList.iterator();
                while (it.hasNext()) {
                    AppsModelClass next = it.next();
                    if (next.getAppName().toLowerCase().contains(trim)) {
                        arrayList.add(next);
                        Log.d("ggg", "onQueryTextChange: not null " + charSequence);
                    }
                }
                String unused2 = AppsAdapter.searchText = charSequence.toString();
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            AppsAdapter.this.appsList.clear();
            AppsAdapter.this.appsList.addAll((List) filterResults.values);
            Iterator<AppsModelClass> it = AppsAdapter.this.appsList.iterator();
            while (it.hasNext()) {
                AppsModelClass next = it.next();
                if (next.isSelected()) {
                    int indexOf = AppsAdapter.this.appsList.indexOf(next);
                    int indexByName = AppsAdapter.this.getIndexByName(next.getAppPkgName());
                    SelectedItems selectedItems = new SelectedItems(next.getAppPkgName(), indexOf, Constants.APPS, next.getAppSize());
                    selectedItems.setItemName(next.getAppName());
                    selectedItems.setItemIcon(next.getAppIcon());
                    SelectedItemsArray.setSelectedItemByName(indexByName, selectedItems);
                    Log.d("rrr", "publishResults:appList pos: " + indexOf);
                    Log.d("rrr", "publishResults:selectedArray index : " + indexByName);
                }
            }
            AppsAdapter.this.notifyDataSetChanged();
            Log.d("ggg", "publishResults: datasetchanged");
        }
    };
    ArrayList<AppsModelClass> appsFullList;
    ArrayList<AppsModelClass> appsList;
    Context mContext;

    public int getItemViewType(int i) {
        return i;
    }

    public AppsAdapter(Context context, ArrayList<AppsModelClass> arrayList) {
        this.appsList = arrayList;
        this.appsFullList = new ArrayList<>(arrayList);
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgChecked;
        public ImageView iv_appIcon;
        public TextView tv_appName;
        public TextView tv_appSize;

        public ViewHolder(View view) {
            super(view);
            this.tv_appName = (TextView) view.findViewById(R.id.appName);
            this.tv_appSize = (TextView) view.findViewById(R.id.appSize);
            this.iv_appIcon = (ImageView) view.findViewById(R.id.appIcon);
            this.imgChecked = (ImageView) view.findViewById(R.id.imgChecked);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).
                inflate(R.layout.recycler_apps_row, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        AppsModelClass appsModelClass = this.appsList.get(i);
        viewHolder.tv_appName.setText(appsModelClass.getAppName());
        viewHolder.tv_appSize.setText(appsModelClass.getAppSize());
        viewHolder.iv_appIcon.setImageDrawable(appsModelClass.getAppIcon());
        if (appsModelClass.isSelected()) {
            viewHolder.imgChecked.setImageResource(R.drawable.checked_circle);
        } else {
            viewHolder.imgChecked.setImageResource(R.drawable.uncheck_circle);
        }
    }

    public int getIndexByName(String str) {
        Iterator<SelectedItems> it = SelectedItemsArray.getAllSelectedItems().iterator();
        while (it.hasNext()) {
            SelectedItems next = it.next();
            if (next.getImgPath().equals(str)) {
                return SelectedItemsArray.getAllSelectedItems().indexOf(next);
            }
        }
        return -1;
    }



    public int getItemCount() {
        return this.appsList.size();
    }

    public long getItemId(int i) {
        return super.getItemId(i);
    }

    public Filter getFilter() {
        return this.appsFilter;
    }
}
