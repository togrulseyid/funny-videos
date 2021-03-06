package com.togrulseyid.funnyvideos.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.togrulseyid.funnyvideos.R;
import com.togrulseyid.funnyvideos.constants.BusinessConstants;
import com.togrulseyid.funnyvideos.models.VideoModel;

public class VideoPlayerActivity extends ActionBarActivity {

	private InterstitialAd interstitial;
	private boolean isAds2;
	
	private VideoModel videoModel;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private YouTubePlayerSupportFragment youTubePlayerSupportFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_player);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
		    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		    
//			Log.e("On Config Change", "LANDSCAPE");
			getSupportActionBar().hide();
		}

		Bundle bundle = getIntent().getExtras();

		try {
			videoModel = (VideoModel) bundle.getSerializable(getResources()
					.getString(R.string._B_SELECTED_VIDEO_ITEM));

			isAds2 = bundle.getBoolean(
					getResources().getString(R.string.intent_bundle_ads2),
					false);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		youTubePlayerSupportFragment = new YouTubePlayerSupportFragment();
		fragmentTransaction.add(R.id.fragmentz, youTubePlayerSupportFragment);
		fragmentTransaction.commit();
		youTubePlayerSupportFragment.setMenuVisibility(true);

		Toast.makeText(this, "For Full Screen turn phone horizontally :)", Toast.LENGTH_SHORT).show();

		youTubePlayerSupportFragment.initialize(
				BusinessConstants.DEVELOPER_KEY, new OnInitializedListener() {
					@Override
					public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
						
						player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
						player.getFullscreenControlFlags();
						player.setPlayerStyle(PlayerStyle.DEFAULT);
						
						player.setShowFullscreenButton(true);
						if (!wasRestored) {
							player.loadVideo(videoModel.getSrc());
						} else
							player.cueVideo("6CHs4x2uqcQ");
					}

					@Override
					public void onInitializationFailure(Provider arg0,
							YouTubeInitializationResult arg1) {
					}
				});
		
		
		
		/*
		 * AdMob Intersetial Ads
		 * */
		
		Log.d("testA","isAds2: " + isAds2);
		
		if (isAds2) {
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
		if (isAds2 && interstitial.isLoaded()) {
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
		getMenuInflater().inflate(R.menu.video_player, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_share) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, extraTextMaker(videoModel.getSrc()));
			startActivity(Intent.createChooser(intent, videoModel.getTitle()));

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private String extraTextMaker(String src) {
		return String.format(getString(R.string.txt_youtube_link_share_info), getString(R.string.app_name), src);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.e("On Config Change", "LANDSCAPE");
			getSupportActionBar().hide();
			
		    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			Log.e("On Config Change", "PORTRAIT");
			getSupportActionBar().show();
		}
	}

}

/**
 * An abstract activity which deals with recovering from errors which may occur
 * during API initialization, but can be corrected through user action.
 */
abstract class YouTubeFailureRecoveryActivity extends YouTubeBaseActivity
		implements YouTubePlayer.OnInitializedListener {

	private static final int RECOVERY_DIALOG_REQUEST = 1;

	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider,
			YouTubeInitializationResult errorReason) {
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
		} else {
			String errorMessage = String.format(
					getString(R.string.message_error_player),
					errorReason.toString());
			Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RECOVERY_DIALOG_REQUEST) {
			// Retry initialization if user performed a recovery action
			getYouTubePlayerProvider().initialize(
					BusinessConstants.DEVELOPER_KEY, this);
		}
	}

	protected abstract YouTubePlayer.Provider getYouTubePlayerProvider();

	
	
	@Override
	protected void onResume() {
		super.onResume();
	}
}
