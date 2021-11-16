package com.greenwich.mexpense.network;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.greenwich.mexpense.R;

import org.json.JSONObject;

import okhttp3.Response;

/**
 * Package com.greenwich.mexpense.network in
 * <p>
 * Project MExpense
 * <p>
 * Created by Maxwell on 16/11/2021
 */
public class ApiReader {
    private static final String connect_error = "connectionError";
    private static final String parse_error = "parseError";
    private static final String request_error = "requestCancelledError";
    private JSONObject jsonObject;
    private ANError anError;

    private final int code;

    public ApiReader(Response okHttpResponse, JSONObject js){
        this.jsonObject = js;
        this.code = okHttpResponse.code();
    }

    public ApiReader(ANError anError){
        this.anError = anError;
        this.code = anError.getErrorCode();
    }

    public String getNetworkErrorMsg(Context context){
        switch (anError.getErrorDetail()) {
            case ApiReader.connect_error:
                return context.getString(R.string.conn_error);
            case ApiReader.parse_error:
                return "Cannot complete this action on your leave application";
            case ApiReader.request_error:
                return context.getString(R.string.req_error);
            default:
                return anError.getErrorDetail();
        }
    }

    public JSONObject getData(){
        return jsonObject;
    }

    public boolean isSuccess(){
        int OK_RESPONSE = 200;
        int CREATED_RESPONSE = 201;
        int DUPLICATE_RESPONSE = 202;
        return code == OK_RESPONSE || code == CREATED_RESPONSE || code == DUPLICATE_RESPONSE;
    }

    public boolean isFailed(){
        int VALIDATION_ERROR = 400;
        return code == VALIDATION_ERROR;
    }

    public boolean isAuthorizationError(){
        int UNAUTHORIZED_ERROR = 401;
        int FORBIDDEN_ERROR = 403;
        return code == UNAUTHORIZED_ERROR || code == FORBIDDEN_ERROR;
    }

    public boolean isSystemError(){
        int NOT_FOUND_ERROR = 500;
        int SYSTEM_ERROR = 501;
        return code == SYSTEM_ERROR || code == NOT_FOUND_ERROR;
    }

    public boolean isBadRequest(){
        int BAD_REQUEST = 404;
        return code == BAD_REQUEST;
    }

    public void handleError(Context context, ANError anError, ApiCallHandler callHandler, String TAG){
        anError.printStackTrace();
        Log.d(TAG, "onError: "+anError.getErrorCode());
        if (isAuthorizationError()){
            callHandler.logout();
        }else if (anError.getErrorCode() != 0){
            callHandler.failed(anError.getResponse().message(), "Network Error");
        }else {
            String error = getNetworkErrorMsg(context);
            callHandler.failed(anError.getErrorDetail().equals(ApiReader.parse_error) ? "Cannot Apply" : "Network Error", error);
        }
    }

}
