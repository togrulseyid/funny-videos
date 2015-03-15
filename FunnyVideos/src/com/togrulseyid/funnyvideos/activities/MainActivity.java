package com.togrulseyid.funnyvideos.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.togrulseyid.funnyvideos.R;
import com.togrulseyid.funnyvideos.fragments.VideosListFragment;
import com.togrulseyid.funnyvideos.views.InfoDialog;

public class MainActivity extends ActionBarActivity {

	private InterstitialAd interstitial;
	private Activity activity;
	private boolean isAds = true;
	private boolean isAds2 = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		activity = this;
		
		Bundle bundle = getIntent().getExtras();
		isAds = bundle.getBoolean(getString(R.string.intent_bundle_ads), true);
		isAds2 = bundle.getBoolean(getString(R.string.intent_bundle_ads2), true);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new VideosListFragment(isAds, isAds2)).commit();
		}
		
		
		/*
		 * AdMob Intersetial Ads
		 * */
		
		if (isAds) {
			
			// request test interstitial ads

			// Create the interstitial.
			interstitial = new InterstitialAd(this);
			interstitial.setAdUnitId(getString(R.string.admob_ad_unit_id));
			// Create ad request.
			 AdRequest adRequest = new AdRequest.Builder().build();
			// Begin loading your interstitial.
			interstitial.loadAd(adRequest);
			
		}

	}
	
	// Invoke displayInterstitial() when you are ready to display an interstitial.
	public void displayInterstitial() {
		if (isAds && interstitial.isLoaded()) {
			interstitial.show();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		displayInterstitial();
	}

	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
			
		case R.id.action_info:
			InfoDialog infoDialog = new InfoDialog(activity, 0, R.string.app_info_title, R.string.app_info_body);
			infoDialog.show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
