package com.benkkstudio.bsjson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.benkkstudio.bsjson.Interface.BSJsonV2Listener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BSJsonV2 {
    private Activity activity;
    private String server;
    private BSJsonV2Listener bsJsonV2Listener;
    private String purchaseCode;
    private RequestBody requestBody;

    private BSJsonV2(Activity activity,
                     String server, RequestBody requestBody,
                     BSJsonV2Listener bsJsonV2Listener,
                     String purchaseCode) {
        this.activity = activity;
        this.server = server;
        this.bsJsonV2Listener = bsJsonV2Listener;
        this.purchaseCode = purchaseCode;
        this.requestBody = requestBody;
        verifyNow();
    }

    private void verifyNow() {
        new VerifyServer().execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class VerifyServer extends AsyncTask<String, String, String> {
        private boolean isVerified;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String responseBody = API.verifyServer("https://api.envato.com/v3/market/author/sale?code=" + purchaseCode);
            try {
                JSONObject jsonObject = new JSONObject(responseBody);
                if(jsonObject.length() > 2){
                    return "LICENSE FOUND";
                } else {
                    return "LICENSE NOT FOUND";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "SERVER ERROR";
            }
        }

        @Override
        protected void onPostExecute(String isLoaded) {
            switch (isLoaded){
                case "LICENSE FOUND":
                    new Load().execute();
                    break;
                case "LICENSE NOT FOUND":
                    Toast.makeText(activity, "Purchase code not valid, please contact administrator !", Toast.LENGTH_LONG).show();
                    break;
                case "SERVER ERROR":
                    Toast.makeText(activity, "Server not response, please try again later !", Toast.LENGTH_LONG).show();
                    break;
            }
            super.onPostExecute(isLoaded);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Load extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            bsJsonV2Listener.onStart();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String responseBody = API.okhttpPost(server, requestBody);
                bsJsonV2Listener.onLoaded(responseBody);
            } catch (Exception e) {
                Log.wtf("ABENK", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            bsJsonV2Listener.onEnd();
            super.onPostExecute(s);
        }
    }

    public static RequestBody makeRequest(JsonObject jsObj) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("data", API.toBase64(jsObj.toString()))
                .build();
    }

    public static class Builder {
        private Activity activity;
        private String server;
        private BSJsonV2Listener bsJsonV2Listener;
        private RequestBody requestBody;
        private String purchaseCode;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public BSJsonV2.Builder setServer(String server) {
            this.server = server;
            return this;
        }


        public BSJsonV2.Builder setParams(RequestBody requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public BSJsonV2.Builder setPurchaseCode(String purchaseCode) {
            this.purchaseCode = purchaseCode;
            return this;
        }

        public BSJsonV2.Builder setListener(BSJsonV2Listener bsJsonV2Listener) {
            this.bsJsonV2Listener = bsJsonV2Listener;
            return this;
        }

        public BSJsonV2 load() {
            return new BSJsonV2(activity, server, requestBody, bsJsonV2Listener, purchaseCode);
        }
    }
}
