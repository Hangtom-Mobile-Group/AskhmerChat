package com.askhmer.chat.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.ViewPhoto;
import com.askhmer.chat.model.Message;
import com.askhmer.chat.network.API;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessagesListAdapter extends BaseAdapter {

	private Context context;
	private List<Message> messagesItems;
	String user_id;
	private SharedPreferencesFile mSharedPrefer;
	private String id = null;
	private boolean found;

	public MessagesListAdapter(Context context, List<Message> navDrawerItems) {
		this.context = context;
		this.messagesItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return messagesItems.size();
	}

	@Override
	public Object getItem(int position) {
		return messagesItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		mSharedPrefer = SharedPreferencesFile.newInstance(context, SharedPreferencesFile.PREFER_FILE_NAME);
		user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);

		/**
		 * The following list not implemented reusable list items as list items
		 * are showing incorrect data Add the solution if you have one
		 * */

		final Message m = messagesItems.get(position);
		id = m.getUserId();

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


		if (m.isSelf()|| id.equals(user_id)) {
			// message belongs to you, so load the right aligned layout
			convertView = mInflater.inflate(R.layout.list_item_message_right, null);

		}else {
			// message belongs to other person, load the left aligned layout
			convertView = mInflater.inflate(R.layout.list_item_message_left, null);

		}

		TextView lblDate = (TextView) convertView.findViewById(R.id.lbl_date_message);
		TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);
		ImageView friProfile = (ImageView) convertView.findViewById(R.id.fri_profile);

		ImageView image_send = (ImageView) convertView.findViewById(R.id.image_send);
		ImageView sticker = (ImageView) convertView.findViewById(R.id.iv_sticker);

		SimpleExoPlayerView simpleExoPlayerView = (SimpleExoPlayerView)convertView.findViewById(R.id.player_audio);
		LinearLayout layoutMsgAudio = (LinearLayout)convertView.findViewById(R.id.layout_msg_audio);

		LinearLayout layoutMsgText = (LinearLayout) convertView.findViewById(R.id.layout_msg_text);
		LinearLayout layoutMsgImg = (LinearLayout) convertView.findViewById(R.id.layout_msg_img);
		LinearLayout layoutMsgSticker = (LinearLayout)convertView.findViewById(R.id.layout_msg_sticker);



		txtMsg.setText(m.getMessage());
		lblDate.setText(m.getMsgDate());

		Uri uri = m.getUri();
		String image_send_path =m.getMessage();


		String imgPath = m.getUserProfile();
		String imgPaht1 = API.UPLOADFILE +m.getUserProfile();
		try {

			if(imgPath.contains("https://graph.facebook.com")){
				Picasso.with(context).load(imgPath).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(friProfile);
			}else if(imgPath.contains("http://chat.askhmer.com/resources/upload/file")){
				Picasso.with(context).load(imgPath).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(friProfile);
			}else{
				Picasso.with(context).load(imgPaht1).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(friProfile);
			}



			if(uri!=null){
				layoutMsgText.setVisibility(View.GONE);
				layoutMsgImg.setVisibility(View.VISIBLE);
				Picasso.with(context).load(uri)
//						.memoryPolicy(MemoryPolicy.NO_CACHE)
						.noFade()
						.fit()
						.centerInside()
						.placeholder(R.drawable.loading)
						.error(R.drawable.loading)
						.into(image_send);
			} else if(image_send_path.contains("http://chat.askhmer.com/resources/upload/file/images")){
				layoutMsgText.setVisibility(View.GONE);
				layoutMsgImg.setVisibility(View.VISIBLE);
				Picasso.with(context).load(image_send_path)
//						.memoryPolicy(MemoryPolicy.NO_CACHE)
						.noFade()
						.fit()
						.centerInside()
						.placeholder(R.drawable.loading)
						.error(R.drawable.loading)
						.into(image_send);
			}else if(image_send_path.contains("http://chat.askhmer.com/resources/upload/file/sticker")){
				layoutMsgText.setVisibility(View.GONE);
				layoutMsgImg.setVisibility(View.GONE);
				layoutMsgSticker.setVisibility(View.VISIBLE);
				Picasso.with(context).load(image_send_path)
//						.memoryPolicy(MemoryPolicy.NO_CACHE)
						.noFade()
						.fit()
						.centerInside()
						.placeholder(R.drawable.loading)
						.error(R.drawable.loading)
						.into(sticker);

			} else if (image_send_path.contains("http://chat.askhmer.com/resources/upload/file/audio")) {
				layoutMsgText.setVisibility(View.GONE);
				layoutMsgImg.setVisibility(View.GONE);
				layoutMsgSticker.setVisibility(View.GONE);
				layoutMsgAudio.setVisibility(View.VISIBLE);

				// Create Player
				android.os.Handler mainHandler = new android.os.Handler();
				BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
				TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);

				TrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);

				LoadControl loadControl = new DefaultLoadControl();
				final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
				simpleExoPlayerView.setPlayer(player);

				// Prepare Player
				DefaultBandwidthMeter bandwidthMeter1 = new DefaultBandwidthMeter();
				DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
						Util.getUserAgent(context, "Askhmer Chat"), bandwidthMeter1);
				ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

				MediaSource videoSource = new ExtractorMediaSource(Uri.parse(image_send_path),
						dataSourceFactory, extractorsFactory, null, null);
				player.prepare(videoSource);

			} else {
				layoutMsgText.setVisibility(View.VISIBLE);
				layoutMsgImg.setVisibility(View.GONE);
				layoutMsgSticker.setVisibility(View.GONE);
				layoutMsgAudio.setVisibility(View.GONE);
			}


			image_send.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ViewPhoto.class);
					intent.putExtra("image",m.getMessage());
					context.startActivity(intent);
				}
			});



		}catch (NullPointerException e){
			e.printStackTrace();
		}


/*
		String path = m.getUserProfile();
		Boolean found = path.contains("facebook");

		String imgPaht1 = API.UPLOADFILE + path;
		String imgPaht2 = path;


		if(found == false){
			Picasso.with(context).load(imgPaht1).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(friProfile);
		}else{
			Picasso.with(context).load(imgPaht2).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(friProfile);
		}
*/

		return convertView;
	}

}
