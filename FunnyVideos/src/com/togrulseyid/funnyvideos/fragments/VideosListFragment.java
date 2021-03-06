package com.togrulseyid.funnyvideos.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.togrulseyid.funnyvideos.R;
import com.togrulseyid.funnyvideos.activities.VideoPlayerActivity;
import com.togrulseyid.funnyvideos.adapters.VideosListAdapter;
import com.togrulseyid.funnyvideos.constants.MessageConstants;
import com.togrulseyid.funnyvideos.models.VideoListModel;
import com.togrulseyid.funnyvideos.models.VideoModel;
import com.togrulseyid.funnyvideos.operations.NetworkOperations;
import com.togrulseyid.funnyvideos.operations.Utility;
import com.togrulseyid.funnyvideos.views.InfoToast;

public class VideosListFragment extends Fragment {

	private static final int MAX_COUNT = 5;
	private PullToRefreshListView listView;
	private TextView textViewTapToRefresh;
	private VideosListAdapter adapter;
	private ArrayList<VideoModel> models;
	private VideosListAsynTask videosListAsynTask;
	private InfoToast infoToast;
	private VideoListModel videoListModel = new VideoListModel();
	private boolean isAds;
	private boolean isAds2;
	
	public VideosListFragment(boolean isAds, boolean isAds2) {
		this.isAds = isAds;
		this.isAds2 = isAds2;
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_videos_list, container,
				false);

		listView = (PullToRefreshListView) view.findViewById(R.id.listViewFragmentVideosList);
		textViewTapToRefresh = (TextView) view.findViewById(R.id.textViewTapToRefresh);

		models = new ArrayList<VideoModel>();
		adapter = new VideosListAdapter(getActivity(), models);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new CustomItemOnItemClickListener(getActivity()));

		listView.setOnRefreshListener(new CustomOnRefreshListener());

		textViewTapToRefresh.setOnClickListener(new FVOnClickListener());
		refreshList(true);
		
		/*
		 * MoPub Advertisement
		 */
		if (isAds) {
			
			moPubView = (MoPubView) view.findViewById(R.id.adview);
			moPubView.setAdUnitId(getString(R.string.mopub_ad_unit_id));
			moPubView.loadAd();
			moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
				@Override
				public void onBannerLoaded(MoPubView banner) {
					moPubView.setVisibility(View.VISIBLE);
//					FVLog.d("Loaded: " + banner.getResponseString());
				}
				@Override
				public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
					moPubView.setVisibility(View.GONE);
//					FVLog.e("MoPubErrorCode: " + errorCode.toString());
				}
				@Override
				public void onBannerExpanded(MoPubView banner) {}
				@Override
				public void onBannerCollapsed(MoPubView banner) {}
				@Override
				public void onBannerClicked(MoPubView banner) {}
			});
			
		}
		
		return view;
	}

	private MoPubView moPubView;

	public void onDestroy() {
		if (isAds) {
			moPubView.destroy();
		}
		super.onDestroy();
	}


	private void refreshList(boolean isUp) {

		setMenuVisibility(false);
		if (isUp) {
			models.clear();
			adapter.notifyDataSetChanged();
			models.add(new VideoModel());

			listView.setMode(Mode.DISABLED);
			listView.setRefreshing(false);

			videosListAsynTask = new VideosListAsynTask(getActivity(), true);
			videoListModel.setStartId(Utility.getLastSessionFromPreference(getActivity()));
			videoListModel.setMaxCount(MAX_COUNT);
			videosListAsynTask.execute(videoListModel);
		} else {
			// load more items

			videosListAsynTask = new VideosListAsynTask(getActivity(), false);
			videoListModel.setStartId(videoListModel.getStartId());
			videoListModel.setMaxCount(MAX_COUNT);
			videosListAsynTask.execute(videoListModel);

		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.title:

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private class VideosListAsynTask extends
			AsyncTask<VideoListModel, Integer, VideoListModel> {

		private Activity activity;
		private boolean isRefresh;

		public VideosListAsynTask(Activity activity, boolean isRefresh) {
			this.activity = activity;
			this.isRefresh = isRefresh;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			textViewTapToRefresh.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}

		@Override
		protected VideoListModel doInBackground(VideoListModel... params) {
			NetworkOperations networkOperations = new NetworkOperations(
					activity);
			return networkOperations.getVideosListModel(params[0]);
		}

		@Override
		protected void onPostExecute(VideoListModel result) {
			super.onPostExecute(result);
			videoListModel = result;

			infoToast = new InfoToast(getActivity());

			if (isAdded() && !isCancelled()) {

				if (result != null){
					if (result.getMessageId() != null) {

						if (result.getMessageId() == MessageConstants.SUCCESSFUL) {

							if (isRefresh) {
								models.clear();
							}

							for (VideoModel videoModel : result.getVideos()) {

								models.add(videoModel);

							}
							adapter.notifyDataSetChanged();

							Utility.saveLastSessionToPreference(getActivity(),
									models.size());

						} else if (result.getMessageId() == MessageConstants.NO_INTERNET_CONNECTION) {

							listView.setVisibility(View.GONE);

							textViewTapToRefresh.setVisibility(View.VISIBLE);
							textViewTapToRefresh.setText(getResources().getString(
									R.string.message_internet_connection_problem));

						} else if (result.getMessageId() == MessageConstants.NO_NETWORK_CONNECTION) {

							// adapter.notifyDataSetChanged();
							listView.setVisibility(View.GONE);

							textViewTapToRefresh.setVisibility(View.VISIBLE);

							textViewTapToRefresh.setText(getResources().getString(
									R.string.message_network_connection_problem));

						} else {
							infoToast.makeToast(result.getMessageId());
							// adapter.notifyDataSetChanged();
						}
					} else {
						infoToast.makeToast(MessageConstants.SERVER_ERROR_1);
						// adapter.notifyDataSetChanged();
					}

				} else {
					textViewTapToRefresh.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				}
				listView.onRefreshComplete();

				if (models.size() > 1) {
					listView.setMode(Mode.PULL_FROM_END);

					// When user have friends set list visible
					textViewTapToRefresh.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
				} else {
					// listView.setMode(Mode.DISABLED);
					// When user have no friends set list gone
					textViewTapToRefresh.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				}

				setMenuVisibility(true);

			}

		}

	}

	class CustomItemOnItemClickListener implements OnItemClickListener {

		private Activity activity;

		public CustomItemOnItemClickListener(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {

			Bundle bundle = new Bundle();

			bundle.putSerializable(
					getResources().getString(R.string._B_SELECTED_VIDEO_ITEM),
					models.get(position-1));
			bundle.putBoolean(getString(R.string.intent_bundle_ads2), isAds2);

			Intent intent = new Intent(activity, VideoPlayerActivity.class);
			intent.putExtras(bundle);

			startActivity(intent);

		}
	}

	private class CustomOnRefreshListener implements
			OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			refreshList(true);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			refreshList(false);
		}

	}

	class FVOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.textViewTapToRefresh:
				refreshList(true);
				break;

			default:
				break;
			}
		}

	}

}
