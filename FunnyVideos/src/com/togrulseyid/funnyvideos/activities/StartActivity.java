package com.togrulseyid.funnyvideos.activities;

import java.io.IOException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.togrulseyid.funnyvideos.R;
import com.togrulseyid.funnyvideos.constants.BusinessConstants;
import com.togrulseyid.funnyvideos.constants.MessageConstants;
import com.togrulseyid.funnyvideos.models.CoreModel;
import com.togrulseyid.funnyvideos.models.GCMModel;
import com.togrulseyid.funnyvideos.operations.NetworkOperations;
import com.togrulseyid.funnyvideos.operations.Utility;

public class StartActivity extends Activity {

	private Activity activity;
	private String regid;
	private GoogleCloudMessaging gcm;
	private TextView textViewAppTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // Remove title bar
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_start);

		activity = this;

		textViewAppTitle = (TextView) findViewById(R.id.textViewAppTitle);
		textViewAppTitle.setText("");

		if (Utility.isSendGCMToServer(getApplicationContext())) {
			// Start Activity without checking version
			startActivity(new Intent(getApplicationContext(),
					MainActivity.class));
			finish();
			// new CheckAppVersionAsynTask().execute();

		} else {
			// Check device for Play Services APK. If check succeeds, proceed
			// with GCM registration.
			if (Utility.checkPlayServices(activity)) {
				// TODO: check if send to server
				gcm = GoogleCloudMessaging.getInstance(activity);

				regid = Utility.getRegistrationId(activity);

				if (regid.isEmpty()) {
					new RegisterInBackgroundAsyncTask().execute();

				} else {
					// Start Activity without checking version
					startActivity(new Intent(getApplicationContext(),
							MainActivity.class));
					finish();

					// new CheckAppVersionAsynTask().execute();

				}

			} else {

				textViewAppTitle
						.setText("No valid Google Play Services found.");

			}
		}

	}

	private class StartAsynTask extends AsyncTask<GCMModel, Integer, GCMModel> {

		@Override
		protected GCMModel doInBackground(GCMModel... params) {
			NetworkOperations networkOperations = new NetworkOperations(
					getApplicationContext());
			return networkOperations.sendGCMToServer(params[0]);
		}

		@Override
		protected void onPostExecute(GCMModel result) {
			super.onPostExecute(result);

			if (result != null && result.getMessageId() != null) {

				if (result.getMessageId() == MessageConstants.SUCCESSFUL) {

					// TODO: Write to pref that GCM has been send to server
					Utility.writeGCMToSharedPreferences(
							getApplicationContext(), result.getGcm());

					// new CheckAppVersionAsynTask().execute();
					startActivity(new Intent(getApplicationContext(),
							MainActivity.class));
					finish();

				} else {
					// TODO: Error Occurred
				}

			} else {
				// TODO: No Internet Connection
			}
		}

	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */

	class RegisterInBackgroundAsyncTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {

				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(activity);
				}

				regid = gcm.register(BusinessConstants.SENDER_ID);
				// Send the registration ID to your server over HTTP, so it can
				// use GCM/HTTP or CCS to send messages to your app.

				// get Telephone IMEI address
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				String imei = telephonyManager.getDeviceId();
				GCMModel gcmModel = new GCMModel();
				gcmModel.setGcm(regid);
				gcmModel.setImei(imei);

				new StartAsynTask().execute(gcmModel);

				// Persist the regID - no need to register again.
				Utility.storeRegistrationId(activity, regid);

			} catch (IOException ex) {
				ex.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (!result) {
				textViewAppTitle
						.setText(R.string.message_error_no_internet_connection_for_gcm);
			}
		}
	}

	private class CheckAppVersionAsynTask extends
			AsyncTask<Void, Integer, CoreModel> {

		@Override
		protected CoreModel doInBackground(Void... params) {
			// timeExecuter("CheckAppVersionAsynTask doInBackground");
			NetworkOperations networkOperations = new NetworkOperations(
					getApplicationContext());

			return networkOperations.checkAppVersion(new CoreModel());
		}

		@Override
		protected void onPostExecute(CoreModel result) {
			super.onPostExecute(result);
			Log.d("testA", "result: " + result);
			// timeExecuter("CheckAppVersionAsynTask onPostExecute if before");
			if (result != null
					&& result.getMessageId() == MessageConstants.SUCCESSFUL) {

				startActivity(new Intent(getApplicationContext(),
						MainActivity.class));
				finish();

			} else {
				startActivity(new Intent(getApplicationContext(),
						AppUpdateActivity.class));
				finish();
			}
			// timeExecuter("CheckAppVersionAsynTask onPostExecute");
		}

	}

}
