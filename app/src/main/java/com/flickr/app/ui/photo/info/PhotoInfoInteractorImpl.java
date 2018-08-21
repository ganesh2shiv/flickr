package com.flickr.app.ui.photo.info;

import com.flickr.app.network.RestService;
import com.flickr.app.ui.photo.info.model.PhotoInfo;
import com.flickr.app.util.Constants;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;

public class PhotoInfoInteractorImpl implements PhotoInfoInteractor {

    private RestService restService;

    public PhotoInfoInteractorImpl(RestService restService) {
        this.restService = restService;
    }

    @Override
    public Single<PhotoInfo> getPhotoInfo(String photoId) {

        Map<String, String> query = new HashMap<>();
        query.put("api_key", Constants.FLICKR_KEY);
        query.put("photo_id", photoId);

        return restService.info(query)
                .map(PhotoInfoParser::parse);
    }
}