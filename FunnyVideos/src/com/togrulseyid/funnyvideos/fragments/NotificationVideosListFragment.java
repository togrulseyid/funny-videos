package com.togrulseyid.funnyvideos.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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
import com.togrulseyid.funnyvideos.R;
import com.togrulseyid.funnyvideos.activities.VideoPlayerActivity;
import com.togrulseyid.funnyvideos.adapters.VideosListAdapter;
import com.togrulseyid.funnyvideos.constants.MessageConstants;
import com.togrulseyid.funnyvideos.models.NotificationModel;
import com.togrulseyid.funnyvideos.models.VideoListModel;
import com.togrulseyid.funnyvideos.models.VideoModel;
import com.togrulseyid.funnyvideos.operations.NetworkOperations;
import com.togrulseyid.funnyvideos.views.InfoToast;

public class NotificationVideosListFragment extends Fragment {

	private PullToRefreshListView listView;
	private TextView textViewTapToRefresh;
	private VideosListAdapter adapter;
	private ArrayList<VideoModel> models;
	private VideosListAsynTask videosListAsynTask;
	private InfoToast infoToast;
	private NotificationModel model;
	
	public NotificationVideosListFragment(NotificationModel model, ActionBar actionBar) {
		this.model = model;
		actionBar.setTitle(model.getMessage());
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
		refreshList();

		return view;
	}

	VideoListModel coreModel = new VideoListModel();

	private void refreshList() {

		setMenuVisibility(false);
		models.clear();
		adapter.notifyDataSetChanged();
		models.add(new VideoModel());

		listView.setMode(Mode.DISABLED);
		listView.setRefreshing(false);

		videosListAsynTask = new VideosListAsynTask(getActivity(), true);
		coreModel.setGcm_id(model.getData());
		videosListAsynTask.execute(coreModel);

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
			return networkOperations.getNotificationVideosListModel(params[0]);
		}

		@Override
		protected void onPostExecute(VideoListModel result) {
			super.onPostExecute(result);
			
			infoToast = new InfoToast(getActivity());

			if (isAdded() && !isCancelled()) {
				if (result != null && result.getMessageId() != null) {
					if (result.getMessageId() == MessageConstants.SUCCESSFUL) {

						if (isRefresh) {
							models.clear();
						}
						for (VideoModel videoModel : result.getVideos()) {
							models.add(videoModel);
						}
						adapter.notifyDataSetChanged();

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

				listView.onRefreshComplete();

				if (models.size() > 1) {
					listView.setMode(Mode.DISABLED);

					// When user have friends set list visible
					textViewTapToRefresh.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
				} else {
					 listView.setMode(Mode.DISABLED);

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
					models.get(position));

			Intent intent = new Intent(activity, VideoPlayerActivity.class);
			intent.putExtras(bundle);

			startActivity(intent);

		}
	}

	private class CustomOnRefreshListener implements
			OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			refreshList();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			refreshList();
		}

	}

	class FVOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.textViewTapToRefresh:
				refreshList();
				break;

			default:
				break;
			}
		}

	}

	public void clearModels() {
		models.clear();
		adapter.notifyDataSetChanged();
	}

}
