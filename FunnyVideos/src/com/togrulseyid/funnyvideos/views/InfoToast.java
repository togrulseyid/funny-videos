package com.togrulseyid.funnyvideos.views;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.togrulseyid.funnyvideos.R;
import com.togrulseyid.funnyvideos.constants.MessageConstants;

public class InfoToast {

	private Activity activity;

	public InfoToast(Activity activity) {
		this.activity = activity;
	}

	public void makeToast(int messageId) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_information,
				(ViewGroup) activity.findViewById(R.id.linearLayoutViewGroup));

		TextView textView = (TextView) layout
				.findViewById(R.id.textViewToastText);
		textView.setText(getMessage(messageId));

		Toast toast = new Toast(activity);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(10000);
		toast.setView(layout);
		toast.show();
	}

	private String getMessage(int messageId) {
		switch (messageId) {
		case MessageConstants.NO_INTERNET_CONNECTION:
			return activity.getResources().getString(
					R.string.message_text_no_internet_connection);

		case MessageConstants.SERVER_ERROR_1:
			return activity.getResources().getString(
					R.string.message_network_connection_problem);

		case MessageConstants.UN_SUCCESSFUL:
			return activity.getResources()
					.getString(R.string.message_exception);

		default:
			return "";
		}

	}

}
