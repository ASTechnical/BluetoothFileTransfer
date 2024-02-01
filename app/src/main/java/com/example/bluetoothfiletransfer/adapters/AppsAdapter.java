package com.example.bluetoothfiletransfer.adapters;

import android.content.Context;
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
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

import java.util.ArrayList;
import java.util.List;



public class AppsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private ArrayList<AppsModelClass> appsFullList;
    private ArrayList<AppsModelClass> appsList;
    private List<NativeAd> nativeAds;   // List to store loaded native ads
    private Context mContext;

    private static String searchText = "";
    private static final int VIEW_TYPE_APP = 0;
    private static final int VIEW_TYPE_AD = 1;
    private static final int ITEMS_PER_AD = 4;
    private static final int ITEMS_PER_ROW = 4;


    private Filter appsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<AppsModelClass> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(appsFullList);
                searchText = "";
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                for (AppsModelClass app : appsFullList) {
                    if (app.getAppName().toLowerCase().contains(trim)) {
                        filteredList.add(app);
                    }
                }
                searchText = charSequence.toString();
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            appsList.clear();
            appsList.addAll((List<AppsModelClass>) filterResults.values);
            //  updateSelectedItems();
            notifyDataSetChanged();
        }
    };


    public AppsAdapter(Context context, ArrayList<AppsModelClass> itemList) {
        this.mContext = context;
        this.appsList = itemList;
        this.appsFullList = new ArrayList<>(itemList);
        this.nativeAds = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return (position % (ITEMS_PER_AD + 1) == 4) ? VIEW_TYPE_AD : VIEW_TYPE_APP;
    }

    @Override
    public Filter getFilter() {
        return this.appsFilter;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (viewType == VIEW_TYPE_APP) {
            View appView = inflater.inflate(R.layout.recycler_apps_row, parent, false);
            return new AppViewHolder(appView);

         /*   case VIEW_TYPE_AD:
            default:
                View adView = inflater.inflate(R.layout.item_native_ad, parent, false);
                return new AdViewHolder(adView);*/
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_APP) {
            AppViewHolder appViewHolder = (AppViewHolder) viewHolder;
            AppsModelClass appsModelClass = appsList.get(getRealPosition(position));
            bindAppData(appViewHolder, appsModelClass);

           /* case VIEW_TYPE_AD:
                AdViewHolder adViewHolder = (AdViewHolder) viewHolder;
                loadNativeAd(adViewHolder);
                break;*/
        }
    }

    private void loadNativeAd(final AdViewHolder adViewHolder) {
        AdLoader.Builder builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_unit_id_for_recview));
        AdLoader adLoader = builder
                .forNativeAd(nativeAd -> {
                    // Handle the loaded ad
                    adViewHolder.bind(nativeAd);
                })
                .withNativeAdOptions(new NativeAdOptions.Builder().build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private int getRealPosition(int position) {
        return position - position / (ITEMS_PER_AD + 1);
    }



    @Override
    public int getItemCount() {
        int adCount = appsList.size() / ITEMS_PER_AD;
        return appsList.size() + adCount;
    }

    private void bindAppData(AppViewHolder viewHolder, AppsModelClass appsModelClass) {
        // Bind app data as usual
        viewHolder.tv_appName.setText(appsModelClass.getAppName());
        viewHolder.tv_appSize.setText(appsModelClass.getAppSize());
        viewHolder.iv_appIcon.setImageDrawable(appsModelClass.getAppIcon());
        setTextViewColors(viewHolder.tv_appName, viewHolder.tv_appSize, appsModelClass.isSelected());
        setCheckedImage(viewHolder.imgChecked, appsModelClass.isSelected());
    }

    private void setTextViewColors(TextView appNameTextView, TextView appSizeTextView, boolean isSelected) {
        int appNameColor = isSelected ? ContextCompat.getColor(mContext, R.color.gray) : ContextCompat.getColor(mContext, android.R.color.black);
        int appSizeColor = isSelected ? ContextCompat.getColor(mContext, android.R.color.darker_gray) : ContextCompat.getColor(mContext, android.R.color.black);

        appNameTextView.setTextColor(appNameColor);
        appSizeTextView.setTextColor(appSizeColor);
    }

    private void setCheckedImage(ImageView imgChecked, boolean isSelected) {
        imgChecked.setImageResource(isSelected ? R.drawable.checked_circle : R.drawable.uncheck_circle);
    }




    private static class AppViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgChecked;
        private ImageView iv_appIcon;
        private TextView tv_appName;
        private TextView tv_appSize;

        public AppViewHolder(View view) {
            super(view);
            tv_appName = view.findViewById(R.id.appName);
            tv_appSize = view.findViewById(R.id.appSize);
            iv_appIcon = view.findViewById(R.id.appIcon);
            imgChecked = view.findViewById(R.id.imgChecked);
        }
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {
        private final TemplateView templateAds;

        public AdViewHolder(View view) {
            super(view);
            templateAds = view.findViewById(R.id.template_ads_id);

        }

        public void bind(NativeAd nativeAd) {
            templateAds.setNativeAd(nativeAd);
            templateAds.setVisibility(View.VISIBLE);

        }

    }

}




