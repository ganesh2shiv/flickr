package com.flickr.app.ui.photo.explore;

import com.flickr.app.network.RestService;
import com.flickr.app.ui.photo.explore.model.Photo;
import com.flickr.app.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;

public class PhotoExploreInteractorImpl implements PhotoExploreInteractor {

    private RestService restService;

    public PhotoExploreInteractorImpl(RestService restService) {
        this.restService = restService;
    }

    @Override
    public Single<List<Photo>> getPhotosList(int page, int limit) {
        Map<String, String> query = new HashMap<>();
        query.put("api_key", Constants.FLICKR_KEY);
        query.put("per_page", String.valueOf(limit));
        query.put("page", String.valueOf(page));

        return restService.explore(query)
                .map(PhotoExploreParser::parse);
    }
}