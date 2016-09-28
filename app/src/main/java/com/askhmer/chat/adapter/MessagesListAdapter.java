package com.askhmer.chat.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.askhmer.chat.R;
import com.askhmer.chat.model.Message;
import com.askhmer.chat.network.API;
import com.askhmer.chat.util.SharedPreferencesFile;
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

		Message m = messagesItems.get(position);
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

		txtMsg.setText(m.getMessage());
		lblDate.setText(m.getMsgDate());


		String imgPath = m.getUserProfile();

//		found = imgPath.contains("https://graph.facebook.com");
		String imgPaht1 = API.UPLOADFILE +m.getUserProfile();

		if(imgPath.contains("https://graph.facebook.com")){
			Picasso.with(context).load(imgPath).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(friProfile);
		}else if(imgPath.contains("http://chat.askhmer.com/resources/upload/file")){
			Picasso.with(context).load(imgPath).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(friProfile);
		}
		else{
			Picasso.with(context).load(imgPaht1).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(friProfile);
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
