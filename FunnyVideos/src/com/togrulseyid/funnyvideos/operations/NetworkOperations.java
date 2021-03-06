package com.togrulseyid.funnyvideos.operations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.util.Log;

import com.togrulseyid.funnyvideos.constants.BusinessConstants;
import com.togrulseyid.funnyvideos.constants.MessageConstants;
import com.togrulseyid.funnyvideos.constants.UrlConstants;
import com.togrulseyid.funnyvideos.models.CoreModel;
import com.togrulseyid.funnyvideos.models.GCMModel;
import com.togrulseyid.funnyvideos.models.SettingNotificationModel;
import com.togrulseyid.funnyvideos.models.VideoListModel;

public class NetworkOperations {

	private Context context;

	public NetworkOperations(Context context) {
		this.context = context;
	}

	private String urlGenerator(String decryptedUrl, VideoListModel videoListModel) {
		return decryptedUrl + "id=" + videoListModel.getStartId() + "&max=" + videoListModel.getMaxCount();
	}

	private String notificationUrlGenerator(String decryptedUrl, VideoListModel videoListModel) {
		return decryptedUrl + videoListModel.getGcm_id();
	}

	public VideoListModel getVideosListModel(VideoListModel model) {
		model = (VideoListModel) SPProvider.initializeObject(model, context);

		if (!Utility.checkNetwork(context)) {
			model.setMessageId(MessageConstants.NO_NETWORK_CONNECTION);
		} else if (!Utility.checkInternetConnection()) {
			model.setMessageId(MessageConstants.NO_INTERNET_CONNECTION);
		} else {
			try {

				ObjectConvertor<CoreModel> objectConvertorModel = new ObjectConvertor<CoreModel>();

				String result = postAndResponseString(
						objectConvertorModel.getClassString(model),
						urlGenerator(Utility.decrypt(
								UrlConstants.URL_VIDEOS_LIST,
								Utility.getAppSignature(context)), model),
						BusinessConstants.CONNECTION_TIMEOUT,
						BusinessConstants.BUSINESS_DATA_TIMEOUT);

				ObjectConvertor<VideoListModel> objectConvertorChannelModelList = new ObjectConvertor<VideoListModel>();

				model = objectConvertorChannelModelList.getClassObject(
						Utility.decrypt(result,
								Utility.getAppSignature(context)),
						VideoListModel.class);

			} catch (ClientProtocolException ex) {
				model.setMessageId(MessageConstants.UN_SUCCESSFUL);
			} catch (IOException ex) {
				model.setMessageId(MessageConstants.UN_SUCCESSFUL);
			}
		}

		return model;
	}

	public VideoListModel getNotificationVideosListModel(VideoListModel videoListModel) {

		videoListModel = (VideoListModel) SPProvider.initializeObject(videoListModel,
				context);
		VideoListModel model = new VideoListModel();

		if (!Utility.checkNetwork(context)) {
			model.setMessageId(MessageConstants.NO_NETWORK_CONNECTION);
		} else if (!Utility.checkInternetConnection()) {
			model.setMessageId(MessageConstants.NO_INTERNET_CONNECTION);
		} else {

			try {

				ObjectConvertor<CoreModel> objectConvertorModel = new ObjectConvertor<CoreModel>();

				String result = postAndResponseString(
						objectConvertorModel.getClassString(videoListModel),
						notificationUrlGenerator(Utility.decrypt(
								UrlConstants.URL_GCM_NOTIFICATIONS_VIDEOS_LIST,
								Utility.getAppSignature(context)),
								videoListModel),
						BusinessConstants.CONNECTION_TIMEOUT,
						BusinessConstants.BUSINESS_DATA_TIMEOUT);

				ObjectConvertor<VideoListModel> objectConvertorUserModel = new ObjectConvertor<VideoListModel>();
//				Log.d("testABCD","" + Utility.decrypt(result, Utility.getAppSignature(context)));
//				Log.d("testABCD","" +notificationUrlGenerator(Utility.decrypt(
//						UrlConstants.URL_GCM_NOTIFICATIONS_VIDEOS_LIST,
//						Utility.getAppSignature(context)),
//						videoListModel));
				
				model = objectConvertorUserModel.getClassObject(Utility.decrypt(result, Utility.getAppSignature(context)), VideoListModel.class);

			} catch (ClientProtocolException ex) {
				model.setMessageId(MessageConstants.UN_SUCCESSFUL);
			} catch (IOException ex) {
				model.setMessageId(MessageConstants.UN_SUCCESSFUL);
			}

		}

		return model;
	}

	private String postAndResponseString(String convertedModel, String url,
			int connectionTimeout, int businessDataTimeout)
			throws ClientProtocolException, IOException {

		Log.d("testA", "input : " + convertedModel);

		HttpPost httpPost = new HttpPost(url);

		StringEntity entity = new StringEntity(convertedModel, HTTP.UTF_8);
		httpPost.setEntity(entity);
		HttpClient httpClient = getClient(connectionTimeout,
				businessDataTimeout);
		HttpResponse response = httpClient.execute(httpPost);

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		StringBuilder result = new StringBuilder();

		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			result.append(line);
		}

		Log.d("testA", "output : " + result.toString());
		Log.d("testA",
				"outputX : "
						+ Utility.decrypt(result.toString(),
								Utility.getAppSignature(context)));

		return result.toString();
	}

	private HttpClient getClient(int connectionTimeOut, int businessDataTimeout) {

		HttpClient httpClient = null;
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				connectionTimeOut);
		HttpConnectionParams.setSoTimeout(httpParameters, businessDataTimeout);

		httpClient = new DefaultHttpClient(httpParameters);

		return httpClient;
	}

	private String urlChecker(String url) {
		return url;
	}

	public CoreModel checkAppVersion(CoreModel model) {
		model = (CoreModel) SPProvider.initializeObject(model, context);

		if (!Utility.checkNetwork(context)) {
			model.setMessageId(MessageConstants.NO_NETWORK_CONNECTION);
		} else if (!Utility.checkInternetConnection()) {
			model.setMessageId(MessageConstants.NO_INTERNET_CONNECTION);
		} else {
			try {

				ObjectConvertor<CoreModel> objectConvertorModel = new ObjectConvertor<CoreModel>();

				String result = postAndResponseString(
						objectConvertorModel.getClassString(model),
						urlChecker(Utility.decrypt(
								UrlConstants.URL_CHECK_APP_VERSION,
								Utility.getAppSignature(context))), 4000, 4000);

				model = objectConvertorModel.getClassObject(
						Utility.decrypt(result,
								Utility.getAppSignature(context)),
								CoreModel.class);

				return model;

			} catch (ClientProtocolException ex) {
				model.setMessageId(MessageConstants.EXCEPTION_ERROR);
			} catch (IOException ex) {
				model.setMessageId(MessageConstants.EXCEPTION_ERROR);
			}
		}

		return model;
	}

	public SettingNotificationModel sendNotificationSettingsModel(
			SettingNotificationModel settingNotificationModel) {

		settingNotificationModel = (SettingNotificationModel) SPProvider
				.initializeObject(settingNotificationModel, context);

		if (!Utility.checkNetwork(context)) {
			settingNotificationModel
					.setMessageId(MessageConstants.NO_NETWORK_CONNECTION);
		} else if (!Utility.checkInternetConnection()) {
			settingNotificationModel
					.setMessageId(MessageConstants.NO_INTERNET_CONNECTION);
		} else {

			try {

				ObjectConvertor<SettingNotificationModel> objectConvertorModel = new ObjectConvertor<SettingNotificationModel>();

				String result = postAndResponseString(
						objectConvertorModel
								.getClassString(settingNotificationModel),
						Utility.decrypt(
								UrlConstants.URL_SEND_SETTINGS_NOTIFICATION_MODEL,
								Utility.getAppSignature(context)),
						BusinessConstants.CONNECTION_TIMEOUT,
						BusinessConstants.BUSINESS_DATA_TIMEOUT);

				ObjectConvertor<SettingNotificationModel> objectConvertorUserModel = new ObjectConvertor<SettingNotificationModel>();

				settingNotificationModel = objectConvertorUserModel
						.getClassObject(
								Utility.decrypt(result,
										Utility.getAppSignature(context)),
								SettingNotificationModel.class);

			} catch (ClientProtocolException ex) {

				settingNotificationModel
						.setMessageId(MessageConstants.UN_SUCCESSFUL);

			} catch (IOException ex) {

				settingNotificationModel
						.setMessageId(MessageConstants.UN_SUCCESSFUL);

			}

		}

		return settingNotificationModel;
	}

	public GCMModel sendGCMToServer(GCMModel gcmModel) {

		gcmModel = SPProvider.initializeGCMObject(gcmModel, context);

		if (!Utility.checkNetwork(context)) {
			gcmModel.setMessageId(MessageConstants.NO_NETWORK_CONNECTION);
		} else if (!Utility.checkInternetConnection()) {
			gcmModel.setMessageId(MessageConstants.NO_INTERNET_CONNECTION);
		} else {

			try {

				ObjectConvertor<GCMModel> objectConvertorModel = new ObjectConvertor<GCMModel>();
				String result = postAndResponseString(
						objectConvertorModel.getClassString(gcmModel),
						Utility.decrypt(
								UrlConstants.URL_GCM_NOTIFICATIONS_SEND_TO_SERVER,
								Utility.getAppSignature(context)),
						BusinessConstants.CONNECTION_TIMEOUT,
						BusinessConstants.BUSINESS_DATA_TIMEOUT);

				ObjectConvertor<GCMModel> objectConvertorUserModel = new ObjectConvertor<GCMModel>();
				gcmModel = objectConvertorUserModel.getClassObject(
						Utility.decrypt(result,
								Utility.getAppSignature(context)),
						GCMModel.class);

			} catch (ClientProtocolException ex) {
				gcmModel.setMessageId(MessageConstants.UN_SUCCESSFUL);
			} catch (IOException ex) {
				gcmModel.setMessageId(MessageConstants.UN_SUCCESSFUL);
			}

		}

		return gcmModel;
	}

	public static void submitUserInfo() {

	}

}