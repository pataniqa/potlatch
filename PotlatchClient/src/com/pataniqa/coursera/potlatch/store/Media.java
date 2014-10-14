package com.pataniqa.coursera.potlatch.store;

import retrofit.client.Response;
import retrofit.mime.TypedFile;
import rx.Observable;

public interface Media {

    Observable<Boolean> setImageData(long id, TypedFile imageData);

    Observable<Response> getImageData(long id);

    Observable<Boolean> setVideoData(long id, TypedFile imageData);

    Observable<Response> getVideoData(long id);

}
