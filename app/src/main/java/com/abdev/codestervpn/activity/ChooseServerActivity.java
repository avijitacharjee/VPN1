package com.abdev.codestervpn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anchorfree.partner.api.response.AvailableCountries;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.vpnsdk.callbacks.Callback;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.facebook.ads.AdError;
import com.abdev.codestervpn.R;
import com.abdev.codestervpn.StaticData;
import com.abdev.codestervpn.adapter.RegionListAdapter;
import com.abdev.codestervpn.dialog.CountryData;
import com.google.gson.Gson;
import com.abdev.codestervpn.utils.AdMod;
import com.abdev.codestervpn.utils.AdModFacebook;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.abdev.codestervpn.utils.Constant.BUNDLE;
import static com.abdev.codestervpn.utils.Constant.COUNTRY_DATA;

public class ChooseServerActivity extends AppCompatActivity {

    @BindView(R.id.regions_recycler_view)
    RecyclerView regionsRecyclerView;

    @BindView(R.id.regions_progress)
    ProgressBar regionsProgressBar;

    Toolbar toolbar;
    private RegionListAdapter regionAdapter;
    private RegionChooserInterface regionChooserInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_server);
        ButterKnife.bind(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
         regionChooserInterface = item -> {
                  Intent intent = new Intent();
                Bundle args = new Bundle();
                Gson gson = new Gson();
                String json = gson.toJson(item);

                args.putString(COUNTRY_DATA, json);
                intent.putExtra(BUNDLE, args);
                setResult(RESULT_OK, intent);
                finish();

        };

        regionsRecyclerView.setHasFixedSize(true);
        regionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        regionAdapter = new RegionListAdapter(item -> regionChooserInterface.onRegionSelected(item), ChooseServerActivity.this);
        regionsRecyclerView.setAdapter(regionAdapter);
        loadServers();

        LoadBannerAd();
    }

    private void loadServers() {
        showProgress();
        UnifiedSDK.getInstance().getBackend().countries(new Callback<AvailableCountries>() {
            @Override
            public void success(@NonNull final AvailableCountries countries) {
                hideProress();
                regionAdapter.setRegions(countries.getCountries());
            }

            @Override
            public void failure(@NonNull VpnException e) {
                hideProress();
            }
        });
    }

    private void showProgress() {
        regionsProgressBar.setVisibility(View.VISIBLE);
        regionsRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void hideProress() {
        regionsProgressBar.setVisibility(View.GONE);
        regionsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public interface RegionChooserInterface {
        void onRegionSelected(CountryData item);
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
