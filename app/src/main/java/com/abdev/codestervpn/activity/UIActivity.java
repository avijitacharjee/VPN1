package com.abdev.codestervpn.activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anchorfree.partner.api.response.RemainingTraffic;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.vpnsdk.callbacks.Callback;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.anchorfree.vpnsdk.vpnservice.VPNState;
import com.abdev.codestervpn.BuildConfig;
 import com.abdev.codestervpn.Preference;
import com.abdev.codestervpn.R;
import com.abdev.codestervpn.StaticData;
import com.abdev.codestervpn.api.APIClient;
import com.abdev.codestervpn.api.ApiResponse;
import com.abdev.codestervpn.api.RestApis;
import com.abdev.codestervpn.service.BackgroundJobService;
import com.abdev.codestervpn.utils.AdMod;
import com.abdev.codestervpn.utils.AdModFacebook;
import com.abdev.codestervpn.utils.Converter;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.MobileAds;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.abdev.codestervpn.utils.Constant.PRIMIUM_STATE;

public abstract class UIActivity extends AppCompatActivity implements View.OnClickListener  {

    protected static final String TAG = MainActivity.class.getSimpleName();

    JobScheduler tm;
    private int mJobId = 111;
    private ComponentName ServiceComponent;
    @BindView(R.id.server_ip)
    TextView server_ip;
    @BindView(R.id.img_connect)
    ImageView img_connect;
    @BindView(R.id.connection_state)
    TextView connectionStateTextView;
    @BindView(R.id.connection_progress)
    ProgressBar connectionProgressBar;
    @BindView(R.id.traffic_limit)
    TextView trafficLimitTextView;
    @BindView(R.id.optimal_server_btn)
    LinearLayout currentServerBtn;
    @BindView(R.id.selected_server)
    TextView selectedServerTextView;
    @BindView(R.id.country_flag)
    ImageView country_flag;
    @BindView(R.id.txt_download)
    TextView txt_download;
    @BindView(R.id.txt_upload)
    TextView txt_upload;
    @BindView(R.id.lin_spped)
    RelativeLayout lin_spped;
    @BindView(R.id.lay_pro)
    LinearLayout lay_pro;
    @BindView(R.id.hamburger_btn)
    LinearLayout hamburger_btn;
    Toolbar toolbar;

    Preference preference;
    boolean mSubscribedToDelaroy = false;
    boolean connected = false;
      int[] Onconnect = {R.drawable.ic_on};
    int[] Ondisconnect = {R.drawable.ic_off};
     private LinearLayout mDrawerList;
    private Handler mUIHandler = new Handler(Looper.getMainLooper());
    /**
     * Update UI and check Remaining Traffic on every 10 seconds
     */
    final Runnable mUIUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
            checkRemainingTraffic();
            mUIHandler.postDelayed(mUIUpdateRunnable, 10000);
        }
    };

    protected abstract void isLoggedIn(Callback<Boolean> callback);

    protected abstract void loginToVpn();

    protected abstract void isConnected(Callback<Boolean> callback);

    protected abstract void connectToVpn();

    protected abstract void disconnectFromVnp();

    protected abstract void chooseServer();

    protected abstract void getCurrentServer(Callback<String> callback);

    protected abstract void checkRemainingTraffic();



    void complain(String message) {
        alert("Error: " + message);
    }

    void alert(String message) {
        android.app.AlertDialog.Builder bld = new android.app.AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        bld.create().show();
    }


    public void unlock() {
        preference.setBooleanpreference(PRIMIUM_STATE, true);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    private void initializeService() {
        ServiceComponent = new ComponentName(UIActivity.this, BackgroundJobService.class);
        tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (tm != null) {
            JobInfo.Builder builder = new JobInfo.Builder(mJobId++, ServiceComponent);
            builder.setMinimumLatency(0);
            tm.schedule(builder.build());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loginToVpn();
        initializeService();

        preference = new Preference(this);
              preference.setBooleanpreference(PRIMIUM_STATE, false);

            lay_pro.setVisibility(View.GONE);
            lin_spped.setVisibility(View.VISIBLE);
            MobileAds.initialize(this, getString(R.string.admob_app_ID));
           // LoadInterstitialAd();

            LoadBannerAd();


         mDrawerList = findViewById(R.id.left_drawer);
        setupDrawerToggle(savedInstanceState);

        LinearLayout info = findViewById(R.id.personal_info);
        LinearLayout password = findViewById(R.id.change_password);
        LinearLayout rate_us = findViewById(R.id.rate_us);
        LinearLayout privacy_policy = findViewById(R.id.privacy_policy);
        LinearLayout terms = findViewById(R.id.terms_to_use);
        LinearLayout share_applink = findViewById(R.id.share_app_link);
        LinearLayout more_apps = findViewById(R.id.more_app);

        info.setOnClickListener(this);
        rate_us.setOnClickListener(this);
        password.setOnClickListener(this);
        privacy_policy.setOnClickListener(this);
        terms.setOnClickListener(this);
        share_applink.setOnClickListener(this);
        more_apps.setOnClickListener(this);
        getip();
    }

    /**
     * Get connected IP 29/03/2020
     */
    private void getip() {

        RestApis mRestApis = APIClient.getRetrofitInstance("https://api.ipify.org").create(RestApis.class);
        Call<ApiResponse> userAdd = mRestApis.requestip("json");
        userAdd.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.e(TAG, "onResponse: "+response.body().getIp());
                if(response!=null) {
                    server_ip.setText(response.body().getIp());
                 }
                else {
                    server_ip.setText(R.string.default_server_ip_text);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                server_ip.setText(R.string.default_server_ip_text);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_info:
                Toast.makeText(this, "Personal information", Toast.LENGTH_SHORT).show();
                break;
            case R.id.change_password:
                Toast.makeText(this, "change password", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rate_us:
                Rate_App();
                break;
            case R.id.privacy_policy:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                break;
            case R.id.terms_to_use:
                startActivity(new Intent(this, TermsToUseActivity.class));
                break;
            case R.id.share_app_link:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, "DownLoad " + getResources().getString(R.string.app_name) + " App\n" + BuildConfig.APP_LINK);
                startActivity(Intent.createChooser(i, "Share Via"));
                break;
            case R.id.more_app:
                Uri uri = Uri.parse("market://search?q=pub:" + "Recca");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/search?q=pub:" + "Recca")));
                }
                break;
        }
     }

    private void Rate_App() {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getApplication().getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplication().getPackageName())));
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
     }
    private SlidingRootNav slidingRoot;
    void setupDrawerToggle(Bundle savedInstanceState ) {
        slidingRoot = new SlidingRootNavBuilder(this)
                .withDragDistance(140)//Horizontal translation of a view. Default == 180dp
                .withRootViewScale(0.6f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
                .withRootViewElevation(10) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
                .withRootViewYTranslation(3) //Content view's translationY will be interpolated between 0 and 4. Default == 0
                .withRootViewYTranslation(0)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_layout)
                .inject();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isConnected(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    startUIUpdateTask();
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopUIUpdateTask();
    }

    @OnClick(R.id.hamburger_btn)
    public void onhamburgerclick(View v) {

        if (slidingRoot.isMenuOpened()) {
            slidingRoot.closeMenu();
        } else {
            slidingRoot.openMenu();
        }
     }


    @OnClick(R.id.img_connect)
    public void onConnectBtnClick(View v) {
        LoadInterstitialAd();
        isConnected(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    disconnectFromVnp();
                } else {
                    connectToVpn();
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
            }
        });
    }

    @OnClick(R.id.optimal_server_btn)
    public void onServerChooserClick(View v) {
        LoadInterstitialAd();
        chooseServer();
    }




    protected void startUIUpdateTask() {
        stopUIUpdateTask();
        mUIHandler.post(mUIUpdateRunnable);
    }

    protected void stopUIUpdateTask() {
        mUIHandler.removeCallbacks(mUIUpdateRunnable);
        updateUI();
    }

    /**
     * Update UI according to VPN state
     */
    protected void updateUI() {

        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                switch (vpnState) {
                    case IDLE: {
                        Log.e(TAG, "success: IDLE" );
                        connectionStateTextView.setText(R.string.disconnected);
                        getip();
//                        server_ip.setText(R.string.default_server_ip_text);
                        if (connected) {
                            connected = false;
                            animate(img_connect, Ondisconnect, 0, false);
                        }
                        country_flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                        selectedServerTextView.setText(R.string.select_country);
                        ChangeBlockVisibility();
                        txt_download.setText(R.string._0_b);
                        txt_upload.setText(R.string._0_b);
                        hideConnectProgress();
                        break;
                    }
                    case CONNECTED: {
                        Log.e(TAG, "success: CONNECTED" );
                        if (!connected) {
                            connected = true;
                            animate(img_connect, Onconnect, 0, false);
                        }
                        connectionStateTextView.setText(R.string.connected);
                        lin_spped.setVisibility(View.VISIBLE);
                        lay_pro.setVisibility(View.GONE);
                        hideConnectProgress();
                        break;
                    }
                    case CONNECTING_VPN:
                    case CONNECTING_CREDENTIALS:
                    case CONNECTING_PERMISSIONS: {
                        connectionStateTextView.setText(R.string.connecting);
                        ChangeBlockVisibility();
                        txt_download.setText(R.string._0_b);
                        txt_upload.setText(R.string._0_b);
                        country_flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                        selectedServerTextView.setText(R.string.select_country);
                        showConnectProgress();
                        break;
                    }
                    case PAUSED: {
                        Log.e(TAG, "success: PAUSED" );
                        connectionStateTextView.setText(R.string.paused);
                        ChangeBlockVisibility();
                        country_flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                        selectedServerTextView.setText(R.string.select_country);
                        txt_download.setText(R.string._0_b);
                        txt_upload.setText(R.string._0_b);
                        break;
                    }
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                country_flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                selectedServerTextView.setText(R.string.select_country);
                txt_download.setText(R.string._0_b);
                txt_upload.setText(R.string._0_b);
            }
        });
        getCurrentServer(new Callback<String>() {
            @Override
            public void success(@NonNull final String currentServer) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        country_flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                        selectedServerTextView.setText(R.string.select_country);
                        if (!currentServer.equals("")) {
                            Locale locale = new Locale("", currentServer);
                            Resources resources = getResources();
                            String sb = "drawable/" + currentServer.toLowerCase();
                            country_flag.setImageResource(resources.getIdentifier(sb, null, getPackageName()));
                            selectedServerTextView.setText(locale.getDisplayCountry());
                        } else {
                            country_flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                            selectedServerTextView.setText(R.string.select_country);
                        }
                    }
                });
            }

            @Override
            public void failure(@NonNull VpnException e) {
                country_flag.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                selectedServerTextView.setText(R.string.select_country);
                txt_download.setText(R.string._0_b);
                txt_upload.setText(R.string._0_b);
            }
        });
    }

    private void ChangeBlockVisibility() {

            lay_pro.setVisibility(View.GONE);

            lin_spped.setVisibility(View.VISIBLE);

    }

    private void animate(final ImageView imageView, final int images[], final int imageIndex, final boolean forever) {

        //imageView <-- The View which displays the images
        //images[] <-- Holds R references to the images to display
        //imageIndex <-- index of the first image to show in images[]
        //forever <-- If equals true then after the last image it starts all over again with the first image resulting in an infinite loop. You have been warned.

        int fadeInDuration = 500; // Configure time values here
        int timeBetween = 3000;
        int fadeOutDuration = 1000;

        imageView.setVisibility(View.VISIBLE);    //Visible or invisible by default - this will apply when the animation ends
        imageView.setImageResource(images[imageIndex]);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(fadeInDuration);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
        fadeOut.setStartOffset(fadeInDuration + timeBetween);
        fadeOut.setDuration(fadeOutDuration);

        AnimationSet animation = new AnimationSet(false); // change to false
        animation.addAnimation(fadeIn);
//        animation.addAnimation(fadeOut);
        animation.setRepeatCount(1);
        imageView.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                if (images.length - 1 > imageIndex) {
                    animate(imageView, images, imageIndex + 1, forever); //Calls itself until it gets to the end of the array
                } else {
                    if (forever) {
                        animate(imageView, images, 0, forever);  //Calls itself to start the animation all over again in a loop if forever = true
                    }
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
    }


    /**
     * Update Traffic Status
     *
     * @param outBytes Upload bytes
     * @param inBytes  Download bytes
     */
    protected void updateTrafficStats(long outBytes, long inBytes) {
        String outString = Converter.humanReadableByteCountOld(outBytes, false);
        String inString = Converter.humanReadableByteCountOld(inBytes, false);

        txt_download.setText(inString);
        txt_upload.setText(outString);
    }

    /**
     * Update Remaining Traffic
     *
     * @param remainingTrafficResponse Response of traffic
     */
    protected void updateRemainingTraffic(RemainingTraffic remainingTrafficResponse) {
        if (remainingTrafficResponse.isUnlimited()) {
            trafficLimitTextView.setText(R.string.unlimited_available);
        } else {
            String trafficUsed = Converter.megabyteCount(remainingTrafficResponse.getTrafficUsed()) + "Mb";
            String trafficLimit = Converter.megabyteCount(remainingTrafficResponse.getTrafficLimit()) + "Mb";
            trafficLimitTextView.setText(getResources().getString(R.string.traffic_limit, trafficUsed, trafficLimit));
        }
    }

    /**
     * Show IP Address of Connected Server
     *
     * @param ipaddress IP Address
     */
    protected void ShowIPaddera(String ipaddress) {
        Log.e(TAG, "ShowIPaddress: " + ipaddress);
        server_ip.setText(ipaddress);
    }


    protected void showConnectProgress() {
        connectionProgressBar.setVisibility(View.VISIBLE);
    }

    protected void hideConnectProgress() {
        connectionProgressBar.setVisibility(View.GONE);
    }

    protected void showMessage(String msg) {
        Toast.makeText(UIActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void LoadInterstitialAd() {

        if (StaticData.GOOGlE_AD) {
            AdMod.buildAdFullScreen(getApplicationContext(), new AdMod.MyAdListener() {
                @Override
                public void onAdClicked() {
                }

                @Override
                public void onAdClosed() {
                }

                @Override
                public void onAdLoaded() {
                }

                @Override
                public void onAdOpened() {
                }

                @Override
                public void onFaildToLoad(int i) {
                }
            });
        } else if (StaticData.FACEBOOK_AD) {
            AdModFacebook.buildAdFullScreen(getApplicationContext(), new AdModFacebook.MyAdListener() {
                @Override
                public void onAdClicked() {
                }

                @Override
                public void onAdClosed() {
                }

                @Override
                public void onAdLoaded() {
                }

                @Override
                public void onAdOpened() {
                }

                @Override
                public void onFaildToLoad(AdError adError) {
                }

                @Override
                public void onInterstitialDismissed() {
                }

                @Override
                public void onInterstitialDisplayed() {
                }

                @Override
                public void onLoggingImpression() {
                }
            });
        }

    }

    public void LoadBannerAd() {
        RelativeLayout adContainer = findViewById(R.id.adView);
        if (StaticData.GOOGlE_AD) {
            AdMod.buildAdBanner(getApplicationContext(), adContainer, 0, new AdMod.MyAdListener() {
                @Override
                public void onAdClicked() {
                }

                @Override
                public void onAdClosed() {
                }

                @Override
                public void onAdLoaded() {
                }

                @Override
                public void onAdOpened() {
                }

                @Override
                public void onFaildToLoad(int i) {
                }
            });
        } else if (StaticData.FACEBOOK_AD) {
            AdModFacebook.buildAdBanner(this, adContainer, 1, new AdModFacebook.MyAdListener() {
                @Override
                public void onAdClicked() {
                }

                @Override
                public void onAdClosed() {
                }

                @Override
                public void onAdLoaded() {
                }

                @Override
                public void onAdOpened() {
                }

                @Override
                public void onFaildToLoad(AdError adError) {
                }

                @Override
                public void onInterstitialDismissed() {
                }

                @Override
                public void onInterstitialDisplayed() {
                }

                @Override
                public void onLoggingImpression() {
                }
            });
        }
    }
}
