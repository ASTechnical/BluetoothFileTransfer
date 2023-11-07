package com.example.bluetoothfiletransfer;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bluetoothfiletransfer.adapters.AppsAdapter;
import com.example.bluetoothfiletransfer.modelclasses.AppsModelClass;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItems;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItemsArray;
import com.example.bluetoothfiletransfer.utils.Constants;
import com.example.bluetoothfiletransfer.utils.RecyclerTouchListener;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class AppsFragment extends Fragment {

    public static AppsAdapter appsAdapter;
    public static List<AppsModelClass> appsList = new ArrayList();
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("message");
            int intExtra = intent.getIntExtra("position", -1);
            Log.d("fff", "onReceive: " + intExtra);
            if (stringExtra.equals(Constants.APPS)) {
                if (intExtra != -1) {
                    try {
                        AppsModelClass appsModelClass = (AppsModelClass) AppsFragment.appsList.get(intExtra);
                        if (appsModelClass.isSelected()) {
                            appsModelClass.setSelected(false);
                        }
                        AppsFragment.appsAdapter.notifyItemChanged(intExtra);
                        Log.d("rrr", "mMessageReceiver:pos: " + intExtra);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Iterator<Integer> it = intent.getIntegerArrayListExtra("positionArray").iterator();
                    while (it.hasNext()) {
                        int intValue = it.next().intValue();
                        AppsModelClass appsModelClass2 = (AppsModelClass) AppsFragment.appsList.get(intValue);
                        if (appsModelClass2.isSelected()) {
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
        View view= layoutInflater.inflate(R.layout.fragment_apps, viewGroup, false);
        Toast.makeText(getActivity(), "apps", Toast.LENGTH_SHORT).show();
        return view;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(this.mMessageReceiver, new IntentFilter("custom-event-name"));
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        new AsyncTaskClass().execute(new String[]{"Apps"});
        this.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), this.recyclerView, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                AppsModelClass appsModelClass = (AppsModelClass) AppsFragment.appsList.get(i);
                if (appsModelClass.isSelected()) {
                    appsModelClass.setSelected(false);
                    SelectedItems selectedItems = new SelectedItems(appsModelClass.getAppPkgName(), i, Constants.APPS, appsModelClass.getAppSize());
                    selectedItems.setItemName(appsModelClass.getAppName());
                    selectedItems.setItemIcon(appsModelClass.getAppIcon());
                    SelectedItemsArray.removeItem(selectedItems);
                    SelectedItemsArray.minusItemCount();
                    FileShareFragment.tv_itemCount.setText(String.valueOf(SelectedItemsArray.getItemCount()));
                    Log.d("fff", "onClick: position removed: " + i);
                    Log.d("fff", "onClick: " + SelectedItemsArray.getArraySize());
                } else {
                    appsModelClass.setSelected(true);
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
        List<ResolveInfo> queryIntentActivities = getContext().getPackageManager().queryIntentActivities(intent, 0);
        Collections.sort(queryIntentActivities, new ResolveInfo.DisplayNameComparator(getContext().getPackageManager()));
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

        /* access modifiers changed from: protected */
        @SuppressLint("WrongConstant")
        public void onPreExecute() {
            super.onPreExecute();
            if (AppsFragment.this.progressBar.getVisibility() == 8 && AppsFragment.this.progressBar.isShown()) {
                AppsFragment.this.progressBar.setVisibility(0);
            }
        }

        /* access modifiers changed from: protected */
        public ArrayList<AppsModelClass> doInBackground(String... strArr) {
            if (strArr[0].equals("Apps")) {
                return AppsFragment.this.getInstalledApplications();
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(ArrayList<AppsModelClass> arrayList) {
            super.onPostExecute(arrayList);
            if (arrayList != null) {
                AppsFragment.this.updateView(arrayList);
            }
        }
    }

    /* access modifiers changed from: private */
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
}