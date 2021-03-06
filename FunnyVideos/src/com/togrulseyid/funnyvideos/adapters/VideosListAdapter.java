package com.togrulseyid.funnyvideos.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.togrulseyid.funnyvideos.R;
import com.togrulseyid.funnyvideos.models.VideoModel;

public class VideosListAdapter extends BaseAdapter {

	private ArrayList<VideoModel> models;
	private Activity activity;
	private ViewHolder viewHolder;
	private int density;

	public VideosListAdapter(Activity activity, ArrayList<VideoModel> models) {
		this.activity = activity;
		this.models = models;
		this.density = activity.getResources().getDisplayMetrics().densityDpi;
	}

	@Override
	public int getCount() {
		return models.size();
	}

	@Override
	public VideoModel getItem(int position) {
		return models.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		View view = convertView;
		if (convertView == null) {

			view = activity.getLayoutInflater().inflate(
					R.layout.item_videos_list_fragment, viewGroup, false);
			viewHolder = new ViewHolder();

			viewHolder.imageView = (ImageView) view
					.findViewById(R.id.imageViewItemVideosListFragment);
			viewHolder.textView = (TextView) view
					.findViewById(R.id.textViewItemVideosListFragment);

			view.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		VideoModel model = getItem(position);

		if (model.getSrc() != null) {
			view.setVisibility(View.VISIBLE);

			if (model.getTitle() != null) {
				
				viewHolder.textView.setText(model.getTitle());
				
			}

			if (model.getSrc() != null) {

				Log.d("testV","density: " + imageGenerator(model.getSrc()));
				Picasso.with(activity).load(imageGenerator(model.getSrc()))
						.error(R.drawable.video_default)
						.placeholder(R.drawable.video_default)
						.into(viewHolder.imageView);

			}

		}

		return view;
	}

	private String imageGenerator(String src) {
		switch (density) {
		case DisplayMetrics.DENSITY_LOW:
			return "http://img.youtube.com/vi/" + src + "/default.jpg";

		case DisplayMetrics.DENSITY_MEDIUM:
			return "http://img.youtube.com/vi/" + src + "/mqdefault.jpg";

		case DisplayMetrics.DENSITY_HIGH:
			return "http://img.youtube.com/vi/" + src + "/hqdefault.jpg";

		case DisplayMetrics.DENSITY_XHIGH:
			return "http://img.youtube.com/vi/" + src + "/sddefault.jpg";

		case DisplayMetrics.DENSITY_XXHIGH:
			return "http://img.youtube.com/vi/" + src + "/maxresdefault.jpg";

		case DisplayMetrics.DENSITY_XXXHIGH:
			return "http://img.youtube.com/vi/" + src + "/maxresdefault.jpg";

		default:
			return "http://img.youtube.com/vi/" + src + "/default.jpg";
		}
	}

	private class ViewHolder {
		private TextView textView;
		private ImageView imageView;
	}
}
