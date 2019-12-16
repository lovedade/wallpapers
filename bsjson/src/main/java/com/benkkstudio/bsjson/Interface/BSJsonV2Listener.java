package com.benkkstudio.bsjson.Interface;

import org.json.JSONObject;

public interface BSJsonV2Listener {
    void onLoaded(JSONObject jsonObject);
    void onError(String error);
}
