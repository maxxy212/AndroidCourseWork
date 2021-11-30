package com.greenwich.mexpense.viewmodel;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.greenwich.mexpense.network.ApiCallHandler;
import com.greenwich.mexpense.network.ApiReader;
import com.greenwich.mexpense.network.Networking;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.Response;

/**
 * Package com.greenwich.mexpense.viewmodel in
 * <p>
 * Project MExpense
 * <p>
 * Created by Maxwell on 30/11/2021
 */
public class TripsViewModel {

    private static final String TAG = TripsViewModel.class.getSimpleName();
    private final Context context;

    private static final String UPLOAD = "http://gillwindallapp1.appspot.com/madservlet";

    @Inject
    public TripsViewModel(@ApplicationContext Context context){
        this.context = context;
    }

    public void uploadTrips(JSONObject jsonObject, final ApiCallHandler callHandler) {
        Networking.postData(UPLOAD, jsonObject, new OkHttpResponseAndJSONObjectRequestListener() {
            @Override
            public void onResponse(Response okHttpResponse, JSONObject response) {
                final ApiReader apiReader = new ApiReader(okHttpResponse, response);
                Log.d(TAG, "onResponse: "+apiReader.toString());
                if (apiReader.isSuccess()) {
                    callHandler.success(apiReader.getData().optString("message"));
                }else {
                    callHandler.failed("Error", apiReader.getNetworkErrorMsg(context));
                }
            }

            @Override
            public void onError(ANError anError) {
                final ApiReader apiReader = new ApiReader(anError);
                apiReader.handleError(context, anError, callHandler, TAG);
            }
        });
    }
}
