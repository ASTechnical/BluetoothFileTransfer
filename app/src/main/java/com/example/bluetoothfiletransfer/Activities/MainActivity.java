package com.example.bluetoothfiletransfer.Activities;

import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.example.bluetoothfiletransfer.Fragments.FileShareFragment;
import com.example.bluetoothfiletransfer.Fragments.ScanDeviceFragment;
import com.example.bluetoothfiletransfer.Fragments.SettingsFragment;
import com.example.bluetoothfiletransfer.R;
import com.example.bluetoothfiletransfer.databinding.ActivityMainBinding;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

@RequiresApi(api = Build.VERSION_CODES.S)
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    AdView adView;
    InterstitialAd mInterstitialAd;
    NativeAd nativeAd1;
    AdRequest adRequest;
    private Fragment currentFragment;
    public boolean filesharefragmentclick =false;
    private final Handler handler = new Handler();
    String[] permission = {Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adRequest = new AdRequest.Builder().build();
        currentFragment = new FileShareFragment();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                showmInterstitialAd();
                showmNativeAd();
            }
        });


        checkPermission();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new ScanDeviceFragment())
                .addToBackStack(null)
                .commit();
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.scan) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new ScanDeviceFragment())
                            .addToBackStack(null)
                            .commit();
                    binding.title.setVisibility(View.VISIBLE);
                } else if (i == R.id.file) {
                    filesharefragmentclick=true;
                    showInterstitialAdAndReplaceFragment(new FileShareFragment());
                    binding.title.setVisibility(View.GONE);
                } else if (i == R.id.settings) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new SettingsFragment())
                            .addToBackStack(null)
                            .commit();
                    binding.title.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

    }
    private void showmNativeAd() {
        MobileAds.initialize(this);
        AdLoader adLoader = new AdLoader.Builder(this,getString(R.string.native_ad_unit_id) )
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        // Set a background color for the native ad template
                        nativeAd1 = nativeAd;
                        int backgroundColor = ContextCompat.getColor(MainActivity.this, android.R.color.background_light);
                        ColorDrawable background = new ColorDrawable(backgroundColor);

                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder()
                                .withMainBackgroundColor(background)
                                .build();

                        TemplateView template = findViewById(R.id.template_ads);
                        template.setVisibility(View.VISIBLE);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }



    private void showInterstitialAdAndReplaceFragment(Fragment fragment) {
        long delayMillis = 1000; // 1 seconds

        // Create a ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading ad...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Runnable wait = new Runnable() {
            @Override
            public void run() {
                // Use a Handler to delay the ad loading
                if (mInterstitialAd != null) {
                    progressDialog.dismiss(); // Dismiss the loading indicator
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    progressDialog.dismiss(); // Dismiss the loading indicator

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .addToBackStack(null)
                            .commit();
                    filesharefragmentclick = false;
                }
                handler.removeCallbacks(this);
            }
        };

        handler.postDelayed(wait, delayMillis);
    }



    public void showmInterstitialAd() {
        InterstitialAd.load(MainActivity.this, getString(R.string.inter_ad_unit_id), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("Error", loadAdError.toString());
                filesharefragmentclick=false;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();

                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        if (SDK_INT >= Build.VERSION_CODES.S ) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragmentContainer, currentFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                        mInterstitialAd = null;
                        filesharefragmentclick=false;

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        mInterstitialAd = null;
                        filesharefragmentclick=false;

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

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if ((mInterstitialAd != null) && (filesharefragmentclick))  {
            mInterstitialAd.show(MainActivity.this);
        } else if((mInterstitialAd == null)&&(filesharefragmentclick)){
            // If ad is not loaded or failed to load, replace the fragment
            if (currentFragment instanceof FileShareFragment) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new FileShareFragment())
                        .addToBackStack(null)
                        .commit();

            }
        }
        if (nativeAd1 == null)
        {
            showmNativeAd();
        }
        if (mInterstitialAd == null)
        {
            showmInterstitialAd();
        }
    }
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(MainActivity.this, permission, 2);
            }
        }
    }

}