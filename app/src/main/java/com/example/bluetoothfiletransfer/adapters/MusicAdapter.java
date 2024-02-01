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

import androidx.annotation.NonNull;
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

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> implements Filterable {
    /* access modifiers changed from: private */
    public static String searchText = "";
    Context mContext;
    private Filter musicFilter = new Filter() {
        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0) {
                Log.d("ggg", "onQueryTextChange: null " + charSequence);
                arrayList.addAll(MusicAdapter.this.musicFullList);
                String unused = MusicAdapter.searchText = "";
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                for (AllItemModelClass next : MusicAdapter.this.musicFullList)
                {
                    if (next.getItemName().toLowerCase().contains(trim))
                    {
                        arrayList.add(next);
                        Log.d("ggg", "onQueryTextChange: not null " + charSequence);
                    }
                }
                String unused2 = MusicAdapter.searchText = charSequence.toString();
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            MusicAdapter.this.musicList.clear();
            MusicAdapter.this.musicList.addAll((List) filterResults.values);
            for (AllItemModelClass next : MusicAdapter.this.musicList)
            {
                if (next.isSelected())
                {
                    SelectedItemsArray.setSelectedItemByName(MusicAdapter.this.getIndexByName(next.getImgPath()),
                            new SelectedItems(next.getImgPath(), MusicAdapter.this.musicList.indexOf(next), Constants.MUSIC, next.getItemSize()));
                }
            }
            MusicAdapter.this.notifyDataSetChanged();
        }
    };
    ArrayList<AllItemModelClass> musicFullList;
    ArrayList<AllItemModelClass> musicList;

    public int getItemViewType(int i) {
        return i;
    }

    public MusicAdapter(Context context, ArrayList<AllItemModelClass> arrayList) {
        this.musicList = arrayList;
        this.mContext = context;
        this.musicFullList = new ArrayList<>(arrayList);
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

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.recycler_pics_row, viewGroup, false));
    }

    @SuppressLint("WrongConstant")
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        AllItemModelClass allItemModelClass = this.musicList.get(i);
        Log.d("iii", "onBindViewHolder: " + i);
        if (viewHolder.playIcon.getVisibility() == 0) {
            viewHolder.playIcon.setVisibility(8);
        }
        ((RequestBuilder) ((RequestBuilder) ((RequestBuilder)
                Glide.with(this.mContext).load(allItemModelClass.getImgPath()).
                        thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL))
                .skipMemoryCache(true)).placeholder((int) R.drawable.baseline_music_note_24)).into(viewHolder.iv_image);
        viewHolder.tv_fileSize.setText(allItemModelClass.getItemSize());
        viewHolder.tv_fileName.setText(allItemModelClass.getItemName());
        if (allItemModelClass.isSelected()) {
            viewHolder.imgChecked.setImageResource(R.drawable.checked_circle);
        } else {
            viewHolder.imgChecked.setImageResource(R.drawable.uncheck_circle);
        }
    }

    public int getIndexByName(String str) {
        String str2 = str;
        for (SelectedItems next : SelectedItemsArray.getAllSelectedItems())
        {
            if (next.getImgPath().equals(str2))
            {
                return SelectedItemsArray.getAllSelectedItems().indexOf(next);
            }
        }
        return -1;
    }


    public int getItemCount() {
        return this.musicList.size();
    }

    public void onViewRecycled(ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        Glide.with(this.mContext).clear((View) viewHolder.iv_image);
        Log.d("yyy", "onViewRecycled: ");
    }

    public Filter getFilter() {
        return this.musicFilter;
    }
}
