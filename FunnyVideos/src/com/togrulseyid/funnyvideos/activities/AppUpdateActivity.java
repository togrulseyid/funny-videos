package com.togrulseyid.funnyvideos.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.togrulseyid.funnyvideos.R;

public class AppUpdateActivity extends ActionBarActivity {

	private ImageButton buttonUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_update);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setCustomView(R.layout.view_actionbar_layout);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

		buttonUpdate = (ImageButton) findViewById(R.id.imageButtonActivityUpdateActionUpdate);

		buttonUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent goToMarket = new Intent(Intent.ACTION_VIEW).setData(Uri
						.parse("market://details?id="
								+ getApplication().getPackageName()));
				startActivity(goToMarket);
			}
		});
	}

}
