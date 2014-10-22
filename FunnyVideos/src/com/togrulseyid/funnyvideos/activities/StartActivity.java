package com.togrulseyid.funnyvideos.activities;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.togrulseyid.funnyvideos.R;
import com.togrulseyid.funnyvideos.constants.MessageConstants;
import com.togrulseyid.funnyvideos.models.CoreModel;
import com.togrulseyid.funnyvideos.models.GCMModel;
import com.togrulseyid.funnyvideos.operations.NetworkOperations;
import com.togrulseyid.funnyvideos.operations.Utility;

public class StartActivity extends Activity {

	private Activity activity;
	private String regid;
	private static final String TAG = "testGCM";

	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	private String SENDER_ID = "748654734166";
	private GoogleCloudMessaging gcm;
	private TextView textViewAppTitle;

	long startTime;
	long duration;
	
	@Override
	protected void onStart() {

		startTime = System.currentTimeMillis();

		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // Remove title bar
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_start);
		
//		Utility.getGcmPreferences(activity).edit().clear().commit();
		activity = this;

		textViewAppTitle = (TextView) findViewById(R.id.textViewAppTitle);
		
		if (Utility.isSendGCMToServer(getApplicationContext())) {
			
			textViewAppTitle.append("isSendGCMToServer\t");
			
			new CheckAppVersionAsynTask().execute();

		} else {
			// Check device for Play Services APK. If check succeeds, proceed
			// with GCM registration.
			
//			timeExecuter("isSendGCMToServer else");
			if (Utility.checkPlayServices(this)) {
//			timeExecuter("checkPlayServices");
			
				// TODO: check if send to server
				gcm = GoogleCloudMessaging.getInstance(this);
				
				regid = Utility.getRegistrationId(activity);

				if (regid.isEmpty()) {

					new RegisterInBackgroundAsyncTask().execute();
					
				} else {
					
					new CheckAppVersionAsynTask().execute();
					
				}
				
//				timeExecuter("checkPlayServices end");
			} else {
				
				textViewAppTitle
						.append("No valid Google Play Services APK found.");
				
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

			Log.d("iemi","" + result);
			
			if (result != null && result.getMessageId() != null) {

				if (result.getMessageId() == MessageConstants.SUCCESSFUL) {
					
					// TODO: Write to pref that GCM has been send to server
					Utility.writeGCMToSharedPreferences(getApplicationContext(), result.getGcm());
					
					new CheckAppVersionAsynTask().execute();
					
					textViewAppTitle.append("writeGCMToSharedPreferences");
					
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
	
	class RegisterInBackgroundAsyncTask  extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
//			timeExecuter("RegisterInBackgroundAsyncTask doInBackground ");
			String msg = "";

			try {

				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(activity);
				}
				
				regid = gcm.register(SENDER_ID);
				
				msg = "Device registered, registration ID=" + regid;

				// Send the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send messages to your app.

				// get Telephone IMEI address
				TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				String imei = telephonyManager.getDeviceId();
				GCMModel gcmModel = new GCMModel();
				gcmModel.setGcm(regid);
				gcmModel.setImei(imei);	
				
				new StartAsynTask().execute(gcmModel);

				// Persist the regID - no need to register again.
				Utility.storeRegistrationId(activity, regid);

			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
				// If there is an error, don't just keep trying to register.
				// Require the user to click a button again, or perform
				// exponential back-off.
			}
			
			Log.d(TAG,"" + msg);
//			timeExecuter("RegisterInBackgroundAsyncTask");
			return null;
		}

	}

	private class CheckAppVersionAsynTask extends
			AsyncTask<Void, Integer, CoreModel> {

		@Override
		protected CoreModel doInBackground(Void... params) {
//			timeExecuter("CheckAppVersionAsynTask doInBackground");
			NetworkOperations networkOperations = new NetworkOperations(
					getApplicationContext());

			return networkOperations.checkAppVersion(new CoreModel());
		}

		@Override
		protected void onPostExecute(CoreModel result) {
			super.onPostExecute(result);

//			timeExecuter("CheckAppVersionAsynTask onPostExecute if before");
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
//			timeExecuter("CheckAppVersionAsynTask onPostExecute");
		}

	}
	
	
	@Override
	protected void onDestroy() {
		
//		Intent service = new Intent(activity, UserInfoSyncService.class);
//		activity.startService(service);
//		getDeviceId		
		
//		timeExecuter("onDestroy");
		
		super.onDestroy();
	}
	
//	void timeExecuter(String x) {
//		duration = (System.currentTimeMillis() - startTime) / 1000; 
//		Log.d("duration",  x + " duration: " + duration);
//		
//	}

}
