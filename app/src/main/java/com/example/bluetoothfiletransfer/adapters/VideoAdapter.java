package com.example.bluetoothfiletransfer.adapters;

import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bluetoothfiletransfer.R;
import com.example.bluetoothfiletransfer.modelclasses.AllItemModelClass;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItems;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItemsArray;
import com.example.bluetoothfiletransfer.utils.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> implements Filterable {
    /* access modifiers changed from: private */
    public static String searchText = "";
    Context mContext;
    private Filter videoFilter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0) {
                Log.d("ggg", "onQueryTextChange: null " + charSequence);
                arrayList.addAll(VideoAdapter.this.videoFullList);
                String unused = VideoAdapter.searchText = "";
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                Iterator<AllItemModelClass> it = VideoAdapter.this.videoFullList.iterator();
                while (it.hasNext()) {
                    AllItemModelClass next = it.next();
                    if (next.getItemName().toLowerCase().contains(trim)) {
                        arrayList.add(next);
                        Log.d("ggg", "onQueryTextChange: not null " + charSequence);
                    }
                }
                String unused2 = VideoAdapter.searchText = charSequence.toString();
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            VideoAdapter.this.videoList.clear();
            VideoAdapter.this.videoList.addAll((List) filterResults.values);
            Iterator<AllItemModelClass> it = VideoAdapter.this.videoList.iterator();
            while (it.hasNext()) {
                AllItemModelClass next = it.next();
                if (next.isSelected()) {
                    SelectedItemsArray.setSelectedItemByName(VideoAdapter.this.getIndexByName(next.getImgPath()), new SelectedItems(next.getImgPath(), VideoAdapter.this.videoList.indexOf(next), Constants.VIDEOS, next.getItemSize()));
                }
            }
            VideoAdapter.this.notifyDataSetChanged();
        }
    };
    ArrayList<AllItemModelClass> videoFullList;
    ArrayList<AllItemModelClass> videoList;

    public int getItemViewType(int i) {
        return i;
    }

    public VideoAdapter(Context context, ArrayList<AllItemModelClass> arrayList) {
        this.videoList = arrayList;
        this.mContext = context;
        this.videoFullList = new ArrayList<>(arrayList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgChecked;
        public ImageView iv_image;
        public ImageView playIcon;
        TextView tv_fileName;
        TextView tv_fileSize;

        public ViewHolder(View view) {
            super(view);
            this.iv_image = (ImageView) view.findViewById(R.id.image);
            this.imgChecked = (ImageView) view.findViewById(R.id.imgChecked);
            this.playIcon = (ImageView) view.findViewById(R.id.playIcon);
            this.tv_fileName = (TextView) view.findViewById(R.id.fileName);
            this.tv_fileSize = (TextView) view.findViewById(R.id.fileSize);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.recycler_pics_row, viewGroup,
                false));
    }

    @SuppressLint("WrongConstant")
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        AllItemModelClass allItemModelClass = this.videoList.get(i);
        Log.d("iii", "onBindViewHolder: " + i);
        if (viewHolder.playIcon.getVisibility() == 0) {
            viewHolder.playIcon.setVisibility(8);
        }
        ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) Glide.with(this.mContext).
                load(allItemModelClass.getImgPath()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL))
                .skipMemoryCache(true)).placeholder((int) R.drawable.notfound)).into(viewHolder.iv_image);
        viewHolder.tv_fileSize.setText(allItemModelClass.getItemSize());
        viewHolder.tv_fileName.setText(allItemModelClass.getItemName());
        if (allItemModelClass.isSelected()) {
            viewHolder.imgChecked.setImageResource(R.drawable.checked_circle);
        } else {
            viewHolder.imgChecked.setImageResource(R.drawable.uncheck_circle);
        }
    }

    public int getIndexByName(String str) {
        String str2 = str.toString();
        Iterator<SelectedItems> it = SelectedItemsArray.getAllSelectedItems().iterator();
        while (it.hasNext()) {
            SelectedItems next = it.next();
            if (next.getImgPath().equals(str2)) {
                return SelectedItemsArray.getAllSelectedItems().indexOf(next);
            }
        }
        return -1;
    }


    public int getItemCount() {
        return this.videoList.size();
    }

    public void onViewRecycled(ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        Glide.with(this.mContext).clear((View) viewHolder.iv_image);
        Log.d("yyy", "onViewRecycled: ");
    }

    public Filter getFilter() {
        return this.videoFilter;
    }
}
