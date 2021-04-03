package com.luvtas.campingau.Remote;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ICloudFunctions {
    @GET("")
    Observable getCustomToken(@Query("access token") String accessToken);
}
