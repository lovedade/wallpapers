package com.benkkstudio.bsjson.Interface;

public interface BSJsonV2Listener {
    void onStart();
    void onEnd();
    void onLoaded(String responseBody);
}
