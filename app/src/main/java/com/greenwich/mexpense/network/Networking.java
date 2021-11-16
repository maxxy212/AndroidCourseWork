package com.greenwich.mexpense.network;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;

import org.json.JSONObject;

/**
 * Package com.greenwich.mexpense.network in
 * <p>
 * Project MExpense
 * <p>
 * Created by Maxwell on 16/11/2021
 */
public class Networking {
    private static final String TAG = Networking.class.getSimpleName();
    private final static String API_ENDPOINT = "http://gillwindallapp1.appspot.com/madservlet";

    public static void postData(String endpoint,
                                JSONObject jsonObject,
                                OkHttpResponseAndJSONObjectRequestListener listener) {
        Log.d("posting shit", API_ENDPOINT + endpoint);
        AndroidNetworking.post(API_ENDPOINT + endpoint)
                .addJSONObjectBody(jsonObject)
                .setTag(endpoint)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Content-Type", "application/json")
                .build()
                .getAsOkHttpResponseAndJSONObject(listener);
    }
}
