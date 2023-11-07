package com.example.bluetoothfiletransfer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetoothfiletransfer.adapters.Adapterrr;
import com.example.bluetoothfiletransfer.adapters.ViewPagerAdapter;
import com.example.bluetoothfiletransfer.databinding.FragmentFileShareBinding;
import com.example.bluetoothfiletransfer.databinding.FragmentScanDeviceBinding;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItems;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItemsArray;
import com.example.bluetoothfiletransfer.utils.Constants;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class FileShareFragment extends Fragment {

    FragmentFileShareBinding binding;
    static final int APPS_TAB = 0;
    static final int FILE_TAB = 4;
    static final int MUSIC_TAB = 3;
    static final int PICS_TAB = 1;
    static final int VIDEO_TAB = 2;
    static String font1 = "DancingScript-Regular.otf";
    static String font2 = "Lobster_1.3.otf";
    public static TextView tv_itemCount;
    TextView appTitle;
    ImageView btnBack;
    ImageView btnSearch;
    TextView btnSend;
    ViewPagerAdapter customPagerAdapter;
    private DrawerLayout drawer;
    ImageView imageApps;
    ImageView imageFiles;
    ImageView imageMusic;
    ImageView imagePics;
    ImageView imageVideos;
    int increment = 0;
    int increment2 = 0;
    LinearLayout linearAppBar;
    LinearLayout linearApps;
    LinearLayout linearFiles;
    LinearLayout linearMusic;
    LinearLayout linearPics;
    LinearLayout linearSearchView;
    LinearLayout linearSend;
    LinearLayout linearVideos;
    NavigationView nav;
    ImageView nav_menu;
    //    String pakgID = getActivity().getPackageName().toString();
    ProgressDialog progressDialog;
    SearchView searchView;
    LinearLayout selectedItemLinear;
    public int selectedTab;
    TextView textApps;
    TextView textFiles;
    TextView textMusic;
    TextView textPics;
    TextView textVideos;
    ViewPager viewPager;
    int i = 1;
    Adapterrr adapterrr;

    //    ViewPagerAdapter viewPagerAdapter, viewPagerAdapter2;
//    public static TextView tv_itemCount;
//    int i = 1;
//    LinearLayout linearSend;
//    ImageView btnSearch;
//    TextView appTitle;
//    SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getActivity(), "creatview", Toast.LENGTH_SHORT).show();
        binding = FragmentFileShareBinding.inflate(inflater, container, false);
//        this.drawer = (DrawerLayout) binding.getRoot().findViewById(R.id.drawer);
        this.viewPager = binding.getRoot().findViewById(R.id.viewpager);
        this.imageApps = binding.getRoot().findViewById(R.id.appsIcon);
        this.imagePics = binding.getRoot().findViewById(R.id.picsIcon);
        this.imageVideos = binding.getRoot().findViewById(R.id.videoIcon);
        this.imageMusic = binding.getRoot().findViewById(R.id.musicIcon);
        this.imageFiles = binding.getRoot().findViewById(R.id.fileIcon);
        this.textApps = binding.getRoot().findViewById(R.id.appsText);
        this.textPics = binding.getRoot().findViewById(R.id.picsText);
        this.textVideos = binding.getRoot().findViewById(R.id.videoText);
        this.textMusic = binding.getRoot().findViewById(R.id.musicText);
        this.textFiles = binding.getRoot().findViewById(R.id.fileText);
        this.selectedItemLinear = binding.getRoot().findViewById(R.id.selectedItemLinear);
        this.btnSend = binding.getRoot().findViewById(R.id.btnSend);
        this.btnSearch = binding.getRoot().findViewById(R.id.btnSearch);
        this.linearAppBar = binding.getRoot().findViewById(R.id.linearAppBar);
        this.linearSearchView = binding.getRoot().findViewById(R.id.linearSearchView);
        this.searchView = binding.getRoot().findViewById(R.id.searchView);
        this.btnBack = binding.getRoot().findViewById(R.id.btnBack);
        tv_itemCount = binding.getRoot().findViewById(R.id.tv_itemCount);
        this.appTitle = binding.getRoot().findViewById(R.id.appTitle);
        this.linearApps = binding.getRoot().findViewById(R.id.linearApps);
        this.linearPics = binding.getRoot().findViewById(R.id.linearPics);
        this.linearVideos = binding.getRoot().findViewById(R.id.linearvideos);
        this.linearMusic = binding.getRoot().findViewById(R.id.linearMusic);
        this.linearFiles = binding.getRoot().findViewById(R.id.linearFiles);
        this.linearSend = binding.getRoot().findViewById(R.id.linearClearSend);
        this.nav_menu = binding.getRoot().findViewById(R.id.nav_menu);
        ((EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text)).setTextColor(getResources()
                .getColor(R.color.black));
        final ImageView imageView = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        imageView.setColorFilter(-1);
        this.searchView.setOnSearchClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                imageView.setVisibility(searchView.getQuery().toString().isEmpty() ? 8 : 0);
            }
        });
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            @SuppressLint("WrongConstant")
            public boolean onQueryTextChange(String str) {
                imageView.setVisibility(str.isEmpty() ? 8 : 0);
                try {
                    AppsFragment.appsAdapter.getFilter().filter(str);
                    PicturesFragment.picsAdapter.getFilter().filter(str);
                    VideosFragment.videoAdapter.getFilter().filter(str);
                    MusicFragment.musicAdapter.getFilter().filter(str);
                    FilesFragment.filesAdapter.getFilter().filter(str);
                    Log.d("ggg", "onQueryTextChange: " + str);
                } catch (NullPointerException e) {
                    Log.d("exex", "onQueryTextChange: Exception " + e);
                }
                return false;
            }
        });

        adapterrr = new Adapterrr(getActivity().getSupportFragmentManager());
        setupViewPager();


        tv_itemCount.setText(String.valueOf(SelectedItemsArray.getItemCount()));
        ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading! please wait.");
        linearVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videosTabSelected();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearAppBar.getVisibility() == View.GONE && linearSearchView.getVisibility() == View.VISIBLE) {
                    linearAppBar.setVisibility(View.VISIBLE);
                    linearSearchView.setVisibility(View.GONE);
                    searchView.setQuery((CharSequence) null, false);

                }

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (linearAppBar.getVisibility() == View.VISIBLE && linearSearchView.getVisibility() == View.INVISIBLE) {
                        linearAppBar.setVisibility(View.INVISIBLE);
                        linearSearchView.setVisibility(View.VISIBLE);
                        searchView.setIconified(false);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        linearApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "appsbtn", Toast.LENGTH_SHORT).show();
                appsTabSelected();
            }
        });
        LinearLayout linearClearSend = binding.getRoot().findViewById(R.id.linearClearSend);
        linearClearSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendByBluetooth();

            }
        });
        linearFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "filesBtn", Toast.LENGTH_SHORT).show();
                fileTabSelected();
            }
        });
        linearMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicTabSelected();
            }
        });
        linearPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picsTabSelected();
            }
        });
        return binding.getRoot();

    }

    public void appsTabSelected() {
        binding.viewpager.setCurrentItem(0);

//        adapterrr.addFrag(new AppsFragment(), "Apps");
//        viewPager.setAdapter(adapterrr);
    }

    public void picsTabSelected() {
        binding.viewpager.setCurrentItem(1);
//        adapterrr.addFrag(new PicturesFragment(), "Pic");
//        viewPager.setAdapter(adapterrr);
    }

    public void videosTabSelected() {

        binding.viewpager.setCurrentItem(2);
//        adapterrr.addFrag(new VideosFragment(), "Video");
//        viewPager.setAdapter(adapterrr);
    }

    public void musicTabSelected() {
//        adapterrr.addFrag(new MusicFragment(), "Music");
//        viewPager.setAdapter(adapterrr);
        binding.viewpager.setCurrentItem(3);
    }

    public void fileTabSelected() {
//        adapterrr.addFrag(new FilesFragment(), "File");
//        viewPager.setAdapter(adapterrr);
        binding.viewpager.setCurrentItem(4);
//        customPagerAdapter.getItem(4);
    }

    @SuppressLint("WrongConstant")
    private void sendByBluetooth() {
        final ArrayList arrayList = new ArrayList();
        ArrayList<SelectedItems> allSelectedItems = SelectedItemsArray.getAllSelectedItems();
        if (!allSelectedItems.isEmpty()) {
            Iterator<SelectedItems> it = allSelectedItems.iterator();
            while (it.hasNext()) {
                SelectedItems next = it.next();
                try {
                    if (next.getFragName().equals(Constants.APPS)) {
                        arrayList.add(FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", new File(getActivity().getPackageManager().getApplicationInfo(next.getImgPath(), 0).publicSourceDir)));
                    } else {
                        arrayList.add(FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", new File(next.getImgPath())));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
            intent.setPackage("com.android.bluetooth");
            intent.setType("application/*");
            intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
            startActivity(Intent.createChooser(intent, (CharSequence) null));
            Log.d("TAG", "The interstitial wasn't loaded yet.");
            return;
        }
        Toast.makeText(getActivity(), "Select Some Files", 0).show();
    }
    private void setupViewPager() {
//        adapterrr.addFrag(new AppsFragment(),"Apps");
//        viewPager.setAdapter(adapterrr);
        if (viewPager.getCurrentItem() == 0) {
            appsTabSelected();
        }
        ViewPagerAdapter customPagerAdapter2 = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        customPagerAdapter = customPagerAdapter2;

        viewPager.setAdapter(customPagerAdapter2);
        if (customPagerAdapter.getCount() > 1) {
            i = this.customPagerAdapter.getCount() - 1;
        }
        viewPager.setOffscreenPageLimit(i);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                if (i == 0) {
                    appsTabSelected();
                } else if (i == 1) {
                    picsTabSelected();
                } else if (i == 2) {
                    videosTabSelected();
                } else if (i == 3) {
                    musicTabSelected();
                } else if (i == 4) {
                    fileTabSelected();
                }
            }
        });
//        ViewPagerAdapter customPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
//        viewPager.setAdapter(customPagerAdapter);
//        viewPager.setOffscreenPageLimit(customPagerAdapter.getCount() - 1);

    }


}