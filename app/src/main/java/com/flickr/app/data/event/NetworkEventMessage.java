package com.flickr.app.data.event;

public class NetworkEventMessage {

    private int resultCode;
    private String resultValue;

    public NetworkEventMessage(int resultCode) {
        this.resultCode = resultCode;
    }

    public NetworkEventMessage(int resultCode, String resultValue) {
        this.resultCode = resultCode;
        this.resultValue = resultValue;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getResultValue() {
        return resultValue;
    }
}