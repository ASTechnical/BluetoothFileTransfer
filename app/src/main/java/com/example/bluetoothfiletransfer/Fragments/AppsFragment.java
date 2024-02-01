package com.example.bluetoothfiletransfer.Fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothfiletransfer.R;
import com.example.bluetoothfiletransfer.adapters.AppsAdapter;
import com.example.bluetoothfiletransfer.modelclasses.AppsModelClass;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItems;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItemsArray;
import com.example.bluetoothfiletransfer.utils.Constants;
import com.example.bluetoothfiletransfer.utils.RecyclerTouchListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AppsFragment extends Fragment {

    private static final int ITEMS_PER_PAGE = 4;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    static AppsAdapter appsAdapter;
    private static List<AppsModelClass> appsList = new ArrayList<>();

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            handleBroadcast(intent);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apps, container, false);
        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver, new IntentFilter("custom-event-name"));

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
                handleItemClick(i);
            }

            public void onClick(View view, int i) {
                handleItemClick(i);
            }
        }));
    }

    private void handleBroadcast(Intent intent) {
        String message = intent.getStringExtra("message");
        int position = intent.getIntExtra("position", -1);

        if (message.equals(Constants.APPS)) {
            handleAppsBroadcast(position, intent);
        }
        Log.d("receiver", "Got position: " + position);
    }

    private void handleAppsBroadcast(int position, Intent intent) {
        if (position != -1) {
            try {
                AppsModelClass appsModelClass = appsList.get(position);
                if (appsModelClass.isSelected()) {
                    appsModelClass.setSelected(false);
                }
                appsAdapter.notifyItemChanged(position);
                Log.d("rrr", "mMessageReceiver:pos: " + position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            handleMultipleItemsBroadcast(intent);
        }
    }

    private void handleMultipleItemsBroadcast(Intent intent) {
        for (Integer integer : intent.getIntegerArrayListExtra("positionArray")) {
            int intValue = integer;
            AppsModelClass appsModelClass = appsList.get(intValue);
            if (appsModelClass.isSelected()) {
                appsModelClass.setSelected(false);
                appsAdapter.notifyItemChanged(intValue);
            }
        }
    }

    private void handleItemClick(int position) {
        AppsModelClass appsModelClass = appsList.get(position);
        if (appsModelClass.isSelected()) {
            handleItemDeselected(appsModelClass, position);
        } else {
            handleItemSelected(appsModelClass, position);
        }
        appsAdapter.notifyItemChanged(position);
    }

    private void handleItemSelected(AppsModelClass appsModelClass, int position) {
        appsModelClass.setSelected(true);
        FileShareFragment.main_bottom_id.setVisibility(View.VISIBLE);
        SelectedItems selectedItems = createSelectedItems(appsModelClass, position);
        SelectedItemsArray.addItem(selectedItems);
        SelectedItemsArray.addItemCount();
        FileShareFragment.tv_itemCount.setText(String.valueOf(SelectedItemsArray.getItemCount()));
        Log.d("fff", "onClick: position added: " + position);
        Log.d("fff", "onClick: " + SelectedItemsArray.getArraySize());
    }

    private void handleItemDeselected(AppsModelClass appsModelClass, int position) {
        appsModelClass.setSelected(false);
        SelectedItems selectedItems = createSelectedItems(appsModelClass, position);
        SelectedItemsArray.removeItem(selectedItems);
        SelectedItemsArray.minusItemCount();
        FileShareFragment.tv_itemCount.setText(String.valueOf(SelectedItemsArray.getItemCount()));
        Log.d("fff", "onClick: position removed: " + position);
        Log.d("fff", "onClick: " + SelectedItemsArray.getArraySize());
    }

    private SelectedItems createSelectedItems(AppsModelClass appsModelClass, int position) {
        SelectedItems selectedItems = new SelectedItems(appsModelClass.getAppPkgName(), position, Constants.APPS, appsModelClass.getAppSize());
        selectedItems.setItemName(appsModelClass.getAppName());
        selectedItems.setItemIcon(appsModelClass.getAppIcon());
        return selectedItems;
    }

    public ArrayList<AppsModelClass> getInstalledApplications() {
        Intent intent = new Intent(Intent.ACTION_MAIN, (Uri) null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> queryIntentActivities = requireContext().getPackageManager().queryIntentActivities(intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            queryIntentActivities.sort(new ResolveInfo.DisplayNameComparator(getContext().getPackageManager()));
        }
        ArrayList<AppsModelClass> arrayList = new ArrayList<>();
        for (ResolveInfo next : queryIntentActivities) {
            try {
                if (next.activityInfo.applicationInfo.icon != 0 && (next.activityInfo.applicationInfo.flags & 1) == 0) {
                    Log.i("knownapp", next.activityInfo.applicationInfo.packageName + " is known and added to list");
                    AppsModelClass appsModelClass = new AppsModelClass();
                    appsModelClass.setAppName(getContext().getPackageManager().getApplicationLabel(next.activityInfo.applicationInfo).toString());
                    appsModelClass.setAppIcon(next.activityInfo.applicationInfo.loadIcon(getContext().getPackageManager()));
                    appsModelClass.setAppPkgName(String.valueOf(next.activityInfo.applicationInfo.packageName));
                    appsModelClass.setSelected(false);
                    appsModelClass.setAppSize(getStringSizeLengthFile(new File(next.activityInfo.applicationInfo.publicSourceDir).length()));
                    arrayList.add(appsModelClass);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public static String getStringSizeLengthFile(long j) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        DecimalFormat decimalFormat2 = new DecimalFormat("0");
        float f = (float) j;
        if (f < 1048576.0f) {
            return decimalFormat2.format((double) (f / 1024.0f)) + " Kb";
        }
        if (f < 1.07374182E9f) {
            return decimalFormat.format((double) (f / 1048576.0f)) + " Mb";
        }
        return f < 1.09951163E12f ? decimalFormat.format((double) (f / 1.07374182E9f)) + " Gb" : "";
    }

    public class AsyncTaskClass extends AsyncTask<String, String, ArrayList<AppsModelClass>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected ArrayList<AppsModelClass> doInBackground(String... strArr) {
            if (strArr[0].equals("Apps")) {
                return getInstalledApplications();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<AppsModelClass> arrayList) {
            super.onPostExecute(arrayList);
            if (arrayList != null) {
                updateView(arrayList);
            }
        }
    }

    private void updateView(ArrayList<AppsModelClass> arrayList) {
        appsList = arrayList;
        hideProgressBar();
        appsAdapter = new AppsAdapter(getContext(), arrayList);
        recyclerView.setAdapter(appsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), ITEMS_PER_PAGE, RecyclerView.VERTICAL, false));
    }

    private void showProgressBar() {
        if (progressBar.getVisibility() == View.GONE && progressBar.isShown()) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getActivity(), "destroyed", Toast.LENGTH_SHORT).show();
        try {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        } catch (Exception e) {
            Log.e("UnRegister Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload the data when the fragment becomes visible again
        updateData();
    }

    private void updateData() {
        // You can check if data is already loaded to avoid redundant reloading
        if (appsList == null || appsList.isEmpty()) {
            new AsyncTaskClass().execute("Apps");
        }
    }
}







/*
package com.example.bluetoothfiletransfer.Fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.bluetoothfiletransfer.R;
import com.example.bluetoothfiletransfer.adapters.AppsAdapter;
import com.example.bluetoothfiletransfer.modelclasses.AppsModelClass;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItems;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItemsArray;
import com.example.bluetoothfiletransfer.utils.Constants;
import com.example.bluetoothfiletransfer.utils.RecyclerTouchListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class AppsFragment extends Fragment {

    public static AppsAdapter appsAdapter;
    public static final int ITEM_PER=4;
    private static final String BANNER_AD_ID= "ca-app-pub-3940256099942544/6300978111";
    private List<Object> recyclerview_items=new ArrayList<>();
    Util util;
    public static List<AppsModelClass> appsList = new ArrayList();
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("message");
            int intExtra = intent.getIntExtra("position", -1);
            Log.d("fff", "onReceive: " + intExtra);
            if (stringExtra.equals(Constants.APPS)) {
                if (intExtra != -1) {
                    try {
                        AppsModelClass appsModelClass = AppsFragment.appsList.get(intExtra);
                        if (appsModelClass.isSelected()) {
                            appsModelClass.setSelected(false);
                        }
                        AppsFragment.appsAdapter.notifyItemChanged(intExtra);
                        Log.d("rrr", "mMessageReceiver:pos: " + intExtra);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    for (Integer integer : intent.getIntegerArrayListExtra("positionArray"))
                    {
                        int intValue = integer;
                        AppsModelClass appsModelClass2 = (AppsModelClass) AppsFragment.appsList.get(intValue);
                        if (appsModelClass2.isSelected())
                        {
                            appsModelClass2.setSelected(false);
                            AppsFragment.appsAdapter.notifyItemChanged(intValue);
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
        View view = layoutInflater.inflate(R.layout.fragment_apps, viewGroup, false);
        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        return view;
    }


    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(this.mMessageReceiver, new IntentFilter("custom-event-name"));
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        new AsyncTaskClass().execute(new String[]{"Apps"});
       */
/* AdmobNativeAdAdapter admobNativeAdAdapter = AdmobNativeAdAdapter.Builder.with("ca-app-pub-3940256099942544/2247696110", adapter,
                "small").adItemInterval(1).build();
        rvContacts.setAdapter(admobNativeAdAdapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(requireContext()));*//*

        this.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), this.recyclerView, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                AppsModelClass appsModelClass = AppsFragment.appsList.get(i);
                if (appsModelClass.isSelected()) {
                    appsModelClass.setSelected(false);
                    SelectedItems selectedItems = new SelectedItems(appsModelClass.getAppPkgName(), i, Constants.APPS, appsModelClass.getAppSize());
                    selectedItems.setItemName(appsModelClass.getAppName());
                    selectedItems.setItemIcon(appsModelClass.getAppIcon());
                    SelectedItemsArray.removeItem(selectedItems);
                    SelectedItemsArray.minusItemCount();
                    //FileShareFragment.main_bottom_id.setVisibility(View.GONE);
                    FileShareFragment.tv_itemCount.setText(String.valueOf(SelectedItemsArray.getItemCount()));
                    Log.d("fff", "onClick: position removed: " + i);
                    Log.d("fff", "onClick: " + SelectedItemsArray.getArraySize());
                } else {
                    appsModelClass.setSelected(true);
                    FileShareFragment.main_bottom_id.setVisibility(View.VISIBLE);
                    SelectedItems selectedItems2 = new SelectedItems(appsModelClass.getAppPkgName(), i, Constants.APPS, appsModelClass.getAppSize());
                    selectedItems2.setItemName(appsModelClass.getAppName());
                    selectedItems2.setItemIcon(appsModelClass.getAppIcon());
                    SelectedItemsArray.addItem(selectedItems2);
                    SelectedItemsArray.addItemCount();
                    FileShareFragment.tv_itemCount.setText(String.valueOf(SelectedItemsArray.getItemCount()));
                    Log.d("fff", "onClick: position added: " + i);
                    Log.d("fff", "onClick: " + SelectedItemsArray.getArraySize());
                }
                AppsFragment.appsAdapter.notifyItemChanged(i);
            }
        }));
    }




    public ArrayList<AppsModelClass> getInstalledApplications() {
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> queryIntentActivities = requireContext().getPackageManager().queryIntentActivities(intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            queryIntentActivities.sort(new ResolveInfo.DisplayNameComparator(getContext().getPackageManager()));
        }
        ArrayList<AppsModelClass> arrayList = new ArrayList<>();
        for (ResolveInfo next : queryIntentActivities) {
            try {
                if (next.activityInfo.applicationInfo.icon != 0 && (next.activityInfo.applicationInfo.flags & 1) == 0) {
                    Log.i("knownapp", next.activityInfo.applicationInfo.packageName + " is known and added to list");
                    AppsModelClass appsModelClass = new AppsModelClass();
                    appsModelClass.setAppName(getContext().getPackageManager().getApplicationLabel(next.activityInfo.applicationInfo).toString());
                    appsModelClass.setAppIcon(next.activityInfo.applicationInfo.loadIcon(getContext().getPackageManager()));
                    appsModelClass.setAppPkgName(String.valueOf(next.activityInfo.applicationInfo.packageName));
                    appsModelClass.setSelected(false);
                    appsModelClass.setAppSize(getStringSizeLengthFile(new File(next.activityInfo.applicationInfo.publicSourceDir).length()));
                    arrayList.add(appsModelClass);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public static String getStringSizeLengthFile(long j) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        DecimalFormat decimalFormat2 = new DecimalFormat("0");
        float f = (float) j;
        if (f < 1048576.0f) {
            return decimalFormat2.format((double) (f / 1024.0f)) + " Kb";
        }
        if (f < 1.07374182E9f) {
            return decimalFormat.format((double) (f / 1048576.0f)) + " Mb";
        }
        return f < 1.09951163E12f ? decimalFormat.format((double) (f / 1.07374182E9f)) + " Gb" : "";
    }

    public class AsyncTaskClass extends AsyncTask<String, String, ArrayList<AppsModelClass>> {
        public AsyncTaskClass() {
        }

        */
/* access modifiers changed from: protected *//*

        @SuppressLint("WrongConstant")
        public void onPreExecute() {
            super.onPreExecute();
            if (AppsFragment.this.progressBar.getVisibility() == 8 && AppsFragment.this.progressBar.isShown()) {
                AppsFragment.this.progressBar.setVisibility(0);
            }
        }

        */
/* access modifiers changed from: protected *//*

        public ArrayList<AppsModelClass> doInBackground(String... strArr) {
            if (strArr[0].equals("Apps")) {
                return AppsFragment.this.getInstalledApplications();
            }
            return null;
        }

        */
/* access modifiers changed from: protected *//*

        public void onPostExecute(ArrayList<AppsModelClass> arrayList) {
            super.onPostExecute(arrayList);
            if (arrayList != null) {
                AppsFragment.this.updateView(arrayList);
            }
        }
    }

    */
/* access modifiers changed from: private *//*

    @SuppressLint("WrongConstant")
    public void updateView(ArrayList<AppsModelClass> arrayList) {
        appsList = arrayList;
        if (this.progressBar.getVisibility() == 0) {
            this.progressBar.setVisibility(8);
        }
        AppsAdapter appsAdapter2 = new AppsAdapter(getContext(), arrayList);
        appsAdapter = appsAdapter2;
        this.recyclerView.setAdapter(appsAdapter2);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, 1, false));
    }

    public void onDestroy() {
        Toast.makeText(getActivity(), "destroyedd", Toast.LENGTH_SHORT).show();
        try {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.mMessageReceiver);
        } catch (Exception e) {
            Log.e("UnRegister Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        // Reload the data when the fragment becomes visible again
        new AsyncTaskClass().execute("Apps");
        updateData();
*/
/*
        if (AppsFragment.this.progressBar.getVisibility() == View.GONE && AppsFragment.this.progressBar.isShown()) {
            AppsFragment.this.progressBar.setVisibility(View.VISIBLE);
        }*//*

    }

    private void updateData() {
        // You can check if data is already loaded to avoid redundant reloading
        if (appsList == null || appsList.isEmpty()) {
            new AsyncTaskClass().execute("Apps");
        }
    }
}*/
