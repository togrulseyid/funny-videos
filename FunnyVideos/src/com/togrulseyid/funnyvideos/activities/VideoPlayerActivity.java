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
import android.widget.Toast;

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

	private VideoModel videoModel;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private YouTubePlayerSupportFragment youTubePlayerSupportFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_player);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.e("On Config Change", "LANDSCAPE");
			getSupportActionBar().hide();
		}

		Intent intent = getIntent();
		videoModel = (VideoModel) intent.getSerializableExtra(getResources()
				.getString(R.string._B_SELECTED_VIDEO_ITEM));

		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		youTubePlayerSupportFragment = new YouTubePlayerSupportFragment();
		fragmentTransaction.add(R.id.fragmentz, youTubePlayerSupportFragment);
		fragmentTransaction.commit();
		youTubePlayerSupportFragment.setMenuVisibility(true);

		Toast.makeText(getApplicationContext(),
				"For Full Screen turn phone horizontally :)",
				Toast.LENGTH_SHORT).show();

		youTubePlayerSupportFragment.initialize(
				BusinessConstants.DEVELOPER_KEY, new OnInitializedListener() {
					@Override
					public void onInitializationSuccess(Provider provider,
							YouTubePlayer player, boolean wasRestored) {
						player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
						player.getFullscreenControlFlags();
						player.setPlayerStyle(PlayerStyle.CHROMELESS);
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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.video_player, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.e("On Config Change", "LANDSCAPE");
			getSupportActionBar().hide();
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

}