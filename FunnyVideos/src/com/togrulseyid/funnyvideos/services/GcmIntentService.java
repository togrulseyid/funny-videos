/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.togrulseyid.funnyvideos.services;

import java.io.IOException;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.togrulseyid.funnyvideos.R;
import com.togrulseyid.funnyvideos.activities.NotificationActivity;
import com.togrulseyid.funnyvideos.models.LocalSettingModel;
import com.togrulseyid.funnyvideos.models.NotificationModel;
import com.togrulseyid.funnyvideos.operations.ObjectConvertor;
import com.togrulseyid.funnyvideos.operations.Utility;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	public static final String TAG = "extrasX";
	private NotificationModel model = null;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */

			ObjectConvertor<NotificationModel> objectConvertor = new ObjectConvertor<NotificationModel>();

			try {

				String data = Utility.decrypt(extras.get("data").toString(),
						Utility.getAppSignature(getApplicationContext()));

				Log.i("extrasX", "data: " + data);
				
				model = objectConvertor.getClassObject(data,
						NotificationModel.class);

				Log.i(TAG, model.toString());
				
			} catch (IOException e) {
				model.setMessage("Error Occured");
				e.printStackTrace();
			}

			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				model.setMessage("Send error");
				sendNotification(model);
				// sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {

				model.setMessage("Deleted messages on server: "
						+ model.getMessage());
				sendNotification(model);
				// sendNotification("Deleted messages on server: " +
				// extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

				// This loop represents the service doing some work.
				if (model != null) {
					if (!Utility.isEmptyOrNull(model.getMessage())) {
						sendNotification(model); // Received e message
						Log.i(TAG, model.toString());
					}
				}}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(NotificationModel model) {
		
		LocalSettingModel localModel = Utility.getLocalSettingModel(getApplicationContext());
		
		if (localModel.isNotification()) {
			Log.i("Utility", localModel.toString());

			long[] vibrationPattern = { 0, 100, 1000, 1000, 2000 };

			mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			Intent intent = new Intent(this, NotificationActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(getResources().getString(R.string._B_NOTIFICATION_OBJECT), model);
			intent.putExtras(bundle);

			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
					intent, 0);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("Funny Videos")
					.setContentText(model.getMessage())
					.setAutoCancel(true)
					.setWhen(System.currentTimeMillis())
					.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
					.setStyle(new NotificationCompat.InboxStyle().setBigContentTitle(model.getMessage()));
			// new NotificationCompat.BigTextStyle().bigText(model.getMessage()));

			if (localModel.isNotification() && localModel.isVibrate()) {
				mBuilder.setVibrate(vibrationPattern);
			}

			mBuilder.setContentIntent(pendingIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		}
	}
	
}
