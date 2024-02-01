package com.example.bluetoothfiletransfer.Fragments;

import static android.os.Build.VERSION.SDK_INT;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetoothfiletransfer.R;
import com.example.bluetoothfiletransfer.adapters.Adapterrr;
import com.example.bluetoothfiletransfer.adapters.ViewPagerAdapter;
import com.example.bluetoothfiletransfer.databinding.FragmentFileShareBinding;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItems;
import com.example.bluetoothfiletransfer.modelclasses.SelectedItemsArray;
import com.example.bluetoothfiletransfer.utils.Constants;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;

public class FileShareFragment extends Fragment {

    FragmentFileShareBinding binding;
    static final int APPS_TAB = 0;
    static final int FILE_TAB = 4;
    static final int MUSIC_TAB = 3;
    static final int PICS_TAB = 1;
    static final int VIDEO_TAB = 2;
    public static TextView tv_itemCount;
    TextView appTitle;
    ImageView btnBack;
    ImageView btnSearch;
    TextView btnSend;
    ViewPagerAdapter customPagerAdapter;
    ImageView imageApps;
    ImageView imageFiles;
    ImageView imageMusic;
    ImageView imagePics;
    ImageView imageVideos;

    LinearLayout linearAppBar;
    LinearLayout linearApps;
    LinearLayout linearFiles;
    LinearLayout linearMusic;
    LinearLayout linearPics;
    LinearLayout linearSearchView;
    LinearLayout linearSend;
    LinearLayout linearVideos;
    ImageView nav_menu;

    SearchView searchView;
    LinearLayout selectedItemLinear;
    public static LinearLayout main_bottom_id;
    InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    View view_app;
    View view_pic;
    View view_video;
    View view_music;
    View view_file;
    TextView textApps;
    TextView textFiles;
    TextView textMusic;
    TextView textPics;
    TextView textVideos;
    ViewPager viewPager;
    int i = 1;
    Adapterrr adapterrr;
    AppCompatButton cancelButton;
    ImageView closeButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFileShareBinding.inflate(inflater, container, false);
        adRequest = new AdRequest.Builder().build();
        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
               // showmInterstitialAd(null);
            }
        });


        this.viewPager = binding.getRoot().findViewById(R.id.viewpager);
        this.imageApps = binding.getRoot().findViewById(R.id.appsIcon);
        this.imagePics = binding.getRoot().findViewById(R.id.picsIcon);
        this.imageVideos = binding.getRoot().findViewById(R.id.videoIcon);
        this.imageMusic = binding.getRoot().findViewById(R.id.musicIcon);
        this.imageFiles = binding.getRoot().findViewById(R.id.file_icon);
        this.textApps = binding.getRoot().findViewById(R.id.appsText);
        this.textPics = binding.getRoot().findViewById(R.id.picsText);
        this.textVideos = binding.getRoot().findViewById(R.id.videoText);
        this.textMusic = binding.getRoot().findViewById(R.id.musicText);
        this.textFiles = binding.getRoot().findViewById(R.id.fileText);
        this.selectedItemLinear = binding.getRoot().findViewById(R.id.selectedItemLinear);
        main_bottom_id = binding.getRoot().findViewById(R.id.main_bottom_id);

        this.view_app = binding.getRoot().findViewById(R.id.view_app);
        this.view_pic = binding.getRoot().findViewById(R.id.view_pic);
        this.view_music = binding.getRoot().findViewById(R.id.view_music);
        this.view_video = binding.getRoot().findViewById(R.id.view_video);
        this.view_file = binding.getRoot().findViewById(R.id.view_file);

        this.btnSend = binding.getRoot().findViewById(R.id.btnSend);
        this.btnSearch = binding.getRoot().findViewById(R.id.btnSearch);
        this.linearAppBar = binding.getRoot().findViewById(R.id.linearAppBar);
        this.linearSearchView = binding.getRoot().findViewById(R.id.linearSearchView);
        this.searchView = binding.getRoot().findViewById(R.id.searchView);
        //this.btnBack = binding.getRoot().findViewById(R.id.btn_back);
        tv_itemCount = binding.getRoot().findViewById(R.id.tv_itemCount);
        this.appTitle = binding.getRoot().findViewById(R.id.appTitle);
        this.linearApps = binding.getRoot().findViewById(R.id.linearApps);
        this.linearPics = binding.getRoot().findViewById(R.id.linearPics);
        this.linearVideos = binding.getRoot().findViewById(R.id.linearvideos);
        this.linearMusic = binding.getRoot().findViewById(R.id.linearMusic);
        this.linearFiles = binding.getRoot().findViewById(R.id.linearFiles);
        this.linearSend = binding.getRoot().findViewById(R.id.linearClearSend);
        this.nav_menu = binding.getRoot().findViewById(R.id.nav_menu);

        ((EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text))
                .setTextColor(getResources().getColor(R.color.black));

        closeButton= searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        closeButton.setImageResource(R.drawable.closed_ic);
        cancelButton = binding.getRoot().findViewById(R.id.cancelButton);
        closeButton.clearColorFilter();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                // Show or hide the close button based on the search text
                closeButton.setVisibility(searchAutoComplete.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
                showSearchLayout();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the search view
             hideSearchLayout();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @SuppressLint("WrongConstant")
            @Override
            public boolean onQueryTextChange(String newText) {
                // Show or hide the close button based on the search text
                closeButton.setVisibility(newText.isEmpty() ? View.GONE : View.VISIBLE);
                // Filter data based on the search text
                try {
                    AppsFragment.appsAdapter.getFilter().filter(newText);
                    PicturesFragment.picsAdapter.getFilter().filter(newText);
                    VideosFragment.videoAdapter.getFilter().filter(newText);
                    MusicFragment.musicAdapter.getFilter().filter(newText);
                    FilesFragment.filesAdapter.getFilter().filter(newText);
                    Log.d("ggg", "onQueryTextChange: " + newText);
                } catch (NullPointerException e) {
                    Log.d("exex", "onQueryTextChange: Exception " + e);
                }

                return false;
            }
        });


        searchView.setBackgroundResource(R.drawable.search_rounded_bg);

        adapterrr = new Adapterrr(getActivity().getSupportFragmentManager());
        setupViewPager();


        tv_itemCount.setText(String.valueOf(SelectedItemsArray.getItemCount()));

        linearVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videosTabSelected();
            }
        });
       /* btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearAppBar.getVisibility() == View.GONE &&
                        linearSearchView.getVisibility() == View.VISIBLE) {
                    linearAppBar.setVisibility(View.VISIBLE);
                    linearSearchView.setVisibility(View.GONE);
                    searchView.setQuery((CharSequence) null, false);

                }

            }
        });*/
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (linearAppBar.getVisibility() == View.VISIBLE &&
                            linearSearchView.getVisibility() == View.INVISIBLE) {
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

                appsTabSelected();
                textApps.setTextColor(getResources().getColor(R.color.splash));
                imageApps.setColorFilter(getResources().getColor(R.color.splash));

                view_app.setBackgroundColor(getResources().getColor(R.color.splash));
                view_pic.setBackgroundColor(getResources().getColor(R.color.lightGray));
                view_music.setBackgroundColor(getResources().getColor(R.color.lightGray));
                view_video.setBackgroundColor(getResources().getColor(R.color.lightGray));
                view_file.setBackgroundColor(getResources().getColor(R.color.lightGray));

                textPics.setTextColor(getResources().getColor(R.color.gray));
                imagePics.setColorFilter(getResources().getColor(R.color.gray));

                textVideos.setTextColor(getResources().getColor(R.color.gray));
                imageVideos.setColorFilter(getResources().getColor(R.color.gray));

                textMusic.setTextColor(getResources().getColor(R.color.gray));
                imageMusic.setColorFilter(getResources().getColor(R.color.gray));

                textFiles.setTextColor(getResources().getColor(R.color.gray));
                imageFiles.setColorFilter(getResources().getColor(R.color.gray));

            }
        });

        linearSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitialAdBeforeBluetooth();
            }
        });
        linearFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fileTabSelected();
                textFiles.setTextColor(getResources().getColor(R.color.splash));
                imageFiles.setColorFilter(getResources().getColor(R.color.splash));

                view_app.setBackgroundColor(getResources().getColor(R.color.lightGray));
                view_pic.setBackgroundColor(getResources().getColor(R.color.lightGray));
                view_music.setBackgroundColor(getResources().getColor(R.color.lightGray));
                view_video.setBackgroundColor(getResources().getColor(R.color.lightGray));
                view_file.setBackgroundColor(getResources().getColor(R.color.splash));

                textApps.setTextColor(getResources().getColor(R.color.gray));
                imageApps.setColorFilter(getResources().getColor(R.color.gray));

                textPics.setTextColor(getResources().getColor(R.color.gray));
                imagePics.setColorFilter(getResources().getColor(R.color.gray));

                textVideos.setTextColor(getResources().getColor(R.color.gray));
                imageVideos.setColorFilter(getResources().getColor(R.color.gray));

                textMusic.setTextColor(getResources().getColor(R.color.gray));
                imageMusic.setColorFilter(getResources().getColor(R.color.gray));
            }
        });
        linearMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicTabSelected();
                textMusic.setTextColor(getResources().getColor(R.color.splash));
                imageMusic.setColorFilter(getResources().getColor(R.color.splash));

                view_app.setBackgroundColor(getResources().getColor(R.color.lightGray));
                view_pic.setBackgroundColor(getResources().getColor(R.color.lightGray));
                view_music.setBackgroundColor(getResources().getColor(R.color.splash));
                view_video.setBackgroundColor(getResources().getColor(R.color.lightGray));
                view_file.setBackgroundColor(getResources().getColor(R.color.lightGray));

                textApps.setTextColor(getResources().getColor(R.color.gray));
                imageApps.setColorFilter(getResources().getColor(R.color.gray));

                textPics.setTextColor(getResources().getColor(R.color.gray));
                imagePics.setColorFilter(getResources().getColor(R.color.gray));

                textVideos.setTextColor(getResources().getColor(R.color.gray));
                imageVideos.setColorFilter(getResources().getColor(R.color.gray));


                textFiles.setTextColor(getResources().getColor(R.color.gray));
                imageFiles.setColorFilter(getResources().getColor(R.color.gray));
            }
        });
        linearPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picsTabSelected();
                textPics.setTextColor(getResources().getColor(R.color.splash));
                imagePics.setColorFilter(getResources().getColor(R.color.splash));
                textApps.setTextColor(getResources().getColor(R.color.gray));
                imageApps.setColorFilter(getResources().getColor(R.color.gray));

                view_app.setBackgroundColor(getResources().getColor(R.color.lightGray));
                view_pic.setBackgroundColor(getResources().getColor(R.color.splash));
                view_music.setBackgroundColor(getResources().getColor(R.color.lightGray));
                view_video.setBackgroundColor(getResources().getColor(R.color.lightGray));
                view_file.setBackgroundColor(getResources().getColor(R.color.lightGray));

                textVideos.setTextColor(getResources().getColor(R.color.gray));
                imageVideos.setColorFilter(getResources().getColor(R.color.gray));

                textMusic.setTextColor(getResources().getColor(R.color.gray));
                imageMusic.setColorFilter(getResources().getColor(R.color.gray));

                textFiles.setTextColor(getResources().getColor(R.color.gray));
                imageFiles.setColorFilter(getResources().getColor(R.color.gray));
            }
        });
        return binding.getRoot();

    }
    private void showInterstitialAdBeforeBluetooth() {

        showmInterstitialAd(() -> sendByBluetooth());
    }
    static void updateVisibility() {
        if (tv_itemCount != null && Integer.parseInt(tv_itemCount.getText().toString()) > 0) {
            main_bottom_id.setVisibility(View.VISIBLE);
        } else {
            main_bottom_id.setVisibility(View.GONE);
        }
    }


    public void showSearchLayout() {
        cancelButton.setVisibility(View.VISIBLE);
        linearSearchView.setVisibility(View.VISIBLE);

    }
    private void showDefaultLayout() {
        linearAppBar.setVisibility(View.VISIBLE);

    }
    private void hideSearchLayout() {
        searchView.setIconified(true);
        linearSearchView.setVisibility(View.GONE);
        showDefaultLayout();
    }


    public void showmInterstitialAd(final Runnable adCallback) {
        InterstitialAd.load(requireContext(), getString(R.string.inter_ad_unit_id), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("Error", loadAdError.toString());
                // If ad fails to load, execute the callback immediately
                adCallback.run();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        // Ad dismissed, execute the callback
                        adCallback.run();
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        mInterstitialAd = null;


                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();


                    }
                });
                mInterstitialAd.show(requireActivity());
            }
        });
    }

    public void appsTabSelected() {
        binding.viewpager.setCurrentItem(0);
        textApps.setTextColor(getResources().getColor(R.color.splash));
        imageApps.setColorFilter(getResources().getColor(R.color.splash));

        view_app.setBackgroundColor(getResources().getColor(R.color.splash));
        view_pic.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_music.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_video.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_file.setBackgroundColor(getResources().getColor(R.color.lightGray));

        textPics.setTextColor(getResources().getColor(R.color.gray));
        imagePics.setColorFilter(getResources().getColor(R.color.gray));

        textVideos.setTextColor(getResources().getColor(R.color.gray));
        imageVideos.setColorFilter(getResources().getColor(R.color.gray));

        textMusic.setTextColor(getResources().getColor(R.color.gray));
        imageMusic.setColorFilter(getResources().getColor(R.color.gray));

        textFiles.setTextColor(getResources().getColor(R.color.gray));
        imageFiles.setColorFilter(getResources().getColor(R.color.gray));
//        adapterrr.addFrag(new AppsFragment(), "Apps");
//        viewPager.setAdapter(adapterrr);
    }

    public void picsTabSelected() {
        binding.viewpager.setCurrentItem(1);
//        adapterrr.addFrag(new PicturesFragment(), "Pic");
//        viewPager.setAdapter(adapterrr);
        textPics.setTextColor(getResources().getColor(R.color.splash));
        imagePics.setColorFilter(getResources().getColor(R.color.splash));
        textApps.setTextColor(getResources().getColor(R.color.gray));
        imageApps.setColorFilter(getResources().getColor(R.color.gray));

        view_app.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_pic.setBackgroundColor(getResources().getColor(R.color.splash));
        view_music.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_video.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_file.setBackgroundColor(getResources().getColor(R.color.lightGray));

        textVideos.setTextColor(getResources().getColor(R.color.gray));
        imageVideos.setColorFilter(getResources().getColor(R.color.gray));

        textMusic.setTextColor(getResources().getColor(R.color.gray));
        imageMusic.setColorFilter(getResources().getColor(R.color.gray));

        textFiles.setTextColor(getResources().getColor(R.color.gray));
        imageFiles.setColorFilter(getResources().getColor(R.color.gray));
    }

    public void videosTabSelected() {

        binding.viewpager.setCurrentItem(2);
//        adapterrr.addFrag(new VideosFragment(), "Video");
//        viewPager.setAdapter(adapterrr);
        textVideos.setTextColor(getResources().getColor(R.color.splash));
        imageVideos.setColorFilter(getResources().getColor(R.color.splash));
        textApps.setTextColor(getResources().getColor(R.color.gray));
        imageApps.setColorFilter(getResources().getColor(R.color.gray));

        view_app.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_pic.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_music.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_video.setBackgroundColor(getResources().getColor(R.color.splash));
        view_file.setBackgroundColor(getResources().getColor(R.color.lightGray));

        textPics.setTextColor(getResources().getColor(R.color.gray));
        imagePics.setColorFilter(getResources().getColor(R.color.gray));


        textMusic.setTextColor(getResources().getColor(R.color.gray));
        imageMusic.setColorFilter(getResources().getColor(R.color.gray));

        textFiles.setTextColor(getResources().getColor(R.color.gray));
        imageFiles.setColorFilter(getResources().getColor(R.color.gray));
    }

    public void musicTabSelected() {
//        adapterrr.addFrag(new MusicFragment(), "Music");
//        viewPager.setAdapter(adapterrr);
        binding.viewpager.setCurrentItem(3);
        textMusic.setTextColor(getResources().getColor(R.color.splash));
        imageMusic.setColorFilter(getResources().getColor(R.color.splash));
        textApps.setTextColor(getResources().getColor(R.color.gray));
        imageApps.setColorFilter(getResources().getColor(R.color.gray));

        view_app.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_pic.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_music.setBackgroundColor(getResources().getColor(R.color.splash));
        view_video.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_file.setBackgroundColor(getResources().getColor(R.color.lightGray));

        textPics.setTextColor(getResources().getColor(R.color.gray));
        imagePics.setColorFilter(getResources().getColor(R.color.gray));

        textVideos.setTextColor(getResources().getColor(R.color.gray));
        imageVideos.setColorFilter(getResources().getColor(R.color.gray));


        textFiles.setTextColor(getResources().getColor(R.color.gray));
        imageFiles.setColorFilter(getResources().getColor(R.color.gray));
    }

    public void fileTabSelected() {
//        adapterrr.addFrag(new FilesFragment(), "File");
//        viewPager.setAdapter(adapterrr);
        binding.viewpager.setCurrentItem(4);
        textFiles.setTextColor(getResources().getColor(R.color.splash));
        imageFiles.setColorFilter(getResources().getColor(R.color.splash));
        textApps.setTextColor(getResources().getColor(R.color.gray));
        imageApps.setColorFilter(getResources().getColor(R.color.gray));

        view_app.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_pic.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_music.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_video.setBackgroundColor(getResources().getColor(R.color.lightGray));
        view_file.setBackgroundColor(getResources().getColor(R.color.splash));

        textPics.setTextColor(getResources().getColor(R.color.gray));
        imagePics.setColorFilter(getResources().getColor(R.color.gray));

        textVideos.setTextColor(getResources().getColor(R.color.gray));
        imageVideos.setColorFilter(getResources().getColor(R.color.gray));

        textMusic.setTextColor(getResources().getColor(R.color.gray));
        imageMusic.setColorFilter(getResources().getColor(R.color.gray));

//        customPagerAdapter.getItem(4);
    }

    @SuppressLint("WrongConstant")
    private void sendByBluetooth() {
        final ArrayList arrayList = new ArrayList();

        ArrayList<SelectedItems> allSelectedItems = SelectedItemsArray.getAllSelectedItems();
        if (!allSelectedItems.isEmpty()) {
            main_bottom_id.setVisibility(View.VISIBLE);

            for (SelectedItems next : allSelectedItems) {
                try {
                    if (next.getFragName().equals(Constants.APPS)) {
                        arrayList.add(FileProvider.getUriForFile(getActivity(),
                                getActivity().getPackageName() +
                                        ".provider", new File(getActivity().getPackageManager().getApplicationInfo(next.getImgPath(), 0).publicSourceDir)));
                    } else {
                        arrayList.add(FileProvider.getUriForFile(getActivity(),
                                getActivity().getPackageName() +
                                        ".provider", new File(next.getImgPath())));
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
        main_bottom_id.setVisibility(View.GONE);

    }

    private void setupViewPager() {
//        adapterrr.addFrag(new AppsFragment(),"Apps");
//        viewPager.setAdapter(adapterrr);
        if (viewPager.getCurrentItem() == 0) {
            appsTabSelected();
            textApps.setTextColor(getResources().getColor(R.color.splash));
            imageApps.setColorFilter(getResources().getColor(R.color.splash));

            view_app.setBackgroundColor(getResources().getColor(R.color.splash));
            view_pic.setBackgroundColor(getResources().getColor(R.color.lightGray));
            view_music.setBackgroundColor(getResources().getColor(R.color.lightGray));
            view_video.setBackgroundColor(getResources().getColor(R.color.lightGray));
            view_file.setBackgroundColor(getResources().getColor(R.color.lightGray));

            textPics.setTextColor(getResources().getColor(R.color.gray));
            imagePics.setColorFilter(getResources().getColor(R.color.gray));

            textVideos.setTextColor(getResources().getColor(R.color.gray));
            imageVideos.setColorFilter(getResources().getColor(R.color.gray));

            textMusic.setTextColor(getResources().getColor(R.color.gray));
            imageMusic.setColorFilter(getResources().getColor(R.color.gray));

            textFiles.setTextColor(getResources().getColor(R.color.gray));
            imageFiles.setColorFilter(getResources().getColor(R.color.gray));

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
                    textApps.setTextColor(getResources().getColor(R.color.splash));
                    imageApps.setColorFilter(getResources().getColor(R.color.splash));
                    view_app.setBackgroundColor(getResources().getColor(R.color.splash));
                    view_pic.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_music.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_video.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_file.setBackgroundColor(getResources().getColor(R.color.lightGray));

                    textPics.setTextColor(getResources().getColor(R.color.gray));
                    imagePics.setColorFilter(getResources().getColor(R.color.gray));

                    textVideos.setTextColor(getResources().getColor(R.color.gray));
                    imageVideos.setColorFilter(getResources().getColor(R.color.gray));

                    textMusic.setTextColor(getResources().getColor(R.color.gray));
                    imageMusic.setColorFilter(getResources().getColor(R.color.gray));

                    textFiles.setTextColor(getResources().getColor(R.color.gray));
                    imageFiles.setColorFilter(getResources().getColor(R.color.gray));
                } else if (i == 1) {
                    picsTabSelected();
                    textPics.setTextColor(getResources().getColor(R.color.splash));
                    imagePics.setColorFilter(getResources().getColor(R.color.splash));
                    textApps.setTextColor(getResources().getColor(R.color.gray));
                    imageApps.setColorFilter(getResources().getColor(R.color.gray));
                    view_app.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_pic.setBackgroundColor(getResources().getColor(R.color.splash));
                    view_music.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_video.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_file.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    textVideos.setTextColor(getResources().getColor(R.color.gray));
                    imageVideos.setColorFilter(getResources().getColor(R.color.gray));

                    textMusic.setTextColor(getResources().getColor(R.color.gray));
                    imageMusic.setColorFilter(getResources().getColor(R.color.gray));

                    textFiles.setTextColor(getResources().getColor(R.color.gray));
                    imageFiles.setColorFilter(getResources().getColor(R.color.gray));
                } else if (i == 2) {
                    videosTabSelected();
                    textVideos.setTextColor(getResources().getColor(R.color.splash));
                    imageVideos.setColorFilter(getResources().getColor(R.color.splash));
                    view_app.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_pic.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_music.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_video.setBackgroundColor(getResources().getColor(R.color.splash));
                    view_file.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    textApps.setTextColor(getResources().getColor(R.color.gray));
                    imageApps.setColorFilter(getResources().getColor(R.color.gray));

                    textPics.setTextColor(getResources().getColor(R.color.gray));
                    imagePics.setColorFilter(getResources().getColor(R.color.gray));


                    textMusic.setTextColor(getResources().getColor(R.color.gray));
                    imageMusic.setColorFilter(getResources().getColor(R.color.gray));

                    textFiles.setTextColor(getResources().getColor(R.color.gray));
                    imageFiles.setColorFilter(getResources().getColor(R.color.gray));
                } else if (i == 3) {
                    musicTabSelected();
                    textMusic.setTextColor(getResources().getColor(R.color.splash));
                    imageMusic.setColorFilter(getResources().getColor(R.color.splash));
                    textApps.setTextColor(getResources().getColor(R.color.gray));
                    imageApps.setColorFilter(getResources().getColor(R.color.gray));
                    view_app.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_pic.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_music.setBackgroundColor(getResources().getColor(R.color.splash));
                    view_video.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_file.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    textPics.setTextColor(getResources().getColor(R.color.gray));
                    imagePics.setColorFilter(getResources().getColor(R.color.gray));

                    textVideos.setTextColor(getResources().getColor(R.color.gray));
                    imageVideos.setColorFilter(getResources().getColor(R.color.gray));


                    textFiles.setTextColor(getResources().getColor(R.color.gray));
                    imageFiles.setColorFilter(getResources().getColor(R.color.gray));
                } else if (i == 4) {
                    fileTabSelected();
                    textFiles.setTextColor(getResources().getColor(R.color.splash));
                    imageFiles.setColorFilter(getResources().getColor(R.color.splash));
                    textApps.setTextColor(getResources().getColor(R.color.gray));
                    imageApps.setColorFilter(getResources().getColor(R.color.gray));

                    view_app.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_pic.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_music.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_video.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    view_file.setBackgroundColor(getResources().getColor(R.color.splash));

                    textPics.setTextColor(getResources().getColor(R.color.gray));
                    imagePics.setColorFilter(getResources().getColor(R.color.gray));

                    textVideos.setTextColor(getResources().getColor(R.color.gray));
                    imageVideos.setColorFilter(getResources().getColor(R.color.gray));

                    textMusic.setTextColor(getResources().getColor(R.color.gray));
                    imageMusic.setColorFilter(getResources().getColor(R.color.gray));


                }
            }
        });


    }


}