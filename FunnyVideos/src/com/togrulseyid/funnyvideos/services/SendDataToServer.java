package com.togrulseyid.funnyvideos.services;

import android.app.IntentService;
import android.content.Intent;

public class SendDataToServer extends IntentService {

	private Object object;

	public SendDataToServer(String name, Object object) {
		super(name);
		this.object = object;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		
		
		System.out.println(object);
		
		stopSelf();
	}

}
