package com.flickr.app.network;

import com.flickr.app.ui.photo.explore.model.PhotoExploreResponse;
import com.flickr.app.ui.photo.info.model.PhotoInfoResponse;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

public interface RestService {

    @Headers({"content-type: application/json"})
    @GET("/services/rest/?method=flickr.interestingness.getList&format=json&nojsoncallback=1")
    Single<Response<PhotoExploreResponse>> explore(@QueryMap Map<String, String> query);

    @Headers({"content-type: application/json"})
    @GET("/services/rest/?method=flickr.photos.getInfo&format=json&nojsoncallback=1")
    Single<Response<PhotoInfoResponse>> info(@QueryMap Map<String, String> query);

}