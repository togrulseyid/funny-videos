package com.togrulseyid.funnyvideos.activities;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
		
		if (Utility.isSendGCMToServer(activity)) {

			new CheckAdsAndAppVersionAsynTask(activity).execute();

		} else {
			// Check device for Play Services APK. If check succeeds, proceed
			// with GCM registration.
			if (Utility.checkPlayServices(activity)) {
				// TODO: check if send to server
				gcm = GoogleCloudMessaging.getInstance(activity);

				//Local Settings Model Setter
				Utility.setLocalSettingModel(activity);
				regid = Utility.getRegistrationId(activity);

				if (regid.isEmpty()) {
					new RegisterInBackgroundAsyncTask(activity).execute();

				} else {

					new CheckAdsAndAppVersionAsynTask(activity).execute();

				}

			} else {

				textViewAppTitle
						.setText("No valid Google Play Services found.");

			}
		}

	}

	private class StartAsynTask extends AsyncTask<GCMModel, Integer, GCMModel> {
		
		private Activity activity;
		public StartAsynTask(Activity activity) {
			this.activity = activity;
		}

		@Override
		protected GCMModel doInBackground(GCMModel... params) {
			NetworkOperations networkOperations = new NetworkOperations(activity);
			return networkOperations.sendGCMToServer(params[0]);
		}

		@Override
		protected void onPostExecute(GCMModel result) {
			super.onPostExecute(result);

			if (result != null && result.getMessageId() != null) {

				if (result.getMessageId().equals(
						MessageConstants.NO_NETWORK_CONNECTION)
						|| result.getMessageId().equals(
								MessageConstants.NO_INTERNET_CONNECTION)) {

					// TODO: No Internet Connection
					noInternet();

				} else if (result.getMessageId() == MessageConstants.SUCCESSFUL) {

					// TODO: Write to pref that GCM has been send to server
					Utility.writeGCMToSharedPreferences(activity,
							result.getGcm());

					new CheckAdsAndAppVersionAsynTask(activity).execute();

				} else {
					// TODO: Error Occurred
				}

			} else {
				// TODO: No Internet Connection
				noInternet();
		
			}
		}

		private void noInternet() {
			textViewAppTitle
					.setText(R.string.message_internet_connection_problem);
			textViewAppTitle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					textViewAppTitle.setText("");
					new StartAsynTask(activity).execute();
				}
			});

		}

	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */

	class RegisterInBackgroundAsyncTask extends AsyncTask<Void, Void, Boolean> {

		private Activity activity;
		public RegisterInBackgroundAsyncTask(Activity activity) {
			this.activity = activity;
		}

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

				new StartAsynTask(activity).execute(gcmModel);

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
	
	/*
	 * Check App Version and Ads 
	 * */
	private class CheckAdsAndAppVersionAsynTask extends AsyncTask<Void, Integer, CoreModel> {
		
		private Activity activity;
		public CheckAdsAndAppVersionAsynTask(Activity activity) {
			this.activity = activity;
		}
		
		@Override
		protected CoreModel doInBackground(Void... params) {
			NetworkOperations networkOperations = new NetworkOperations(activity);
			return networkOperations.checkAppVersion(new CoreModel());
		}

		@Override
		protected void onPostExecute(CoreModel result) {
			super.onPostExecute(result);

			Log.d("testA", "results: " + result);
			Log.d("testA", "results: " + result.isAds());
			if (result != null && result.getMessageId() != null) {

				if (result.getMessageId().equals(
						MessageConstants.NO_NETWORK_CONNECTION)
						|| result.getMessageId().equals(
								MessageConstants.NO_INTERNET_CONNECTION)) {

					// TODO: no internet connection
					noInternet();

				} else if (result.getMessageId().equals(
						MessageConstants.SUCCESSFUL)) {
					Intent intent = new Intent(activity, MainActivity.class);

					Bundle bundle = new Bundle();
					bundle.putBoolean(getString(R.string.intent_bundle_ads),
							result.isAds());
					bundle.putBoolean(getString(R.string.intent_bundle_ads2),
							result.isAds2());
					intent.putExtras(bundle);
					startActivity(intent);
					finish();

				} else {

					startActivity(new Intent(activity, AppUpdateActivity.class));
					finish();

				}
			} else {
				// TODO: no internet connection
				noInternet();
			}
		}
		
		private void noInternet() {
			textViewAppTitle
					.setText(R.string.message_internet_connection_problem);
			textViewAppTitle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					textViewAppTitle.setText("");
					new CheckAdsAndAppVersionAsynTask(activity).execute();
				}
			});
		}
	}
	
}
