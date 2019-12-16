package com.benkkstudio.bsjson;

import android.app.Activity;
import android.widget.Toast;


import com.benkkstudio.bsjson.Interface.BSJsonOnSuccessListener;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.logging.Logger;

import cz.msebera.android.httpclient.Header;

public class BSJson {
    private Activity activity;
    private String server;
    private JsonObject jsObj;
    private BSJsonOnSuccessListener bsJsonOnSuccessListener;
    private RequestParams requestParams;
    private String purchaseCode;

    private BSJson(Activity activity,
                   String server,
                   JsonObject jsObj,
                   RequestParams requestParams,
                   BSJsonOnSuccessListener bsJsonOnSuccessListener,
                   String purchaseCode) {
        this.activity = activity;
        this.server = server;
        this.jsObj = jsObj;
        this.requestParams = requestParams;
        this.bsJsonOnSuccessListener = bsJsonOnSuccessListener;
        this.purchaseCode = purchaseCode;
        verifyNow();
    }

    private void verifyNow() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("code", purchaseCode);
        client.addHeader("Authorization", "Bearer 031Cm94VBFWVIwOGuyvfTcvvmvF3EM9b");
        client.addHeader("User-Agent", "Purchase code verification on benkkstudio.xyz");
        client.get("https://api.envato.com/v3/market/author/sale", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                load();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(activity, "Your purchase code not valid", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void load() {
        if (jsObj != null) {
            Constant.client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("data", API.toBase64(jsObj.toString()));
            Constant.client.post(server, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (bsJsonOnSuccessListener != null) {
                        bsJsonOnSuccessListener.onSuccess(statusCode, responseBody);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (bsJsonOnSuccessListener != null) {
                        bsJsonOnSuccessListener.onFiled(statusCode, responseBody, error);
                    }
                }
            });
        } else if (requestParams != null){
            Constant.client = new AsyncHttpClient();
            requestParams = new RequestParams();
            Constant.client.post(server, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (bsJsonOnSuccessListener != null) {
                        bsJsonOnSuccessListener.onSuccess(statusCode, responseBody);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (bsJsonOnSuccessListener != null) {
                        bsJsonOnSuccessListener.onFiled(statusCode, responseBody, error);
                    }
                }
            });
        } else {
            Constant.client = new AsyncHttpClient();
            Constant.client.post(server, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (bsJsonOnSuccessListener != null) {
                        bsJsonOnSuccessListener.onSuccess(statusCode, responseBody);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (bsJsonOnSuccessListener != null) {
                        bsJsonOnSuccessListener.onFiled(statusCode, responseBody, error);
                    }
                }
            });
        }
    }

    public static class Builder {
        private Activity activity;
        private String server;
        private JsonObject jsObj;
        private BSJsonOnSuccessListener bsJsonOnSuccessListener;
        private RequestParams requestParams;
        private String purchaseCode;
        public Builder(Activity activity) {
            this.activity = activity;
        }

        public BSJson.Builder setServer(String server) {
            this.server = server;
            return this;
        }

        public BSJson.Builder setObject(JsonObject jsObj) {
            this.jsObj = jsObj;
            return this;
        }

        public BSJson.Builder setParams(RequestParams requestParams) {
            this.requestParams = requestParams;
            return this;
        }

        public BSJson.Builder setPurchaseCode(String purchaseCode) {
            this.purchaseCode = purchaseCode;
            return this;
        }

        public BSJson.Builder setListener(BSJsonOnSuccessListener bsJsonOnSuccessListener) {
            this.bsJsonOnSuccessListener = bsJsonOnSuccessListener;
            return this;
        }

        public BSJson load() {
            return new BSJson(activity, server, jsObj, requestParams, bsJsonOnSuccessListener, purchaseCode);
        }
    }
}
