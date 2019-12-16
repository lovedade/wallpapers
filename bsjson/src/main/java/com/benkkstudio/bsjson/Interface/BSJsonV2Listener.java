package com.benkkstudio.bsjson.Interface;

public interface BSJsonV2Listener {
    void onStart();
    void onLoaded(String responseBody);
    void onEnd();
}
