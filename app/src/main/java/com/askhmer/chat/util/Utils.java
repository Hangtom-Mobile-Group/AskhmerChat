package com.askhmer.chat.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {

	private Context context;
	private SharedPreferences sharedPref;

	private static final String KEY_SHARED_PREF = "ANDROID_WEB_CHAT";
	private static final int KEY_MODE_PRIVATE = 0;
	private static final String KEY_SESSION_ID = "sessionId",
			FLAG_MESSAGE = "message";

	String user_id;
	private SharedPreferencesFile mSharedPrefer;

	public Utils(Context context) {
		this.context = context;
		sharedPref = this.context.getSharedPreferences(KEY_SHARED_PREF,
				KEY_MODE_PRIVATE);
	}

	public void storeSessionId(String sessionId) {
		Editor editor = sharedPref.edit();
		editor.putString(KEY_SESSION_ID, sessionId);
		editor.commit();
	}

	public String getSessionId() {
		return sharedPref.getString(KEY_SESSION_ID, null);
	}

	public String getSendMessageJSON(String message) {
		String json = null;

		mSharedPrefer = SharedPreferencesFile.newInstance(context, SharedPreferencesFile.PREFER_FILE_NAME);
		user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);

		try {
			JSONObject jObj = new JSONObject();
			jObj.put("flag", FLAG_MESSAGE);
			jObj.put("sessionId", getSessionId());
			jObj.put("message", message);
//			jObj.put("userId",user_id);

			json = jObj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

}
