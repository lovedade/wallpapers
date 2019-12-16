package com.benkkstudio.bsjsonsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.benkkstudio.bsjson.API;
import com.benkkstudio.bsjson.BSJsonV2;
import com.benkkstudio.bsjson.Interface.BSJsonV2Listener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
        jsObj.addProperty("method_name", "settings");
        new BSJsonV2.Builder(this)
                .setParams(jsObj)
                .setPurchaseCode("52394c52-11f0-4c5a-91d7-7c2c7c054fdb")
                .setServer("https://benkkstudio.xyz/bsvideostatus/api.php")
                .load();
    }
}
