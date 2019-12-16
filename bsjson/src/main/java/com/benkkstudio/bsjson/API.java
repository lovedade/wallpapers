package com.benkkstudio.bsjson;

import android.util.Base64;

import com.google.gson.annotations.SerializedName;
import com.loopj.android.http.RequestParams;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class API {

    @SerializedName("sign")
    private String sign;
    @SerializedName("salt")
    private String salt;

    public API() {
        String apiKey = "benkkstudio";
        salt = "" + getRandomSalt();
        sign = md5(apiKey + salt);
    }

    private int getRandomSalt() {
        Random random = new Random();
        return random.nextInt(900);
    }

    private String md5(String input) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(String.format("%02x", messageDigest[i]));
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toBase64(String input) {
        byte[] encodeValue = Base64.encode(input.getBytes(), Base64.DEFAULT);
        return new String(encodeValue);
    }

    public static String okhttpGET(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(7000, TimeUnit.MILLISECONDS)
                .writeTimeout(7000, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String verifyServer(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(7000, TimeUnit.MILLISECONDS)
                .writeTimeout(7000, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer 031Cm94VBFWVIwOGuyvfTcvvmvF3EM9b")
                .addHeader("User-Agent", "Purchase code verification on benkkstudio.xyz")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String okhttpPost(String url, RequestBody requestBody) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(7000, TimeUnit.MILLISECONDS)
                .writeTimeout(7000, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
