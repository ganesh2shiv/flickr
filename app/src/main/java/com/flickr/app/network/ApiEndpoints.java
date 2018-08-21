package com.flickr.app.network;

public enum ApiEndpoints {

    MOCK("Mock", "https://private-5d2e7c-flickr.apiary-mock.com/"),

    RELEASE("Release", "https://api.flickr.com/");

    public final String name;
    public final String url;

    ApiEndpoints(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public static ApiEndpoints from(String endpoint) {
        for (ApiEndpoints value : values()) {
            if (value.url != null && value.url.equals(endpoint)) {
                return value;
            }
        }
        return MOCK;
    }

    public static boolean isReleaseMode(String endpoint) {
        return from(endpoint) == RELEASE;
    }
}