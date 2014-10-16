package com.togrulseyid.funnyvideos.activities;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.togrulseyid.funnyvideos.R;
import com.togrulseyid.funnyvideos.constants.MessageConstants;
import com.togrulseyid.funnyvideos.constants.UrlConstants;
import com.togrulseyid.funnyvideos.models.CoreModel;
import com.togrulseyid.funnyvideos.models.GCMModel;
import com.togrulseyid.funnyvideos.models.VideoListModel;
import com.togrulseyid.funnyvideos.models.VideoModel;
import com.togrulseyid.funnyvideos.operations.NetworkOperations;
import com.togrulseyid.funnyvideos.operations.ObjectConvertor;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // Remove title bar
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_start);
		
		
		VideoListModel videoListModel = new VideoListModel();

		ArrayList<VideoModel> list = new ArrayList<VideoModel>();
		
		VideoModel model = new VideoModel();
		model.setSrc("http://");
		model.setTitle("9Gags");
		list.add(model);
		
		VideoModel model2 = new VideoModel();
		model2.setSrc("http2://");
		model2.setTitle("9Gags2");
		list.add(model2);
		
		videoListModel.setVideos(list);
		
		ObjectConvertor<VideoListModel> objectConvertor = new ObjectConvertor<VideoListModel>();
		
		String result = null;
		try {
			result = objectConvertor.getClassString(videoListModel);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		Log.d("model", result);
		
		finish();
		
		activity = this;
		
//		Utility.getGcmPreferences(activity).edit().clear().commit();

		textViewAppTitle = (TextView) findViewById(R.id.textViewAppTitle);
		
		if (Utility.isSendGCMToServer(getApplicationContext())) {
			Log.d(TAG, "isSendGCMToServer");
			
			textViewAppTitle.append("isSendGCMToServer\t");
			
			new CheckAppVersionAsynTask().execute();

		} else {
			// Check device for Play Services APK. If check succeeds, proceed
			// with GCM registration.
			
			if (Utility.checkPlayServices(this)) {

				// TODO: check if send to server
				gcm = GoogleCloudMessaging.getInstance(this);
				
				regid = Utility.getRegistrationId(activity);

				if (regid.isEmpty()) {

					new RegisterInBackgroundAsyncTask().execute();
					
				} else {
					
					new CheckAppVersionAsynTask().execute();
					
				}
				

			} else {
				
				textViewAppTitle
						.append("No valid Google Play Services APK found.");
				
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
			
			Log.d(TAG, "StartAsynTask result: " + result.toString());
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
	
	class RegisterInBackgroundAsyncTask  extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			String msg = "";

			try {

				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(activity);
				}
				
				regid = gcm.register(SENDER_ID);
				
				Log.d(TAG, "" + regid);
				
				msg = "Device registered, registration ID=" + regid;

				// You should send the registration ID to your server over
				// HTTP, so it
				// can use GCM/HTTP or CCS to send messages to your app.
				/**
				 * Sends the registration ID to your server over HTTP, so it can use
				 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
				 * since the device sends upstream messages to a server that echoes back the
				 * message using the 'from' address in the message.
				 */
				GCMModel gcmModel = new GCMModel();
				gcmModel.setGcm(regid);
				new StartAsynTask().execute(gcmModel);
				

				// For this demo: we don't need to send it because the
				// device will send
				// upstream messages to a server that echo back the message
				// using the
				// 'from' address in the message.

				// Persist the regID - no need to register again.
				Utility.storeRegistrationId(activity, regid);

			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
				// If there is an error, don't just keep trying to register.
				// Require the user to click a button again, or perform
				// exponential back-off.
			}
			return msg;
		}

	}

	private class CheckAppVersionAsynTask extends
			AsyncTask<Void, Integer, CoreModel> {

		@Override
		protected CoreModel doInBackground(Void... params) {
			NetworkOperations networkOperations = new NetworkOperations(
					getApplicationContext());

			return networkOperations.checkAppVersion(new CoreModel());
		}

		@Override
		protected void onPostExecute(CoreModel result) {
			super.onPostExecute(result);

			if (result != null
					&& result.getMessageId() == MessageConstants.APP_VERSION_INCORRECT) {

				startActivity(new Intent(getApplicationContext(),
						AppUpdateActivity.class));
				finish();

			} else {

				startActivity(new Intent(getApplicationContext(),
						MainActivity.class));
				finish();
			}
		}

	}

}
