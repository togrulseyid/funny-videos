package com.togrulseyid.funnyvideos.operations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.togrulseyid.funnyvideos.constants.BusinessConstants;
import com.togrulseyid.funnyvideos.models.LocalSettingModel;

public class Utility {

	public static final boolean isEmptyOrNull(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/*
	 * This method used for EditText
	 */
	public static final String nullStringToEmpty(String string) {
		if (string == null) {
			return "";
		}
		return string;
	}

	/*
	 * This method used for Date type EditText
	 */
	public static final String nullStringToDate(String string) {
		if (string == null) {
			return "--/--/----";
		}
		return string;
	}

	public static String getMD5EncodedString(String string) {

		byte[] data = string.getBytes();
		String result = null;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = new BigInteger(1, md.digest(data)).toString(16);
		} catch (NoSuchAlgorithmException e) {
		}

		return result;
	}

	public static final String getAppSignature(Context context) {

		String appSignature = new BigInteger(1, getApkMd5(context))
				.toString(16);

		if (appSignature != null && appSignature.length() < 32) {
			appSignature = "0" + appSignature;
		}

		Log.d("testV","appSignature: " + appSignature);
//		return appSignature;
		return "8b31f4fa7fa1bb349d73545066c1a62a";
	}

	private static final byte[] getApkMd5(Context context) {

		try {
			Signature[] signatures = context.getPackageManager()
					.getPackageInfo(context.getPackageName(),
							PackageManager.GET_SIGNATURES).signatures;

			Signature signature = signatures[0];
			byte[] hexBytes = signature.toByteArray();
			MessageDigest digest = MessageDigest.getInstance("MD5");
			return digest.digest(hexBytes);
		} catch (NameNotFoundException ex) {

		} catch (Exception e) {

		}
		return null;
	}

	/*
	 * Check network connection (it does not depend WiFi or Cell Network)
	 */
	public static final boolean checkNetwork(Context context) {

		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null) {
			return false;
		}

		return networkInfo.isConnected();
	}

	/*
	 * 
	 */
	public static final boolean checkInternetConnection() {

		HttpParams httpParameters = new BasicHttpParams();
		// if HTTP connection time is less than
		// BusinessConstants.INTERNET_CHEKING_TIMEOUT seconds, will be
		// IOException
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				BusinessConstants.INTERNET_CHEKING_TIMEOUT);
		// if HTTP downloading time is less than
		// BusinessConstants.INTERNET_CHEKING_TIMEOUT seconds, will be
		// IOException
		HttpConnectionParams.setSoTimeout(httpParameters,
				BusinessConstants.INTERNET_CHEKING_TIMEOUT);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		try {

			// Developer thought that if Google down, down all servers in the
			// world :)
			HttpGet httpGet = new HttpGet("https://www.google.com");

			HttpResponse response = httpClient.execute(httpGet);

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();

			String line = null;
			try {
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
				if (sb.length() > 0) {
					return true;
				} else {
					return false;
				}
			} catch (Exception ex) {
				return false;
			}

		} catch (ClientProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	@SuppressLint("TrulyRandom") 
	public static String encrypt(String strToEncrypt, String key) {

		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			final SecretKeySpec secretKeySpec = new SecretKeySpec(
					key.getBytes(), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

			final String encryptedString = Base64.encodeToString(
					cipher.doFinal(strToEncrypt.getBytes()), Base64.DEFAULT);

			return encryptedString;
		} catch (Exception e) {
		}
		return null;

	}

	public static String decrypt(String strToDecrypt, String key) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			final SecretKeySpec secretKeySpec = new SecretKeySpec(
					key.getBytes(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			final String decryptedString = new String(cipher.doFinal(Base64
					.decode(strToDecrypt.getBytes(), Base64.DEFAULT)));
			return decryptedString;
		} catch (Exception e) {
		}
		return null;
	}

//	public static final File getOuputAdsContentFile(Context context, int adsId) {
//
//		File file = new File(
//				context.getExternalFilesDir(Environment.DIRECTORY_PICTURES
//						+ File.separator + adsDirectory + File.separator
//						+ Integer.toString(adsId)), Integer.toString(adsId)
//						+ jpeg);
//		return file;
//	}
//
//	public static final File getOutputAdsDir(Context context, int adsId) {
//
//		File file = new File(
//				context.getExternalFilesDir(Environment.DIRECTORY_PICTURES
//						+ File.separator + adsDirectory),
//				Integer.toString(adsId));
//
//		return file;
//
//	}
//
//	public boolean clearAdsContent(int adsId, Context context, int dirSelection) {
//
//		File dir = null;
//
//		if (dirSelection == BusinessConstants.CLEAR_ADS) {
//			dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES
//					+ File.separator + adsDirectory + File.separator + adsId);
//		} else if (dirSelection == BusinessConstants.CLEAR_CORE) {
//			dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//		}
//
//		if (dir.exists() && dir.isDirectory()) {
//			if (recycledFiles == null) {
//				recycledFiles = new ArrayList<File>();
//			}
//			deleteDirectory(dir);
//			for (int i = recycledFiles.size() - 1; i >= 0; i--) {
//				recycledFiles.get(i).delete();
//			}
//		}
//
//		recycledFiles = null;
//
//		return true;
//	}
//
//	private void deleteDirectory(File rootFile) {
//		if (rootFile.exists()) {
//			if (rootFile.isDirectory()) {
//				recycledFiles.add(rootFile);
//				File files[] = rootFile.listFiles();
//				for (File subFile : files) {
//					if (subFile.isDirectory()) {
//						deleteDirectory(subFile);
//					} else {
//						subFile.delete();
//					}
//				}
//			} else {
//				rootFile.delete();
//			}
//		}
//	}
//
//	public static void sendGoogleAnalytics(String actionName, Activity activity) {
//
//		Bundle bundle = new Bundle();
//		bundle.putString(
//				activity.getResources().getString(R.string._B_ACTION_NAME),
//				actionName);
//		Intent intent = new Intent(activity, GoogleAnalyticsSenderService.class);
//		intent.putExtras(bundle);
//		activity.startService(intent);
//	}
	
	
	public static void hideKeyBoard(EditText editText, Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	/**
	 * Hides the soft keyboard
	 */
	public static void hideSoftKeyboard(Activity activity) {
		if (activity.getCurrentFocus() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity
					.getCurrentFocus().getWindowToken(), 0);
		}
	}


	@SuppressLint("NewApi") @SuppressWarnings("deprecation")
	public static void setRelativeLayoutBackground(
			RelativeLayout relativeLayout, Drawable drawable) {
		if (Build.VERSION.SDK_INT >= 16) {
			relativeLayout.setBackground(drawable);
		} else {
			relativeLayout.setBackgroundDrawable(drawable);
		}
	}

	@SuppressLint("NewApi") @SuppressWarnings("deprecation")
	public static void setViewBackground(View view, Drawable drawable) {
		if (Build.VERSION.SDK_INT >= 16) {
			view.setBackground(drawable);
		} else {
			view.setBackgroundDrawable(drawable);
		}
	}

	public static String parseURL(String actionMethodData) {
		if (actionMethodData.startsWith("http://")
				|| actionMethodData.startsWith("https://")) {
			return actionMethodData;
		}
		return "http://" + actionMethodData;
	}

	/*
	 * Load Resource String by Id
	 * */
	
	public static int loadgResourcesByString(Context context, String type, String title) {
		return context.getResources().getIdentifier(title, type, context.getPackageName());
	}
	
	
	public static LocalSettingModel getLocalSettingModel(Context activity) {
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
		boolean vibrate = sharedPref.getBoolean("notifications_new_message_vibrate", false);
		boolean notification = sharedPref.getBoolean("notifications_get_notifications", false);
		boolean saveSession = sharedPref.getBoolean("notifications_last_session", false);
		
		LocalSettingModel localSettingModel = new LocalSettingModel();
		
		localSettingModel.setNotification(notification);
		
		localSettingModel.setSaveSession(saveSession);
		
		localSettingModel.setVibrate(vibrate);
		
		return localSettingModel;
	}
}
