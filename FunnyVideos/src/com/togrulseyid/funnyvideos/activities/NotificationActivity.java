package com.togrulseyid.funnyvideos.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.togrulseyid.funnyvideos.R;
import com.togrulseyid.funnyvideos.fragments.NotificationVideosListFragment;
import com.togrulseyid.funnyvideos.models.NotificationModel;

public class NotificationActivity extends ActionBarActivity {

	private Fragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Bundle bundle = getIntent().getExtras();

		if (bundle != null
				&& bundle.getSerializable(getResources().getString(
						R.string._B_NOTIFICATION_OBJECT)) != null) {

			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);

			fragment = new NotificationVideosListFragment(
					(NotificationModel) bundle.getSerializable(getResources()
							.getString(R.string._B_NOTIFICATION_OBJECT)), getSupportActionBar());

		}

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.notificationContainer, fragment).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			finish();

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
