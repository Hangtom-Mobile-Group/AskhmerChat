package com.askhmer.chat.network;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GsonObjectRequest extends JsonObjectRequest {

    public GsonObjectRequest(int method, String url, JSONObject params, Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, params, reponseListener,errorListener);
    }

    public GsonObjectRequest(int method, String url, Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, reponseListener,errorListener);
    }

    // Header for API Access
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String,String> header = new HashMap<>();
        header.put("Authorization",API.key);
        return header;
    }

    // Increase Time Out Duration
    @Override
    public RetryPolicy getRetryPolicy() {
        //return new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}