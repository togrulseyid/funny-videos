package com.togrulseyid.funnyvideos.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.togrulseyid.funnyvideos.R;
import com.togrulseyid.funnyvideos.constants.MessageConstants;
import com.togrulseyid.funnyvideos.models.SettingNotificationModel;
import com.togrulseyid.funnyvideos.operations.NetworkOperations;
import com.togrulseyid.funnyvideos.operations.Utility;

public class UserInfoSyncService extends IntentService {

	public UserInfoSyncService() {
		super("UserInfoSyncService");
	}

	public UserInfoSyncService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Log.d("testA", "started");
		NetworkOperations networkOperations = new NetworkOperations(getApplicationContext());

		Bundle bundle = intent.getExtras();
		boolean ntf = bundle.getBoolean(getString(R.string._B_BOOL_IS_NOTIFICATION));
		
		SettingNotificationModel notificationModel = new SettingNotificationModel();
		notificationModel.setGcm(Utility
				.getRegistrationId(getApplicationContext()));
		notificationModel.setImei(Utility.getDeviceId(getApplicationContext()));
		notificationModel.setNtf(ntf);

		Log.d("testA", "started sending");
		SettingNotificationModel outModel = networkOperations.sendNotificationSettingsModel(notificationModel);

		Log.d("testA", "started ending");
		Log.d("testA", "" + outModel);
		if (outModel != null
				&& outModel.getMessageId() != MessageConstants.SUCCESSFUL) {
			Log.d("testA", "Ok we send That shit to server ");
			stopSelf();
		}

		stopSelf();
	}
}
