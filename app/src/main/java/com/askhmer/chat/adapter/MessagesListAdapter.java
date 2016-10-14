package com.askhmer.chat.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.ViewPhoto;
import com.askhmer.chat.model.Message;
import com.askhmer.chat.network.API;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessagesListAdapter extends BaseAdapter {

	private Context context;
	private MediaPlayer mediaPlayer;
	private List<Message> messagesItems;
	String user_id;
	private SharedPreferencesFile mSharedPrefer;
	private String id = null;
	private boolean found;
	private  ToggleButton currentToggleButton;

	public MessagesListAdapter(Context context, List<Message> navDrawerItems) {
		this.context = context;
		this.messagesItems = navDrawerItems;
	}


	public void stopMedia() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
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

//		SimpleExoPlayerView simpleExoPlayerView = (SimpleExoPlayerView)convertView.findViewById(R.id.player_audio);
		LinearLayout layoutMsgAudio = (LinearLayout)convertView.findViewById(R.id.layout_msg_audio);

		LinearLayout layoutMsgText = (LinearLayout) convertView.findViewById(R.id.layout_msg_text);
		RelativeLayout layoutMsgImg = (RelativeLayout) convertView.findViewById(R.id.layout_msg_img);
		RelativeLayout layoutMsgSticker = (RelativeLayout)convertView.findViewById(R.id.layout_msg_sticker);


		final ProgressBar progressBarImg = (ProgressBar) convertView.findViewById(R.id.progressBar_image);
		progressBarImg.setVisibility(View.VISIBLE);

		final ProgressBar progressBarSticker = (ProgressBar) convertView.findViewById(R.id.progressBar_sticker);
		progressBarSticker.setVisibility(View.VISIBLE);



		final Button btnPlayAudio = (Button) convertView.findViewById(R.id.btn_play_audio);


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
//						.placeholder(R.drawable.progress_animation)
						.error(R.drawable.image_error)
						.into(image_send, new Callback() {
							@Override
							public void onSuccess() {
								if (progressBarImg != null) {
									progressBarImg.setVisibility(View.GONE);
								}
							}

							@Override
							public void onError() {

							}
						});
			} else if(image_send_path.contains("http://chat.askhmer.com/resources/upload/file/images")){
				layoutMsgText.setVisibility(View.GONE);
				layoutMsgImg.setVisibility(View.VISIBLE);
				Picasso.with(context).load(image_send_path)
//						.memoryPolicy(MemoryPolicy.NO_CACHE)
						.noFade()
						.fit()
						.centerInside()
//						.placeholder(R.drawable.progress_animation)
						.error(R.drawable.image_error)
						.into(image_send, new Callback() {
							@Override
							public void onSuccess() {
								if (progressBarImg != null) {
									progressBarImg.setVisibility(View.GONE);
								}
							}

							@Override
							public void onError() {

							}
						});
			}else if(image_send_path.contains("http://chat.askhmer.com/resources/upload/file/sticker")){
				layoutMsgText.setVisibility(View.GONE);
				layoutMsgImg.setVisibility(View.GONE);
				layoutMsgSticker.setVisibility(View.VISIBLE);
				Picasso.with(context).load(image_send_path)
//						.memoryPolicy(MemoryPolicy.NO_CACHE)
						.noFade()
						.fit()
						.centerInside()
//						.placeholder(R.drawable.progress_animation)
						.error(R.drawable.image_error)
						.into(sticker, new Callback() {
							@Override
							public void onSuccess() {
								if (progressBarSticker != null) {
									progressBarSticker.setVisibility(View.GONE);
								}
							}

							@Override
							public void onError() {

							}
						});

			} else if (image_send_path.contains("http://chat.askhmer.com/resources/upload/file/audio")) {
				layoutMsgText.setVisibility(View.GONE);
				layoutMsgImg.setVisibility(View.GONE);
				layoutMsgSticker.setVisibility(View.GONE);
				layoutMsgAudio.setVisibility(View.VISIBLE);


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
					intent.putExtra("image", m.getMessage());
					context.startActivity(intent);
				}
			});




			btnPlayAudio.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						if (mediaPlayer != null && mediaPlayer.isPlaying() ) {
							mediaPlayer.stop();
						}
						mediaPlayer = MediaPlayer.create(context, Uri.parse(m.getMessage()));
						mediaPlayer.start();
					} catch (Exception e) {
						e.getMessage();
					}

				}
			});


/*
			btnPlayAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					currentToggleButton=btnPlayAudio;

					if(isChecked){
						if (mediaPlayer != null && mediaPlayer.isPlaying() ) {
							stopMyMedia(currentToggleButton);
						}
					}else {

						if (mediaPlayer != null && mediaPlayer.isPlaying() ) {
							mediaPlayer.stop();
						}
						mediaPlayer = MediaPlayer.create(context, Uri.parse(m.getMessage()));
						mediaPlayer.start();

						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								btnPlayAudio.setChecked(true);
							}
						});

					}
				}
			});
*/


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

	public void stopMyMedia(ToggleButton toggleButton){
		mediaPlayer.stop();
		toggleButton.setChecked(true);
	}

}
