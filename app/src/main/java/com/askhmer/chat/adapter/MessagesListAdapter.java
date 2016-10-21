package com.askhmer.chat.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.ViewPhoto;
import com.askhmer.chat.listener.SendAudioListener;
import com.askhmer.chat.model.Message;
import com.askhmer.chat.network.API;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessagesListAdapter extends BaseAdapter {

	private Context context;
	private MediaPlayer mediaPlayer;
	private List<Message> messagesItems;
	String user_id;
	private SharedPreferencesFile mSharedPrefer;
	private String id = null;
	//private boolean found;
//	private  ToggleButton currentToggleButton;

	private MediaObserver observer = null;
	private int current_Progress=0;
	private SendAudioListener sendAudioListener;
	private ImageButton currentImageButton;
	private RelativeLayout currentAudioLayout;
	private LinearLayout currentCoverLayout;


	public MessagesListAdapter(Context context, List<Message> navDrawerItems) {
		this.context = context;
		this.messagesItems = navDrawerItems;
		this.sendAudioListener= (SendAudioListener) context;
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
	public View getView(final int position, View convertView, ViewGroup parent) {

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


		final LinearLayout layoutMsgText = (LinearLayout) convertView.findViewById(R.id.layout_msg_text);
		RelativeLayout layoutMsgImg = (RelativeLayout) convertView.findViewById(R.id.layout_msg_img);
		RelativeLayout layoutMsgSticker = (RelativeLayout)convertView.findViewById(R.id.layout_msg_sticker);


		final ProgressBar progressBarImg = (ProgressBar) convertView.findViewById(R.id.progressBar_image);
		progressBarImg.setVisibility(View.VISIBLE);

		final ProgressBar progressBarSticker = (ProgressBar) convertView.findViewById(R.id.progressBar_sticker);
		progressBarSticker.setVisibility(View.VISIBLE);

		//For Audio
		CardView layoutMsgAudio = (CardView)convertView.findViewById(R.id.layout_msg_audio);
		final TextView audioTimeTextView= (TextView) convertView.findViewById(R.id.txt_media_second);
		final ImageButton btnPlayAudio = (ImageButton) convertView.findViewById(R.id.btn_play_audio);
		final RelativeLayout audioLayout= (RelativeLayout) convertView.findViewById(R.id.audio_layout);
		final LinearLayout coverLayout= (LinearLayout) convertView.findViewById(R.id.cover_layout);
		//TextView txtPlay= (TextView) convertView.findViewById(R.id.txtplay);

		if (m.isSelf()|| id.equals(user_id)) {
			btnPlayAudio.setImageResource(R.drawable.playbuttonright);
			btnPlayAudio.setTag(R.drawable.playbuttonright);
		}else{
			btnPlayAudio.setImageResource(R.drawable.playbuttonleft);
			btnPlayAudio.setTag(R.drawable.playbuttonleft);
		}

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
			} else if(image_send_path.contains("http://chat.askhmer.com/resources/upload/file/thumnails")){
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
			/*	txtPlay.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//Toast.makeText(context, "Txt play Click", Toast.LENGTH_SHORT).show();
						btnPlayAudio.performClick();
					}
				});
*/
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
					if(m.getMessage()!=null) {
						String imagePath = m.getMessage().replace("thumnails", "images");
						intent.putExtra("image", imagePath);
					}else{
						intent.putExtra("image", m.getUri().toString());
					}
					context.startActivity(intent);
				}
			});

			audioLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					btnPlayAudio.performClick();
				}
			});

			audioLayout.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					sendAudioListener.longItemClick(position);
					return true;
				}
			});

			btnPlayAudio.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					current_Progress=0;
					if(currentImageButton != (ImageButton)v){
						sendAudioListener.changeImageButton(currentImageButton,R.drawable.playbuttonleft,R.drawable.playbuttonright);
						sendAudioListener.setCoverLayoutWidth(currentAudioLayout,currentCoverLayout,0,0);
					}
					currentAudioLayout= audioLayout;
					currentCoverLayout= coverLayout;

					final ImageButton imageButton= (ImageButton) v;
					currentImageButton=imageButton;
					int resource= (int) imageButton.getTag();
					if(resource==R.drawable.playbuttonleft ||resource==R.drawable.playbuttonright) {
						//imageButton.setImageResource(R.drawable.stopbuttonleft);
						//imageButton.setTag(R.drawable.stopbuttonleft);
						sendAudioListener.changeImageButton(imageButton,R.drawable.stopbuttonleft,R.drawable.stopbuttonright);
						try {
							if (mediaPlayer != null && mediaPlayer.isPlaying()) {
								mediaPlayer.stop();
								mediaPlayer.release();
								mediaPlayer=null;
							}
							if (observer != null) {
								observer.stop();
							}
							try {
								mediaPlayer = MediaPlayer.create(context, Uri.parse(m.getMessage()));
								sendAudioListener.setCoverLayoutWidth(audioLayout,coverLayout, mediaPlayer.getDuration(),-1);
								mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
									@Override
									public void onCompletion(MediaPlayer mMediaPlayer) {
										observer.stop();
										int second = (int) Math.ceil(mediaPlayer.getDuration() / 1000);
										String seconStr = second > 9 ? second + "" : "0" + second;
										sendAudioListener.setAudioTime(audioTimeTextView, "0:" + seconStr);
										sendAudioListener.changeImageButton(imageButton,R.drawable.playbuttonleft,R.drawable.playbuttonright);
										sendAudioListener.setCoverLayoutWidth(audioLayout,coverLayout,0,0);
									}
								});
								observer = new MediaObserver(mediaPlayer, audioTimeTextView);
								mediaPlayer.start();
								new Thread(observer).start();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} catch (Exception e) {
							e.getMessage();
						}
					}else if(resource==R.drawable.stopbuttonleft || resource==R.drawable.stopbuttonright){
						//imageButton.setImageResource(R.drawable.playbutton);
						//imageButton.setTag(R.drawable.playbutton);
						sendAudioListener.changeImageButton(imageButton,R.drawable.playbuttonleft,R.drawable.playbuttonright);
						sendAudioListener.setCoverLayoutWidth(audioLayout,coverLayout,0,0);
						int second = (int) Math.ceil(mediaPlayer.getDuration() / 1000);
						String seconStr = second > 9 ? second + "" : "0" + second;
						sendAudioListener.setAudioTime(audioTimeTextView, "0:" + seconStr);
						try {
							if (mediaPlayer != null && mediaPlayer.isPlaying()) {
								mediaPlayer.stop();
								mediaPlayer.release();
								mediaPlayer=null;
							}
							if (observer != null) {
								observer.stop();
							}
						}catch (Exception e){
							e.printStackTrace();
						}
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


	private class MediaObserver implements Runnable {
		private AtomicBoolean stop = new AtomicBoolean(false);
		private TextView textView;
		private MediaPlayer mediaPlayer;

		public MediaObserver(MediaPlayer mediaPlayer,TextView textView){
			  this.mediaPlayer=mediaPlayer;
			  this.textView=textView;
		}
		public void stop() {
			stop.set(true);
		}

		@Override
		public void run() {
			while (!stop.get()) {
				long second=(long) Math.ceil(mediaPlayer.getDuration()/1000)-current_Progress;
				if(second >= 0) {
					String seconStr = second > 9 ? second + "" : "0" + second;
					sendAudioListener.setAudioTime(textView, "0:" + seconStr);
				}
				try {
					Thread.sleep(1000);
					current_Progress+=1;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
