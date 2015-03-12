package com.togrulseyid.funnyvideos.views;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.togrulseyid.funnyvideos.R;

public class InfoDialog extends Dialog {

	private String title;
	private String message;
	private TextView textViewMesageBody;
	private TextView textViewMessageTitle;
	private ImageView imageViewIcon;
	private Button buttonOK;
	private Context context;
	private int icon;

	public InfoDialog(Context context, int icon, String title, String message) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.title = title;
		this.message = message;
		this.context = context;
		this.icon = icon;
	}

	public InfoDialog(Context context, int icon, int title, int message) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.title = context.getString(title);
		this.message = context.getString(message);
		this.context = context;
		this.icon = icon;
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setGravity(Gravity.CENTER);
		setContentView(R.layout.dialog_info);
		textViewMesageBody = (TextView) findViewById(R.id.textViewDialogInfoMessageBody);
		textViewMessageTitle = (TextView) findViewById(R.id.textViewDialogInfoMessageTitle);
		buttonOK = (Button) findViewById(R.id.buttonDialogInfoCancel);
		imageViewIcon = (ImageView) findViewById(R.id.imageViewDialogInfoIcon);

		buttonOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});

		if (icon != 0) {
			imageViewIcon.setVisibility(View.VISIBLE);
			imageViewIcon.setBackgroundResource(icon);
		}
		textViewMesageBody.setText(message);
		textViewMessageTitle.setText(title);
	}

	/* Getters and Setters */

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitle(int title) {
		this.title = context.getString(title);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessage(int message) {
		this.message = context.getString(message);
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getIcon() {
		return icon;
	}

	public String getMessage() {
		return message;
	}

	public String getTitle() {
		return title;
	}
}
