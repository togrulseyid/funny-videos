package com.togrulseyid.funnyvideos.activities;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.togrulseyid.funnyvideos.R;
import com.togrulseyid.funnyvideos.constants.MessageConstants;
import com.togrulseyid.funnyvideos.models.CoreModel;
import com.togrulseyid.funnyvideos.models.GCMModel;
import com.togrulseyid.funnyvideos.operations.NetworkOperations;

public class StartActivity extends Activity {

	private Activity context;
	private String regid;
	public static final String EXTRA_MESSAGE = "Assalamu Aleykum server";
	// public static final String PROPERTY_REG_ID =
	// "AIzaSyCexoD_iCiAYD3svq9Njo6kk6iRgq93RxY";
	public static final String PROPERTY_REG_ID = "XXXXX";
	private static final String PROPERTY_APP_VERSION = "1.0";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String TAG = "testGCM";
	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	private String SENDER_ID = "748654734166";
	private GoogleCloudMessaging gcm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_start);

		if (isSendGCMToServer()) {
			Log.d("testG","isSendGCMToServer");
			new CheckAppVersionAsynTask().execute();
			
			
		} else {
			// Check device for Play Services APK. If check succeeds, proceed
			// with GCM registration.
			if (checkPlayServices()) {
				// TODO: check if send to server
				gcm = GoogleCloudMessaging.getInstance(this);
				regid = getRegistrationId(context);

				if (regid.isEmpty()) {
					registerInBackground();
				}

			} else {
				Log.d(TAG, "No valid Google Play Services APK found.");
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

				if (result.getMessageId() != MessageConstants.SUCCESSFUL) {
					// TODO: Write to pref that GCM has been send to server
					writeGCMToSharedPreferences(result.getGcm());
				} else {
					// TODO: Error Occurred
				}

			} else {
				// TODO: No Internet Connection
			}
		}

	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.d(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					Log.d("regid", "" + regid);
					msg = "Device registered, registration ID=" + regid;

					// You should send the registration ID to your server over
					// HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.
					sendRegistrationIdToBackend();

					// For this demo: we don't need to send it because the
					// device will send
					// upstream messages to a server that echo back the message
					// using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				// mDisplay.append(msg + "\n");

				// sendGCMToServer(msg);

			}
		}.execute(null, null, null);
	}

	/**
	 * Stores the registration ID and the app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		Log.d(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGcmPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but how you store the regID in your app is up to you.
		return getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
		// Your implementation here.

		new StartAsynTask().execute(new GCMModel());
		// TODO: SEND DATA TO SERVER
	}

	/**
	 * Gets the current registration ID for application on GCM service, if there
	 * is one.
	 * <p>
	 * If result is empty, the app needs to register. @return registration ID,
	 * or empty string if there is no existing registration ID.
	 * </p>
	 * */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.d(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.d(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	public void writeGCMToSharedPreferences(String gcmID) {
		final SharedPreferences prefs = getGcmPreferences(context);
		prefs.edit()
				.putBoolean(getString(R.string._SP_VIDEO_APP_IS_GCM_SEND), true)
				.commit();
	}

	public boolean isSendGCMToServer() {
		final SharedPreferences prefs = getGcmPreferences(context);
		return prefs.getBoolean(getString(R.string._SP_VIDEO_APP_IS_GCM_SEND),
				false);
	}

	private class CheckAppVersionAsynTask extends
			AsyncTask<Void, Integer, CoreModel> {

		@Override
		protected CoreModel doInBackground(Void... params) {
			CoreModel coreModel = null;
			try {
				// Thread.sleep(BusinessConstants.SLEEP_ROOT_ACTIVITY_TIME_MILLI_SECOND);
				NetworkOperations networkOperations = new NetworkOperations(getApplicationContext());
				coreModel = networkOperations.checkAppVersion(new CoreModel());
			} catch (Exception e) {
			}

			return coreModel;
		}

		@Override
		protected void onPostExecute(CoreModel result) {
			super.onPostExecute(result);
			

			Log.d("testG", result.toString());

			if (result.getMessageId() == MessageConstants.APP_VERSION_INCORRECT) {
				startActivity(new Intent(getApplicationContext(), AppUpdateActivity.class));
				finish();
			} else {
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		}

	}

}
